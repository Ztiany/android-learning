package com.hencoder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.hencoder.constraintlayout.R

class ConstraintSetX : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //showExample1()
        showExample2()
    }

    private fun showExample1() {
        setContentView(R.layout.activity_constraint_set)
    }

    private fun showExample2() {
        setContentView(R.layout.activity_constraint_start)
    }

    fun onClick1(view: View) {
        val constraintLayout = view as ConstraintLayout
        val constraintSet = ConstraintSet().apply {
            clone(constraintLayout)
            connect(
                    R.id.twitter,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM
            )
        }
        constraintSet.applyTo(constraintLayout)
    }

    fun onClick2(view: View) {
        val constraintLayout = view as ConstraintLayout
        val constraintSet = ConstraintSet().apply {
            isForceId = false
            //从另一个布局中复制新的约束
            clone(this@ConstraintSetX,
                R.layout.activity_constraint_end
            )
        }
        TransitionManager.beginDelayedTransition(constraintLayout)
        //把新的约束规则应用到 constraintLayout
        constraintSet.applyTo(constraintLayout)
    }

}
