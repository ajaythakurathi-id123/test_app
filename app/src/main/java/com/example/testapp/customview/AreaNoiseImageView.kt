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
    private val updateInterval = 500L // Update color every second

    val areaSize = 64 // Adjust area size as needed


    init {
        noisePaint.alpha = 64 // Adjust for noise intensity

        // Generate noise texture
        generateNoiseTexture()

        // Schedule color updates
        scheduleColorUpdates()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /*private fun generateNoiseTexture() {
        val noiseSize = 256
        noiseBitmap = Bitmap.createBitmap(noiseSize, noiseSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(noiseBitmap)

        // Call the area noise generation method
//        generateAreaNoise(canvas, noiseBitmap, 16) // Adjust area size as needed
        generateAreaNoise(canvas, noiseBitmap, 32) // Adjust area size as needed

        noiseShader = BitmapShader(noiseBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        noisePaint.shader = noiseShader
    }
    */
    private fun generateNoiseTexture() {
        Log.d(TAG, "generateNoiseTexture: ")

        val noiseSize = 256
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
//        generateAreaNoise(canvas, noiseBitmap, areaSize)

        // **Clear the canvas (no noise)**
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        noiseShader = null

        // for noise
//        noiseShader = BitmapShader(noiseBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
//        noisePaint.shader = noiseShader
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

                // ... (Rest of the area noise generation as before)
// Generate random color for the area
                /*val color = Color.argb(
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256)
                )*/

                // Create a temporary canvas for the area
                val areaBitmap = Bitmap.createBitmap(areaSize, areaSize, Bitmap.Config.ARGB_8888)
                val areaCanvas = Canvas(areaBitmap)

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

    //noise
    /*override fun onDraw(canvas: Canvas) {
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
    }*/

    // without noise
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Save the canvas state
        canvas.saveLayer(null, paint, Canvas.ALL_SAVE_FLAG)

        // Draw the image
        super.onDraw(canvas)

        // Apply color overlay (without noise)
        for (x in 0 until noiseBitmap.width step areaSize) {
            for (y in 0 until noiseBitmap.height step areaSize) {
                val color = areaColors[(x / areaSize) + (y / areaSize) * (noiseBitmap.width / areaSize)]
                noisePaint.color = color
                canvas.drawRect(x.toFloat(), y.toFloat(), (x + areaSize).toFloat(), (y + areaSize).toFloat(), noisePaint)
            }
        }

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