package com.example.testapp.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView

/*The android:angle in the gradient tag specifies the direction of the gradient.

android:angle="0": The gradient will go from left to right (horizontal gradient).
android:angle="45": The gradient will go from top-left to bottom-right (diagonal gradient).
android:angle="90": The gradient will go from top to bottom (vertical gradient).
android:angle="135": The gradient will go from top-right to bottom-left (diagonal gradient in the opposite direction).*/

class LayeredLinearHologram @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    // layer 1
    /*init {
        val colorAnimator = ValueAnimator.ofArgb(Color.WHITE, Color.CYAN, Color.GREEN)
        colorAnimator.duration = 2000 // Duration of the color change
        colorAnimator.repeatCount = ValueAnimator.INFINITE
        colorAnimator.repeatMode = ValueAnimator.REVERSE

        colorAnimator.addUpdateListener { animation ->
            val color = animation.animatedValue as Int
            // Update the layer list drawable dynamically
            updateGradientColor(color)
        }
        colorAnimator.start()
    }

    private fun updateGradientColor(color: Int) {
        // Get the current drawable (this should be your layered drawable)
        val drawable = drawable as? LayerDrawable
        if (drawable != null) {
            // Update the first layer's gradient color
//            val gradientDrawable1 = drawable.getDrawable(0) as? GradientDrawable
//            val gradientDrawable2 = drawable.getDrawable(1) as? GradientDrawable

            // Update the gradient color for the first and second layer (if applicable)
            *//*if (gradientDrawable1 != null) {
                gradientDrawable1.colors = intArrayOf(color, Color.BLACK)
            }
            if (gradientDrawable2 != null) {
                gradientDrawable2.colors = intArrayOf(color, Color.TRANSPARENT)
            }*//*
// Layer 1: Base Gradient (Transparent to White to Black)
            val gradientDrawable1 = drawable.getDrawable(0) as? GradientDrawable
            gradientDrawable1?.setColors(
                intArrayOf(Color.parseColor("#77ffffff"), Color.TRANSPARENT, Color.parseColor("#77000000"))  // Adjust start, center, and end colors
            )

            // Layer 2: Green to Blue Gradient (Vertical)
            val gradientDrawable2 = drawable.getDrawable(1) as? GradientDrawable
            gradientDrawable2?.setColors(
                intArrayOf(Color.parseColor("#7799cc00"), Color.TRANSPARENT, Color.parseColor("#7733b5e5"))
            )
            // Invalidate to trigger a redraw with the new gradient
            invalidate()
        }
    }*/

    // layer 2
    init {
        // Initialize ValueAnimator for animating colors
        val colorAnimator = ValueAnimator.ofArgb(
            Color.WHITE, Color.CYAN, Color.GREEN, Color.RED
        )
        colorAnimator.duration = 2000 // Duration for one cycle
        colorAnimator.repeatCount = ValueAnimator.INFINITE // Repeat animation indefinitely
        colorAnimator.repeatMode = ValueAnimator.REVERSE // Reverse direction after each cycle

        // Update listener to change the gradient colors dynamically
        colorAnimator.addUpdateListener { animation ->
            val color = animation.animatedValue as Int
            updateGradientColors(color)
        }

        // Start the animation
        colorAnimator.start()
    }

    private fun updateGradientColors(color: Int) {
        // Retrieve the current LayerDrawable
        val drawable = drawable as? LayerDrawable
        if (drawable != null) {
            // Layer 1: Base Gradient (Transparent to White to Black)
            val gradientDrawable1 = drawable.getDrawable(0) as? GradientDrawable
            gradientDrawable1?.setColors(
                intArrayOf(color, Color.TRANSPARENT, Color.BLACK)  // Adjust start, center, and end colors
            )

            // Layer 2: Green to Blue Gradient (Vertical)
            val gradientDrawable2 = drawable.getDrawable(1) as? GradientDrawable
            gradientDrawable2?.setColors(
                intArrayOf(Color.parseColor("#7799cc00"), Color.TRANSPARENT, Color.parseColor("#7733b5e5"))
            )

            // Layer 3: Cyan to Transparent Gradient (45-degree angle)
            val gradientDrawable3 = drawable.getDrawable(2) as? GradientDrawable
            gradientDrawable3?.setColors(
                intArrayOf(Color.parseColor("#77b3e5f2"), Color.TRANSPARENT)
            )

            // Layer 4: Red to Transparent Gradient (135-degree angle)
            val gradientDrawable4 = drawable.getDrawable(3) as? GradientDrawable
            gradientDrawable4?.setColors(
                intArrayOf(Color.parseColor("#77f44336"), Color.TRANSPARENT)
            )

            // Layer 5: Purple to Transparent Gradient (Horizontal)
            val gradientDrawable5 = drawable.getDrawable(4) as? GradientDrawable
            gradientDrawable5?.setColors(
                intArrayOf(Color.parseColor("#77ab47bc"), Color.TRANSPARENT)
            )

            // Trigger a redraw to apply the updated gradient
            invalidate()
        }
    }
}