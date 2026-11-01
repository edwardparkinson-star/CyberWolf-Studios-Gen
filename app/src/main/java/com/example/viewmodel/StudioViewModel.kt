package com.example.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AudioAnalyzer
import com.example.audio.BeatAudioPlayer
import com.example.audio.PlayerPlaybackState
import com.example.data.StudioPresets
import com.example.engine.ExportProgressState
import com.example.engine.VideoExportEngine
import com.example.engine.VisualSceneGenerator
import com.example.model.AudioTrack
import com.example.model.CameraMotion
import com.example.model.ColorPalette
import com.example.model.CommunityComment
import com.example.model.CommunityPost
import com.example.model.ExportResolution
import com.example.model.GeneratedScene
import com.example.model.ImaginationType
import com.example.model.SongSection
import com.example.model.VideoAspectRatio
import com.example.model.VideoProject
import com.example.model.VisualCustomization
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

enum class AppScreen {
  STUDIO,
  CUSTOMIZE,
  UPLOAD,
  COMMUNITY,
  LIBRARY
}

data class StudioUiState(
  val currentScreen: AppScreen = AppScreen.STUDIO,
  val currentTrack: AudioTrack = StudioPresets.demoTracks.first(),
  val customization: VisualCustomization = VisualCustomization(
    imaginationType = StudioPresets.imaginationTypes.first(),
    palette = StudioPresets.palettes.first(),
    cameraMotion = CameraMotion.HYPERSPACE,
    resolution = ExportResolution.UHD_4K,
    aspectRatio = VideoAspectRatio.RATIO_16_9
  ),
  val generatedScenes: List<GeneratedScene> = emptyList(),
  val communityPosts: List<CommunityPost> = StudioPresets.communityPosts,
  val savedProjects: List<VideoProject> = emptyList(),
  val isAnalyzingAudio: Boolean = false,
  val showExportDialog: Boolean = false,
  val showPublishDialog: Boolean = false,
  val publishCaptionInput: String = "",
  val publishCreatorName: String = "CyberCreator",
  val toastMessage: String? = null
)

class StudioViewModel(application: Application) : AndroidViewModel(application) {

  private val audioPlayer = BeatAudioPlayer(application.applicationContext)
  val playbackState: StateFlow<PlayerPlaybackState> = audioPlayer.state
  val exportState: StateFlow<ExportProgressState> = VideoExportEngine.progressState

  private val _uiState = MutableStateFlow(StudioUiState())
  val uiState: StateFlow<StudioUiState> = _uiState.asStateFlow()

  init {
    val initialTrack = _uiState.value.currentTrack
    val initialScenes = VisualSceneGenerator.generateScenes(initialTrack, _uiState.value.customization)
    _uiState.value = _uiState.value.copy(generatedScenes = initialScenes)
    audioPlayer.loadTrack(initialTrack)
  }

  fun navigateTo(screen: AppScreen) {
    _uiState.value = _uiState.value.copy(currentScreen = screen)
  }

  fun selectTrack(track: AudioTrack) {
    audioPlayer.loadTrack(track)
    val scenes = VisualSceneGenerator.generateScenes(track, _uiState.value.customization)
    _uiState.value = _uiState.value.copy(
      currentTrack = track,
      generatedScenes = scenes,
      currentScreen = AppScreen.STUDIO
    )
  }

