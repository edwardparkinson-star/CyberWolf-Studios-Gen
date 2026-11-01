package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CommunityPost
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.LaserMagenta
import com.example.ui.theme.NeonCyan

@Composable
fun CommunityPostCard(
  post: CommunityPost,
  onLikeToggle: (String) -> Unit,
  onFollowToggle: (String) -> Unit,
  onPlayPost: (CommunityPost) -> Unit,
  onAddComment: (postId: String, commentText: String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var showComments by remember { mutableStateOf(false) }
  var commentInput by remember { mutableStateOf("") }

  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFF131024))
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // 1. Creator Profile Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .background(Color(post.creatorAvatarColor), CircleShape)
              .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = post.creatorName.take(1).uppercase(),
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = post.creatorName,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
            Text(
              text = "${post.creatorHandle} • ${post.createdAt}",
              color = Color(0xFF8E88B1),
              fontSize = 11.sp
            )
          }
        }

        // Follow / Following Button
        Button(
          onClick = { onFollowToggle(post.id) },
          colors = ButtonDefaults.buttonColors(
            containerColor = if (post.isFollowing) Color(0xFF221C3F) else ElectricViolet
          ),
          shape = RoundedCornerShape(20.dp),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 4.dp),
          modifier = Modifier.height(32.dp)
        ) {
          Icon(
            imageVector = if (post.isFollowing) Icons.Default.PersonRemove else Icons.Default.PersonAdd,
            contentDescription = null,
            tint = if (post.isFollowing) NeonCyan else Color.White,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (post.isFollowing) "Following" else "Follow",
            color = if (post.isFollowing) NeonCyan else Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 2. Video Title & Caption
      Text(
        text = post.videoTitle,
        color = Color.White,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 16.sp
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = post.caption,
        color = Color(0xFFC7C2E4),
        fontSize = 13.sp,
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      // 3. Audio & Style Tags
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFF1E1838)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.GraphicEq,
              contentDescription = null,
              tint = NeonCyan,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${post.trackTitle} (${post.bpm} BPM)",
              color = NeonCyan,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFF281C3D)
        ) {
          Text(
            text = post.imaginationStyle,
            color = LaserMagenta,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 4. Video Preview Thumbnail with 4K badge and Play overlay
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(16f / 9f)
          .clip(RoundedCornerShape(14.dp))
          .clickable { onPlayPost(post) }
      ) {
        Image(
          painter = painterResource(id = post.imageRes),
          contentDescription = "Video Thumbnail",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxWidth()
        )

        // Dark tint
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.3f))
        )

        // 4K Badge
        if (post.is4K) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color.Black.copy(alpha = 0.75f),
            border = androidx.compose.foundation.BorderStroke(1.dp, ElectricGold),
            modifier = Modifier
              .align(Alignment.TopStart)
              .padding(10.dp)
          ) {
            Text(
              text = "4K ULTRA HD",
              color = ElectricGold,
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        // Center Play Icon
        Icon(
          imageVector = Icons.Default.PlayCircleFilled,
          contentDescription = "Play Video",
          tint = Color.White.copy(alpha = 0.9f),
          modifier = Modifier
            .size(54.dp)
            .align(Alignment.Center)
        )

        // Duration Tag
        Surface(
          shape = RoundedCornerShape(4.dp),
          color = Color.Black.copy(alpha = 0.75f),
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(10.dp)
        ) {
          Text(
            text = "0:${post.durationSec}",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 5. Interactive Action Bar: Like, Comments, Views, Share
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Like Button
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .clickable { onLikeToggle(post.id) }
              .padding(4.dp)
          ) {
            Icon(
              imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Like",
              tint = if (post.isLiked) LaserMagenta else Color(0xFFC7C2E4),
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
              text = "${post.likeCount}",
              color = if (post.isLiked) LaserMagenta else Color(0xFFC7C2E4),
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.width(16.dp))

          // Comment Button
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .clickable { showComments = !showComments }
              .padding(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.ChatBubbleOutline,
              contentDescription = "Comments",
              tint = if (showComments) NeonCyan else Color(0xFFC7C2E4),
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
              text = "${post.comments.size}",
              color = if (showComments) NeonCyan else Color(0xFFC7C2E4),
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Visibility,
            contentDescription = null,
            tint = Color(0xFF8881A8),
            modifier = Modifier.size(15.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "${post.views}",
            color = Color(0xFF8881A8),
            fontSize = 12.sp
          )
          Spacer(modifier = Modifier.width(12.dp))
          IconButton(
            onClick = {
              val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                  android.content.Intent.EXTRA_TEXT,
                  "Watch \"${post.videoTitle}\" by ${post.creatorName} on CyberWolf Studios Gen! #AIMusicVideo"
                )
              }
              context.startActivity(android.content.Intent.createChooser(shareIntent, "Share Post"))
            },
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Share",
              tint = Color(0xFFC7C2E4),
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }

      // 6. Expandable Comments Section
      AnimatedVisibility(visible = showComments) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0C0A17))
            .padding(12.dp)
        ) {
          Text(
            text = "Community Comments (${post.comments.size})",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )

          Spacer(modifier = Modifier.height(8.dp))

          // List comments
          post.comments.forEach { comment ->
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = comment.author,
                  color = NeonCyan,
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 12.sp
                )
                Text(
                  text = comment.timestamp,
                  color = Color(0xFF756F97),
                  fontSize = 10.sp
                )
              }
              Text(
                text = comment.text,
                color = Color(0xFFDFDBF2),
                fontSize = 12.sp,
                lineHeight = 16.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Add Comment Input
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = commentInput,
              onValueChange = { commentInput = it },
              placeholder = { Text("Add to conversation...", fontSize = 12.sp, color = Color(0xFF6A648C)) },
              colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = ElectricViolet,
                unfocusedBorderColor = Color(0xFF2C2454),
                focusedContainerColor = Color(0xFF131024),
                unfocusedContainerColor = Color(0xFF131024)
              ),
              shape = RoundedCornerShape(20.dp),
              modifier = Modifier
                .weight(1f)
                .height(48.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
              onClick = {
                if (commentInput.isNotBlank()) {
                  onAddComment(post.id, commentInput.trim())
                  commentInput = ""
                }
              },
              modifier = Modifier
                .size(42.dp)
                .background(ElectricViolet, CircleShape)
            ) {
              Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send Comment",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }
    }
  }
}
