package com.aurelia_truly_jessica.transvice_test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddEditActivityTest {

    @Rule
    public ActivityTestRule<AddEditActivity> mActivityTestRule = new ActivityTestRule<>(AddEditActivity.class);

    @Test
    public void addEditActivityTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.btn_save), withText("Save"),
                        childAtPosition(
                                allOf(withId(R.id.ll_button),
                                        childAtPosition(
                                                withId(R.id.relativeLayout),
                                                4)),
                                1),
                        isDisplayed()));
        materialButton.perform(click());
        onView(isRoot()).perform(waitFor(3000));

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.etTglService),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.inputTglService),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText.perform(replaceText("22"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.btn_save), withText("Save"),
                        childAtPosition(
                                allOf(withId(R.id.ll_button),
                                        childAtPosition(
                                                withId(R.id.relativeLayout),
                                                4)),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());
        onView(isRoot()).perform(waitFor(3000));

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.etTglService), withText("22"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.inputTglService),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText2.perform(replaceText("2021/07/07"));

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.etTglService), withText("2021/07/07"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.inputTglService),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText3.perform(closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.btn_save), withText("Save"),
                        childAtPosition(
                                allOf(withId(R.id.ll_button),
                                        childAtPosition(
                                                withId(R.id.relativeLayout),
                                                4)),
                                1),
                        isDisplayed()));
        materialButton3.perform(click());
        onView(isRoot()).perform(waitFor(3000));

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.etWaktuService),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.inputWaktuService),
                                        0),
                                1),
                        isDisplayed()));
        textInputEditText4.perform(replaceText("00:07:07"), closeSoftKeyboard());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.btn_save), withText("Save"),
                        childAtPosition(
                                allOf(withId(R.id.ll_button),
                                        childAtPosition(
                                                withId(R.id.relativeLayout),
                                                4)),
                                1),
                        isDisplayed()));
        materialButton4.perform(click());
        onView(isRoot()).perform(waitFor(3000));

        ViewInteraction materialAutoCompleteTextView = onView(
                allOf(withId(R.id.etJenisService),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.inputJenisService),
                                        0),
                                1),
                        isDisplayed()));
        materialAutoCompleteTextView.perform(click());

        onView(withText("Cek Filter"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.btn_save), withText("Save"),
                        childAtPosition(
                                allOf(withId(R.id.ll_button),
                                        childAtPosition(
                                                withId(R.id.relativeLayout),
                                                4)),
                                1),
                        isDisplayed()));
        materialButton5.perform(click());
        onView(isRoot()).perform(waitFor(3000));

        ViewInteraction materialAutoCompleteTextView2 = onView(
                allOf(withId(R.id.etJenisKendaraan),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.inputJenisKendaraan),
                                        0),
                                1),
                        isDisplayed()));
        materialAutoCompleteTextView2.perform(click());

        onView(withText("Mobil"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.btn_save), withText("Save"),
                        childAtPosition(
                                allOf(withId(R.id.ll_button),
                                        childAtPosition(
                                                withId(R.id.relativeLayout),
                                                4)),
                                1),
                        isDisplayed()));
        materialButton6.perform(click());
        onView(isRoot()).perform(waitFor(3000));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    public static ViewAction waitFor(long delay) {
        return new ViewAction() {
            @Override public Matcher<View> getConstraints() {
                return isRoot();
            }
            @Override public String getDescription() {
                return "wait for " + delay + "milliseconds";
            }
            @Override public void perform(UiController uiController,
                                          View view) {
                uiController.loopMainThreadForAtLeast(delay);
            }
        };
    }
}
