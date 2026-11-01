package com.example.engine

import com.example.model.AudioTrack
import com.example.model.GeneratedScene
import com.example.model.VisualCustomization

object VisualSceneGenerator {

  fun generateScenes(
    track: AudioTrack,
    customization: VisualCustomization
  ): List<GeneratedScene> {
    val style = customization.imaginationType
    val palette = customization.palette
    val motion = customization.cameraMotion

    return track.sections.mapIndexed { index, section ->
      val isDrop = section.isDropOrChorus
      val energyTag = if (isDrop) "PEAK BASS DROP & EXPLOSIVE DYNAMICS" else "SMOOTH RHYTHMIC FLOW"

      val referenceInfluenceText = if (customization.referenceImageUri != null) {
        " Blending aesthetics, color gradient, and visual mood from user reference image (${(customization.referenceWeight * 100).toInt()}% influence)."
      } else ""

      val prompt = buildString {
        append("${style.title} Style [${style.category}]. ")
        append("Scene ${index + 1}: ${section.name}. ")
        append("Atmosphere: ${style.promptKeywords}. ")
        append("Action: $energyTag. Camera Movement: ${motion.title}. ")
        append("Color Palette: ${palette.name} (${palette.description}). ")
        append("Synchronized to ${track.bpm} BPM rhythm and lyrics: \"${section.lyricsSnippet}\".$referenceInfluenceText")
        if (customization.customPromptNotes.isNotBlank()) {
          append(" Creator Notes: ${customization.customPromptNotes}.")
        }
      }

      val particleCount = when {
        isDrop -> (80 * customization.particleDensity).toInt()
        section.beatIntensity > 0.6f -> (50 * customization.particleDensity).toInt()
        else -> (25 * customization.particleDensity).toInt()
      }

      GeneratedScene(
        index = index,
        sectionName = section.name,
        startMs = section.startMs,
        endMs = section.endMs,
        prompt = prompt,
        imageRes = style.fallbackDrawable,
        referenceImageUri = customization.referenceImageUri,
        lyrics = section.lyricsSnippet,
        particleCount = particleCount,
        isDrop = isDrop
      )
    }
  }
}
