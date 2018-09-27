package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameDependencyProvider
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link GameSettingsClassScope}.
 *
 */
class GameSettingsClassBuilder(dependency: ParentComponent) : ViewBuilder<GameSettingsClassView, GameSettingsClassRouter, GameSettingsClassBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [GameSettingsClassRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [GameSettingsClassRouter].
     */
    fun build(parentViewGroup: ViewGroup): GameSettingsClassRouter {
        val view = createView(parentViewGroup)
        val interactor = GameSettingsClassInteractor()
        val component = DaggerGameSettingsClassBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.gamesettingsclassRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): GameSettingsClassView {
        return GameSettingsClassView(parentViewGroup.context)
    }

    interface ParentComponent : ActiveGameDependencyProvider

    @dagger.Module
    abstract class Module {

        @GameSettingsClassScope
        @Binds
        internal abstract fun presenter(view: GameSettingsClassView): GameSettingsClassPresenter

        @dagger.Module
        companion object {

            @GameSettingsClassScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: GameSettingsClassView,
                    interactor: GameSettingsClassInteractor): GameSettingsClassRouter {
                return GameSettingsClassRouter(view, interactor, component)
            }
        }

    }

    @GameSettingsClassScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<GameSettingsClassInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: GameSettingsClassInteractor): Builder

            @BindsInstance
            fun view(view: GameSettingsClassView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun gamesettingsclassRouter(): GameSettingsClassRouter
    }

    @Scope
    @Retention(AnnotationRetention.BINARY)
    internal annotation class GameSettingsClassScope

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    internal annotation class GameSettingsClassInternal
}