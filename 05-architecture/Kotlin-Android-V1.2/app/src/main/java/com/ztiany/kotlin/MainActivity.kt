package com.ztiany.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ztiany.kotlin.anko.common.AnkoCommonActivity
import com.ztiany.kotlin.anko.common.DialogsActivity
import com.ztiany.kotlin.anko.common.LoggerActivity
import com.ztiany.kotlin.anko.common.intents.RawActivity
import com.ztiany.kotlin.anko.coroutines.AnkoCoroutinesActivity
import com.ztiany.kotlin.anko.layouts.AnkoLayoutsActivity
import com.ztiany.kotlin.coroutines.CoroutinesActivity
import com.ztiany.kotlin.extension.ExtensionSampleActivity
import org.jetbrains.anko.button
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.verticalLayout


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {

            button("Android Extension 插件") {
                onClick {
                    startActivity(intentFor<ExtensionSampleActivity>())
                }
            }
            button("Android 协程示例") {
                onClick {
                    startActivity(intentFor<CoroutinesActivity>())
                }
            }

            button("Anko Common") {
                onClick {
                    startActivity(intentFor<AnkoCommonActivity>())
                }
            }

            button("Anko Common Intents") {
                onClick {
                    startActivity(intentFor<RawActivity>())
                }
            }

            button("Anko Common Dialogs") {
                onClick {
                    startActivity(intentFor<DialogsActivity>())
                }
            }

            button("Anko Common Logger") {
                onClick {
                    startActivity(intentFor<LoggerActivity>())
                }
            }

            button("Anko Layouts") {
                onClick {
                    startActivity(intentFor<AnkoLayoutsActivity>())
                }
            }

            button("Anko Coroutines") {
                onClick {
                    startActivity(intentFor<AnkoCoroutinesActivity>())
                }
            }
        }

    }

}
