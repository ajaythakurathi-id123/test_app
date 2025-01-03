package com.example.testapp.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

class AreaNoiseImageView : AppCompatImageView {

    companion object {
        private const val TAG = "AreaNoiseImageView"
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val noisePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var noiseBitmap: Bitmap
    private var noiseShader: BitmapShader? = null
    private val random = Random
    private var areaColors: MutableList<Int> = mutableListOf()

    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 10L // Update color every second

    private val areaSize = 8 // Adjust area size as needed
    private val noiseSize = 256

    // Glitch effect parameters
    private var glitchIntensity = 0.05f // Adjust for intensity
    private var glitchInterval = 500L // Interval for glitch effects


    // List to hold custom colors
    private var customColors: List<Int> = listOf(
        Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
        Color.CYAN, Color.MAGENTA, Color.GRAY, Color.WHITE
    )


    init {
        noisePaint.alpha = 100 // Adjust for noise intensity

        // Generate noise texture
        generateNoiseTexture()

        // Schedule color updates
        scheduleColorUpdates()

        // Schedule color updates and glitch effects
//        scheduleColorUpdates()
//        scheduleGlitchEffects()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    private fun generateNoiseTexture() {
        Log.d(TAG, "generateNoiseTexture: ")

        noiseBitmap = Bitmap.createBitmap(noiseSize, noiseSize, Bitmap.Config.ARGB_8888)
//        noiseBitmap = Bitmap.createBitmap(noiseSize, noiseSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(noiseBitmap)

        // Calculate number of areas
        val numAreasX = noiseSize / areaSize
        val numAreasY = noiseSize / areaSize

        // Generate initial area colors
        for (i in 0 until numAreasX * numAreasY) {
            areaColors.add(getRandomColor())
        }

        // Generate area noise with initial colors
        generateAreaNoise(canvas, noiseBitmap, areaSize)

        // for noise
        noiseShader = BitmapShader(noiseBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        noisePaint.shader = noiseShader
    }

    private fun generateAreaNoise(canvas: Canvas, noiseBitmap: Bitmap, areaSize: Int) {

        Log.d(TAG, "generateAreaNoise: ")

        var areaIndex = 0
        val random = Random
        val areaRadius = areaSize / 2

        for (x in 0 until noiseBitmap.width step areaSize) {
            for (y in 0 until noiseBitmap.height step areaSize) {
                val centerX = x + areaRadius
                val centerY = y + areaRadius

                // Get color for the current area
                val color = areaColors[areaIndex]

                // Create a temporary canvas for the area
                val areaBitmap = Bitmap.createBitmap(areaSize, areaSize, Bitmap.Config.ARGB_8888)
                val areaCanvas = Canvas(areaBitmap)

                // Randomly select a corner for the light source
                val corner = random.nextInt(4)
                var lightSourceX = centerX
                var lightSourceY = centerY
                when (corner) {
                    0 -> { /* Top Left */
                    }

                    1 -> lightSourceX = centerX + areaRadius
                    2 -> lightSourceY = centerY + areaRadius
                    3 -> {
                        lightSourceX = centerX + areaRadius; lightSourceY = centerY + areaRadius
                    }
                }

                // Draw random noise within the area
                /*for (ax in 0 until areaSize) {
                    for (ay in 0 until areaSize) {
                        val distanceToSource = Math.sqrt(
                            Math.pow(
                                (ax - lightSourceX).toDouble(),
                                2.0
                            ) + Math.pow((ay - lightSourceY).toDouble(), 2.0)
                        )
                        val maxDistance = Math.sqrt(Math.pow(areaRadius.toDouble(), 2.0) * 2.0)
                        val brightness = (maxDistance - distanceToSource) / maxDistance
                        val alpha = (brightness * 255).toInt().coerceIn(0, 255)
                        areaCanvas.drawPoint(
                            ax.toFloat(),
                            ay.toFloat(),
                            Paint().apply {
                                this.color = Color.argb(
                                    alpha,
                                    Color.red(color),
                                    Color.green(color),
                                    Color.blue(color)
                                )
                            }
                        )
                    }
                }*/

                // Draw random noise within the area
                for (ax in 0 until areaSize) {
                    for (ay in 0 until areaSize) {
                        val distance = Math.sqrt(
                            (ax - areaRadius).toDouble().pow(2.0) + (ay - areaRadius).toDouble()
                                .pow(2.0)
                        )
                        if (distance <= areaRadius) {
                            val angle = random.nextDouble(0.0, Math.PI * 2) // Random angle
                            val offsetX = cos(angle) * distance
                            val offsetY = sin(angle) * distance

                            areaCanvas.drawPoint(
                                (ax + offsetX).toFloat(),
                                (ay + offsetY).toFloat(),
                                Paint().apply { this.color = color }
                            )
                        }
                    }
                }

                // Draw the area noise onto the main canvas
                canvas.drawBitmap(areaBitmap, x.toFloat(), y.toFloat(), null)
                areaIndex++
            }
        }
    }

    private fun scheduleColorUpdates() {
        Log.d(TAG, "scheduleColorUpdates: ")

        handler.postDelayed({
            updateAreaColors()
            scheduleColorUpdates() // Schedule next update
        }, updateInterval)
    }


    private fun scheduleGlitchEffects() {
        handler.postDelayed({
            applyGlitchEffect()
            scheduleGlitchEffects()
        }, glitchInterval)
    }

    private fun getRandomColorFromList(): Int {
        return customColors[random.nextInt(customColors.size)]
    }

    private fun updateAreaColors() {
        Log.d("HologramImageView", "updateAreaColors() called")
        /* val randomAreaIndex = random.nextInt(areaColors.size)
         val oldColor = areaColors[randomAreaIndex]
         areaColors[randomAreaIndex] = getRandomColor()
         Log.d("HologramImageView", "Area color changed: $oldColor -> ${areaColors[randomAreaIndex]}")*/


//        areaColors= mutableListOf()
        areaColors.clear()
        // Regenerate the noise texture with updated colors
        generateNoiseTexture()

        invalidate()
    }

    private fun applyPixelDisplacement() {
        // Introduce small random pixel offsets
        val maxOffset = 2
        for (i in areaColors.indices) {
            val xOffset = random.nextInt(maxOffset * 2) - maxOffset
            val yOffset = random.nextInt(maxOffset * 2) - maxOffset
            // ... (Handle boundary conditions to prevent out-of-bounds access) ...
        }
    }

    private fun applyColorChannelSeparation() {
        // Separate color channels (example: red)
        for (i in areaColors.indices) {
            val color = areaColors[i]
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)
            areaColors[i] = Color.rgb(r, 0, 0) // Red channel only
        }
    }

    private fun applyScanlineFlicker() {
        // Simulate scanline flicker
        val numScanlines = 10
        val scanlineWidth = height / numScanlines
        val flickerChance = 0.1f // Adjust for flicker frequency
        for (y in 0 until numScanlines) {
            if (random.nextFloat() < flickerChance) {
                // Invert colors within the scanline
                for (x in 0 until noiseSize step areaSize) {
                    val areaIndex = (x / areaSize) + (y * (noiseSize / areaSize))
                    if (areaIndex < areaColors.size) {
                        val color = areaColors[areaIndex]
                        val invertedColor = Color.rgb(
                            255 - Color.red(color),
                            255 - Color.green(color),
                            255 - Color.blue(color)
                        )
                        areaColors[areaIndex] = invertedColor
                    }
                }
            }
        }
    }

    private fun applyGlitchEffect() {
        // Randomly choose a glitch type
        when (random.nextInt(3)) {
            0 -> applyPixelDisplacement() // Pixel displacement
            1 -> applyColorChannelSeparation() // Color channel separation
            2 -> applyScanlineFlicker() // Scanline flicker
        }

        invalidate()
    }

    //noise
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        Log.d(TAG, "onDraw: ")

        // Save the canvas state
        canvas.saveLayer(null, paint, Canvas.ALL_SAVE_FLAG)

        // Draw the image
        super.onDraw(canvas)

        // Apply noise overlay
        noiseShader?.let {
            // No translation, keep noise static
            // it.setLocalMatrix(Matrix().apply { postTranslate(noiseOffsetX, noiseOffsetY) })
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), noisePaint)

        // Restore the canvas state
        canvas.restore()
    }


    private fun getRandomColor(): Int {
        return Color.argb(
            random.nextInt(256),
            random.nextInt(256),
            random.nextInt(256),
            random.nextInt(256)
        )
    }
}