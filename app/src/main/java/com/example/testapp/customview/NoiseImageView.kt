package com.example.testapp.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.animation.BounceInterpolator
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

class NoiseImageView : AppCompatImageView {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val noisePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var noiseBitmap: Bitmap
    private var noiseShader: BitmapShader? = null
    private val animator = ValueAnimator.ofFloat(0f, 1f)
    private var noiseOffset = 0f
    private var noiseScale = 1f // Adjust noise scale
    private val noiseMatrix = Matrix() // Pre-allocate Matrix object

    init {
//        noisePaint.alpha = 64 // Adjust for noise intensity
        noisePaint.alpha = 250 // Adjust for noise intensity
//        animator.duration = 3000 // Animation duration in milliseconds
        animator.duration = 50 // Animation duration in milliseconds
        animator.interpolator = BounceInterpolator()
//        animator.interpolator = LinearInterpolator()
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
//        animator.repeatMode = ValueAnimator.RESTART
        animator.addUpdateListener { animation ->
            if (::noiseBitmap.isInitialized) {
                noiseOffset = (animation.animatedValue as Float) * noiseBitmap.width
                invalidate()
            }
        }
        animator.start()

        // Generate or load noise texture
        generateNoiseTexture()

    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun generateNoiseTexture() {
        // Generate noise bitmap (replace with your preferred method)
        noiseBitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(noiseBitmap)

//        generateAreaNoise(canvas, noiseBitmap, 16)



        val random = java.util.Random()
        for (x in 0 until noiseBitmap.width) {
            for (y in 0 until noiseBitmap.height) {
                val color = Color.argb(random.nextInt(256), random.nextInt(256), random.nextInt(256), random.nextInt(256))
                canvas.drawPoint(x.toFloat(), y.toFloat(), Paint().apply { this.color = color })
            }
        }
        noiseShader = BitmapShader(noiseBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        noisePaint.shader = noiseShader
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Save the canvas state
        canvas.saveLayer(null, paint, Canvas.ALL_SAVE_FLAG)

        // Draw the image
        super.onDraw(canvas)

        // Apply noise overlay
        noiseShader?.let {
            noiseMatrix.reset()
            matrix.setScale(noiseScale, noiseScale) // Apply scale
            noiseMatrix.postTranslate(noiseOffset, 1f)
//            noiseMatrix.postTranslate(noiseOffset, 0f)
            it.setLocalMatrix(noiseMatrix)
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), noisePaint)

        // Restore the canvas state
        canvas.restore()
    }

    private fun generateAreaNoise(canvas: Canvas, noiseBitmap: Bitmap, areaSize: Int) {
        val random = Random
        val areaRadius = areaSize / 2

        for (x in 0 until noiseBitmap.width step areaSize) {
            for (y in 0 until noiseBitmap.height step areaSize) {
                val centerX = x + areaRadius
                val centerY = y + areaRadius

                // Generate random color for the area
                val color = Color.argb(
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256)
                )

                // Create a temporary canvas for the area
                val areaCanvas = Canvas(Bitmap.createBitmap(areaSize, areaSize, Bitmap.Config.ARGB_8888))

                // Draw random noise within the area
                for (ax in 0 until areaSize) {
                    for (ay in 0 until areaSize) {
                        val distance = sqrt(
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
//                canvas.drawBitmap(areaCanvas.bitmap, x.toFloat(), y.toFloat(), null)
                canvas.drawBitmap(noiseBitmap, x.toFloat(), y.toFloat(), null)
            }
        }
    }
}