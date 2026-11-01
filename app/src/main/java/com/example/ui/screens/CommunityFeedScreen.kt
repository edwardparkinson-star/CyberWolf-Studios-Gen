package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.CommunityPost
import com.example.ui.components.CommunityPostCard
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.LaserMagenta
import com.example.ui.theme.NeonCyan
import com.example.viewmodel.StudioUiState

@Composable
fun CommunityFeedScreen(
  uiState: StudioUiState,
  onLikeToggle: (String) -> Unit,
  onFollowToggle: (String) -> Unit,
  onPlayPost: (CommunityPost) -> Unit,
  onAddComment: (postId: String, commentText: String) -> Unit,
  onShowPublishDialog: () -> Unit,
  onDismissPublishDialog: () -> Unit,
  onUpdatePublishCaption: (String) -> Unit,
  onConfirmPublish: () -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }

  var selectedFilter by remember { mutableStateOf("All") }
  val filters = listOf("All", "Cyberpunk", "Cosmic", "Synthwave", "Surrealism", "4K Masters")

  val filteredPosts = when (selectedFilter) {
    "Cyberpunk" -> uiState.communityPosts.filter { it.imaginationStyle.contains("Cyber", true) }
    "Cosmic" -> uiState.communityPosts.filter { it.imaginationStyle.contains("Cosmic", true) }
    "Synthwave" -> uiState.communityPosts.filter { it.imaginationStyle.contains("Synth", true) }
    "Surrealism" -> uiState.communityPosts.filter { it.imaginationStyle.contains("Surreal", true) }
    "4K Masters" -> uiState.communityPosts.filter { it.is4K }
    else -> uiState.communityPosts
  }

  Box(modifier = modifier.fillMaxSize().background(Color(0xFF090812))) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Header
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
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
                text = "CyberWolf Community",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
              )
              Text(
                text = "Discover AI music videos, like & follow creators",
                color = Color(0xFF8881AA),
                fontSize = 12.sp
              )
            }
          }

          Button(
            onClick = onShowPublishDialog,
            colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
            shape = RoundedCornerShape(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            modifier = Modifier.height(34.dp)
          ) {
            Icon(
              imageVector = Icons.Default.CloudUpload,
              contentDescription = null,
              modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Publish", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      // Filter Chips
      item {
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(filters) { filter ->
            val selected = selectedFilter == filter
            FilterChip(
              selected = selected,
              onClick = { selectedFilter = filter },
              label = { Text(filter, fontSize = 11.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = ElectricViolet,
                selectedLabelColor = Color.White,
                containerColor = Color(0xFF141026),
                labelColor = Color(0xFFB5AFDB)
              ),
              border = null,
              shape = RoundedCornerShape(8.dp)
            )
          }
        }
      }

      // Post Cards
      items(filteredPosts, key = { it.id }) { post ->
        CommunityPostCard(
          post = post,
          onLikeToggle = onLikeToggle,
          onFollowToggle = onFollowToggle,
          onPlayPost = onPlayPost,
          onAddComment = onAddComment
        )
      }

      item {
        Spacer(modifier = Modifier.height(60.dp))
      }
    }
  }

  // Publish Modal Dialog
  if (uiState.showPublishDialog) {
    Dialog(onDismissRequest = onDismissPublishDialog) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131024))
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "Publish to Community Feed",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Share \"${uiState.currentTrack.title}\" (${uiState.customization.imaginationType.title}) with creators worldwide!",
            color = Color(0xFF8881AA),
            fontSize = 12.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
            value = uiState.publishCaptionInput,
            onValueChange = onUpdatePublishCaption,
            placeholder = { Text("Write a caption or description for your video...", fontSize = 12.sp, color = Color(0xFF6C658D)) },
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              focusedBorderColor = NeonCyan,
              unfocusedBorderColor = Color(0xFF28214D),
              focusedContainerColor = Color(0xFF0C0A17),
              unfocusedContainerColor = Color(0xFF0C0A17)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(100.dp)
          )

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            Button(
              onClick = onDismissPublishDialog,
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E183B)),
              shape = RoundedCornerShape(10.dp)
            ) {
              Text("Cancel", color = Color.White)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
              onClick = onConfirmPublish,
              colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
              shape = RoundedCornerShape(10.dp)
            ) {
              Text("Publish Now", color = Color.White, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}
