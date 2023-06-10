package me.ztiany.koin.android.isolated

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import timber.log.Timber

/**
 *@author Ztiany
 */
class IsolatedActivity : AppCompatActivity(), IsolatedKoinComponent, AndroidScopeComponent {

    override val scope: Scope by activityScope()

    private val isolatedRepository by inject<IsolatedRepository>()

    private val presenter1: IsolatedPresenter by inject {
        parametersOf("A")
    }

    private val presenter2: IsolatedPresenter by inject {
        parametersOf("B" /*won't be injected into IsolatedPresenter.*/)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isolated)

        Timber.d("isolatedRepository = $isolatedRepository")
        Timber.d("presenter1 = $presenter1")
        Timber.d("presenter1 = presenter2 = ${presenter1 == presenter2}")
        Timber.d("presenter1.getList() = ${presenter1.getList()}")
    }

}