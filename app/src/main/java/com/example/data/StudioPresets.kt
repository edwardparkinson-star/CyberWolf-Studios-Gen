package com.example.data

import com.example.R
import com.example.model.AudioTrack
import com.example.model.ColorPalette
import com.example.model.ImaginationType
import com.example.model.SongSection

object StudioPresets {

  val palettes: List<ColorPalette> = listOf(
    ColorPalette(
      id = "neon_cyber",
      name = "Neon Cyberpunk",
      colors = listOf(0xFF00E5FF, 0xFF8A4FFF, 0xFFFF2E93, 0xFF0A0914),
      description = "Electric cyan, ultraviolet, and hot laser magenta over void black"
    ),
    ColorPalette(
      id = "sunset_horizon",
      name = "Sunset Synth",
      colors = listOf(0xFFFFB800, 0xFFFF5E36, 0xFF9B51E0, 0xFF1B0A2A),
      description = "Warm golden sun, radiant coral orange, and twilight violet"
    ),
    ColorPalette(
      id = "emerald_matrix",
      name = "Emerald Matrix",
      colors = listOf(0xFF00FF88, 0xFF00D2B4, 0xFF0D5C3A, 0xFF04140D),
      description = "High-voltage lime green, toxic jade, and deep terminal black"
    ),
    ColorPalette(
      id = "solar_flare",
      name = "Solar Flare",
      colors = listOf(0xFFFF3366, 0xFFFF7700, 0xFFFFCC00, 0xFF24040E),
      description = "Molten crimson, intense amber, and incandescent stellar gold"
    ),
    ColorPalette(
      id = "obsidian_gold",
      name = "Obsidian Luxury 4K",
      colors = listOf(0xFFFFD700, 0xFFE5A93C, 0xFF8C6D31, 0xFF111116),
      description = "Royal gold leaf, champagne shimmer, and velvet obsidian"
    ),
    ColorPalette(
      id = "acid_prism",
      name = "Acid Prism",
      colors = listOf(0xFFB5179E, 0xFF7209B7, 0xFF4CC9F0, 0xFF0B0320),
      description = "Psychedelic violet, ultra fuchsia, and kaleidoscopic turquoise"
    )
  )

