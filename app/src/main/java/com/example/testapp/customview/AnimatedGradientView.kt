package com.example.testapp.customview

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.example.testapp.R
import kotlin.math.sin


class AnimatedGradientView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "AnimatedGradientView"
    }

    //    private val image: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.logo)
    private var currentColor: Int = ContextCompat.getColor(context, R.color.purple_500)
    private val rectF = RectF()
    private var waveOffset = 0f
    private var currentGlowColor = Color.BLUE

    private val glowColorArr = intArrayOf(
        Color.BLUE,
        Color.TRANSPARENT,
        Color.CYAN,
        Color.TRANSPARENT,
    )

    private val colorArr = intArrayOf(
        Color.BLUE,
        Color.YELLOW,
        Color.GREEN,
        Color.CYAN,
        Color.RED
    )
    /*private val colorArr = intArrayOf(
        Color.BLUE,
        Color.CYAN,
    )*/

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var shader: Shader? = null

    //    private lateinit var shader: Shader
    private var gradientPosition = 0f

    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 6000 // Animation duration in milliseconds
        interpolator = LinearInterpolator()
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        addUpdateListener {
            gradientPosition = it.animatedValue as Float
            waveOffset += 0.2f // Adjust wave speed
            updateShader()
            invalidate()
        }
    }

    private val glowAnimator = ValueAnimator.ofFloat(20f, 50f).apply {
        duration = 1500
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        addUpdateListener {
            val blurRadius = it.animatedValue as Float
            Log.d(TAG, "blurRadius: $blurRadius")
            paint.maskFilter = BlurMaskFilter(blurRadius*2, BlurMaskFilter.Blur.INNER)
//            paint.maskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
            invalidate()
        }
    }

    private val glowColorAnimator = ValueAnimator.ofArgb(
        Color.BLUE,
        Color.YELLOW,
        Color.GREEN,
        Color.CYAN,
        Color.RED
//        Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.RED
    ).apply {
        duration = 4000 // Color shift duration in milliseconds
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        addUpdateListener {
            currentGlowColor = it.animatedValue as Int
            updateShader() // Reapply shader with the new glow color
            invalidate()
        }
    }

    init {
//        paint.shader = shader

        animator.duration = 2000 // Animation duration in milliseconds
        animator.interpolator = LinearInterpolator()
        animator.repeatCount = ValueAnimator.INFINITE
//        animator.repeatMode = ValueAnimator.RESTART
        animator.repeatMode = ValueAnimator.REVERSE
        animator.addUpdateListener { animation ->
            gradientPosition = animation.animatedValue as Float

            waveOffset += 0.1f // Adjust wave speed
            updateShader()
            invalidate()
        }

        // Add a BlurMaskFilter for the glow effect
        paint.maskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.NORMAL) // Glow effect
        animator.start()
        glowColorAnimator.start()
    }

    /*private fun updateShader() {
        val amplitude = height / 3f // Wave height
        val wavelength = width.toFloat() // Wave length

        val waveShift = width * gradientPosition
        val x1 = waveShift
        val x2 = waveShift + width

        // Enhanced Yin-Yang Wave
        val waveY = { x: Float ->
            try {
                amplitude * sin(2 * Math.PI * x / wavelength + waveOffset).toFloat() +
                        amplitude * sin(4 * Math.PI * x / wavelength + waveOffset).toFloat()
            } catch (e: Exception) {
                Log.e("WaveGradientView", "Invalid waveY input: x=$x, waveOffset=$waveOffset", e)
                0f // Fallback to a safe default value
            }
        }

        val y1 = waveY(x1).coerceIn(-amplitude, height.toFloat())
        val y2 = waveY(x2).coerceIn(-amplitude, height.toFloat())

        if (y1.isNaN() || y2.isNaN()) {
            Log.e("WaveGradientView", "Invalid gradient coordinates: y1=$y1, y2=$y2")
            return // Skip drawing to avoid crashing
        }

        try {
            shader = LinearGradient(
                x1, y1,
                x2, y2,
                intArrayOf(currentGlowColor, Color.TRANSPARENT),
//                glowColorArr,
                null,
                Shader.TileMode.MIRROR
            )

            val matrix = Matrix()
            matrix.setTranslate(-waveShift, 0f)
            shader?.setLocalMatrix(matrix)

            paint.shader = shader
        } catch (e: IllegalArgumentException) {
            Log.e("WaveGradientView", "Error creating shader: x1=$x1, y1=$y1, x2=$x2, y2=$y2", e)
        }
    }*/


    /*private fun updateShader() {
        val x1 = width * gradientPosition
//        val x2 = width * (gradientPosition + 2f) // Add a slight offset for dynamic shift
        val x2 = width * (gradientPosition + 0.5f) // Add a slight offset for dynamic shift

        shader = LinearGradient(
            x1, 0f, x2, height.toFloat(),
            colorArr,
            null,
            Shader.TileMode.MIRROR
//            Shader.TileMode.CLAMP
        )
        paint.shader = shader
    }*/

    private fun updateShader() {
        // Calculate wave positions (adjust amplitude and wavelength as needed)
        val amplitude = 20f // Adjust wave amplitude
        val wavelength = 100f // Adjust wavelength
        val waveFunction: (Float) -> Float =
            { x -> amplitude * sin(2 * Math.PI * x / wavelength + waveOffset).toFloat() }

        val x1 = width * gradientPosition
        val x2 = width * (gradientPosition + 0.5f) // Add a slight offset for dynamic shift

        // Create a path for the wave
        val path = Path()
        path.moveTo(0f, 0f)
        for (x in 0..width step 10) {
            val y = waveFunction(x.toFloat())
            path.lineTo(x.toFloat(), y)
        }
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(0f, height.toFloat())
        path.close()

        // Create a LinearGradient along the wave
        val gradientColors = intArrayOf(Color.TRANSPARENT, Color.BLACK)
        val gradientPositions = floatArrayOf(0f, 0.8f)
        shader = LinearGradient(
            x1, 0f, x2, height.toFloat(),
            colorArr,
            null,
            Shader.TileMode.MIRROR
//            Shader.TileMode.CLAMP
        )
        shader = LinearGradient(
            0f, 0f, width.toFloat(), 0f,
            gradientColors,
            gradientPositions,
            Shader.TileMode.CLAMP
        )

        // Apply a transformation matrix to align the gradient with the wave path
        val matrix = Matrix()
        // Optionally, customize the matrix for transformations (e.g., scaling, translating)
        (shader as LinearGradient).setLocalMatrix(matrix)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    /*private val baseColors = intArrayOf(
        Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED
    )
    private var dynamicColors = baseColors.copyOf() // Copy of colors for animation

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        maskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.NORMAL) // Glow effect
    }
    private var shader: Shader? = null

    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 5000 // Duration for the color streaming effect
        interpolator = LinearInterpolator()
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener {
            shiftColors()
            updateShader()
            invalidate()
        }
    }

    init {
        animator.start()
        updateShader()
    }

    private fun shiftColors() {
        // Shift colors in the array to create the streaming effect
        val firstColor = dynamicColors.first()
        System.arraycopy(dynamicColors, 1, dynamicColors, 0, dynamicColors.size - 1)
        dynamicColors[dynamicColors.size - 1] = firstColor
    }

    private fun updateShader() {
        try {
            shader = LinearGradient(
                0f, 0f,
                width.toFloat(), 0f, // Horizontal gradient
                dynamicColors, // Use the animated color array
                null,
                Shader.TileMode.MIRROR
            )
            paint.shader = shader
        } catch (e: IllegalArgumentException) {
            Log.e("WaveGradientView", "Error updating shader", e)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }*/
}