package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.engine.ExportProgressState
import com.example.engine.VideoExportEngine
import com.example.model.SocialPlatform
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.LaserMagenta
import com.example.ui.theme.NeonCyan

@Composable
fun Export4KDialog(
  exportState: ExportProgressState,
  onDismiss: () -> Unit,
  onPublishToCommunity: () -> Unit
) {
  val context = LocalContext.current
  val file = exportState.completedFile
  val isCompleted = exportState.isCompleted

  Dialog(onDismissRequest = { if (isCompleted) onDismiss() }) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(24.dp))
        .border(1.dp, Color(0xFF382F6B), RoundedCornerShape(24.dp)),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF110E21))
    ) {
      Column(
        modifier = Modifier.padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        if (!isCompleted) {
          // --- Rendering in progress ---
          Box(
            modifier = Modifier
              .size(56.dp)
              .background(Color(0xFF201940), CircleShape)
              .border(1.dp, ElectricGold, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.HighQuality,
              contentDescription = null,
              tint = ElectricGold,
              modifier = Modifier.size(30.dp)
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "Mastering 4K Ultra HD Clip",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "${exportState.resolution.width} x ${exportState.resolution.height} • 60 FPS • 50 Mbps",
            color = ElectricGold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
          )

          Spacer(modifier = Modifier.height(18.dp))

          LinearProgressIndicator(
            progress = { exportState.progress },
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(RoundedCornerShape(4.dp)),
            color = ElectricViolet,
            trackColor = Color(0xFF231B45)
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Frame stats and ETA
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "Frame ${exportState.currentFrame} / ${exportState.totalFrames}",
              color = Color(0xFFC4BFDF),
              fontSize = 12.sp
            )
            Text(
              text = "${(exportState.progress * 100).toInt()}%",
              color = NeonCyan,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "Speed: ${exportState.renderFps.toInt()} FPS",
              color = Color(0xFF8C86AF),
              fontSize = 11.sp
            )
            Text(
              text = "ETA: ~${exportState.estimatedSecondsRemaining}s",
              color = Color(0xFF8C86AF),
              fontSize = 11.sp
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFF191433),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = exportState.currentPass,
              color = Color(0xFFB0A9D8),
              fontSize = 11.sp,
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(10.dp)
            )
          }
        } else {
          // --- Export Complete ---
          Box(
            modifier = Modifier
              .size(56.dp)
              .background(Color(0xFF0F3A22), CircleShape)
              .border(1.dp, Color(0xFF00FF88), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = Color(0xFF00FF88),
              modifier = Modifier.size(32.dp)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "4K Music Video Exported!",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )

          Text(
            text = "Ultra HD 3840x2160 Master • 60 FPS • Stereo AAC",
            color = ElectricGold,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )

          Spacer(modifier = Modifier.height(18.dp))

          Text(
            text = "SHARE DIRECTLY TO SOCIAL PLATFORMS",
            color = Color(0xFF9F98C5),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Social Platform Share Buttons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
          ) {
            SocialShareIcon(
              name = "YouTube",
              color = Color(0xFFFF0000),
              onClick = {
                file?.let { VideoExportEngine.shareToSocialPlatform(context, it, SocialPlatform.YOUTUBE) }
              }
            )
            SocialShareIcon(
              name = "Instagram",
              color = Color(0xFFE1306C),
              onClick = {
                file?.let { VideoExportEngine.shareToSocialPlatform(context, it, SocialPlatform.INSTAGRAM) }
              }
            )
            SocialShareIcon(
              name = "TikTok",
              color = Color(0xFF00F2FE),
              onClick = {
                file?.let { VideoExportEngine.shareToSocialPlatform(context, it, SocialPlatform.TIKTOK) }
              }
            )
            SocialShareIcon(
              name = "Facebook",
              color = Color(0xFF1877F2),
              onClick = {
                file?.let { VideoExportEngine.shareToSocialPlatform(context, it, SocialPlatform.FACEBOOK) }
              }
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Publish to CyberWolf Community Feed
          Button(
            onClick = onPublishToCommunity,
            colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("publish_to_community_button")
          ) {
            Icon(
              imageVector = Icons.Default.CloudUpload,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Publish to CyberWolf Community", fontWeight = FontWeight.Bold)
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Android System Share
          OutlinedButton(
            onClick = {
              file?.let {
                val intent = VideoExportEngine.createShareIntent(context, it)
                context.startActivity(android.content.Intent.createChooser(intent, "Share 4K Video"))
              }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = null,
              tint = NeonCyan,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("More Apps / Save to Files", color = NeonCyan)
          }

          Spacer(modifier = Modifier.height(6.dp))

          Button(
            onClick = onDismiss,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E183B)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Done", color = Color.White)
          }
        }
      }
    }
  }
}

@Composable
private fun SocialShareIcon(
  name: String,
  color: Color,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clip(RoundedCornerShape(12.dp))
      .clickable { onClick() }
      .padding(6.dp)
  ) {
    Box(
      modifier = Modifier
        .size(46.dp)
        .background(color.copy(alpha = 0.2f), CircleShape)
        .border(1.5.dp, color, CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = name.take(2).uppercase(),
        color = color,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 13.sp
      )
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = name,
      color = Color.White,
      fontSize = 11.sp,
      fontWeight = FontWeight.Medium
    )
  }
}
