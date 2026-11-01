package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.PlayerPlaybackState
import com.example.model.AudioTrack
import com.example.model.SongSection
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.LaserMagenta
import com.example.ui.theme.NeonCyan
import java.util.Locale

@Composable
fun WaveformTimeline(
  track: AudioTrack,
  playbackState: PlayerPlaybackState,
  onTogglePlay: () -> Unit,
  onSeek: (Long) -> Unit,
  onSectionClick: (SongSection) -> Unit,
  modifier: Modifier = Modifier
) {
  val currentPos = playbackState.positionMs
  val totalDuration = playbackState.durationMs.coerceAtLeast(1L)
  val progress = (currentPos.toFloat() / totalDuration).coerceIn(0f, 1f)

  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .background(Color(0xFF131024))
      .padding(16.dp)
      .testTag("waveform_timeline_container")
  ) {
    // 1. Time & BPM Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .background(if (playbackState.isPlaying) NeonCyan else Color.Gray, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "${formatTime(currentPos)} / ${formatTime(totalDuration)}",
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )
      }

      Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF1F1A3A)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Whatshot,
            contentDescription = null,
            tint = ElectricGold,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "${track.bpm} BPM",
            color = ElectricGold,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // 2. Interactive Waveform Visual Canvas
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(Color(0xFF0A0914))
        .pointerInput(totalDuration) {
          detectTapGestures { offset ->
            val seekFrac = (offset.x / size.width).coerceIn(0f, 1f)
            val seekMs = (seekFrac * totalDuration).toLong()
            onSeek(seekMs)
          }
        }
    ) {
      Canvas(modifier = Modifier.fillMaxWidth().height(56.dp)) {
        val width = size.width
        val height = size.height
        val samples = track.waveform
        val barCount = samples.size
        val barWidth = width / barCount
        val playheadX = progress * width

        for (i in 0 until barCount) {
          val x = i * barWidth
          val sampleHeight = (samples[i] * height * 0.85f).coerceAtLeast(6f)
          val y = (height - sampleHeight) / 2f

          val isPast = x <= playheadX
          val barColor = when {
            isPast -> NeonCyan
            else -> Color(0xFF383163)
          }

          drawRoundRect(
            color = barColor,
            topLeft = Offset(x + (barWidth * 0.15f), y),
            size = Size(barWidth * 0.7f, sampleHeight),
            cornerRadius = CornerRadius(4f, 4f)
          )
        }

        // Draw Playhead line
        drawLine(
          color = Color.White,
          start = Offset(playheadX, 0f),
          end = Offset(playheadX, height),
          strokeWidth = 3f
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // 3. Section Jump Chips (Intro, Verse, Drop, Outro)
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(track.sections) { section ->
        val isActive = currentPos in section.startMs until section.endMs
        AssistChip(
          onClick = { onSectionClick(section) },
          label = {
            Text(
              text = section.name,
              fontSize = 11.sp,
              fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
            )
          },
          colors = AssistChipDefaults.assistChipColors(
            containerColor = when {
              isActive && section.isDropOrChorus -> LaserMagenta
              isActive -> ElectricViolet
              section.isDropOrChorus -> Color(0xFF3A122E)
              else -> Color(0xFF1E1936)
            },
            labelColor = Color.White
          ),
          border = null,
          shape = RoundedCornerShape(8.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // 4. Center Play / Rewind / Fast Forward Controls
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      FilledIconButton(
        onClick = { onSeek((currentPos - 5000L).coerceAtLeast(0L)) },
        colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFF1F1A3A)),
        modifier = Modifier.size(42.dp)
      ) {
        Icon(
          imageVector = Icons.Default.FastRewind,
          contentDescription = "Rewind 5s",
          tint = Color.White
        )
      }

      Spacer(modifier = Modifier.width(18.dp))

      FilledIconButton(
        onClick = onTogglePlay,
        colors = IconButtonDefaults.filledIconButtonColors(containerColor = ElectricViolet),
        modifier = Modifier.size(54.dp)
      ) {
        Icon(
          imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
          contentDescription = if (playbackState.isPlaying) "Pause" else "Play",
          tint = Color.White,
          modifier = Modifier.size(30.dp)
        )
      }

      Spacer(modifier = Modifier.width(18.dp))

      FilledIconButton(
        onClick = { onSeek((currentPos + 5000L).coerceAtMost(totalDuration)) },
        colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFF1F1A3A)),
        modifier = Modifier.size(42.dp)
      ) {
        Icon(
          imageVector = Icons.Default.FastForward,
          contentDescription = "Fast Forward 5s",
          tint = Color.White
        )
      }
    }
  }
}

private fun formatTime(ms: Long): String {
  val totalSec = ms / 1000
  val minutes = totalSec / 60
  val seconds = totalSec % 60
  return String.format(Locale.US, "%d:%02d", minutes, seconds)
}