  val imaginationTypes: List<ImaginationType> = listOf(
    ImaginationType(
      id = "cyberpunk",
      title = "Cyberpunk Neo-Tokyo",
      category = "Sci-Fi & Cyber",
      tagLine = "Neon skyscrapers, flying vehicles & digital holographic rain",
      promptKeywords = "Cyberpunk Neo-Tokyo metropolis, towering neon holograms, flying hover-cars, rainy reflections, chromatic violet and cyan glow, volumetric laser smoke, 8k cinematic",
      fallbackDrawable = R.drawable.img_cyberpunk_neon,
      dominantColors = listOf(0xFF00E5FF, 0xFF8A4FFF, 0xFFFF2E93),
      beatReactivityPreset = 0.95f
    ),
    ImaginationType(
      id = "cosmic_astral",
      title = "Cosmic Astral Odyssey",
      category = "Space & Cosmic",
      tagLine = "Swirling deep space nebulae, stardust & celestial portals",
      promptKeywords = "Deep space astral nebula pulsating with celestial stardust, swirling galactic spiral arms, glowing aurora portals, planetary rings, ethereal cosmic light, IMAX 70mm",
      fallbackDrawable = R.drawable.img_cosmic_pulse,
      dominantColors = listOf(0xFFFF2E93, 0xFF7928CA, 0xFF00DFD8),
      beatReactivityPreset = 0.85f
    ),
    ImaginationType(
      id = "synthwave_80s",
      title = "Retro Synthwave 80s",
      category = "Retro & Vintage",
      tagLine = "Wireframe neon grid, glowing low-poly sun & laser horizons",
      promptKeywords = "1980s synthwave outrun aesthetic, purple wireframe landscape, giant glowing striped sun on horizon, neon palm silhouettes, chrome reflections, analog tape scanlines",
      fallbackDrawable = R.drawable.img_synthwave_sunset,
      dominantColors = listOf(0xFFFF5E36, 0xFFFFB800, 0xFF9B51E0),
      beatReactivityPreset = 0.9f
    ),
    ImaginationType(
      id = "hyper_surreal",
      title = "Hyper-Surreal Dalí",
      category = "Surrealism",
      tagLine = "Floating crystal monoliths, liquid horizons & dream physics",
      promptKeywords = "Surrealist dreamscape inspired by Salvador Dalí, floating geometric glass monoliths, liquid golden horizon, gravity-defying architecture, bioluminescent sands, mystical atmosphere",
      fallbackDrawable = R.drawable.img_surreal_dreamscape,
      dominantColors = listOf(0xFF8A4FFF, 0xFFFFB800, 0xFF00E5FF),
      beatReactivityPreset = 0.75f
    ),
    ImaginationType(
      id = "anime_ghibli",
      title = "Anime Dreamscape",
      category = "Animation",
      tagLine = "Hand-painted watercolor clouds, floating islands & magical spirits",
      promptKeywords = "Makoto Shinkai and Studio Ghibli style, magnificent painted cumulus clouds, floating enchanted sky islands, glowing wind particles, nostalgic golden hour anime aesthetic",
      fallbackDrawable = R.drawable.img_surreal_dreamscape,
      dominantColors = listOf(0xFF48CAE4, 0xFFADE8F4, 0xFFF77F00),
      beatReactivityPreset = 0.7f
    ),
    ImaginationType(
      id = "abstract_fractal",
      title = "Quantum Fractal Geometry",
      category = "Abstract",
      tagLine = "Pulsating Mandelbrot fractals, sacred geometry & liquid chrome",
      promptKeywords = "Higher dimensional Mandelbrot fractal morphing to bass rhythm, sacred kaleidoscope geometry, liquid iridescent mercury reflections, prismatic refractions, mathematical beauty",
      fallbackDrawable = R.drawable.img_cosmic_pulse,
      dominantColors = listOf(0xFF7209B7, 0xFFF72585, 0xFF4CC9F0),
      beatReactivityPreset = 1.0f
    ),
    ImaginationType(
      id = "dark_techno",
      title = "Dark Industrial Techno",
      category = "Club & Rave",
      tagLine = "Brutalist concrete monoliths, laser arrays & obsidian mist",
      promptKeywords = "Berlin underground brutalist warehouse, strobe laser ceiling arrays piercing dense atmospheric fog, obsidian concrete monoliths, bass-reactive strobe lighting, hypnotic minimalism",
      fallbackDrawable = R.drawable.img_cyberpunk_neon,
      dominantColors = listOf(0xFFE63946, 0xFF1D3557, 0xFFF1FAEE),
      beatReactivityPreset = 0.95f
    ),
    ImaginationType(
      id = "psychedelic_70s",
      title = "Psychedelic Liquid Light",
      category = "Psychedelic",
      tagLine = "Swirling liquid oil projections, prism rainbows & vintage vinyl vibes",
      promptKeywords = "1970s psychedelic liquid light show, macro organic oil swirl patterns, iridescent rainbow gradients, chromatic lens flares, kaleidoscopic symmetry, vintage analog grain",
      fallbackDrawable = R.drawable.img_synthwave_sunset,
      dominantColors = listOf(0xFFFF007F, 0xFF00F0FF, 0xFFFFE600),
      beatReactivityPreset = 0.8f
    ),
    ImaginationType(
      id = "biopunk_organic",
      title = "Cyber-Organic Biopunk",
      category = "Sci-Fi & Nature",
      tagLine = "Bioluminescent alien flora, crystalline jellyfish & neural vines",
      promptKeywords = "Alien bioluminescent jungle, pulsing neural tendrils, floating crystalline jellyfish emitting cyan light, phosphorescent spores dancing to rhythm, Pandora avatar atmosphere",
      fallbackDrawable = R.drawable.img_cosmic_pulse,
      dominantColors = listOf(0xFF00F5D4, 0xFF7B2CBF, 0xFF9D4EDD),
      beatReactivityPreset = 0.85f
    ),
    ImaginationType(
      id = "mythic_fantasy",
      title = "Mythological Gods & Fantasy",
      category = "Epic Fantasy",
      tagLine = "Celestial temples in mountain peaks, phoenix auroras & glowing runes",
      promptKeywords = "Ancient celestial temple suspended in golden mountain peaks, fiery phoenix spreading glowing wings across aurora skies, mystical runic circles glowing to drum beats, epic fantasy",
      fallbackDrawable = R.drawable.img_surreal_dreamscape,
      dominantColors = listOf(0xFFFF9E00, 0xFFFF0054, 0xFF9E0059),
      beatReactivityPreset = 0.8f
    ),
    ImaginationType(
      id = "cinematic_8k",
      title = "Cinematic 8K Hyper-Real",
      category = "Cinematic",
      tagLine = "Anamorphic 35mm lens, atmospheric golden haze & sweeping landscapes",
      promptKeywords = "Shot on ARRI Alexa 65 anamorphic lens, photorealistic Icelandic black sand beach with glowing aurora borealis, volumetric coastal sea spray, dramatic cinema lighting, 8k masterpiece",
      fallbackDrawable = R.drawable.img_cyberpunk_neon,
      dominantColors = listOf(0xFF1E3A8A, 0xFFF59E0B, 0xFF10B981),
      beatReactivityPreset = 0.75f
    ),
    ImaginationType(
      id = "glitchcore_matrix",
      title = "Glitchcore & Cyber Matrix",
      category = "Digital / Glitch",
      tagLine = "Pixel sorting, datamoshing, CRT phosphor & cybernetic wireframes",
      promptKeywords = "Cyber matrix datamosh glitch art, RGB split chromatic displacement, CRT phosphor grid lines, digital artifacting synced to heavy snare hits, hacker terminal aesthetic",
      fallbackDrawable = R.drawable.img_cyberpunk_neon,
      dominantColors = listOf(0xFF00FF66, 0xFFFF0033, 0xFF0066FF),
      beatReactivityPreset = 1.0f
    )
  )

