package com.composeplayground.ui.extensions

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

fun Context.openUrlWithCustomTabs(url: String, configure: CustomTabsIntent.Builder.() -> Unit) {
    val customTabsIntent = CustomTabsIntent.Builder().apply {
        configure()
    }.build()

    customTabsIntent.launchUrl(this, Uri.parse(url))
}