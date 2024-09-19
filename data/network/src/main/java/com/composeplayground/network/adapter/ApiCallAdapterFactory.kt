package com.composeplayground.network.adapter

import android.util.Log
import com.composeplayground.network.exception.UnknownApiException
import com.composeplayground.network.exception.ValidationFailed
import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject

class ApiCallAdapterFactory @Inject constructor(private val moshi: Moshi) : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }
        require(returnType is ParameterizedType) {
            "return type must be parameterized"
        }
        return Adapter<Any>(getParameterUpperBound(0, returnType), moshi)
    }
}

private class Adapter<T>(
    private val responseType: Type,
    private val moshi: Moshi
) : CallAdapter<T, Call<T>> {
    override fun responseType(): Type = responseType
    override fun adapt(call: Call<T>): Call<T> = ApiCall(call, moshi)
}

class ApiCall<T>(
    private val delegate: Call<T>,
    private val moshi: Moshi
) : Call<T> by delegate {
    override fun enqueue(callback: Callback<T>) =
        delegate.enqueue(object : Callback<T> by callback {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    callback.onResponse(this@ApiCall, response)
                    return
                }
                val errorBodyString = response.errorBody()?.string()
                if (errorBodyString == null) {
                    callback.onResponse(this@ApiCall, response)
                    return
                }

                try {
                    val apiException = when (response.code()) {
                        422 -> ValidationFailed(response)
                        503 -> ValidationFailed(response)
                        else -> UnknownApiException(response)
                    }
                    callback.onFailure(this@ApiCall, apiException)
                } catch (throwable: Throwable) {
                    callback.onFailure(this@ApiCall, throwable)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                // TODO: track UnknownHostException exception
                callback.onFailure(this@ApiCall, t)
            }
        })

    private inline fun <reified T> convertErrorBodyTo(errorBodyJson: String, moshi: Moshi): T? {
        try {
            val adapter = moshi.adapter(T::class.java)
            return adapter.fromJson(errorBodyJson)
        } catch (exception: Exception) {
            // TODO: track exception
        }
        return null
    }
}