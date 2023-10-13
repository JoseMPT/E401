package mx.tecnm.cdhidalgo.e401

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Pair
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Splash : AppCompatActivity() {
    private lateinit var logo : ImageView
    private lateinit var animation : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        logo = findViewById(R.id.logo)
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_splash)

        logo.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Login::class.java)

            val transition = ActivityOptions.makeSceneTransitionAnimation(this, Pair(logo, "logo_animation"))

            startActivity(intent, transition.toBundle())
        }, 2000)
    }
}