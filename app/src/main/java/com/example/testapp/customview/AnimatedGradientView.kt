package com.example.testapp.customview

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.example.testapp.R


class AnimatedGradientView : View {

    private val image: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.logo)
    private var currentColor: Int = ContextCompat.getColor(context, R.color.purple_500)
    private val rectF = RectF()

    private val colorArr = intArrayOf(
        Color.BLUE,
        Color.CYAN,
        Color.GREEN,
        Color.YELLOW,
        Color.RED
    )

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var shader: Shader = LinearGradient(
        0f, 0f, width.toFloat(), height.toFloat(),
        colorArr + colorArr.reversedArray() + colorArr + colorArr.reversedArray(),
        null, Shader.TileMode.MIRROR
//        null, Shader.TileMode.CLAMP
    )
    private val animator = ValueAnimator.ofFloat(0f, 1f)
    private var gradientPosition = 0f

    init {
        paint.shader = shader

        animator.duration = 5000 // Animation duration in milliseconds
        animator.interpolator = LinearInterpolator()
        animator.repeatCount = ValueAnimator.INFINITE
//        animator.repeatMode = ValueAnimator.RESTART
        animator.repeatMode = ValueAnimator.REVERSE
        animator.addUpdateListener { animation ->
            gradientPosition = animation.animatedValue as Float
            updateShader()
            invalidate()
        }
        animator.start()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private fun updateShader() {
        val x1 = width * gradientPosition
//        val x2 = width * (gradientPosition + 2f) // Add a slight offset for dynamic shift
        val x2 = width * (gradientPosition + 0.2f) // Add a slight offset for dynamic shift

        shader = LinearGradient(
            x1, 0f, x2, height.toFloat(),
            colorArr + colorArr.reversedArray() + colorArr + colorArr.reversedArray(),
            null,
            Shader.TileMode.MIRROR
//            Shader.TileMode.CLAMP
        )
        paint.shader = shader
    }

    /*override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw background color
        canvas.drawColor(currentColor)

        // Calculate the aspect ratio
        val aspectRatio = image.width.toFloat() / image.height.toFloat()

        // Calculate the target width and height to maintain the aspect ratio
        val targetWidth: Float
        val targetHeight: Float

        if (width / aspectRatio > height) {
            targetWidth = height * aspectRatio
            targetHeight = height.toFloat()
        } else {
            targetWidth = width.toFloat()
            targetHeight = width / aspectRatio
        }

        // Calculate the position to center the image
        val x = (width - targetWidth) / 2f
        val y = (height - targetHeight) / 2f

        // Set the destination rectangle
        rectF.set(x, y, x + targetWidth, y + targetHeight)

        // Draw the image
        canvas.drawBitmap(image, null, rectF, paint)
    }*/
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }
}