package com.example.model

import androidx.annotation.DrawableRes
import com.example.R

enum class ExportResolution(
  val label: String,
  val width: Int,
  val height: Int,
  val bitRateMbps: Int,
  val tag: String
) {
  UHD_4K("4K Ultra HD", 3840, 2160, 50, "4K • 60 FPS"),
  VERTICAL_4K("Vertical 4K (Reels/Shorts)", 2160, 3840, 50, "4K 9:16"),
  FHD_1080P("1080p Full HD", 1920, 1080, 20, "1080p • 60 FPS"),
  HD_720P("720p HD", 1280, 720, 10, "720p • 30 FPS")
}

enum class VideoAspectRatio(val label: String, val ratioFloat: Float, val iconLabel: String) {
  RATIO_16_9("16:9 Landscape", 16f / 9f, "16:9"),
  RATIO_9_16("9:16 Vertical", 9f / 16f, "9:16"),
  RATIO_1_1("1:1 Square", 1f, "1:1")
}

enum class CameraMotion(val title: String, val subtitle: String) {
  ORBITAL("Orbital 360", "Smooth 360 rotating drone camera around focal subjects"),
  HYPERSPACE("Hyperspace Rush", "Forward high-speed dive that accelerates on beat drops"),
  DOLLY_ZOOM("Dolly Zoom (Vertigo)", "Cinematic optical compression pulling back on drops"),
  FLOATING_DRIFT("Floating Dream Drift", "Weightless drifting steadicam with gentle roll"),
  STROBE_PUNCH("Strobe Beat Punch", "Aggressive camera punch-ins on kick and snare hits")
}

data class ColorPalette(
  val id: String,
  val name: String,
  val colors: List<Long>,
  val description: String
)

data class ImaginationType(
  val id: String,
  val title: String,
  val category: String,
  val tagLine: String,
  val promptKeywords: String,
  @DrawableRes val fallbackDrawable: Int,
  val dominantColors: List<Long>,
  val beatReactivityPreset: Float = 0.8f
)

data class SongSection(
  val id: String,
  val name: String,
  val startMs: Long,
  val endMs: Long,
  val lyricsSnippet: String,
  val beatIntensity: Float, // 0.0 to 1.0
  val isDropOrChorus: Boolean = false
)

data class AudioTrack(
  val id: String,
  val title: String,
  val artist: String,
  val durationMs: Long,
  val bpm: Int,
  val waveform: List<Float>,
  val sections: List<SongSection>,
  val isDemo: Boolean,
  val audioUri: String? = null
)

data class VisualCustomization(
  val imaginationType: ImaginationType,
  val palette: ColorPalette,
  val referenceImageUri: String? = null,
  val referenceWeight: Float = 0.7f,
  val cameraMotion: CameraMotion = CameraMotion.HYPERSPACE,
  val beatReactivity: Float = 0.85f,
  val particleDensity: Float = 0.75f,
  val glitchIntensity: Float = 0.6f,
  val showLyrics: Boolean = true,
  val resolution: ExportResolution = ExportResolution.UHD_4K,
  val aspectRatio: VideoAspectRatio = VideoAspectRatio.RATIO_16_9,
  val customPromptNotes: String = ""
)

data class GeneratedScene(
  val index: Int,
  val sectionName: String,
  val startMs: Long,
  val endMs: Long,
  val prompt: String,
  @DrawableRes val imageRes: Int,
  val referenceImageUri: String? = null,
  val lyrics: String,
  val particleCount: Int,
  val isDrop: Boolean
)

data class VideoProject(
  val id: String,
  val title: String,
  val track: AudioTrack,
  val customization: VisualCustomization,
  val scenes: List<GeneratedScene>,
  val createdAt: Long = System.currentTimeMillis(),
  val isExported: Boolean = false,
  val exportedUri: String? = null,
  val exportResolution: ExportResolution = ExportResolution.UHD_4K
)

enum class SocialPlatform(
  val platformName: String,
  val packageName: String,
  val actionHint: String
) {
  YOUTUBE("YouTube", "com.google.android.youtube", "Upload to YouTube (16:9 / Shorts)"),
  INSTAGRAM("Instagram", "com.instagram.android", "Share to Reels & Stories"),
  TIKTOK("TikTok", "com.zhiliaoapp.musically", "Post to TikTok Sound Sync"),
  FACEBOOK("Facebook", "com.facebook.katana", "Share to Facebook Video"),
  ALL("More Apps", "", "Share via Android System Sheet")
}

data class CommunityComment(
  val id: String,
  val author: String,
  val text: String,
  val timestamp: String,
  val likes: Int = 0
)

data class CommunityPost(
  val id: String,
  val creatorName: String,
  val creatorHandle: String,
  val creatorAvatarColor: Long,
  val videoTitle: String,
  val caption: String,
  val trackTitle: String,
  val artist: String,
  val bpm: Int,
  val durationSec: Int,
  val imaginationStyle: String,
  @DrawableRes val imageRes: Int,
  val likeCount: Int,
  val isLiked: Boolean = false,
  val comments: List<CommunityComment> = emptyList(),
  val views: Int,
  val isFollowing: Boolean = false,
  val createdAt: String,
  val is4K: Boolean = true,
  val referenceImageUri: String? = null
)

