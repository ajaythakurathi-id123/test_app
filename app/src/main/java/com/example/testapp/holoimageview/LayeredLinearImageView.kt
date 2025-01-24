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
import com.example.testapp.R

class LayeredLinearImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        // Animator for cycling through holographic colors
        val colorAnimator = ValueAnimator.ofArgb(
            Color.parseColor("#44ffffff"), // Semi-transparent white
            Color.parseColor("#4499cc00"), // Semi-transparent green
            Color.parseColor("#44b3e5f2"), // Semi-transparent cyan
            Color.parseColor("#44f44336"), // Semi-transparent red
            Color.parseColor("#44ab47bc")  // Semi-transparent purple
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

        // Check if the current drawable is a LayerDrawable
        val layerDrawable = drawable as? LayerDrawable
        if (layerDrawable == null) {
            Log.e("LayeredImageView", "Drawable is not a LayerDrawable")
            return
        }

        // Ensure the LayerDrawable has at least 2 layers (image + gradients)
        if (layerDrawable.numberOfLayers < 2) {
            Log.e("LayeredImageView", "LayerDrawable does not have enough layers. Found: ${layerDrawable.numberOfLayers}")
            return
        }

        // Get the gradient drawable (assume it's the second layer)
        val gradientLayer = layerDrawable.getDrawable(1) as? LayerDrawable
        if (gradientLayer == null) {
            Log.e("LayeredImageView", "Gradient layer is missing or not a LayerDrawable")
            return
        }

        // Layer 1: Base Gradient (Transparent to White to Black)
        val gradientDrawable1 = gradientLayer.getDrawable(0) as? GradientDrawable
        gradientDrawable1?.setColors(
            intArrayOf(Color.TRANSPARENT, color, Color.BLACK) // Adjust start, center, and end colors
        )

        // Layer 2: Vertical Gradient (Top to Bottom - Green to Blue)
        val gradientDrawable2 = gradientLayer.getDrawable(1) as? GradientDrawable
        gradientDrawable2?.setColors(
            intArrayOf(
                Color.parseColor("#4499cc00"), // Semi-transparent green
                color,                         // Dynamic center color
                Color.parseColor("#4433b5e5")  // Semi-transparent blue
            )
        )

        // Layer 3: Diagonal Gradient (Top Left to Bottom Right - Cyan to Transparent)
        val gradientDrawable3 = gradientLayer.getDrawable(2) as? GradientDrawable
        gradientDrawable3?.setColors(
            intArrayOf(
                Color.parseColor("#44b3e5f2"), // Semi-transparent cyan
                color,                         // Dynamic center color
                Color.TRANSPARENT              // Transparent
            )
        )

        // Layer 4: Diagonal Gradient (Top Right to Bottom Left - Red to Transparent)
        val gradientDrawable4 = gradientLayer.getDrawable(3) as? GradientDrawable
        gradientDrawable4?.setColors(
            intArrayOf(
                Color.parseColor("#44f44336"), // Semi-transparent red
                color,                         // Dynamic center color
                Color.TRANSPARENT              // Transparent
            )
        )

        // Layer 5: Horizontal Gradient (Left to Right - Purple to Transparent)
        val gradientDrawable5 = gradientLayer.getDrawable(4) as? GradientDrawable
        gradientDrawable5?.setColors(
            intArrayOf(
                Color.parseColor("#44ab47bc"), // Semi-transparent purple
                color,                         // Dynamic center color
                Color.TRANSPARENT              // Transparent
            )
        )

        invalidate() // Redraw the view to apply changes
    }
    /*private fun updateGradientColors(color: Int) {
        // Check if the current drawable is a LayerDrawable
        val layerDrawable = drawable as? LayerDrawable
        if (layerDrawable == null) {
            Log.e("LayeredImageView", "Drawable is not a LayerDrawable")
            return
        }

        // Ensure the LayerDrawable has at least 2 layers (image + gradients)
        if (layerDrawable.numberOfLayers < 2) {
            Log.e("LayeredImageView", "LayerDrawable does not have enough layers. Found: ${layerDrawable.numberOfLayers}")
            return
        }

        // Get the gradient drawable (assume it's the second layer)
        val gradientLayer = layerDrawable.getDrawable(1) as? LayerDrawable
        if (gradientLayer == null) {
            Log.e("LayeredImageView", "Gradient layer is missing or not a LayerDrawable")
            return
        }

        // Update individual gradient layers
        for (i in 0 until gradientLayer.numberOfLayers) {
            val gradient = gradientLayer.getDrawable(i) as? GradientDrawable
            gradient?.setColors(
                intArrayOf(
                    Color.TRANSPARENT, // Start color
                    color,             // Dynamic center color
                    Color.BLACK        // End color
                )
            )
        }

        invalidate() // Redraw the view
    }*/


    /**
     * Sets a new image resource for the ImageView and overlays gradient layers.
     * @param imgRes The resource ID of the new image.
     */
    /*fun setImageResourceWithAnimation(imgRes: Int) {
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
    }*/

    fun setImageResourceWithAnimation(imgRes: Int) {
        // Original image resource
        val imageDrawable = context.getDrawable(imgRes) ?: return

        // Load the gradient drawable from XML (your layered gradient)
        val gradientDrawable = context.getDrawable(R.drawable.layered_linear_gradient_2) as? LayerDrawable
            ?: return

        // Combine the original image and the gradients
        val combinedDrawable = LayerDrawable(arrayOf(imageDrawable, gradientDrawable))

        // Set the combined drawable to the ImageView
        setImageDrawable(combinedDrawable)
    }

    // Helper methods to create the gradient drawables
    /*private fun createGradientDrawable1(): GradientDrawable {
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
    }*/
}

