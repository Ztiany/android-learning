package me.ztiany.shrink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import me.ztiany.shrink.beans.Person

class MainActivity : AppCompatActivity() {

    private val  person by lazy {
        Person()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        person.name = "Abc"
        mainTv.text = person.name
    }

}
