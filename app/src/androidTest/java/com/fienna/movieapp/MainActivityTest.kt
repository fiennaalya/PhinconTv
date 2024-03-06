package com.fienna.movieapp

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    private val dummyEmail = "lala@gmail.com"
    private val dummyPassword = "12345678"
    private val delayTimeMs = 1000L
    private val idlingResource = DelayIdlingResources(delayTimeMs)

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(DelayIdlingResources(delayTimeMs))
    }

    @Test
    fun TestLoginSuccess() {
        IdlingRegistry.getInstance().register(idlingResource)
        Espresso.onView(withId(R.id.btn_login_onboarding))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        IdlingRegistry.getInstance().unregister(idlingResource)
        Espresso.onView(withId(R.id.btn_login_onboarding)).perform(ViewActions.click())

        IdlingRegistry.getInstance().register(idlingResource)
        Espresso.onView(withId(R.id.tiet_Email))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        IdlingRegistry.getInstance().unregister(idlingResource)
        Espresso.onView(withId(R.id.tiet_Email))
            .perform(ViewActions.typeText(dummyEmail))
            .perform(closeSoftKeyboard())

        IdlingRegistry.getInstance().register(idlingResource)
        Espresso.onView(withId(R.id.tietPass))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        IdlingRegistry.getInstance().unregister(idlingResource)
        Espresso.onView(withId(R.id.tietPass))
            .perform(ViewActions.typeText(dummyPassword))
            .perform(closeSoftKeyboard())

        IdlingRegistry.getInstance().register(idlingResource)
        Espresso.onView(withId(R.id.btn_login))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        IdlingRegistry.getInstance().unregister(idlingResource)
        Espresso.onView(withId(R.id.btn_login)).perform(ViewActions.click())
    }
}


class DelayIdlingResources(private val delayTimeMs: Long) : IdlingResource {
    private var resourcesCallback: IdlingResource.ResourceCallback? = null
    private val startTime = System.currentTimeMillis()
    override fun getName(): String {
        return DelayIdlingResources::class.java.simpleName
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        resourcesCallback = callback
    }

    override fun isIdleNow(): Boolean {
        val elapseTime = System.currentTimeMillis() - startTime
        val isIdle = elapseTime >= delayTimeMs
        if (isIdle && resourcesCallback != null) {
            resourcesCallback?.onTransitionToIdle()
        }
        return isIdle
    }
}
