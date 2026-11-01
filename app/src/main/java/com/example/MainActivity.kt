package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.Export4KDialog
import com.example.ui.screens.CommunityFeedScreen
import com.example.ui.screens.CustomizationScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.StudioScreen
import com.example.ui.screens.UploadMusicScreen
import com.example.ui.theme.ElectricGold
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.LaserMagenta
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.viewmodel.AppScreen
import com.example.viewmodel.StudioViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        CyberWolfAppRoot()
      }
    }
  }
}

@Composable
fun CyberWolfAppRoot(
  viewModel: StudioViewModel = viewModel()
) {
  val context = LocalContext.current
  val uiState by viewModel.uiState.collectAsState()
  val playbackState by viewModel.playbackState.collectAsState()
  val exportState by viewModel.exportState.collectAsState()
  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(uiState.toastMessage) {
    uiState.toastMessage?.let { msg ->
      Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
      viewModel.clearToast()
    }
  }

  Scaffold(
    contentWindowInsets = WindowInsets.systemBars,
    snackbarHost = { SnackbarHost(snackbarHostState) },
    bottomBar = {
      NavigationBar(
        containerColor = Color(0xFF0C0A17),
        tonalElevation = 8.dp
      ) {
        NavigationBarItem(
          selected = uiState.currentScreen == AppScreen.STUDIO,
          onClick = { viewModel.navigateTo(AppScreen.STUDIO) },
          icon = {
            Icon(
              imageVector = Icons.Default.SmartDisplay,
              contentDescription = "Studio"
            )
          },
          label = {
            Text(
              "Studio",
              fontSize = 11.sp,
              fontWeight = if (uiState.currentScreen == AppScreen.STUDIO) FontWeight.Bold else FontWeight.Normal
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = NeonCyan,
            selectedTextColor = NeonCyan,
            indicatorColor = Color(0xFF1E173D),
            unselectedIconColor = Color(0xFF7B759F),
            unselectedTextColor = Color(0xFF7B759F)
          ),
          modifier = Modifier.testTag("nav_studio")
        )

        NavigationBarItem(
          selected = uiState.currentScreen == AppScreen.CUSTOMIZE,
          onClick = { viewModel.navigateTo(AppScreen.CUSTOMIZE) },
          icon = {
            Icon(
              imageVector = Icons.Default.Palette,
              contentDescription = "Visuals"
            )
          },
          label = {
            Text(
              "Visuals",
              fontSize = 11.sp,
              fontWeight = if (uiState.currentScreen == AppScreen.CUSTOMIZE) FontWeight.Bold else FontWeight.Normal
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = ElectricViolet,
            selectedTextColor = ElectricViolet,
            indicatorColor = Color(0xFF1E173D),
            unselectedIconColor = Color(0xFF7B759F),
            unselectedTextColor = Color(0xFF7B759F)
          ),
          modifier = Modifier.testTag("nav_visuals")
        )

        NavigationBarItem(
          selected = uiState.currentScreen == AppScreen.UPLOAD,
          onClick = { viewModel.navigateTo(AppScreen.UPLOAD) },
          icon = {
            Icon(
              imageVector = Icons.Default.LibraryMusic,
              contentDescription = "Music"
            )
          },
          label = {
            Text(
              "Music",
              fontSize = 11.sp,
              fontWeight = if (uiState.currentScreen == AppScreen.UPLOAD) FontWeight.Bold else FontWeight.Normal
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = LaserMagenta,
            selectedTextColor = LaserMagenta,
            indicatorColor = Color(0xFF1E173D),
            unselectedIconColor = Color(0xFF7B759F),
            unselectedTextColor = Color(0xFF7B759F)
          ),
          modifier = Modifier.testTag("nav_music")
        )

        NavigationBarItem(
          selected = uiState.currentScreen == AppScreen.COMMUNITY,
          onClick = { viewModel.navigateTo(AppScreen.COMMUNITY) },
          icon = {
            Icon(
              imageVector = Icons.Default.People,
              contentDescription = "Community"
            )
          },
          label = {
            Text(
              "Community",
              fontSize = 11.sp,
              fontWeight = if (uiState.currentScreen == AppScreen.COMMUNITY) FontWeight.Bold else FontWeight.Normal
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = ElectricGold,
            selectedTextColor = ElectricGold,
            indicatorColor = Color(0xFF1E173D),
            unselectedIconColor = Color(0xFF7B759F),
            unselectedTextColor = Color(0xFF7B759F)
          ),
          modifier = Modifier.testTag("nav_community")
        )

        NavigationBarItem(
          selected = uiState.currentScreen == AppScreen.LIBRARY,
          onClick = { viewModel.navigateTo(AppScreen.LIBRARY) },
          icon = {
            Icon(
              imageVector = Icons.Default.VideoLibrary,
              contentDescription = "Library"
            )
          },
          label = {
            Text(
              "Library",
              fontSize = 11.sp,
              fontWeight = if (uiState.currentScreen == AppScreen.LIBRARY) FontWeight.Bold else FontWeight.Normal
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color(0xFF00FF88),
            selectedTextColor = Color(0xFF00FF88),
            indicatorColor = Color(0xFF1E173D),
            unselectedIconColor = Color(0xFF7B759F),
            unselectedTextColor = Color(0xFF7B759F)
          ),
          modifier = Modifier.testTag("nav_library")
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (uiState.currentScreen) {
        AppScreen.STUDIO -> {
          StudioScreen(
            uiState = uiState,
            playbackState = playbackState,
            onTogglePlay = { viewModel.togglePlay() },
            onSeek = { viewModel.seekTo(it) },
            onSectionClick = { viewModel.jumpToSection(it) },
            onSelectAspectRatio = { viewModel.setAspectRatio(it) },
            onSelectImagination = { viewModel.selectImaginationType(it) },
            onStartExport4K = { viewModel.startExport4K() },
            onNavigate = { viewModel.navigateTo(it) }
          )
        }

        AppScreen.CUSTOMIZE -> {
          CustomizationScreen(
            uiState = uiState,
            onSelectImagination = { viewModel.selectImaginationType(it) },
            onSelectPalette = { viewModel.selectPalette(it) },
            onSelectReferenceImage = { viewModel.selectReferenceImage(it) },
            onSetReferenceWeight = { viewModel.setReferenceWeight(it) },
            onUpdateCustomization = { viewModel.updateCustomization(it) },
            onNavigateBack = { viewModel.navigateTo(AppScreen.STUDIO) }
          )
        }

        AppScreen.UPLOAD -> {
          UploadMusicScreen(
            uiState = uiState,
            onSelectTrack = { viewModel.selectTrack(it) },
            onUploadAudioFile = { viewModel.uploadUserAudio(it) },
            onNavigateBack = { viewModel.navigateTo(AppScreen.STUDIO) }
          )
        }

        AppScreen.COMMUNITY -> {
          CommunityFeedScreen(
            uiState = uiState,
            onLikeToggle = { viewModel.toggleLikePost(it) },
            onFollowToggle = { viewModel.toggleFollowPost(it) },
            onPlayPost = { viewModel.playCommunityPostInStudio(it) },
            onAddComment = { postId, text -> viewModel.addCommentToPost(postId, text) },
            onShowPublishDialog = { viewModel.showPublishModal() },
            onDismissPublishDialog = { viewModel.dismissPublishModal() },
            onUpdatePublishCaption = { viewModel.updatePublishCaption(it) },
            onConfirmPublish = { viewModel.confirmPublishToCommunity() },
            onNavigateBack = { viewModel.navigateTo(AppScreen.STUDIO) }
          )
        }

        AppScreen.LIBRARY -> {
          LibraryScreen(
            uiState = uiState,
            onSelectProject = { project ->
              viewModel.selectTrack(project.track)
              viewModel.updateCustomization { project.customization }
              viewModel.navigateTo(AppScreen.STUDIO)
            },
            onNavigateBack = { viewModel.navigateTo(AppScreen.STUDIO) }
          )
        }
      }

      // 4K Exporting & Social Share Modal Dialog
      if (uiState.showExportDialog) {
        Export4KDialog(
          exportState = exportState,
          onDismiss = { viewModel.dismissExportDialog() },
          onPublishToCommunity = {
            viewModel.dismissExportDialog()
            viewModel.showPublishModal()
          }
        )
      }
    }
  }
}
