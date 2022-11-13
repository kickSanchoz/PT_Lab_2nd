package ru.pt_lab_2nd.android.utils

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import ru.pt_lab_2nd.R

fun getPlaceholder(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        setStyle(CircularProgressDrawable.DEFAULT)
        setColorSchemeColors(ResourcesCompat.getColor(context.resources, R.color.red, null))
    }
}