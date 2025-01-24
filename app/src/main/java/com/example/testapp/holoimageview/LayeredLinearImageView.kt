package com.example.testapp.holoimageview

//LayeredLinearImageView
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

class LayeredLinearImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

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

        // Ensure the LayerDrawable has the expected number of layers
        /*if (drawable.numberOfLayers < 5) {
            Log.d("TAG", "updateGradientColors: not 5")
        }*/

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

    /**
     * Sets a new image resource for the ImageView and overlays gradient layers.
     * @param imgRes The resource ID of the new image.
     */
    fun setImageResourceWithAnimation(imgRes: Int) {
        val originalDrawable = context.getDrawable(imgRes) ?: return

        // Create a LayerDrawable with your gradients
        val layerDrawable = LayerDrawable(arrayOf(
            createGradientDrawable1(), // Layer 1
            createGradientDrawable2(), // Layer 2
            createGradientDrawable3(), // Layer 3
            createGradientDrawable4(), // Layer 4
            createGradientDrawable5()  // Layer 5
        ))

        // Set the original image as the bottom layer
        val imageLayerDrawable = LayerDrawable(arrayOf(originalDrawable, layerDrawable))
        setImageDrawable(imageLayerDrawable)
    }

    // Helper methods to create the gradient drawables
    private fun createGradientDrawable1(): GradientDrawable {
        return GradientDrawable().apply {
            orientation = GradientDrawable.Orientation.LEFT_RIGHT
            colors = intArrayOf(Color.TRANSPARENT, Color.WHITE, Color.BLACK)
        }
    }

    private fun createGradientDrawable2(): GradientDrawable {
        return GradientDrawable().apply {
            orientation = GradientDrawable.Orientation.TOP_BOTTOM
            colors = intArrayOf(Color.parseColor("#7799cc00"), Color.WHITE, Color.parseColor("#7733b5e5"))
        }
    }

    private fun createGradientDrawable3(): GradientDrawable {
        return GradientDrawable().apply {
            orientation = GradientDrawable.Orientation.TL_BR
            colors = intArrayOf(Color.parseColor("#77b3e5f2"), Color.TRANSPARENT)
        }
    }

    private fun createGradientDrawable4(): GradientDrawable {
        return GradientDrawable().apply {
            orientation = GradientDrawable.Orientation.TR_BL
            colors = intArrayOf(Color.parseColor("#77f44336"), Color.TRANSPARENT)
        }
    }

    private fun createGradientDrawable5(): GradientDrawable {
        return GradientDrawable().apply {
            orientation = GradientDrawable.Orientation.LEFT_RIGHT
            colors = intArrayOf(Color.parseColor("#77ab47bc"), Color.TRANSPARENT)
        }
    }
}

