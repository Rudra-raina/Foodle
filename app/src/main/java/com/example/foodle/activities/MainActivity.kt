package com.example.foodle.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.foodle.R
import com.example.foodle.databinding.ActivityMainBinding
import com.example.foodle.notifications.NotifyWorker
import com.example.foodle.utils.Constants
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityMainBinding
    private lateinit var mNavController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.bottomNavView.background=null

        mBinding.bottomNavView.menu.getItem(2).isEnabled=false

        mNavController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_fav, R.id.navigation_search,R.id.navigation_notifs
            )
        )
        setupActionBarWithNavController(mNavController, appBarConfiguration)
        mBinding.bottomNavView.setupWithNavController(mNavController)

        mBinding.fab.setOnClickListener(){
            val intent= Intent(this,AddEditDishActivity::class.java)
            startActivity(intent)
        }

        window.statusBarColor= ContextCompat.getColor(this,R.color.app_bg_color)
        supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(this@MainActivity,R.drawable.app_bg_color))

        if(intent.hasExtra(Constants.NOTIFICATION_ID)){
            val notificationId=intent.getIntExtra(Constants.NOTIFICATION_ID,0)
            mBinding.bottomNavView.selectedItemId=R.id.navigation_notifs
        }

        Log.e("pop","onCreate")
        startWork()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController,null)
    }

    fun hideBottomNavView(){
        mBinding.navLayout.clearAnimation()
        mBinding.navLayout.animate().translationY(mBinding.navLayout.height.toFloat()).duration=300
        mBinding.navLayout.visibility=View.GONE
    }

    fun showBottomNavView(){
        mBinding.navLayout.clearAnimation()
        mBinding.navLayout.animate().translationY(0f).duration=300
        mBinding.navLayout.visibility=View.VISIBLE
    }

    private fun createConstraints()= Constraints.Builder()
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .setRequiresCharging(false)
        .setRequiresBatteryNotLow(false)
        .build()

    private fun createWorkRequest()= PeriodicWorkRequestBuilder<NotifyWorker>(15,TimeUnit.MINUTES)
        .setConstraints(createConstraints())
        .build()

    private fun startWork(){
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("Dish Notify work",ExistingPeriodicWorkPolicy.KEEP,createWorkRequest())
    }
}