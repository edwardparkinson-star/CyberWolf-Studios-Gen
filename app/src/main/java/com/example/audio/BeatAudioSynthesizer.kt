package com.example.audio

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * High-performance audio synthesizer that produces authentic stereo electronic music
 * with kick drums, snares, sub-bass, and melodic synth arpeggios synchronized to BPM.
 */
object BeatAudioSynthesizer {

  fun getOrCreateDemoAudio(context: Context, trackId: String, bpm: Int, durationMs: Long): File {
    val dir = File(context.cacheDir, "demo_audio").apply { mkdirs() }
    val file = File(dir, "${trackId}_${bpm}.wav")
    if (file.exists() && file.length() > 1000) {
      return file
    }

    try {
      generateWavFile(file, bpm, durationMs)
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return file
  }

  private fun generateWavFile(file: File, bpm: Int, durationMs: Long) {
    val sampleRate = 22050
    val channels = 2
    val totalSeconds = (durationMs / 1000.0).coerceIn(10.0, 45.0)
    val totalSamples = (totalSeconds * sampleRate).toInt()
    val bytesPerSample = 2 // 16-bit PCM

    val beatIntervalSec = 60.0 / bpm
    val samplesPerBeat = (beatIntervalSec * sampleRate).toInt()

    FileOutputStream(file).use { fos ->
      // Write placeholder 44-byte WAV header
      fos.write(ByteArray(44))

      val bufferSize = 4096
      val byteBuffer = ByteBuffer.allocate(bufferSize * channels * bytesPerSample).order(ByteOrder.LITTLE_ENDIAN)
      var currentSample = 0

      while (currentSample < totalSamples) {
        byteBuffer.clear()
        val chunk = minOf(bufferSize, totalSamples - currentSample)

        for (i in 0 until chunk) {
          val sampleIndex = currentSample + i
          val time = sampleIndex.toDouble() / sampleRate
          val beatPos = (sampleIndex % samplesPerBeat).toDouble() / sampleRate
          val beatNumber = sampleIndex / samplesPerBeat

          // 1. Kick Drum on every beat (beats 0, 1, 2, 3...)
          val kickDecay = exp(-beatPos * 18.0)
          val kickPitch = 120.0 * exp(-beatPos * 25.0) + 40.0
          val kick = sin(2.0 * PI * kickPitch * beatPos) * kickDecay * 0.7

          // 2. Snare / Clap on beats 2 and 4 (in 4/4 time)
          val isSnareBeat = (beatNumber % 4 == 1) || (beatNumber % 4 == 3)
          val snare = if (isSnareBeat && beatPos < 0.25) {
            val snareDecay = exp(-beatPos * 20.0)
            val noise = ((Math.random() * 2.0) - 1.0) * 0.5
            val tone = sin(2.0 * PI * 180.0 * beatPos) * 0.5
            (noise + tone) * snareDecay * 0.5
          } else 0.0

          // 3. Arpeggiated Synth Melody
          val chordRoot = when ((beatNumber / 8) % 4) {
            0 -> 220.0 // A3
            1 -> 261.63 // C4
            2 -> 196.0 // G3
            else -> 174.61 // F3
          }
          val arpStep = (beatPos / (beatIntervalSec / 4.0)).toInt() % 4
          val arpMultiplier = when (arpStep) {
            0 -> 1.0
            1 -> 1.25
            2 -> 1.5
            else -> 2.0
          }
          val synthFreq = chordRoot * arpMultiplier
          val arpTime = beatPos % (beatIntervalSec / 4.0)
          val synthEnvelope = exp(-arpTime * 8.0)
          val synth = (sin(2.0 * PI * synthFreq * time) + 0.3 * sin(2.0 * PI * (synthFreq * 2.0) * time)) * synthEnvelope * 0.35

          // 4. Sub-Bass
          val bassFreq = chordRoot / 2.0
          val bass = sin(2.0 * PI * bassFreq * time) * 0.4

          // Mix stereo output
          val mixedMono = (kick + snare + synth + bass).coerceIn(-1.0, 1.0)
          val leftSample = (mixedMono * 0.95 * Short.MAX_VALUE).toInt().toShort()
          val rightSample = (mixedMono * Short.MAX_VALUE).toInt().toShort()

          byteBuffer.putShort(leftSample)
          byteBuffer.putShort(rightSample)
        }

        fos.write(byteBuffer.array(), 0, chunk * channels * bytesPerSample)
        currentSample += chunk
      }
    }

    // Update WAV header with exact byte sizes
    val totalAudioLen = totalSamples.toLong() * channels * bytesPerSample
    val totalDataLen = totalAudioLen + 36
    RandomAccessFile(file, "rw").use { raf ->
      raf.seek(0)
      writeWavHeader(raf, totalAudioLen, totalDataLen, sampleRate.toLong(), channels, (sampleRate * channels * bytesPerSample).toLong())
    }
  }

  private fun writeWavHeader(
    raf: RandomAccessFile,
    totalAudioLen: Long,
    totalDataLen: Long,
    longSampleRate: Long,
    channels: Int,
    byteRate: Long
  ) {
    val header = ByteArray(44)
    header[0] = 'R'.code.toByte()
    header[1] = 'I'.code.toByte()
    header[2] = 'F'.code.toByte()
    header[3] = 'F'.code.toByte()
    header[4] = (totalDataLen and 0xff).toByte()
    header[5] = (totalDataLen shr 8 and 0xff).toByte()
    header[6] = (totalDataLen shr 16 and 0xff).toByte()
    header[7] = (totalDataLen shr 24 and 0xff).toByte()
    header[8] = 'W'.code.toByte()
    header[9] = 'A'.code.toByte()
    header[10] = 'V'.code.toByte()
    header[11] = 'E'.code.toByte()
    header[12] = 'f'.code.toByte()
    header[13] = 'm'.code.toByte()
    header[14] = 't'.code.toByte()
    header[15] = ' '.code.toByte()
    header[16] = 16 // 16 for PCM
    header[17] = 0
    header[18] = 0
    header[19] = 0
    header[20] = 1 // PCM format = 1
    header[21] = 0
    header[22] = channels.toByte()
    header[23] = 0
    header[24] = (longSampleRate and 0xff).toByte()
    header[25] = (longSampleRate shr 8 and 0xff).toByte()
    header[26] = (longSampleRate shr 16 and 0xff).toByte()
    header[27] = (longSampleRate shr 24 and 0xff).toByte()
    header[28] = (byteRate and 0xff).toByte()
    header[29] = (byteRate shr 8 and 0xff).toByte()
    header[30] = (byteRate shr 16 and 0xff).toByte()
    header[31] = (byteRate shr 24 and 0xff).toByte()
    header[32] = (channels * 2).toByte() // block align
    header[33] = 0
    header[34] = 16 // bits per sample
    header[35] = 0
    header[36] = 'd'.code.toByte()
    header[37] = 'a'.code.toByte()
    header[38] = 't'.code.toByte()
    header[39] = 'a'.code.toByte()
    header[40] = (totalAudioLen and 0xff).toByte()
    header[41] = (totalAudioLen shr 8 and 0xff).toByte()
    header[42] = (totalAudioLen shr 16 and 0xff).toByte()
    header[43] = (totalAudioLen shr 24 and 0xff).toByte()
    raf.write(header, 0, 44)
  }
}
