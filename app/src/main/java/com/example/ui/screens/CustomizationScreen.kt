package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraRoll
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudioPresets
import com.example.model.CameraMotion
import com.example.model.ColorPalette
import com.example.model.ExportResolution
import com.example.model.ImaginationType
import com.example.model.VisualCustomization
import com.example.ui.components.ReferenceImagePickerCard
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.LaserMagenta
import com.example.ui.theme.NeonCyan
import com.example.viewmodel.AppScreen
import com.example.viewmodel.StudioUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomizationScreen(
  uiState: StudioUiState,
  onSelectImagination: (ImaginationType) -> Unit,
  onSelectPalette: (ColorPalette) -> Unit,
  onSelectReferenceImage: (Uri?) -> Unit,
  onSetReferenceWeight: (Float) -> Unit,
  onUpdateCustomization: ((VisualCustomization) -> VisualCustomization) -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }
  val scrollState = rememberScrollState()
  val customization = uiState.customization

  var selectedCategoryFilter by remember { mutableStateOf("All") }
  var customNotes by remember(customization.customPromptNotes) { mutableStateOf(customization.customPromptNotes) }

  val categories = listOf("All", "Sci-Fi & Cyber", "Space & Cosmic", "Retro & Vintage", "Surrealism", "Abstract", "Club & Rave", "Animation", "Cinematic")

  val filteredTypes = if (selectedCategoryFilter == "All") {
    StudioPresets.imaginationTypes
  } else {
    StudioPresets.imaginationTypes.filter { it.category == selectedCategoryFilter }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFF090812))
      .verticalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    // Header
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
          contentDescription = "Back to Studio",
          tint = Color.White
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = "Visual Customization",
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        )
        Text(
          text = "Imagination styles, palettes, reference photos & 4K",
          color = Color(0xFF8881AA),
          fontSize = 12.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // 1. SECTION: Wide Variety of Imagination Types
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "1. IMAGINATION TYPES (${StudioPresets.imaginationTypes.size} VARIETIES)",
        color = NeonCyan,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 12.sp,
        letterSpacing = 1.sp
      )
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Category Filter Chips
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(categories) { category ->
        val selected = selectedCategoryFilter == category
        FilterChip(
          selected = selected,
          onClick = { selectedCategoryFilter = category },
          label = { Text(category, fontSize = 11.sp) },
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

    Spacer(modifier = Modifier.height(12.dp))

    // Imagination Cards Grid
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      filteredTypes.forEach { type ->
        val isSelected = customization.imaginationType.id == type.id
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectImagination(type) }
            .testTag("imagination_type_${type.id}"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF1B1538) else Color(0xFF131024)
          ),
          border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, NeonCyan) else null
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Image(
              painter = painterResource(id = type.fallbackDrawable),
              contentDescription = type.title,
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(
                  text = type.title,
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = Color(0xFF281F48)
                ) {
                  Text(
                    text = type.category,
                    color = LaserMagenta,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(3.dp))

              Text(
                text = type.tagLine,
                color = Color(0xFFB0A9D3),
                fontSize = 11.sp,
                lineHeight = 15.sp
              )

              Spacer(modifier = Modifier.height(6.dp))

              // Color Swatch Bar
              Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                type.dominantColors.forEach { c ->
                  Box(
                    modifier = Modifier
                      .size(10.dp)
                      .background(Color(c), CircleShape)
                  )
                }
              }
            }

            if (isSelected) {
              Spacer(modifier = Modifier.width(8.dp))
              Box(
                modifier = Modifier
                  .size(26.dp)
                  .background(NeonCyan, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = "Selected",
                  tint = Color(0xFF090812),
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(22.dp))

    // 2. SECTION: Color Palettes
    Text(
      text = "2. COLOR PALETTE SELECTION",
      color = NeonCyan,
      fontWeight = FontWeight.ExtraBold,
      fontSize = 12.sp,
      letterSpacing = 1.sp
    )
    Spacer(modifier = Modifier.height(10.dp))

    FlowRow(
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      StudioPresets.palettes.forEach { palette ->
        val isSelected = customization.palette.id == palette.id
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (isSelected) Color(0xFF211942) else Color(0xFF131024),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) ElectricViolet else Color(0xFF231B45)
          ),
          modifier = Modifier
            .clickable { onSelectPalette(palette) }
            .testTag("palette_${palette.id}")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              palette.colors.take(3).forEach { hex ->
                Box(
                  modifier = Modifier
                    .size(14.dp)
                    .background(Color(hex), CircleShape)
                )
              }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = palette.name,
              color = if (isSelected) Color.White else Color(0xFFB5AFDB),
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(22.dp))

    // 3. SECTION: Reference Image Upload (Aesthetic Influence)
    Text(
      text = "3. REFERENCE IMAGE (AESTHETIC INFLUENCE)",
      color = NeonCyan,
      fontWeight = FontWeight.ExtraBold,
      fontSize = 12.sp,
      letterSpacing = 1.sp
    )
    Spacer(modifier = Modifier.height(8.dp))

    ReferenceImagePickerCard(
      referenceImageUri = customization.referenceImageUri,
      referenceWeight = customization.referenceWeight,
      onImageSelected = onSelectReferenceImage,
      onWeightChanged = onSetReferenceWeight
    )

    Spacer(modifier = Modifier.height(22.dp))

    // 4. SECTION: Camera Motion Dynamics
    Text(
      text = "4. CAMERA MOTION DYNAMICS",
      color = NeonCyan,
      fontWeight = FontWeight.ExtraBold,
      fontSize = 12.sp,
      letterSpacing = 1.sp
    )
    Spacer(modifier = Modifier.height(10.dp))

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      CameraMotion.values().forEach { motion ->
        val isSelected = customization.cameraMotion == motion
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (isSelected) Color(0xFF211942) else Color(0xFF131024),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) LaserMagenta else Color(0xFF261D47)
          ),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onUpdateCustomization { it.copy(cameraMotion = motion) } }
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Videocam,
              contentDescription = null,
              tint = if (isSelected) LaserMagenta else Color(0xFF8881AA),
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = motion.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
              Text(
                text = motion.subtitle,
                color = Color(0xFF8881AA),
                fontSize = 11.sp
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(22.dp))

    // 5. SECTION: 4K Resolution & Render Settings
    Text(
      text = "5. EXPORT RESOLUTION (4K ULTRA HD)",
      color = ElectricGold,
      fontWeight = FontWeight.ExtraBold,
      fontSize = 12.sp,
      letterSpacing = 1.sp
    )
    Spacer(modifier = Modifier.height(10.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      ExportResolution.values().forEach { res ->
        val isSelected = customization.resolution == res
        Card(
          modifier = Modifier
            .weight(1f)
            .clickable { onUpdateCustomization { it.copy(resolution = res) } },
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF2A2148) else Color(0xFF131024)
          ),
          border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, ElectricGold) else null
        ) {
          Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = if (res == ExportResolution.UHD_4K) "4K UHD" else if (res == ExportResolution.VERTICAL_4K) "4K 9:16" else res.label.substringBefore(" "),
              color = if (isSelected) ElectricGold else Color.White,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "${res.width}p",
              color = Color(0xFF8C86AF),
              fontSize = 10.sp
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(22.dp))

    // 6. SECTION: Beat Sensitivity & Particle Sliders
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF131024))
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Beat Dynamics & FX Controls",
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Beat Reactivity
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("Kick Beat Pulse Zoom", color = Color(0xFFB5AFDB), fontSize = 12.sp)
          Text("${(customization.beatReactivity * 100).toInt()}%", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Slider(
          value = customization.beatReactivity,
          onValueChange = { newVal -> onUpdateCustomization { it.copy(beatReactivity = newVal) } },
          colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Particle Density
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("Beat Particle Stardust Density", color = Color(0xFFB5AFDB), fontSize = 12.sp)
          Text("${(customization.particleDensity * 100).toInt()}%", color = LaserMagenta, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Slider(
          value = customization.particleDensity,
          onValueChange = { newVal -> onUpdateCustomization { it.copy(particleDensity = newVal) } },
          colors = SliderDefaults.colors(thumbColor = LaserMagenta, activeTrackColor = LaserMagenta)
        )
      }
    }

    Spacer(modifier = Modifier.height(22.dp))

    // 7. Creator Prompt Notes Input
    Text(
      text = "CUSTOM CREATOR PROMPT NOTES (OPTIONAL)",
      color = Color(0xFF8C86AF),
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 1.sp
    )
    Spacer(modifier = Modifier.height(6.dp))
    OutlinedTextField(
      value = customNotes,
      onValueChange = {
        customNotes = it
        onUpdateCustomization { c -> c.copy(customPromptNotes = it) }
      },
      placeholder = { Text("e.g. Include purple lightning strikes on chorus, anamorphic lens flares...", fontSize = 12.sp, color = Color(0xFF5E587E)) },
      colors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = NeonCyan,
        unfocusedBorderColor = Color(0xFF28214D),
        focusedContainerColor = Color(0xFF131024),
        unfocusedContainerColor = Color(0xFF131024)
      ),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Apply Button
    Button(
      onClick = onNavigateBack,
      colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
      shape = RoundedCornerShape(14.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .testTag("apply_customization_button")
    ) {
      Icon(
        imageVector = Icons.Default.AutoAwesome,
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text("Apply & Launch in Studio", fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}
