package com.ztiany.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ztiany.kotlin.anko.common.AnkoCommonActivity
import com.ztiany.kotlin.anko.common.DialogsActivity
import com.ztiany.kotlin.anko.common.LoggerActivity
import com.ztiany.kotlin.anko.common.intents.RawActivity
import com.ztiany.kotlin.anko.coroutines.AnkoCoroutinesActivity
import com.ztiany.kotlin.anko.layouts.AnkoLayoutsActivity
import com.ztiany.kotlin.coroutines.CoroutineUIGuideActivity
import com.ztiany.kotlin.coroutines.CoroutinesActivity
import com.ztiany.kotlin.coroutines.LifecycleCoroutineExSampleActivity
import com.ztiany.kotlin.extension.ExtensionSampleActivity
import com.ztiany.kotlin.ktx.KtxActivity
import org.jetbrains.anko.button
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.scrollView
import org.jetbrains.anko.verticalLayout


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scrollView {

            verticalLayout {

                button("Android Extension 插件") {
                    setOnClickListener {
                        startActivity(intentFor<ExtensionSampleActivity>())
                    }
                }
                button("Android 协程示例") {
                    setOnClickListener {
                        startActivity(intentFor<CoroutinesActivity>())
                    }
                }

                button("Anko Common") {
                    setOnClickListener {
                        startActivity(intentFor<AnkoCommonActivity>())
                    }
                }

                button("Anko Common Intents") {
                    setOnClickListener {
                        startActivity(intentFor<RawActivity>())
                    }
                }

                button("Anko Common Dialogs") {
                    setOnClickListener {
                        startActivity(intentFor<DialogsActivity>())
                    }
                }

                button("Anko Common Logger") {
                    setOnClickListener {
                        startActivity(intentFor<LoggerActivity>())
                    }
                }

                button("Anko Layouts") {
                    setOnClickListener {
                        startActivity(intentFor<AnkoLayoutsActivity>())
                    }
                }

                button("Anko Coroutines") {
                    setOnClickListener {
                        startActivity(intentFor<AnkoCoroutinesActivity>())
                    }
                }

                button("Ktx") {
                    setOnClickListener {
                        startActivity(intentFor<KtxActivity>())
                    }
                }

                button("Coroutine official UI Guide") {
                    setOnClickListener {
                        startActivity(intentFor<CoroutineUIGuideActivity>())
                    }
                }
             button("Lifecycle Coroutine Ex") {
                    setOnClickListener {
                        startActivity(intentFor<LifecycleCoroutineExSampleActivity>())
                    }
                }

            }
        }

    }

}