  val demoTracks: List<AudioTrack> = listOf(
    AudioTrack(
      id = "demo_cyberwolf",
      title = "CyberWolf Awakening",
      artist = "CyberWolf Studios",
      durationMs = 36000L,
      bpm = 128,
      waveform = generateWaveform(seed = 42, length = 64),
      sections = listOf(
        SongSection("s1", "Intro: Dawn of Wire", 0L, 6000L, "Signals rising through the obsidian silence...", 0.35f, false),
        SongSection("s2", "Verse 1: Neon Grid", 6000L, 14000L, "We run the wire, electric veins ignite the city tonight", 0.65f, false),
        SongSection("s3", "Build-Up: Heartbeat", 14000L, 18000L, "Feel the voltage surge — 3, 2, 1 — INITIATE!", 0.85f, false),
        SongSection("s4", "DROP: Cyber Overdrive", 18000L, 28000L, "CYBERWOLF UNLEASHED! BASS IMPACT MAXIMUM!", 1.0f, true),
        SongSection("s5", "Outro: Horizon Fade", 28000L, 36000L, "Echoes in the stardust, the future is now.", 0.45f, false)
      ),
      isDemo = true
    ),
    AudioTrack(
      id = "demo_cosmic",
      title = "Cosmic Horizon",
      artist = "Aetheria",
      durationMs = 34000L,
      bpm = 140,
      waveform = generateWaveform(seed = 88, length = 64),
      sections = listOf(
        SongSection("s1", "Intro: Deep Vacuum", 0L, 5000L, "Beyond the event horizon, starlight breathes", 0.3f, false),
        SongSection("s2", "Verse: Nebula Drift", 5000L, 12000L, "Drifting in oceans of ultraviolet dreams", 0.6f, false),
        SongSection("s3", "Tension: Gravity Well", 12000L, 16000L, "Singularity pulling us into light...", 0.85f, false),
        SongSection("s4", "SUPERNOVA DROP", 16000L, 26000L, "BLINDING STELLAR RADIANCE! SHATTER THE SKY!", 1.0f, true),
        SongSection("s5", "Outro: Stardust Ring", 26000L, 34000L, "Resting in the orbit of forever", 0.4f, false)
      ),
      isDemo = true
    ),
    AudioTrack(
      id = "demo_sunset",
      title = "Sunset Boulevard '84",
      artist = "Retro Pulse",
      durationMs = 32000L,
      bpm = 118,
      waveform = generateWaveform(seed = 19, length = 64),
      sections = listOf(
        SongSection("s1", "Intro: Cassette Click", 0L, 4000L, "Tape rolling... 1984 stereo master", 0.3f, false),
        SongSection("s2", "Verse: Palm Highway", 4000L, 12000L, "Cruise down the boulevard into the golden sunset", 0.6f, false),
        SongSection("s3", "Chorus: Neon Dreams", 12000L, 24000L, "Hold on to the summer, colors never fade away", 0.95f, true),
        SongSection("s4", "Outro: Night Cruise", 24000L, 32000L, "City lights reflect across the hood forever", 0.4f, false)
      ),
      isDemo = true
    ),
    AudioTrack(
      id = "demo_lofi",
      title = "Midnight Rain in Shinjuku",
      artist = "ChillHop Realm",
      durationMs = 30000L,
      bpm = 85,
      waveform = generateWaveform(seed = 33, length = 64),
      sections = listOf(
        SongSection("s1", "Intro: Vinyl Scratch", 0L, 6000L, "Rain tapping softly on the window pane...", 0.35f, false),
        SongSection("s2", "Main Groove: Coffee Steam", 6000L, 22000L, "Lost in warm chords, drifting through quiet streets", 0.7f, false),
        SongSection("s3", "Outro: Soft Fade", 22000L, 30000L, "Until the morning comes again...", 0.35f, false)
      ),
      isDemo = true
    )
  )

