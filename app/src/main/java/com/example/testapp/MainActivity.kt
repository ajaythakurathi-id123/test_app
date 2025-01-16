package com.example.testapp

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.lifecycleScope
import com.example.testapp.databinding.ActivityMainBinding
import com.example.testapp.databinding.HologramMotionLayoutBinding
import com.example.testapp.scanner.camera.BarcodeScanActivity
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.Base32
import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator
import dev.turingcomplete.kotlinonetimepassword.HmacAlgorithm
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordConfig
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordGenerator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    //    val patternPlus = "\\+(\\d{1,3}\\s?)?(\\(\\d+\\)\\s?)?(\\d{1,5}[-\\s]?)*\\d{1,10}(ext\\d{1,5})?"   //Arjun
    val patternPlus =
        "\\+\\d{7,15}\$|^\\+\\d+ext\\d{1,5}\$"  //Ajay, working
    val patternUS = "^1\\s?\\-?\\(?\\d{3}\\)?[-\\s]?\\d{3}[-\\s]?\\d{4}$"

    private lateinit var binding: ActivityMainBinding
    private lateinit var hologramBinding: HologramMotionLayoutBinding

    private var isAtStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate: ")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        val motionHologram = binding.layoutMotion.motionHologram
        // Access motion_hologram through hologramBinding
//        val motionHologram = hologramBinding.motionHologram

        // Example usage
//        motionHologram.setTransition(R.id.start, R.id.end)

        /*binding.btnTransition.setOnClickListener {
            if (isAtStart) {
                motionHologram.transitionToEnd()
            } else {
                motionHologram.transitionToStart()
            }
            isAtStart = !isAtStart
        }*/

        // Start the animation loop
        lifecycleScope.launch {
            startAnimationLoop(motionHologram)
        }

        // Tilt up
        /*motionHologram.setTransition(R.id.start, R.id.tiltUp)
        motionHologram.transitionToEnd()

        // Tilt down
        motionHologram.setTransition(R.id.start, R.id.tiltDown)
        motionHologram.transitionToEnd()

        // Tilt left
        motionHologram.setTransition(R.id.start, R.id.tiltLeft)
        motionHologram.transitionToEnd()

        // Tilt right
        motionHologram.setTransition(R.id.start, R.id.tiltRight)
        motionHologram.transitionToEnd()*/

//        motionHologram.transitionToEnd()
//        motionHologram.transitionToStart()

        binding.tv.setOnClickListener {
//            setWebView("https://www.google.com")
//            setWebView("https://www.id123.io/terms/")
            setWebView("https://id123.io/terms/")
        }

        binding.btnNavToSecond.setOnClickListener {
//            startActivity(Intent(applicationContext, MainActivity::class.java))
//            startActivity(Intent(applicationContext, SecondActivity::class.java))
            topt = generateOtp()
//            topt = toptGoogle()
//            topt =
//                generateTOTP("6SVR62A5VINOHE2PGZM5BOPR44XIF646VNQT6CTO46RIXQ7GJXYIK6XDFTXVHPDXKZKBIM45LEWUBNL2R5SO7F6E6YMJCPHPVNTI6ZY")
            Log.d(TAG, "onCreate: TOPT: $topt")
        }

        binding.btnVerify.setOnClickListener {
            val isValid = timeBasedOneTimePasswordGenerator!!.isValid(topt)
            Log.d(TAG, "onCreate: TOPT: isValid: $isValid")
        }

        binding.btnScan.setOnClickListener {
            startActivity(Intent(applicationContext, BarcodeScanActivity::class.java))
        }