  fun uploadUserAudio(uri: Uri) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isAnalyzingAudio = true)
      try {
        val analyzedTrack = AudioAnalyzer.analyzeUploadedAudio(getApplication(), uri)
        selectTrack(analyzedTrack)
      } catch (e: Exception) {
        e.printStackTrace()
      } finally {
        _uiState.value = _uiState.value.copy(isAnalyzingAudio = false)
      }
    }
  }

  fun updateCustomization(updater: (VisualCustomization) -> VisualCustomization) {
    val newCustomization = updater(_uiState.value.customization)
    val newScenes = VisualSceneGenerator.generateScenes(_uiState.value.currentTrack, newCustomization)
    _uiState.value = _uiState.value.copy(
      customization = newCustomization,
      generatedScenes = newScenes
    )
  }

  fun selectImaginationType(type: ImaginationType) {
    updateCustomization {
      it.copy(
        imaginationType = type,
        beatReactivity = type.beatReactivityPreset
      )
    }
  }

  fun selectPalette(palette: ColorPalette) {
    updateCustomization { it.copy(palette = palette) }
  }

  fun selectReferenceImage(uri: Uri?) {
    updateCustomization { it.copy(referenceImageUri = uri?.toString()) }
  }

  fun setReferenceWeight(weight: Float) {
    updateCustomization { it.copy(referenceWeight = weight) }
  }

  fun setAspectRatio(ratio: VideoAspectRatio) {
    updateCustomization { it.copy(aspectRatio = ratio) }
  }

  fun setResolution(res: ExportResolution) {
    updateCustomization { it.copy(resolution = res) }
  }

  fun togglePlay() {
    audioPlayer.togglePlay()
  }

  fun seekTo(posMs: Long) {
    audioPlayer.seekTo(posMs)
  }

  fun jumpToSection(section: SongSection) {
    audioPlayer.seekTo(section.startMs)
    if (!playbackState.value.isPlaying) {
      audioPlayer.play()
    }
  }

  fun startExport4K() {
    audioPlayer.pause()
    _uiState.value = _uiState.value.copy(showExportDialog = true)
    viewModelScope.launch {
      val exported = VideoExportEngine.export4KMusicVideo(
        context = getApplication(),
        track = _uiState.value.currentTrack,
        customization = _uiState.value.customization
      )
      saveCurrentProject(exportedFile = exported)
    }
  }

  fun dismissExportDialog() {
    VideoExportEngine.dismissDialog()
    _uiState.value = _uiState.value.copy(showExportDialog = false)
  }

  fun showPublishModal() {
    _uiState.value = _uiState.value.copy(showPublishDialog = true)
  }

  fun dismissPublishModal() {
    _uiState.value = _uiState.value.copy(showPublishDialog = false)
  }

  fun updatePublishCaption(caption: String) {
    _uiState.value = _uiState.value.copy(publishCaptionInput = caption)
  }

  fun confirmPublishToCommunity() {
    val current = _uiState.value
    val newPost = CommunityPost(
      id = "post_${UUID.randomUUID()}",
      creatorName = current.publishCreatorName,
      creatorHandle = "@${current.publishCreatorName.lowercase().replace(" ", "_")}",
      creatorAvatarColor = current.customization.palette.colors.first(),
      videoTitle = "${current.currentTrack.title} (4K Master)",
      caption = if (current.publishCaptionInput.isNotBlank()) current.publishCaptionInput else "Produced with CyberWolf Studios Gen in 4K resolution! Matched to ${current.currentTrack.bpm} BPM rhythm 🔥",
      trackTitle = current.currentTrack.title,
      artist = current.currentTrack.artist,
      bpm = current.currentTrack.bpm,
      durationSec = (current.currentTrack.durationMs / 1000).toInt(),
      imaginationStyle = current.customization.imaginationType.title,
      imageRes = current.customization.imaginationType.fallbackDrawable,
      likeCount = 1,
      isLiked = true,
      comments = emptyList(),
      views = 12,
      isFollowing = true,
      createdAt = "Just now",
      is4K = true,
      referenceImageUri = current.customization.referenceImageUri
    )

    _uiState.value = current.copy(
      communityPosts = listOf(newPost) + current.communityPosts,
      showPublishDialog = false,
      publishCaptionInput = "",
      toastMessage = "Published to CyberWolf Community!"
    )
  }

  fun toggleLikePost(postId: String) {
    val updated = _uiState.value.communityPosts.map { post ->
      if (post.id == postId) {
        val newLiked = !post.isLiked
        val newCount = if (newLiked) post.likeCount + 1 else (post.likeCount - 1).coerceAtLeast(0)
        post.copy(isLiked = newLiked, likeCount = newCount)
      } else post
    }
    _uiState.value = _uiState.value.copy(communityPosts = updated)
  }

  fun toggleFollowPost(postId: String) {
    val post = _uiState.value.communityPosts.firstOrNull { it.id == postId } ?: return
    val targetHandle = post.creatorHandle
    val newFollowing = !post.isFollowing

    val updated = _uiState.value.communityPosts.map {
      if (it.creatorHandle == targetHandle) {
        it.copy(isFollowing = newFollowing)
      } else it
    }
    _uiState.value = _uiState.value.copy(communityPosts = updated)
  }

  fun addCommentToPost(postId: String, text: String) {
    val newComment = CommunityComment(
      id = "c_${UUID.randomUUID()}",
      author = _uiState.value.publishCreatorName,
      text = text,
      timestamp = "Just now",
      likes = 0
    )
    val updated = _uiState.value.communityPosts.map { post ->
      if (post.id == postId) {
        post.copy(comments = post.comments + newComment)
      } else post
    }
    _uiState.value = _uiState.value.copy(communityPosts = updated)
  }

  fun playCommunityPostInStudio(post: CommunityPost) {
    // Find or create track
    val matchedTrack = StudioPresets.demoTracks.firstOrNull { it.title == post.trackTitle }
      ?: StudioPresets.demoTracks.first()
    val matchedStyle = StudioPresets.imaginationTypes.firstOrNull { it.title == post.imaginationStyle }
      ?: StudioPresets.imaginationTypes.first()

    updateCustomization {
      it.copy(
        imaginationType = matchedStyle,
        referenceImageUri = post.referenceImageUri
      )
    }
    selectTrack(matchedTrack)
    _uiState.value = _uiState.value.copy(currentScreen = AppScreen.STUDIO)
  }

  private fun saveCurrentProject(exportedFile: File?) {
    val current = _uiState.value
    val newProject = VideoProject(
      id = UUID.randomUUID().toString(),
      title = "${current.currentTrack.title} - ${current.customization.imaginationType.title}",
      track = current.currentTrack,
      customization = current.customization,
      scenes = current.generatedScenes,
      isExported = exportedFile != null,
      exportedUri = exportedFile?.absolutePath,
      exportResolution = current.customization.resolution
    )
    _uiState.value = current.copy(savedProjects = listOf(newProject) + current.savedProjects)
  }

  fun clearToast() {
    _uiState.value = _uiState.value.copy(toastMessage = null)
  }

  override fun onCleared() {
    super.onCleared()
    audioPlayer.release()
  }
}
