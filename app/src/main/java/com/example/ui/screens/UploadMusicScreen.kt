package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudioPresets
import com.example.model.AudioTrack
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.LaserMagenta
import com.example.ui.theme.NeonCyan
import com.example.viewmodel.StudioUiState

@Composable
fun UploadMusicScreen(
  uiState: StudioUiState,
  onSelectTrack: (AudioTrack) -> Unit,
  onUploadAudioFile: (Uri) -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }
  val scrollState = rememberScrollState()

  val audioPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri: Uri? ->
    if (uri != null) {
      onUploadAudioFile(uri)
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFF090812))
      .verticalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    // Top Bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onNavigateBack,
        modifier = Modifier
          .size(40.dp)
          .background(Color(0xFF16122C), CircleShape)
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = Color.White
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = "Music Upload & Audio Engine",
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        )
        Text(
          text = "Upload MP3/WAV or select studio demo tracks",
          color = Color(0xFF8881AA),
          fontSize = 12.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // 1. Upload Your Own Music File Action Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .border(1.5.dp, NeonCyan, RoundedCornerShape(20.dp))
        .clickable { audioPickerLauncher.launch("audio/*") }
        .testTag("upload_audio_file_card"),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF131024))
    ) {
      Column(
        modifier = Modifier.padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        if (uiState.isAnalyzingAudio) {
          CircularProgressIndicator(
            color = NeonCyan,
            modifier = Modifier.size(48.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "Analyzing Beat, BPM & Energy...",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
          Text(
            text = "Extracting rhythm grid and lyric markers",
            color = Color(0xFF8881AA),
            fontSize = 12.sp
          )
        } else {
          Box(
            modifier = Modifier
              .size(60.dp)
              .background(NeonCyan.copy(alpha = 0.15f), CircleShape)
              .border(1.5.dp, NeonCyan, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.UploadFile,
              contentDescription = null,
              tint = NeonCyan,
              modifier = Modifier.size(32.dp)
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "Upload Your Music File",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "Tap to pick MP3, WAV, AAC, or M4A from your device",
            color = Color(0xFFB5AFDB),
            fontSize = 12.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF231C3F)
          ) {
            Text(
              text = "Supports Zero-Storage-Permission System Picker",
              color = NeonCyan,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // 2. Curated Studio Master Tracks
    Text(
      text = "OR SELECT CURATED STUDIO MASTER TRACKS",
      color = Color(0xFF8881AA),
      fontWeight = FontWeight.Bold,
      fontSize = 11.sp,
      letterSpacing = 1.sp
    )

    Spacer(modifier = Modifier.height(10.dp))

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      StudioPresets.demoTracks.forEach { demoTrack ->
        val isSelected = uiState.currentTrack.id == demoTrack.id
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectTrack(demoTrack) }
            .testTag("demo_track_${demoTrack.id}"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF1E1838) else Color(0xFF131024)
          ),
          border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, ElectricViolet) else null
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .background(
                  if (isSelected) ElectricViolet else Color(0xFF1F1A3A),
                  CircleShape
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = if (isSelected) Color.White else NeonCyan,
                modifier = Modifier.size(24.dp)
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = demoTrack.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
              Text(
                text = "${demoTrack.artist} • ${demoTrack.durationMs / 1000}s",
                color = Color(0xFF8881AA),
                fontSize = 11.sp
              )
              Spacer(modifier = Modifier.height(4.dp))

              Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = Color(0xFF281C3F)
                ) {
                  Text(
                    text = "${demoTrack.bpm} BPM",
                    color = ElectricGold,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = Color(0xFF1D1B36)
                ) {
                  Text(
                    text = "${demoTrack.sections.size} Sections Synced",
                    color = NeonCyan,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
            }

            if (isSelected) {
              Box(
                modifier = Modifier
                  .size(26.dp)
                  .background(ElectricViolet, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = "Active",
                  tint = Color.White,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}
