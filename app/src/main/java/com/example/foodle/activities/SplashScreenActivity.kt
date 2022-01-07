package com.example.foodle.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.foodle.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            window.insetsController!!.hide(WindowInsets.Type.statusBars())
        }else{
            @Suppress("DEPRECATION")
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        },2940)
    }
}