package com.ztiany.androidx.jetpack.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ztiany.androidx.kotlin.databinding.ActivitiesSecondBinding
import java.util.UUID

internal const val SECOND_ACTIVITY_KET = "second_activity_ket"

class SecondActivity : AppCompatActivity() {

    private val vb by lazy { ActivitiesSecondBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vb.root)
        vb.btnSetResult.setOnClickListener {
            setResult(RESULT_OK, Intent().apply {
                putExtra(SECOND_ACTIVITY_KET, "Result from SecondActivity" + UUID.randomUUID())
            })
            supportFinishAfterTransition()
        }
    }

}