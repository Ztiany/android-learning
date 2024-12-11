package me.ztiany.compose.facility.data

import android.content.Context
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.provider.ErrorMessage
import com.android.sdk.net.core.provider.HttpConfig
import me.ztiany.compose.R
import me.ztiany.compose.facility.utils.NetworkUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber
import java.util.concurrent.TimeUnit

internal fun newHttpConfig(): HttpConfig {

    return object : HttpConfig {

        override fun baseUrl() = "https://www.wanandroid.com/"

        override fun configRetrofit(okHttpClient: OkHttpClient, builder: Retrofit.Builder) = false

        override fun configHttp(builder: OkHttpClient.Builder) {
            builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .apply {
                    with(HttpLoggingInterceptor { message -> Timber.w("OkHttp: $message") }) {
                        level = HttpLoggingInterceptor.Level.BODY
                        builder.addInterceptor(this)
                    }
                }
        }
    }

}

internal fun Context.newErrorMessage(): ErrorMessage {
    return object : ErrorMessage {
        override fun netErrorMessage(exception: Throwable): CharSequence {
            if (NetworkUtils.isConnected(this@newErrorMessage)) {
                return getString(R.string.error_service_error)
            }
            return getString(R.string.error_net_error)
        }

        override fun serverDataErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_service_data_error)
        }

        override fun serverReturningNullEntityErrorMessage(exception: Throwable?): CharSequence {
            return getString(R.string.error_service_no_data_error)
        }

        override fun serverInternalErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_service_error)
        }

        override fun clientRequestErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_request_error)
        }

        override fun apiErrorMessage(exception: ApiErrorException): CharSequence {
            return getString(R.string.error_api_code_mask_tips, exception.code)
        }

        override fun unknownErrorMessage(exception: Throwable): CharSequence {
            return getString(R.string.error_unknown) + "：${exception.message}"
        }
    }
}

