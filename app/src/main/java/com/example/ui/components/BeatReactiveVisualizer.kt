package com.example.ui.components

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.audio.PlayerPlaybackState
import com.example.model.GeneratedScene
import com.example.model.VideoAspectRatio
import com.example.model.VisualCustomization
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.LaserMagenta
import com.example.ui.theme.NeonCyan
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class VisualParticle(
  val x: Float,
  val y: Float,
  val speed: Float,
  val angle: Float,
  val size: Float,
  val color: Color
)

@Composable
fun BeatReactiveVisualizer(
  playbackState: PlayerPlaybackState,
  customization: VisualCustomization,
  scenes: List<GeneratedScene>,
  onTogglePlay: () -> Unit,
  modifier: Modifier = Modifier
) {
  val beatPulse = playbackState.beatPulse
  val isDrop = playbackState.isDrop
  val isPlaying = playbackState.isPlaying
  val currentPos = playbackState.positionMs

  // Determine active scene
  val activeScene = scenes.firstOrNull { currentPos in it.startMs until it.endMs }
    ?: scenes.lastOrNull()

  // Generate particles based on particle density
  val particles = remember(customization.particleDensity) {
    val rng = Random(1234)
    val count = (60 * customization.particleDensity).toInt().coerceIn(15, 100)
    List(count) {
      VisualParticle(
        x = rng.nextFloat(),
        y = rng.nextFloat(),
        speed = 0.2f + rng.nextFloat() * 0.8f,
        angle = rng.nextFloat() * (2 * PI).toFloat(),
        size = 2f + rng.nextFloat() * 4.5f,
        color = when (rng.nextInt(3)) {
          0 -> NeonCyan
          1 -> LaserMagenta
          else -> ElectricViolet
        }
      )
    }
  }

  // Camera zoom punch on kick beat
  val zoomScale = if (isPlaying) {
    1f + (beatPulse * 0.08f * customization.beatReactivity)
  } else 1f

  BoxWithConstraints(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .background(Color(0xFF07060E))
      .border(1.dp, Color(0xFF2C2454), RoundedCornerShape(20.dp))
      .clickable { onTogglePlay() }
      .testTag("beat_visualizer_container")
  ) {
    val targetRatio = customization.aspectRatio.ratioFloat

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(targetRatio.coerceIn(0.5625f, 1.777f))
        .clip(RoundedCornerShape(18.dp))
    ) {
      // 1. Scene Visual Keyframe (from Reference image or generated visual asset)
      Box(
        modifier = Modifier
          .fillMaxSize()
          .scale(zoomScale)
      ) {
        if (customization.referenceImageUri != null) {
          AsyncImage(
            model = Uri.parse(customization.referenceImageUri),
            contentDescription = "Visual Scene",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        } else {
          val res = activeScene?.imageRes ?: customization.imaginationType.fallbackDrawable
          Image(
            painter = painterResource(id = res),
            contentDescription = "AI Scene Keyframe",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        }

        // Dark gradient overlay for readability & mood
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color.Black.copy(alpha = 0.45f),
                  Color.Transparent,
                  Color.Black.copy(alpha = if (isDrop) 0.5f else 0.75f)
                )
              )
            )
        )
      }

      // 2. Beat Reactive Procedural Canvas Shaders & Particles
      Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f

        // Strobe flash on heavy drops
        if (isDrop && beatPulse > 0.85f && isPlaying) {
          drawRect(
            color = Color.White.copy(alpha = (beatPulse - 0.85f) * 0.9f)
          )
        }

        // Animated Beat Ring Spectrum around center
        val ringRadius = (minOf(width, height) * 0.28f) * (1f + beatPulse * 0.15f)
        val numBars = 36
        for (i in 0 until numBars) {
          val angle = (i.toFloat() / numBars) * 2f * PI.toFloat()
          val barIntensity = (sin(angle * 3f + (currentPos / 180f)) * 0.5f + 0.5f) * beatPulse
          val barLen = 10f + barIntensity * 45f

          val startX = centerX + cos(angle) * ringRadius
          val startY = centerY + sin(angle) * ringRadius
          val endX = centerX + cos(angle) * (ringRadius + barLen)
          val endY = centerY + sin(angle) * (ringRadius + barLen)

          drawLine(
            color = if (i % 2 == 0) NeonCyan.copy(alpha = 0.85f) else LaserMagenta.copy(alpha = 0.85f),
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 3.5f
          )
        }

        // Particle Storm dancing to beat
        particles.forEachIndexed { idx, p ->
          val timeShift = (currentPos / 1000f) * p.speed
          val px = ((p.x + cos(p.angle) * timeShift) % 1f + 1f) % 1f * width
          val py = ((p.y + sin(p.angle) * timeShift) % 1f + 1f) % 1f * height
          val pulseSize = p.size * (1f + beatPulse * 0.6f)

          drawCircle(
            color = p.color.copy(alpha = (0.3f + beatPulse * 0.6f).coerceIn(0.2f, 0.95f)),
            radius = pulseSize,
            center = Offset(px, py)
          )
        }

        // Bottom Audio Spectrum Waves
        val wavePoints = 40
        val waveStep = width / wavePoints
        val wavePath = Path()
        wavePath.moveTo(0f, height)

        for (w in 0..wavePoints) {
          val wx = w * waveStep
          val freq = sin(w * 0.35f + (currentPos / 120f)) * 0.5f + 0.5f
          val waveH = freq * (18f + beatPulse * 45f)
          val wy = height - 12f - waveH
          if (w == 0) wavePath.lineTo(wx, wy) else wavePath.lineTo(wx, wy)
        }
        wavePath.lineTo(width, height)
        wavePath.close()

        drawPath(
          path = wavePath,
          brush = Brush.horizontalGradient(
            colors = listOf(
              NeonCyan.copy(alpha = 0.35f),
              ElectricViolet.copy(alpha = 0.45f),
              LaserMagenta.copy(alpha = 0.35f)
            )
          )
        )
      }

      // 3. Top Badges: 4K UHD, Imagination Style, Aspect Ratio
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // 4K Badge & Live FPS
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(12.dp))
            .border(1.dp, ElectricGold.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Box(
            modifier = Modifier
              .size(7.dp)
              .background(if (isPlaying) Color(0xFF00FF88) else Color(0xFFFFB800), CircleShape)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = customization.resolution.tag,
            color = ElectricGold,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }

        // Imagination Style Badge
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color.Black.copy(alpha = 0.65f),
          border = borderStroke(1.dp, ElectricViolet.copy(alpha = 0.6f))
        ) {
          Text(
            text = customization.imaginationType.title,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }
      }

      // 4. Center Play/Pause indicator overlay when paused or clicked
      AnimatedVisibility(
        visible = !isPlaying,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier.align(Alignment.Center)
      ) {
        Box(
          modifier = Modifier
            .size(64.dp)
            .background(Color.Black.copy(alpha = 0.7f), CircleShape)
            .border(2.dp, NeonCyan, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play Music Video",
            tint = NeonCyan,
            modifier = Modifier.size(36.dp)
          )
        }
      }

      // 5. Kinetic Lyrics & Scene Section Overlay at Bottom
      Column(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Active Scene Section Chip
        if (activeScene != null) {
          Text(
            text = activeScene.sectionName.uppercase(),
            color = if (isDrop) LaserMagenta else NeonCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.2.sp,
            modifier = Modifier
              .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp, vertical = 2.dp)
          )
          Spacer(modifier = Modifier.height(6.dp))
        }

        // Kinetic Karaoke Lyrics
        if (customization.showLyrics && activeScene != null && activeScene.lyrics.isNotBlank()) {
          Text(
            text = "♪ ${activeScene.lyrics} ♪",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier
              .fillMaxWidth(0.9f)
              .scale(1f + (beatPulse * 0.05f))
              .background(
                Brush.horizontalGradient(
                  colors = listOf(
                    Color.Transparent,
                    Color.Black.copy(alpha = 0.7f),
                    Color.Transparent
                  )
                )
              )
              .padding(vertical = 4.dp)
          )
        }
      }
    }
  }
}

private fun borderStroke(width: androidx.compose.ui.unit.Dp, color: Color) =
  androidx.compose.foundation.BorderStroke(width, color)
