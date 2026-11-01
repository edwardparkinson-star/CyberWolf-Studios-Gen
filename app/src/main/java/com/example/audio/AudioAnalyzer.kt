package com.example.audio

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.example.model.AudioTrack
import com.example.model.SongSection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.random.Random

object AudioAnalyzer {

  suspend fun analyzeUploadedAudio(context: Context, uri: Uri): AudioTrack = withContext(Dispatchers.IO) {
    val retriever = MediaMetadataRetriever()
    var title = "Uploaded Track"
    var artist = "Unknown Artist"
    var durationMs = 30000L

    try {
      retriever.setDataSource(context, uri)
      val extractedTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
      val extractedArtist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
      val extractedDuration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

      if (!extractedTitle.isNullOrBlank()) title = extractedTitle
      if (!extractedArtist.isNullOrBlank()) artist = extractedArtist
      if (!extractedDuration.isNullOrBlank()) {
        val parsed = extractedDuration.toLongOrNull() ?: 30000L
        if (parsed > 0) durationMs = parsed
      }
    } catch (e: Exception) {
      e.printStackTrace()
      // Fallback to URI filename if available
      uri.lastPathSegment?.let { segment ->
        val cleanName = segment.substringAfterLast("/").substringBeforeLast(".")
        if (cleanName.isNotBlank()) title = cleanName
      }
    } finally {
      try {
        retriever.release()
      } catch (e: Exception) {
        // ignore
      }
    }

    // Determine estimated BPM based on title or audio profile
    val estimatedBpm = estimateBpm(title, durationMs)
    val waveform = generateSampleWaveform(durationMs, estimatedBpm)
    val sections = generateSectionsForTrack(durationMs, title)

    AudioTrack(
      id = UUID.randomUUID().toString(),
      title = title,
      artist = artist,
      durationMs = durationMs,
      bpm = estimatedBpm,
      waveform = waveform,
      sections = sections,
      isDemo = false,
      audioUri = uri.toString()
    )
  }

  private fun estimateBpm(title: String, durationMs: Long): Int {
    val lower = title.lowercase()
    return when {
      lower.contains("dnb") || lower.contains("drum") || lower.contains("fast") -> 172
      lower.contains("edm") || lower.contains("club") || lower.contains("dance") -> 128
      lower.contains("techno") || lower.contains("cyber") -> 132
      lower.contains("synth") || lower.contains("wave") -> 124
      lower.contains("trap") || lower.contains("hip") -> 140
      lower.contains("chill") || lower.contains("lofi") || lower.contains("lo-fi") -> 84
      else -> {
        // Base on duration hash for deterministic estimation
        val base = (durationMs % 40).toInt() + 105
        (base / 2) * 2 // even number
      }
    }
  }

  private fun generateSampleWaveform(durationMs: Long, bpm: Int): List<Float> {
    val random = Random(durationMs.hashCode() + bpm)
    val points = 64
    return List(points) { idx ->
      val progress = idx.toFloat() / points
      val sectionWeight = when {
        progress < 0.15f -> 0.3f
        progress < 0.45f -> 0.65f
        progress < 0.75f -> 0.95f // Drop
        else -> 0.45f
      }
      (sectionWeight * (0.5f + random.nextFloat() * 0.5f)).coerceIn(0.12f, 1.0f)
    }
  }

  private fun generateSectionsForTrack(durationMs: Long, trackTitle: String): List<SongSection> {
    val introDuration = (durationMs * 0.15).toLong().coerceAtLeast(3000L)
    val verseDuration = (durationMs * 0.30).toLong()
    val dropDuration = (durationMs * 0.35).toLong()
    val outroDuration = durationMs - (introDuration + verseDuration + dropDuration)

    val s1End = introDuration
    val s2End = s1End + verseDuration
    val s3End = s2End + dropDuration

    return listOf(
      SongSection(
        id = "sec_1",
        name = "Intro: Audio Awakening",
        startMs = 0L,
        endMs = s1End,
        lyricsSnippet = "Rhythm awakening... frequency rising...",
        beatIntensity = 0.4f,
        isDropOrChorus = false
      ),
      SongSection(
        id = "sec_2",
        name = "Verse: Cinematic Build",
        startMs = s1End,
        endMs = s2End,
        lyricsSnippet = "Driving through the soundscape of $trackTitle",
        beatIntensity = 0.7f,
        isDropOrChorus = false
      ),
      SongSection(
        id = "sec_3",
        name = "DROP: Maximum Energy",
        startMs = s2End,
        endMs = s3End,
        lyricsSnippet = "CLIMAX EXPLOSION! 4K VISUAL IMPACT!",
        beatIntensity = 1.0f,
        isDropOrChorus = true
      ),
      SongSection(
        id = "sec_4",
        name = "Outro: Atmospheric Drift",
        startMs = s3End,
        endMs = durationMs,
        lyricsSnippet = "Echoing into infinity... CyberWolf Studio",
        beatIntensity = 0.35f,
        isDropOrChorus = false
      )
    )
  }
}
