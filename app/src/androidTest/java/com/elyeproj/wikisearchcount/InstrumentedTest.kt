package com.elyeproj.wikisearchcount

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.jakewharton.espresso.OkHttp3IdlingResource
import okreplay.AndroidTapeRoot
import okreplay.OkReplay
import okreplay.OkReplayConfig
import okreplay.OkReplayRuleChain
import okreplay.TapeMode
import org.junit.Before
import org.junit.ClassRule
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

    private val okReplayConfig = OkReplayConfig.Builder()
            .tapeRoot(AndroidTapeRoot(InstrumentationRegistry.getContext(), javaClass))
            .defaultMode(TapeMode.WRITE_ONLY) // or TapeMode.READ_ONLY
            .sslEnabled(true)
            .interceptor(WikiApiService.okReplayInterceptor)
            .build()

    companion object {
        @ClassRule
        @JvmField
        val grantExternalStoragePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    @get:Rule
    private val activityRule = ActivityTestRule(MainActivity::class.java, false, false)
    private val fetchingIdlingResource = FetchingIdlingResource()

    @get:Rule
    val testRule = OkReplayRuleChain(okReplayConfig, activityRule).get()

    @Before
    fun setup() {
        val resource = OkHttp3IdlingResource.create("OkHttp", WikiApiService.okHttpClient)
        IdlingRegistry.getInstance().register(resource)

        activityRule.launchActivity(null)
    }

    @Test
    @OkReplay(tape = "instrumental launch and search", mode = TapeMode.WRITE_ONLY)
    fun launchAndSearch() {
        Espresso.onView(ViewMatchers.withId(R.id.edit_search))
                .perform(ViewActions.replaceText("Trump"))
        Espresso.onView(ViewMatchers.withId(R.id.btn_search))
                .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.txt_search_result))
                .check(ViewAssertions.matches(ViewMatchers.withText("26369 result found")))
    }
}
