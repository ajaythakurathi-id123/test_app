package com.example.testapp.customview

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.example.testapp.R


class HologramImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    AppCompatImageView(context, attrs) {

    private var hologramIntensity = 1.0f // Initial intensity
    private val image: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.logo)
    private var currentColor: Int = ContextCompat.getColor(context, R.color.purple_500)
    private val targetColor: Int = ContextCompat.getColor(context, R.color.teal_200)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()
    private val colorList = listOf(
        getColor("#FF0000"),
        getColor("#FF7F00"),
        getColor("#FFFF00"),
        getColor("#00FF00"),
        getColor("#0000FF"),
        getColor("#4B0082"),
        getColor("#8F00FF")
    )


    fun startAnimation() {
//        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), currentColor, targetColor)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), *colorList.toTypedArray())
        colorAnimation.duration = 2000 // Adjust the duration as needed
        colorAnimation.interpolator = LinearInterpolator()
        colorAnimation.repeatCount = ValueAnimator.INFINITE
        colorAnimation.repeatMode = ValueAnimator.REVERSE

        colorAnimation.addUpdateListener { animator ->
            currentColor = animator.animatedValue as Int
            invalidate()
        }

        colorAnimation.start()
    }

    private fun getColor(colorCode: String): Int {
        return Color.parseColor(colorCode)
    }

    override fun onDraw(canvas: Canvas) {
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
    }
}