package com.elyeproj.wikisearchcount

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTest {
    @get:Rule
    private val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    fun setup() {
        activityRule.launchActivity(null)
    }

    @Test
    @Throws(Exception::class)
    fun launchAndSearch() {
        Espresso.onView(ViewMatchers.withId(R.id.edit_search)).
                perform(ViewActions.replaceText("Trump"))
        Espresso.onView(ViewMatchers.withId(R.id.btn_search))
                .perform(ViewActions.click())
        Thread.sleep(5000)
        Espresso.onView(ViewMatchers.withId(R.id.txt_search_result))
                .check(ViewAssertions.matches(ViewMatchers.withText("22909 result found")))
    }
}