//        setWebView("https://www.google.com")
//        testRegex()
    }

    /*private fun applySmoothGradientEffect(imageView: AppCompatImageView) {
        // Set up the GradientDrawable for the gradient effect
        val gradientDrawable = createGradientDrawable()
        imageView.foreground = gradientDrawable // Apply gradient as foreground

        // Animate the gradient
        animateGradient(gradientDrawable)
    }

    private fun createGradientDrawable(): LinearGradient {
        val colors = intArrayOf(
            Color.argb(76, 255, 0, 0), // Red with transparency
            Color.argb(76, 0, 0, 255), // Blue with transparency
            Color.argb(76, 0, 255, 0), // Green with transparency
            Color.argb(76, 255, 255, 0) // Yellow with transparency
        )

        // Create a LinearGradient (you can change to SweepGradient for more dynamic effect)
        return LinearGradient(
            0f, 0f, 0f, 1000f, colors, null, Shader.TileMode.CLAMP
        )
    }

    private fun animateGradient(gradientDrawable: LinearGradient) {
        // Create the ObjectAnimator to animate the gradient movement (rotation here)
        val angleAnimator = ObjectAnimator.ofFloat(gradientDrawable, "angle", 0f, 360f)
        angleAnimator.duration = 5000 // Duration of each animation (5 seconds)
        angleAnimator.repeatCount = ObjectAnimator.INFINITE
        angleAnimator.start()
    }*/

    private suspend fun startAnimationLoop(motionLayout: MotionLayout) {
        val transitions = listOf(
            R.id.start to R.id.tiltUp,
            R.id.tiltUp to R.id.tiltRight,
            R.id.tiltRight to R.id.tiltDown,
            R.id.tiltDown to R.id.tiltLeft,
            R.id.tiltLeft to R.id.start
        )

        while (true) { // Infinite loop for cycling animation
            for ((start, end) in transitions) {
                motionLayout.setTransition(start, end)
                motionLayout.transitionToEnd()
                delay(200) // 200ms delay for each transition
            }
        }
    }

    var topt = ""
    val secret =
        "PKTN4O5Y66ZZUIEEPHCQMDXPVK6WPCXKRARLUNYDOYO3JEAAUKN6RYV4XWTET6V3K65BHXJPQCCY2DZUYK2RRRKJGJ3KDS23QOV3QAI"
    var timeBasedOneTimePasswordGenerator: TimeBasedOneTimePasswordGenerator? = null

    private fun toptGoogle(): String {
// Warning: the length of the plain text may be limited, see next chapter
        val plainTextSecret = secret.toByteArray(Charsets.UTF_8)

// This is the encoded one to use in most of the generators (Base32 is from the Apache commons codec library)
        val base32EncodedSecret = Base32().encodeToString(plainTextSecret)
        println("Base32 encoded secret to be used in the Google Authenticator app: $base32EncodedSecret")

        val googleAuthenticator = GoogleAuthenticator(plainTextSecret)
//        val googleAuthenticator = GoogleAuthenticator(base32EncodedSecret)
        return googleAuthenticator.generate()
    }

    private fun generateOtp(): String {
        val config = TimeBasedOneTimePasswordConfig(
            codeDigits = 6,
            hmacAlgorithm = HmacAlgorithm.SHA1,
            timeStep = 30,
            timeStepUnit = TimeUnit.SECONDS
        )

//            val byteArray = secret.toByteArray(Charsets.UTF_8)
//            val base32EncodedSecret = Base32().encodeToString(byteArray)
//            Timber.d("Base32EncodedSecret: %s", base32EncodedSecret)


        timeBasedOneTimePasswordGenerator = TimeBasedOneTimePasswordGenerator(
//            Base32().encode(secret.toByteArray()),
            Base32().encode(secret.toByteArray()),
//            secret.toByteArray(),
            config
        )

        return timeBasedOneTimePasswordGenerator!!.generate()
//        Log.d(TAG, "OTP:: $otp")

    }

    private val TIME_STEP = 120 // Time step in seconds
    private val DIGITS = 6 // Number of digits in the OTP
    private val ALGORITHM = "HmacSHA1"

    private fun generateTOTP(secretKey: String): String {
        val counter =
            System.currentTimeMillis() / 1000 / TIME_STEP // Get current time in 30-second intervals
        val counterHex = java.lang.Long.toHexString(counter)
        var counterHexPadded = counterHex
        while (counterHexPadded.length < 16) { // Pad counter to 16 characters with leading zeros
            counterHexPadded = "0$counterHexPadded"
        }

        val keyBytes = secretKey.toByteArray()
        val counterBytes = hexStringToByteArray(counterHexPadded)

        val keySpec = SecretKeySpec(keyBytes, ALGORITHM)
        return try {
            val mac = Mac.getInstance(ALGORITHM)
            mac.init(keySpec)
            val hash = mac.doFinal(counterBytes)

            // Generate the OTP from the hash
            val offset = hash[hash.size - 1].toInt() and 0xf // Determine the starting offset
            val binary =
                ((hash[offset].toInt() and 0x7f) shl 24) or ((hash[offset + 1].toInt() and 0xff) shl 16) or
                        ((hash[offset + 2].toInt() and 0xff) shl 8) or (hash[offset + 3].toInt() and 0xff)

            binary.toString()
//            val otp = binary % 10.0.pow(DIGITS.toDouble())
//                .toInt() // Truncate to specified number of digits

            // Pad the OTP with leading zeros
//            String.format("%0${DIGITS}d", otp)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun hexStringToByteArray(hexString: String): ByteArray {
        val len = hexString.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(hexString[i], 16) shl 4) + Character.digit(
                hexString[i + 1],
                16
            )).toByte()
            i += 2
        }
        return data
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    private fun setWebView(url: String) {
        binding.webView.visibility = View.VISIBLE
        binding.webView.loadUrl(url)

        /*val webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: android.webkit.WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(url)
                return true
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
        binding.webView.webViewClient = webViewClient*/
        /*binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: android.webkit.WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(url)
                return true
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }*/
    }

    override fun onBackPressed() {

        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            binding.webView.visibility = View.GONE
            super.onBackPressed()
        }
    }

    private fun testRegex() {
        val regexPlus = Regex(patternPlus)
        val regexUS = Regex(patternUS)

        /*val phoneNoList = listOf(
            "+123 1239 32323",
            "+1 123932323",[
            "+1123213213",
            "+123 12345"
        )*/
        val phoneNoList = listOf(
            "+(8655) 7828 70",
            "+91 8540ext567",
            "+91 7798628540ext567",
            "+91 26717-90",
            "99786754",
            "+91 779862854000",
            "+18779688041",
            "1(555)555-5555",
            "1(555) 555-5555",
            "1 (555) 555 5555",
            "1 555-555-5555",
            "1-555-555-5555",
            "(250) 555-0199",
            "+86-55-78 28 (70)",
            "7798628540",
            "+779862854099",
            "898341276234",
            "(250) 555-0199",
            "+91 865578ext70",
            "+91 67854",
            "7798628540",
            "1(555)555-5555",
            "1(555) 555-5555",
            "1 (555) 555 5555",
            "1 555-555-5555",
            "1-555-555-5555",
            "(250) 555-0199",
            "(250) 555-0199",
            "8655782870",
            "+91 8655782870",
            "+91 7798628540ext76",
            "996456432",
            "17775555555",
            "1(555) 555-7777",
            "1 (777) 555 4567",
            "1 555-555-5555",
            "1-555-555-5555",
            "(250) 555-0199",
            "250-555-0199",
            "250/555-0199",
            "250-5550199",
            "1–800–4-LAWYER",
            "+001-212-456-7890",
            "+44 20 7183 8750",
        )



        for (phoneNo in phoneNoList) {
            val phoneWithoutSpace = phoneNo.replace(" ", "")
            val regexPlusResult = regexPlus.find(phoneWithoutSpace)
            val regexUSResult = regexUS.find(phoneNo)

            if (regexPlusResult != null) {
                Log.d(TAG, "$phoneNo :regexPlusResult: Matched")
            } else {
                Log.d(TAG, "$phoneNo :regexPlusResult: Not Matched")
            }
            if (regexUSResult != null) {
                Log.d(TAG, "$phoneNo :regexUSResult: Matched")
            } else {
                Log.d(TAG, "$phoneNo :regexUSResult: Not Matched")
            }

            Log.d(TAG, "-------------------: ")

        }
    }

    companion object {
        private const val TAG = "MainActivity_${SecondActivity.MESSAGE}"
    }
}