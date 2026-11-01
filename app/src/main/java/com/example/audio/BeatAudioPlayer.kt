package com.example.audio

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.example.model.AudioTrack
import com.example.model.SongSection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.exp

data class PlayerPlaybackState(
  val currentTrack: AudioTrack? = null,
  val isPlaying: Boolean = false,
  val positionMs: Long = 0L,
  val durationMs: Long = 1L,
  val progress: Float = 0f,
  val currentSection: SongSection? = null,
  val beatPulse: Float = 0f, // 0.0 to 1.0 (peaks on beat kick)
  val isDrop: Boolean = false,
  val currentBeatNumber: Int = 0
)

class BeatAudioPlayer(private val context: Context) {

  private var mediaPlayer: MediaPlayer? = null
  private val scope = CoroutineScope(Dispatchers.Main + Job())
  private var tickerJob: Job? = null

  private val _state = MutableStateFlow(PlayerPlaybackState())
  val state: StateFlow<PlayerPlaybackState> = _state.asStateFlow()

  fun loadTrack(track: AudioTrack) {
    releasePlayer()

    val player = MediaPlayer()
    try {
      if (track.isDemo) {
        val audioFile = BeatAudioSynthesizer.getOrCreateDemoAudio(
          context = context,
          trackId = track.id,
          bpm = track.bpm,
          durationMs = track.durationMs
        )
        player.setDataSource(audioFile.absolutePath)
      } else if (track.audioUri != null) {
        player.setDataSource(context, Uri.parse(track.audioUri))
      }
      player.prepare()
      player.isLooping = true
      mediaPlayer = player

      val actualDuration = if (player.duration > 0) player.duration.toLong() else track.durationMs
      _state.value = _state.value.copy(
        currentTrack = track.copy(durationMs = actualDuration),
        positionMs = 0L,
        durationMs = actualDuration,
        progress = 0f,
        isPlaying = false,
        currentSection = track.sections.firstOrNull()
      )
    } catch (e: Exception) {
      e.printStackTrace()
      _state.value = _state.value.copy(
        currentTrack = track,
        positionMs = 0L,
        durationMs = track.durationMs,
        progress = 0f,
        isPlaying = false,
        currentSection = track.sections.firstOrNull()
      )
    }
  }

  fun togglePlay() {
    if (_state.value.isPlaying) {
      pause()
    } else {
      play()
    }
  }

  fun play() {
    val player = mediaPlayer ?: return
    try {
      player.start()
      _state.value = _state.value.copy(isPlaying = true)
      startTicker()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun pause() {
    try {
      mediaPlayer?.pause()
      _state.value = _state.value.copy(isPlaying = false)
      stopTicker()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun seekTo(positionMs: Long) {
    try {
      val clamped = positionMs.coerceIn(0L, _state.value.durationMs)
      mediaPlayer?.seekTo(clamped.toInt())
      updatePosition(clamped)
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun startTicker() {
    tickerJob?.cancel()
    tickerJob = scope.launch {
      while (isActive && _state.value.isPlaying) {
        val player = mediaPlayer
        if (player != null && player.isPlaying) {
          val pos = player.currentPosition.toLong()
          updatePosition(pos)
        }
        delay(33) // ~30 fps updates for buttery smooth animation
      }
    }
  }

  private fun stopTicker() {
    tickerJob?.cancel()
    tickerJob = null
  }

  private fun updatePosition(pos: Long) {
    val track = _state.value.currentTrack ?: return
    val duration = _state.value.durationMs.coerceAtLeast(1L)
    val progress = (pos.toFloat() / duration).coerceIn(0f, 1f)

    // Calculate current section
    val currentSection = track.sections.firstOrNull { pos in it.startMs until it.endMs }
      ?: track.sections.lastOrNull()

    // Calculate exact beat pulse from BPM
    val beatIntervalMs = (60000.0 / track.bpm).coerceAtLeast(100.0)
    val beatProgress = (pos % beatIntervalMs.toLong()) / beatIntervalMs // 0.0 to 1.0 within beat
    val beatNumber = (pos / beatIntervalMs.toLong()).toInt()

    // Exponential decay curve: peaks at beat onset (1.0) and decays rapidly
    val decay = exp(-beatProgress * 4.5).toFloat()
    val sectionIntensity = currentSection?.beatIntensity ?: 0.5f
    val pulse = (decay * sectionIntensity).coerceIn(0f, 1f)

    val isDrop = currentSection?.isDropOrChorus == true

    _state.value = _state.value.copy(
      positionMs = pos,
      progress = progress,
      currentSection = currentSection,
      beatPulse = pulse,
      isDrop = isDrop,
      currentBeatNumber = beatNumber
    )
  }

  private fun releasePlayer() {
    stopTicker()
    try {
      mediaPlayer?.stop()
      mediaPlayer?.release()
    } catch (e: Exception) {
      // ignore
    }
    mediaPlayer = null
  }

  fun release() {
    releasePlayer()
  }
}
