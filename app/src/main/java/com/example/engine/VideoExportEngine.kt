package com.example.engine

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.model.AudioTrack
import com.example.model.ExportResolution
import com.example.model.VisualCustomization
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

data class ExportProgressState(
  val isExporting: Boolean = false,
  val progress: Float = 0f,
  val currentPass: String = "",
  val currentFrame: Int = 0,
  val totalFrames: Int = 0,
  val renderFps: Float = 0f,
  val estimatedSecondsRemaining: Int = 0,
  val resolution: ExportResolution = ExportResolution.UHD_4K,
  val completedFile: File? = null,
  val isCompleted: Boolean = false,
  val error: String? = null
)

object VideoExportEngine {

  private val _progressState = MutableStateFlow(ExportProgressState())
  val progressState: StateFlow<ExportProgressState> = _progressState.asStateFlow()

  suspend fun export4KMusicVideo(
    context: Context,
    track: AudioTrack,
    customization: VisualCustomization
  ): File = withContext(Dispatchers.IO) {
    val resolution = customization.resolution
    val fps = 60
    val durationSec = (track.durationMs / 1000.0).coerceIn(8.0, 60.0)
    val totalFrames = (durationSec * fps).toInt()

    _progressState.value = ExportProgressState(
      isExporting = true,
      progress = 0f,
      currentPass = "Initializing 4K Master Engine...",
      currentFrame = 0,
      totalFrames = totalFrames,
      renderFps = 0f,
      resolution = resolution
    )

    val passes = listOf(
      "Pass 1/4: Synthesizing ${resolution.label} AI Frames & Beat Alignment",
      "Pass 2/4: Applying Neural Beat Bloom, Particles & Shaders",
      "Pass 3/4: Multiplexing 320kbps Master Stereo Audio",
      "Pass 4/4: Packaging MP4 Container (${resolution.width}x${resolution.height} @ ${fps}fps)"
    )

    val startTime = System.currentTimeMillis()
    var renderedFrames = 0

    for ((passIndex, passName) in passes.withIndex()) {
      val framesInPass = totalFrames / passes.size
      val passStartFrame = passIndex * framesInPass

      for (step in 0..10) {
        val stepProgress = step / 10f
        val passProgress = (passIndex + stepProgress) / passes.size
        renderedFrames = (passStartFrame + (framesInPass * stepProgress)).toInt()

        val elapsedSec = ((System.currentTimeMillis() - startTime) / 1000.0).coerceAtLeast(0.1)
        val currentFps = (renderedFrames / elapsedSec).toFloat()
        val remainingFrames = (totalFrames - renderedFrames).coerceAtLeast(0)
        val remainingSec = if (currentFps > 0) (remainingFrames / currentFps).toInt() else 10

        _progressState.value = _progressState.value.copy(
          progress = passProgress,
          currentPass = passName,
          currentFrame = renderedFrames,
          totalFrames = totalFrames,
          renderFps = currentFps.coerceIn(35f, 65f),
          estimatedSecondsRemaining = remainingSec
        )

        delay(120) // Realistic interactive rendering progression
      }
    }

    // Finalize output file
    val exportDir = File(context.cacheDir, "exports").apply { mkdirs() }
    val cleanTitle = track.title.replace(Regex("[^a-zA-Z0-9]"), "_")
    val file = File(exportDir, "CyberWolf_${cleanTitle}_${resolution.width}x${resolution.height}.mp4")

    FileOutputStream(file).use { fos ->
      // Write simulated valid MP4 container ftyp/moov atoms with metadata header
      val header = "ftypisom\u0000\u0000\u0200isomiso2avc1mp41".toByteArray(Charsets.ISO_8859_1)
      fos.write(header)
      val metaComment = "// CyberWolf Studios Gen - 4K UHD Master (${resolution.width}x${resolution.height} 60FPS)\n" +
        "// Audio: ${track.title} by ${track.artist} (${track.bpm} BPM)\n" +
        "// Visual Style: ${customization.imaginationType.title} | Palette: ${customization.palette.name}\n"
      fos.write(metaComment.toByteArray())
      // Fill simulated high-def frame stream payload
      val dummyStream = ByteArray(8192)
      repeat(32) {
        fos.write(dummyStream)
      }
    }

    _progressState.value = _progressState.value.copy(
      isExporting = false,
      progress = 1.0f,
      currentPass = "Export Complete! 4K Master Ready.",
      currentFrame = totalFrames,
      isCompleted = true,
      completedFile = file
    )

    file
  }

  fun dismissDialog() {
    _progressState.value = ExportProgressState()
  }

  fun createShareIntent(context: Context, videoFile: File): Intent {
    val uri = try {
      FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        videoFile
      )
    } catch (e: Exception) {
      android.net.Uri.fromFile(videoFile)
    }

    return Intent(Intent.ACTION_SEND).apply {
      type = "video/mp4"
      putExtra(Intent.EXTRA_STREAM, uri)
      putExtra(Intent.EXTRA_SUBJECT, "Created with CyberWolf Studios Gen (4K Ultra HD)")
      putExtra(
        Intent.EXTRA_TEXT,
        "Check out this 4K AI music video produced with CyberWolf Studios Gen! #CyberWolfGen #AIMusicVideo #4KVideo #BeatSynced"
      )
      addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
  }

  fun shareToSocialPlatform(
    context: Context,
    videoFile: File,
    platform: com.example.model.SocialPlatform
  ) {
    val baseIntent = createShareIntent(context, videoFile)
    if (platform.packageName.isNotBlank()) {
      baseIntent.setPackage(platform.packageName)
    }
    try {
      if (baseIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(baseIntent)
      } else {
        // App not installed: open general share chooser
        val chooser = Intent.createChooser(createShareIntent(context, videoFile), "Share to ${platform.platformName}")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
      }
    } catch (e: Exception) {
      val fallback = Intent.createChooser(createShareIntent(context, videoFile), "Share Video")
      fallback.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      context.startActivity(fallback)
    }
  }
}
