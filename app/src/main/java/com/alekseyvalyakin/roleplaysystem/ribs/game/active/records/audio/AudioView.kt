package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio


import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.view.menu.MenuBuilder
import android.support.v7.view.menu.MenuPopupHelper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.OnClickListener
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.adapter.AudioAdapter
import com.alekseyvalyakin.roleplaysystem.utils.openFolder
import com.alekseyvalyakin.roleplaysystem.utils.playerView
import com.alekseyvalyakin.roleplaysystem.utils.updateWithAnimateToStartOnNewItem
import com.alekseyvalyakin.roleplaysystem.views.player.PlayerView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class AudioView constructor(
        context: Context
) : _RelativeLayout(context), AudioPresenter {

    private val recyclerView: RecyclerView
    private val playerView: PlayerView
    private val relay = PublishRelay.create<AudioPresenter.UiEvent>()
    private val flexibleAdapter = AudioAdapter(emptyList(), relay)
    private var latestViewModel: AudioViewModel? = null
    private val smoothScroller = object : LinearSmoothScroller(getContext()) {
        override fun getVerticalSnapPreference(): Int {
            return LinearSmoothScroller.SNAP_TO_START
        }
    }

    init {
        layoutTransition = LayoutTransition()
        playerView = playerView {
            id = R.id.player_view
            visibility = View.GONE
            setLeftClickListener(OnClickListener {
                showMenu()
            })
            setRightClickListener(OnClickListener {
                latestViewModel?.run {
                    relay.accept(AudioPresenter.UiEvent.TogglePlay(audioState.file, !audioState.isPlaying))
                }
            })
            setProgressListener(object : PlayerView.ProgressChangedListener {
                override fun onProgressChanged(progress: Int) {
                    latestViewModel?.run {
                        relay.accept(AudioPresenter.UiEvent.SeekTo(audioState.file, progress))
                    }
                }
            })
        }.lparams(matchParent, wrapContent) {
            alignParentBottom()
        }

        recyclerView = recyclerView {
            id = R.id.recycler_view
            clipToPadding = false
            clipChildren = false
            layoutManager = LinearLayoutManager(context)
            adapter = flexibleAdapter
        }.lparams(width = matchParent, height = matchParent) {
            above(R.id.player_view)
        }
    }

    override fun update(viewModel: AudioViewModel) {
        this.latestViewModel = viewModel
        flexibleAdapter.updateWithAnimateToStartOnNewItem(
                recyclerView,
                smoothScroller,
                viewModel.items,
                animated = true
        )
        if (viewModel.audioState.isEmpty()) {
            playerView.visibility = View.GONE
        } else {
            playerView.update(viewModel.audioState)
            playerView.visibility = View.VISIBLE
        }
    }

    override fun observe(): Observable<AudioPresenter.UiEvent> {
        return relay
    }

    @SuppressLint("RestrictedApi")
    fun showMenu() {
        val menu = PopupMenu(context, playerView)
        menu.inflate(R.menu.audio_file_menu)
        menu.setOnMenuItemClickListener({
            return@setOnMenuItemClickListener true
        })

        val menuHelper = MenuPopupHelper(context, menu.menu as MenuBuilder, playerView)
        menuHelper.setForceShowIcon(true)
        menuHelper.show()
        menu.setOnMenuItemClickListener {
            latestViewModel?.run {
                when (it.itemId) {
                    R.id.action_directory -> {
                        context.openFolder(this.audioState.file.parentFile)
                    }

                }
            }
            false
        }
    }
}
