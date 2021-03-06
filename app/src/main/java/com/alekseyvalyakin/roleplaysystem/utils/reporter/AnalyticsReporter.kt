package com.alekseyvalyakin.roleplaysystem.utils.reporter

import android.app.Activity
import android.content.Context
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import com.uber.rib.core.toAndroidBundle
import java.lang.ref.WeakReference
import java.util.*

class AnalyticsReporterImpl(context: Context) : AnalyticsReporter {

    private val fireBaseAnalytics = FirebaseAnalytics.getInstance(context)
    private var activity = WeakReference<Activity>(null)

    override fun setCurrentUser(userId: String?) {
        fireBaseAnalytics.setUserId(userId)
        Crashlytics.setUserIdentifier(userId)
    }

    override fun logEvent(event: AnalyticsEvent) {
        fireBaseAnalytics.logEvent(event.name,
                event.bundle?.toAndroidBundle())
    }

    override fun updateUserLocale() {
        setUserProperty(LOCALE, Locale.getDefault().country)
    }

    override fun reset() {
        setCurrentUser(null)
        Crashlytics.setUserIdentifier(null)
    }

    override fun setCurrentScreen(currentScreenName: String) {
        activity.get()?.run {
            fireBaseAnalytics.setCurrentScreen(this, currentScreenName, null)
        }
        Crashlytics.setString(CRASHLYTICS_SCREEN, currentScreenName)
    }

    override fun attachActivity(activity: Activity) {
        this.activity = WeakReference(activity)
    }

    override fun detachActivity() {
        this.activity = WeakReference<Activity>(null)
    }

    private fun setUserProperty(key: String, value: String?) {
        fireBaseAnalytics.setUserProperty(key, value)
    }

    companion object {
        const val LOCALE = "locale"
        const val CRASHLYTICS_SCREEN = "screen"
    }
}

interface AnalyticsReporter {
    fun setCurrentUser(userId: String?)

    fun logEvent(event: AnalyticsEvent)

    fun reset()

    fun attachActivity(activity: Activity)

    fun detachActivity()

    fun setCurrentScreen(currentScreenName: String)

    fun updateUserLocale()
}

open class AnalyticsEvent(
        val name: String,
        val bundle: com.uber.rib.core.Bundle? = null
)