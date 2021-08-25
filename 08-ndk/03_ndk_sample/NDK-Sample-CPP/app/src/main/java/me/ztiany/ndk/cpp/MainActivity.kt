package me.ztiany.ndk.cpp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.main_activity.*
import me.ztiany.ndk.cpp.flock.FlockActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setUpViews()
    }

    private fun setUpViews() {
        mainBtnFlockSample.setOnClickListener {
            startActivity(Intent(this, FlockActivity::class.java))
        }
    }

}
