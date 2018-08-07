package com.alekseyvalyakin.roleplaysystem.ribs.root

import com.alekseyvalyakin.roleplaysystem.data.game.Game
import com.alekseyvalyakin.roleplaysystem.ribs.auth.AuthBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.auth.AuthRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.transition.CreateGameAttachTransition
import com.alekseyvalyakin.roleplaysystem.ribs.main.MainBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.main.MainRouter
import com.uber.rib.core.DefaultAttachTransition
import com.uber.rib.core.DefaultDetachTransition
import com.uber.rib.core.RouterNavigatorFactory
import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link RootBuilder.RootScope}.
 *
 * Root router of the app
 */
class RootRouter(
        view: RootView,
        interactor: RootInteractor,
        component: RootBuilder.Component,
        routerNavigatorFactory: RouterNavigatorFactory,
        private val authBuilder: AuthBuilder,
        private val mainBuilder: MainBuilder,
        private val createGameBuilder: CreateGameBuilder
) : ViewRouter<RootView, RootInteractor, RootBuilder.Component>(view, interactor, component) {
    private val router = routerNavigatorFactory.create<RootState>(this)!!
    private val authAttachTransition = object : DefaultAttachTransition<AuthRouter, RootState, AuthBuilder>(authBuilder, view) {}
    private val authDetachTransition = object : DefaultDetachTransition<AuthRouter, RootState>(view) {}

    private val mainAttachTransition = object : DefaultAttachTransition<MainRouter, RootState, MainBuilder>(mainBuilder, view) {}
    private val mainDetachTransition = object : DefaultDetachTransition<MainRouter, RootState>(view) {}
    private val createGameDetachTransition = object : DefaultDetachTransition<CreateGameRouter, RootState>(view) {}

    fun attachAuth() {
        router.pushTransientState(RootState.AUTH, authAttachTransition, authDetachTransition)
    }

    fun attachMain() {
        router.pushRetainedState(RootState.MAIN, mainAttachTransition, mainDetachTransition)
    }

    fun attachCreateGame(game: Game) {
        router.pushRetainedState(RootState.CREATE_GAME,
                CreateGameAttachTransition<RootState>(createGameBuilder, view, game),
                createGameDetachTransition)
    }

    fun onBackPressed(): Boolean {
        val currentRouter = router.peekRouter()
        if (currentRouter != null && currentRouter.handleBackPress()) {
            return true
        }
        router.popState()
        return router.peekState() != null
    }

}
