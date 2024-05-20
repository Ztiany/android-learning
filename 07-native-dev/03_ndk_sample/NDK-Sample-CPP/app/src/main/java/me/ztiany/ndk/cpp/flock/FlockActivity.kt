package me.ztiany.ndk.cpp.flock

import android.annotation.SuppressLint
import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.flock_activity_operation.*
import me.ztiany.ndk.cpp.R
import java.io.File
import java.util.concurrent.Executors

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-08-21 13:55
 */
class FlockActivity : AppCompatActivity() {

    private val fileLockTester = NativeFileLockTester()

    private val executor = Executors.newSingleThreadExecutor()

    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                FILE_LOCK_DOLOCK_SHARE -> executor.execute {
                    fileLockTester.nativeFileLockDoLock(isExclusive = false, wait = true)
                }
                FILE_LOCK_DOLOCK_EXCLUSIVE -> executor.execute {
                    fileLockTester.nativeFileLockDoLock(isExclusive = true, wait = true)
                }
                FILE_LOCK_UNLOCK_SHARE -> executor.execute {
                    fileLockTester.nativeFileLockUnlock(isExclusive = false)
                }
                FILE_LOCK_UNLOCK_EXCLUSIVE -> executor.execute {
                    fileLockTester.nativeFileLockUnlock(isExclusive = true)
                }
            }
        }
    }

    private val messenger = Messenger(handler)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flock_activity_operation)

        val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), FILE_LOCK_NAME);
        fileLockTester.nativeFileLockInit(file.absolutePath)

        setUpViews()
    }

    private fun setUpViews() {
        flockBtnOriginDoShareLock.setOnClickListener {
            executor.execute {
                fileLockTester.nativeFileLockDoLock(isExclusive = false, wait = true)
            }
        }

        flockBtnOriginDoExclusiveLock.setOnClickListener {
            executor.execute {
                fileLockTester.nativeFileLockDoLock(isExclusive = true, wait = true)
            }
        }

        flockBtnOriginUnlockShare.setOnClickListener {
            executor.execute {
                fileLockTester.nativeFileLockUnlock(isExclusive = false)
            }
        }

        flockBtnOriginUnlockExclusive.setOnClickListener {
            executor.execute {
                fileLockTester.nativeFileLockUnlock(isExclusive = true)
            }
        }

        flockBtnOriginOpenSub.setOnClickListener {
            val intent = Intent(this, FlockSubProcessActivity::class.java)
            intent.putExtra(FILE_LOCK_NAME, messenger)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdownNow()
        fileLockTester.nativeFileLockDestroy()
    }

}