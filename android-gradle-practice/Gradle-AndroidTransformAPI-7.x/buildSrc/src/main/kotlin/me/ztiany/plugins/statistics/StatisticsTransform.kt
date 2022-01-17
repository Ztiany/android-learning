package me.ztiany.plugins.statistics

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform

class StatisticsTransform:Transform() {

    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        TODO("Not yet implemented")
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        TODO("Not yet implemented")
    }

    override fun isIncremental(): Boolean {
        TODO("Not yet implemented")
    }

}