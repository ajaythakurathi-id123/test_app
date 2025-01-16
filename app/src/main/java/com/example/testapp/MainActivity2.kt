package com.example.testapp

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.testapp.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity(R.layout.activity_main_2) {

    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)

        setContentView(binding.root)

        // Apply the smooth gradient effect
//        applySmoothGradientEffect(binding.imageView)


        //radial_gradient
//        val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.blob_scale)
//        binding.imageView.startAnimation(scaleAnimation)
    }

    private fun applySmoothGradientEffect(imageView: ImageView) {
        // Create a ShapeDrawable with a shader-based gradient
        val gradientDrawable = createGradientDrawable()

        // Set the ShapeDrawable as foreground or overlay it
//        imageView.background = gradientDrawable

        imageView.foreground = gradientDrawable

        // Animate the gradient
        animateGradient(gradientDrawable)
    }

    private fun createGradientDrawable(): ShapeDrawable {
        val colors = intArrayOf(
            Color.argb(76, 255, 0, 0), // Red with transparency
            Color.argb(76, 0, 0, 255), // Blue with transparency
            Color.argb(76, 0, 255, 0), // Green with transparency
            Color.argb(76, 255, 255, 0) // Yellow with transparency
        )

        // Create the shader and set it to the drawable
        val shader = LinearGradient(
            0f, 0f, 0f, 1000f, colors, null, Shader.TileMode.CLAMP
        )

        val shapeDrawable = ShapeDrawable(RectShape())
        shapeDrawable.paint.shader = shader

        return shapeDrawable
    }

    private fun animateGradient(drawable: ShapeDrawable) {
        val paint = drawable.paint

        val animator = ValueAnimator.ofFloat(0f, 1000f)
        animator.duration = 2000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            val shader = LinearGradient(
                0f, animatedValue, 0f, animatedValue + 1000f,
                intArrayOf(
                    Color.argb(76, 255, 0, 0),
                    Color.argb(76, 0, 0, 255),
                    Color.argb(76, 0, 255, 0),
                    Color.argb(76, 255, 255, 0)
                ),
                null,
                Shader.TileMode.CLAMP
            )
            paint.shader = shader
            drawable.invalidateSelf()
        }
        animator.start()
    }
}
