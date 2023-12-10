package ru.alex0d.investmentanalyst.api

import okhttp3.OkHttpClient

fun makeRequest(url: String): String? {
    val client = OkHttpClient()

    val request = okhttp3.Request.Builder()
        .url(url)
        .build()

    val response = client.newCall(request).execute()
    val body = response.body?.string() ?: return null
    if (body == "[]") return null
    return body
}