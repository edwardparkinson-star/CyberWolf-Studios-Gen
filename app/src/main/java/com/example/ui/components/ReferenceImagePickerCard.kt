package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.LaserMagenta
import com.example.ui.theme.NeonCyan

@Composable
fun ReferenceImagePickerCard(
  referenceImageUri: String?,
  referenceWeight: Float,
  onImageSelected: (Uri?) -> Unit,
  onWeightChanged: (Float) -> Unit,
  modifier: Modifier = Modifier
) {
  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      onImageSelected(uri)
    }
  }

  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFF131024))
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
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
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Reference Image (Aesthetic Guide)",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
        }

        if (referenceImageUri != null) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFF261D47)
          ) {
            Text(
              text = "ACTIVE",
              color = NeonCyan,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      if (referenceImageUri != null) {
        // Preview Active Reference Image
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0C0A17))
            .padding(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          AsyncImage(
            model = Uri.parse(referenceImageUri),
            contentDescription = "Selected Reference Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .size(68.dp)
              .clip(RoundedCornerShape(10.dp))
              .border(1.dp, NeonCyan.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
          )

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Style & Palette Extracted",
              color = Color.White,
              fontWeight = FontWeight.SemiBold,
              fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "AI will blend visual textures, lighting & colors into scene prompts",
              color = Color(0xFF9E98BF),
              fontSize = 11.sp,
              lineHeight = 15.sp
            )
          }

          IconButton(
            onClick = { onImageSelected(null) },
            modifier = Modifier
              .size(32.dp)
              .background(Color(0xFF231C3D), CircleShape)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Remove reference image",
              tint = LaserMagenta,
              modifier = Modifier.size(16.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Weight Slider
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Tune,
              contentDescription = null,
              tint = ElectricGold,
              modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Aesthetic Influence Weight",
              color = Color(0xFFC7C2E4),
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          }
          Text(
            text = "${(referenceWeight * 100).toInt()}%",
            color = ElectricGold,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Slider(
          value = referenceWeight,
          onValueChange = onWeightChanged,
          valueRange = 0.1f..1.0f,
          colors = SliderDefaults.colors(
            thumbColor = ElectricGold,
            activeTrackColor = ElectricGold,
            inactiveTrackColor = Color(0xFF2A2448)
          ),
          modifier = Modifier.testTag("reference_weight_slider")
        )
      } else {
        // Upload Button
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0C0A17))
            .border(1.dp, Color(0xFF2E2756), RoundedCornerShape(12.dp))
            .clickable {
              photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
              )
            }
            .padding(vertical = 18.dp, horizontal = 16.dp)
            .testTag("upload_reference_image_button"),
          contentAlignment = Alignment.Center
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AddPhotoAlternate,
              contentDescription = null,
              tint = NeonCyan,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Select Reference Mood Image",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
              )
              Text(
                text = "Pick photo from gallery to guide AI colors and visual mood",
                color = Color(0xFF8881AA),
                fontSize = 11.sp
              )
            }
          }
        }
      }
    }
  }
}
