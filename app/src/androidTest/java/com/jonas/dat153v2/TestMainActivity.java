package com.jonas.dat153v2;


import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.jonas.dat153v2.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestMainActivity {
    @Rule
    public IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class);



    @Test
    public void checkWelcomeText() {
        onView(withText("Velkommen!")).check(matches(isDisplayed()));
    }


    //Test if goToDatabaseButton-intent goes to Dataset-activity
    @Test
    public void checkIntentToDataset(){

        onView(withId(R.id.goToDatabseButton))
                .perform(click());


        intended(hasComponent("com.jonas.dat153v2.activities.Dataset"));
    }






    //Test if goToGameButton-intent goes to Game-activity
    @Test
    public void checkIntentToGame(){

        onView(withId(R.id.goToGameButton))
                .perform(click());


        intended(allOf(
                hasComponent("com.jonas.dat153v2.activities.Game")
        ));
    }

}
