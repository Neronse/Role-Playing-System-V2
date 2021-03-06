package com.alekseyvalyakin.roleplaysystem.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.app.RpsApp
import org.jetbrains.anko.dip

private const val DEFAULT_STATUS_BAR_HEIGHT_NEW = 24
private const val DEFAULT_STATUS_BAR_HEIGHT_OLD = 25

fun Context.getCompatColor(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.getIntDimen(@DimenRes res: Int): Int {
    return this.resources.getDimensionPixelSize(res)
}

fun Context.getFloatDimen(@DimenRes res: Int): Float {
    return this.resources.getDimension(res)
}

fun Context.getCommonDimen(): Int {
    return getIntDimen(R.dimen.dp_8)
}

fun Context.getDoubleCommonDimen(): Int {
    return getIntDimen(R.dimen.dp_16)
}

fun Context.getCompatDrawable(@DrawableRes res: Int): Drawable {
    return ContextCompat.getDrawable(this, res)!!
}

fun Context.getCompatColorState(@ColorRes res: Int): ColorStateList {
    return ContextCompat.getColorStateList(this, res)!!
}

fun Context.getSelectableItemBackGround(): Int {
    val outValue = TypedValue()
    theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
    return outValue.resourceId
}

fun Context.getSelectableItemBorderless(): Int {
    val outValue = TypedValue()
    theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true)
    return outValue.resourceId
}

fun Context.getCotrolHighlightColor(): Int {
    val outValue = TypedValue()
    theme.resolveAttribute(android.R.attr.colorControlHighlight, outValue, true)
    return outValue.resourceId
}

fun Context.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        return resources.getDimensionPixelSize(resourceId)
    }
    val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        dip(DEFAULT_STATUS_BAR_HEIGHT_NEW)
    } else {
        dip(DEFAULT_STATUS_BAR_HEIGHT_OLD)
    }

    return Math.ceil(result.toDouble()).toInt()
}

fun Context.getToolbarHeight(): Int {
    val resourceId = resources.getIdentifier("action_bar_size", "dimen", "android")
    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else dip(56)
}

fun Int.dip(): Int {
    return RpsApp.app.dip(this)
}

fun Context.getScreenOrientation(): Int {
    return resources.configuration.orientation
}

fun Context.isOrientationLandscape(): Boolean {
    return getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE
}

fun Context.getDisplayMetrics(): DisplayMetrics {
    return resources.displayMetrics
}

fun Context.getDisplayWidth(): Int {
    return getDisplayMetrics().widthPixels
}

fun Context.getDisplayHeight(): Int {
    return getDisplayMetrics().heightPixels
}

fun Context.getDividerDrawable(): Drawable {
    val attrs = intArrayOf(android.R.attr.listDivider)

    val styledAttributes = obtainStyledAttributes(attrs)
    val drawable: Drawable = styledAttributes.getDrawable(0)!!
    styledAttributes.recycle()
    return drawable
}