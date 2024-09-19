package com.composeplayground.network.exception

import retrofit2.HttpException
import retrofit2.Response

data class UnknownApiException(override val response: Response<*>) : HttpException(response),
    ResponseException