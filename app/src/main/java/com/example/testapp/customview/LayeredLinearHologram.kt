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
        // Animator for cycling through holographic colors
        val colorAnimator = ValueAnimator.ofArgb(
            Color.parseColor("#77ffffff"), // Semi-transparent white
            Color.parseColor("#7799cc00"), // Semi-transparent green
            Color.parseColor("#77b3e5f2"), // Semi-transparent cyan
            Color.parseColor("#77f44336"), // Semi-transparent red
            Color.parseColor("#77ab47bc")  // Semi-transparent purple
        )
        colorAnimator.duration = 2000 // Animation duration in milliseconds
        colorAnimator.repeatCount = ValueAnimator.INFINITE
        colorAnimator.repeatMode = ValueAnimator.REVERSE
        colorAnimator.addUpdateListener { animation ->
            val color = animation.animatedValue as Int
            updateGradientColors(color) // Update gradient colors dynamically
        }
        colorAnimator.start()
    }

    /**
     * Updates the gradient colors for all layers dynamically.
     * @param color The animated color used for updating gradients.
     */
    private fun updateGradientColors(color: Int) {
        val drawable = drawable as? LayerDrawable ?: return

        // Layer 1: Base Gradient (Transparent to White to Black)
        val gradientDrawable1 = drawable.getDrawable(0) as? GradientDrawable
        gradientDrawable1?.setColors(
            intArrayOf(Color.TRANSPARENT, color, Color.BLACK) // Adjust start, center, and end colors
        )

        // Layer 2: Vertical Gradient (Top to Bottom - Green to Blue)
        val gradientDrawable2 = drawable.getDrawable(1) as? GradientDrawable
        gradientDrawable2?.setColors(
            intArrayOf(
                Color.parseColor("#7799cc00"), // Semi-transparent green
                color,                         // Dynamic center color
                Color.parseColor("#7733b5e5")  // Semi-transparent blue
            )
        )

        // Layer 3: Diagonal Gradient (Top Left to Bottom Right - Cyan to Transparent)
        val gradientDrawable3 = drawable.getDrawable(2) as? GradientDrawable
        gradientDrawable3?.setColors(
            intArrayOf(
                Color.parseColor("#77b3e5f2"), // Semi-transparent cyan
                color,                         // Dynamic center color
                Color.TRANSPARENT              // Transparent
            )
        )

        // Layer 4: Diagonal Gradient (Top Right to Bottom Left - Red to Transparent)
        val gradientDrawable4 = drawable.getDrawable(3) as? GradientDrawable
        gradientDrawable4?.setColors(
            intArrayOf(
                Color.parseColor("#77f44336"), // Semi-transparent red
                color,                         // Dynamic center color
                Color.TRANSPARENT              // Transparent
            )
        )

        // Layer 5: Horizontal Gradient (Left to Right - Purple to Transparent)
        val gradientDrawable5 = drawable.getDrawable(4) as? GradientDrawable
        gradientDrawable5?.setColors(
            intArrayOf(
                Color.parseColor("#77ab47bc"), // Semi-transparent purple
                color,                         // Dynamic center color
                Color.TRANSPARENT              // Transparent
            )
        )

        invalidate() // Redraw the view to apply changes
    }
}