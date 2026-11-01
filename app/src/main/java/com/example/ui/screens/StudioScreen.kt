package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.PlayerPlaybackState
import com.example.data.StudioPresets
import com.example.model.VideoAspectRatio
import com.example.ui.components.BeatReactiveVisualizer
import com.example.ui.components.WaveformTimeline
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.LaserMagenta
import com.example.ui.theme.NeonCyan
import com.example.viewmodel.AppScreen
import com.example.viewmodel.StudioUiState

@Composable
fun StudioScreen(
  uiState: StudioUiState,
  playbackState: PlayerPlaybackState,
  onTogglePlay: () -> Unit,
  onSeek: (Long) -> Unit,
  onSectionClick: (com.example.model.SongSection) -> Unit,
  onSelectAspectRatio: (VideoAspectRatio) -> Unit,
  onSelectImagination: (com.example.model.ImaginationType) -> Unit,
  onStartExport4K: () -> Unit,
  onNavigate: (AppScreen) -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()
  val track = uiState.currentTrack
  val customization = uiState.customization
  val currentPos = playbackState.positionMs

  val currentScene = uiState.generatedScenes.firstOrNull { currentPos in it.startMs until it.endMs }
    ?: uiState.generatedScenes.firstOrNull()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFF090812))
      .verticalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    // Top Studio Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .background(NeonCyan, CircleShape)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "CYBERWOLF STUDIOS",
            color = NeonCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.5.sp
          )
        }
        Text(
          text = track.title,
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        )
      }

      // Primary Export 4K Button
      Button(
        onClick = onStartExport4K,
        colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.testTag("export_4k_primary_button")
      ) {
        Icon(
          imageVector = Icons.Default.HighQuality,
          contentDescription = null,
          tint = ElectricGold,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Export 4K",
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Main Beat Reactive Visualizer Player
    BeatReactiveVisualizer(
      playbackState = playbackState,
      customization = customization,
      scenes = uiState.generatedScenes,
      onTogglePlay = onTogglePlay
    )

    Spacer(modifier = Modifier.height(14.dp))

    // Aspect Ratio & Quick Visual Style Switcher Row
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Aspect Ratio Selector
      Row(
        modifier = Modifier
          .background(Color(0xFF131024), RoundedCornerShape(10.dp))
          .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        VideoAspectRatio.values().forEach { ratio ->
          val selected = customization.aspectRatio == ratio
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(if (selected) ElectricViolet else Color.Transparent)
              .clickable { onSelectAspectRatio(ratio) }
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = ratio.iconLabel,
              color = if (selected) Color.White else Color(0xFF8C86AF),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      // Customize Button
      OutlinedButton(
        onClick = { onNavigate(AppScreen.CUSTOMIZE) },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
        modifier = Modifier.height(34.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Tune,
          contentDescription = null,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text("Customize FX", fontSize = 11.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Quick Imagination Style Slider
    Text(
      text = "IMAGINATION STYLE",
      color = Color(0xFF8C86AF),
      fontSize = 10.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.sp
    )
    Spacer(modifier = Modifier.height(6.dp))

    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(StudioPresets.imaginationTypes.take(6)) { style ->
        val isSelected = customization.imaginationType.id == style.id
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = if (isSelected) ElectricViolet else Color(0xFF151226),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) NeonCyan else Color(0xFF262044)
          ),
          modifier = Modifier.clickable { onSelectImagination(style) }
        ) {
          Text(
            text = style.title,
            color = if (isSelected) Color.White else Color(0xFFBDB8DB),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Interactive Waveform Timeline
    WaveformTimeline(
      track = track,
      playbackState = playbackState,
      onTogglePlay = onTogglePlay,
      onSeek = onSeek,
      onSectionClick = onSectionClick
    )

    Spacer(modifier = Modifier.height(14.dp))

    // Active AI Storyboard Scene Card
    if (currentScene != null) {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131024))
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = NeonCyan,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "ACTIVE SCENE STORYBOARD PROMPT",
                color = NeonCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFF231C3D)
            ) {
              Text(
                text = "${currentScene.sectionName}",
                color = if (currentScene.isDrop) LaserMagenta else Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = currentScene.prompt,
            color = Color(0xFFDDD8F5),
            fontSize = 12.sp,
            lineHeight = 17.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Quick Action Bar: Upload Music, Customize, Community
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      StudioShortcutCard(
        title = "Upload Music",
        subtitle = "Change track",
        icon = Icons.Default.LibraryMusic,
        color = NeonCyan,
        onClick = { onNavigate(AppScreen.UPLOAD) },
        modifier = Modifier.weight(1f)
      )
      StudioShortcutCard(
        title = "Visuals & FX",
        subtitle = "Styles & 4K",
        icon = Icons.Default.Palette,
        color = ElectricViolet,
        onClick = { onNavigate(AppScreen.CUSTOMIZE) },
        modifier = Modifier.weight(1f)
      )
      StudioShortcutCard(
        title = "Community",
        subtitle = "Trending videos",
        icon = Icons.Default.People,
        color = LaserMagenta,
        onClick = { onNavigate(AppScreen.COMMUNITY) },
        modifier = Modifier.weight(1f)
      )
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
private fun StudioShortcutCard(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.clickable { onClick() },
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFF131024))
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .background(color.copy(alpha = 0.2f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = color,
          modifier = Modifier.size(18.dp)
        )
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = title,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
      )
      Text(
        text = subtitle,
        color = Color(0xFF8881AA),
        fontSize = 10.sp
      )
    }
  }
}
