package com.composeplayground.network.exception

import retrofit2.Response

interface ResponseException {
    val response: Response<*>
}