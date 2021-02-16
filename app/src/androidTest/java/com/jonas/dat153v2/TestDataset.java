package com.jonas.dat153v2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;


import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;




import com.jonas.dat153v2.activities.AddImage;
import com.jonas.dat153v2.activities.Dataset;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static android.app.Instrumentation.ActivityResult;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestDataset {



    @Rule
    public IntentsTestRule<Dataset> mActivityTestRule = new IntentsTestRule<>(Dataset.class);

    @Before
    public void stubCameraIntent() {
//        Intent resultData = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        resultData.putExtra("image", mActivityTestRule.getActivity().scaledBM(BitmapFactory.decodeResource(mActivityTestRule.getActivity().getResources(), R.drawable.bergen3)));

        ActivityResult result = createImageCaptureActivityResultStub();
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);

    }


    @Test
    public void testAddImage() {

        onView(withId(R.id.goToAddImageBtn)).perform(click());

        int listSizeBefore = mActivityTestRule.getActivity().getSize();
        int currentSize = listSizeBefore;

        onView(withId(R.id.editTextImageName)).perform(typeText("TESTTEST"), closeSoftKeyboard());




        onView(withId(R.id.addImageButton)).perform(click());


        onView(withId(R.id.save_gameObject)).perform(click());

        currentSize = mActivityTestRule.getActivity().getSize();

        assertThat("size", currentSize, greaterThan(listSizeBefore));


    }

    @Test
    public void testDeleteImage(){
        int listSizeBefore = mActivityTestRule.getActivity().getSize();
        int currentSize = listSizeBefore;

        if (currentSize == 0){
            testAddImage();
            listSizeBefore++;
        }

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.deleteCard),
                        childAtPosition(
                                allOf(withId(R.id.markerConstraintLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.cardview.widget.CardView")),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatImageView.perform(click());

        currentSize = mActivityTestRule.getActivity().getSize();

        assertThat("checkSize", listSizeBefore, greaterThan(currentSize));
    }

    private ActivityResult createImageCaptureActivityResultStub() {
        // Put the drawable in a bundle.
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", BitmapFactory.decodeResource(
                mActivityTestRule.getActivity().getResources(), R.drawable.fvbk));

        // Create the Intent that will include the bundle.
        Intent resultData = new Intent();
        resultData.putExtras(bundle);

        // Create the ActivityResult with the Intent.
        return new ActivityResult(Activity.RESULT_OK, resultData);
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

}
