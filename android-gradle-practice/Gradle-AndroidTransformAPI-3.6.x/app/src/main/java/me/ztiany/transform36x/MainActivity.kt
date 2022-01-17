package me.ztiany.transform36x

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.Flowable
import io.reactivex.Observable

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Flowable.defer { Flowable.just(1) }.subscribe {

        }
    }

}