  val communityPosts: List<com.example.model.CommunityPost> = listOf(
    com.example.model.CommunityPost(
      id = "post_1",
      creatorName = "NeonValkyrie",
      creatorHandle = "@valkyrie_ai",
      creatorAvatarColor = 0xFF8A4FFF,
      videoTitle = "Tokyo Velocity 2099",
      caption = "Generated this 4K cyberpunk music video matched to 140 BPM heavy bass drops! Notice how the camera rushes into neon alleys on every kick drum 🔥",
      trackTitle = "CyberWolf Awakening",
      artist = "CyberWolf Studios",
      bpm = 128,
      durationSec = 36,
      imaginationStyle = "Cyberpunk Neo-Tokyo",
      imageRes = R.drawable.img_cyberpunk_neon,
      likeCount = 1420,
      isLiked = false,
      comments = listOf(
        com.example.model.CommunityComment("c1", "Synthia", "The beat drop transition at 0:18 is absolutely insane!", "2h ago", 24),
        com.example.model.CommunityComment("c2", "BassDropKid", "Those neon laser reflections in 4K look so clean on my TV", "5h ago", 12)
      ),
      views = 8940,
      isFollowing = true,
      createdAt = "2 hours ago",
      is4K = true
    ),
    com.example.model.CommunityPost(
      id = "post_2",
      creatorName = "AstroBeatLab",
      creatorHandle = "@astro_beats",
      creatorAvatarColor = 0xFFFF2E93,
      videoTitle = "Stardust Symphony in 4K",
      caption = "Exploring astral nebulae and cosmic portals synced with celestial pads and ambient sub-bass. Rendered at 3840x2160 60FPS! ✨🪐",
      trackTitle = "Cosmic Horizon",
      artist = "Aetheria",
      bpm = 140,
      durationSec = 34,
      imaginationStyle = "Cosmic Astral Odyssey",
      imageRes = R.drawable.img_cosmic_pulse,
      likeCount = 2890,
      isLiked = true,
      comments = listOf(
        com.example.model.CommunityComment("c3", "NovaSound", "How did you get the particles to swirl on the snare hit? Mind blowing!", "1d ago", 45),
        com.example.model.CommunityComment("c4", "GalaxyVoyager", "Watching this in 4K with headphones is a spiritual experience.", "1d ago", 19)
      ),
      views = 15300,
      isFollowing = false,
      createdAt = "Yesterday",
      is4K = true
    ),
    com.example.model.CommunityPost(
      id = "post_3",
      creatorName = "RetroWave84",
      creatorHandle = "@retrowave84",
      creatorAvatarColor = 0xFFFFB800,
      videoTitle = "Sunset Boulevard (Outrun Master)",
      caption = "Full retro 80s aesthetics with chrome grid roads, wireframe mountains and glowing VHS scanlines. Uploaded reference photo for the golden sunset tones 🌴🚗",
      trackTitle = "Sunset Boulevard '84",
      artist = "Retro Pulse",
      bpm = 118,
      durationSec = 32,
      imaginationStyle = "Retro Synthwave 80s",
      imageRes = R.drawable.img_synthwave_sunset,
      likeCount = 980,
      isLiked = false,
      comments = listOf(
        com.example.model.CommunityComment("c5", "MiamiVices", "Pure nostalgia vibes. The color palette match is perfect.", "3d ago", 18)
      ),
      views = 6720,
      isFollowing = false,
      createdAt = "3 days ago",
      is4K = true
    ),
    com.example.model.CommunityPost(
      id = "post_4",
      creatorName = "DalíDimensional",
      creatorHandle = "@dali_dimensions",
      creatorAvatarColor = 0xFF00E5FF,
      videoTitle = "Defying Gravity: Liquid Time",
      caption = "Surrealist crystal monoliths and liquid horizons pulsing to ambient lo-fi beats. AI prompt influenced by dream physics and floating planets 🌌",
      trackTitle = "Midnight Rain in Shinjuku",
      artist = "ChillHop Realm",
      bpm = 85,
      durationSec = 30,
      imaginationStyle = "Hyper-Surreal Dalí",
      imageRes = R.drawable.img_surreal_dreamscape,
      likeCount = 3120,
      isLiked = true,
      comments = listOf(
        com.example.model.CommunityComment("c6", "ArtBot", "Best visual storytelling I've seen in CyberWolf Gen so far!", "4d ago", 33)
      ),
      views = 21400,
      isFollowing = true,
      createdAt = "4 days ago",
      is4K = true
    )
  )

  private fun generateWaveform(seed: Int, length: Int): List<Float> {
    val random = kotlin.random.Random(seed)
    return List(length) { i ->
      val progress = i.toFloat() / length
      val envelope = when {
        progress < 0.2f -> 0.2f + progress * 2f
        progress < 0.45f -> 0.6f + random.nextFloat() * 0.3f
        progress < 0.75f -> 0.8f + random.nextFloat() * 0.2f // Drop section
        else -> 0.4f + (1f - progress) * 0.4f
      }
      (envelope * (0.4f + random.nextFloat() * 0.6f)).coerceIn(0.1f, 1.0f)
    }
  }
}
