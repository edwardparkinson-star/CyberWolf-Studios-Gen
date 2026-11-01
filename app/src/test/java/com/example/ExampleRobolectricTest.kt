package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("CyberWolf Studios Gen", appName)
  }

  @Test
  fun `verify studio presets and imagination types`() {
    val types = com.example.data.StudioPresets.imaginationTypes
    org.junit.Assert.assertTrue(types.isNotEmpty())
    org.junit.Assert.assertTrue(types.any { it.title.contains("Cyberpunk") })
    org.junit.Assert.assertTrue(types.any { it.title.contains("Cosmic") })
    org.junit.Assert.assertTrue(types.any { it.title.contains("Surreal") })

    val palettes = com.example.data.StudioPresets.palettes
    org.junit.Assert.assertTrue(palettes.isNotEmpty())

    val tracks = com.example.data.StudioPresets.demoTracks
    org.junit.Assert.assertTrue(tracks.isNotEmpty())
    org.junit.Assert.assertTrue(tracks.first().bpm > 0)
  }
}
