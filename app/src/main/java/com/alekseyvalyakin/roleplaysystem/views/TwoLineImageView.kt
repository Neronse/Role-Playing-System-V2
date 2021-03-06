package com.alekseyvalyakin.roleplaysystem.views

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getCompatColor
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.setForegroundSelectableItemBackGround
import com.alekseyvalyakin.roleplaysystem.utils.setTextSizeFromRes
import org.jetbrains.anko.*

class TwoLineImageView(context: Context) : _FrameLayout(context) {

    init {
        setForegroundSelectableItemBackGround()
        relativeLayout {
            backgroundColor = Color.WHITE
            minimumHeight = getIntDimen(R.dimen.dp_72)
            rightPadding = getIntDimen(R.dimen.dp_16)
            topPadding = getIntDimen(R.dimen.dp_16)
            imageView {
                id = R.id.avatar
            }.lparams(width = getIntDimen(R.dimen.dp_40), height = getIntDimen(R.dimen.dp_40)) {
                leftMargin = getIntDimen(R.dimen.dp_16)
                marginStart = getIntDimen(R.dimen.dp_16)
            }
            relativeLayout {
                imageView {
                    id = R.id.arrow_right
                    visibility = View.GONE
                    imageResource = R.drawable.ic_arrow_right
                }.lparams(width = getIntDimen(R.dimen.dp_24), height = getIntDimen(R.dimen.dp_24)) {
                    alignParentRight()
                    centerVertically()
                }
                textView {
                    id = R.id.primary_line
                    maxLines = 1
                    textColor = getCompatColor(R.color.colorTextPrimary)
                    setTextSizeFromRes(R.dimen.dp_16)
                }.lparams(width = matchParent) {
                    leftOf(R.id.arrow_right)
                }
                textView {
                    id = R.id.secondary_line
                    maxLines = 1
                    textColor = getCompatColor(R.color.colorTextSecondary)
                    setTextSizeFromRes(R.dimen.dp_14)
                }.lparams(width = matchParent) {
                    below(R.id.primary_line)
                    leftOf(R.id.arrow_right)
                }
            }.lparams(width = matchParent) {
                centerVertically()
                leftMargin = getIntDimen(R.dimen.dp_72)
                marginStart = getIntDimen(R.dimen.dp_72)
            }
        }.lparams(matchParent, wrapContent)

        layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
    }
}