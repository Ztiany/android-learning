package me.ztiany.koin.android.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.ztiany.koin.android.R
import me.ztiany.koin.android.common.AppRepository
import me.ztiany.koin.android.common.ErrorHandler
import me.ztiany.koin.android.databinding.ActivityMainBinding
import me.ztiany.koin.android.isolated.IsolatedActivity
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.Scope
import timber.log.Timber

class MainActivity : AppCompatActivity(), AndroidScopeComponent {

    override val scope: Scope by activityScope()

    private val appRepository: AppRepository by inject()

    private val errorHandler: ErrorHandler by inject()

    private val presenter: MainPresenter by inject()

    private lateinit var vb: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vb = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        setUpViews()
        testKoin()
    }

    private fun testKoin() {
        Timber.d(appRepository.getConfiguration())
        errorHandler.handleError(RuntimeException("Test Error"))
        Timber.d("presenter = $presenter")
        Timber.d("appRepository = $appRepository")
        Timber.d("errorHandler = $errorHandler")
    }

    private fun setUpViews() {
        vb.btnStartIsolatedActivity.setOnClickListener {
            startActivity(Intent(this, IsolatedActivity::class.java))
        }
    }

}