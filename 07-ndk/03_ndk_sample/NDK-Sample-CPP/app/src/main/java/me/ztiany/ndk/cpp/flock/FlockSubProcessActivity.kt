package me.ztiany.ndk.cpp.flock

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.flock_activity_operation.*
import kotlinx.android.synthetic.main.flock_activity_operation.flockBtnOriginDoExclusiveLock
import kotlinx.android.synthetic.main.flock_activity_operation.flockBtnOriginDoShareLock
import kotlinx.android.synthetic.main.flock_activity_operation.flockBtnOriginUnlockExclusive
import kotlinx.android.synthetic.main.flock_activity_operation.flockBtnOriginUnlockShare
import kotlinx.android.synthetic.main.flock_activity_operation_sub.*
import me.ztiany.ndk.cpp.R
import java.io.File
import java.util.concurrent.Executors

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-08-21 13:55
 */
class FlockSubProcessActivity : AppCompatActivity() {

    private val fileLockTester = NativeFileLockTester()

    private val executor = Executors.newSingleThreadExecutor()

    private var messenger: Messenger? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flock_activity_operation_sub)
        messenger = intent.getParcelableExtra(FILE_LOCK_NAME)

        setUpViews()
    }

    private fun setUpViews() {
        flockBtnInit.setOnClickListener {
            val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), FILE_LOCK_NAME);
            fileLockTester.nativeFileLockInit(file.absolutePath)
        }

        flockBtnDoShareLock.setOnClickListener {
            executor.execute {
                fileLockTester.nativeFileLockDoLock(isExclusive = false, wait = true)
            }
        }
        flockBtnDoExclusiveLock.setOnClickListener {
            executor.execute {
                fileLockTester.nativeFileLockDoLock(isExclusive = true, wait = true)
            }
        }
        flockBtnUnlockShare.setOnClickListener {
            executor.execute {
                fileLockTester.nativeFileLockUnlock(isExclusive = false)
            }
        }
        flockBtnUnlockExclusive.setOnClickListener {
            executor.execute {
                fileLockTester.nativeFileLockUnlock(isExclusive = true)
            }
        }

        flockBtnOriginDoShareLock.setOnClickListener {
            messenger?.send(Message.obtain(null, FILE_LOCK_DOLOCK_SHARE))
        }
        flockBtnOriginDoExclusiveLock.setOnClickListener {
            messenger?.send(Message.obtain(null, FILE_LOCK_DOLOCK_EXCLUSIVE))
        }
        flockBtnOriginUnlockShare.setOnClickListener {
            messenger?.send(Message.obtain(null, FILE_LOCK_UNLOCK_SHARE))
        }
        flockBtnOriginUnlockExclusive.setOnClickListener {
            messenger?.send(Message.obtain(null, FILE_LOCK_UNLOCK_EXCLUSIVE))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdownNow()
        fileLockTester.nativeFileLockDestroy()
    }

}