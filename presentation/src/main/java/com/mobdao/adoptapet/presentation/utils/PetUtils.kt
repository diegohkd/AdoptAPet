package com.mobdao.adoptapet.presentation.utils

import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetUtils @Inject constructor() {
    fun formattedDescriptionWorkaround(description: String): String =
        HtmlCompat
            .fromHtml(
                HtmlCompat.fromHtml(description, FROM_HTML_MODE_LEGACY).toString(),
                FROM_HTML_MODE_LEGACY,
            ).toString()
}
