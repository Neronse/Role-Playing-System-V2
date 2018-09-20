package com.alekseyvalyakin.roleplaysystem.views.backdrop

import android.view.View
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

class BackViewContainer<T>(
        view: T,
        width: Int = matchParent,
        height: Int = wrapContent
) : BaseViewContainer<T>(view, width, height) where T : View, T : BackView {

    fun getPeekHeightDif(): Int {
        return view.measuredHeight
    }

    fun onShown() {
        view.onShown()
    }

    fun onHidden() {
        view.onHidden()
    }
}