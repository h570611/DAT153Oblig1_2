package com.jonas.dat153v2;


import android.util.Log;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;


import com.jonas.dat153v2.activities.Game;
import com.jonas.dat153v2.activities.MainActivity;
import com.jonas.dat153v2.database.GameObject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThan;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestGame {

    @Rule
    public ActivityTestRule<Game> gameTestRule = new ActivityTestRule<Game>(Game.class);

    @Test
    public void checkScore() throws InterruptedException {

        int score = gameTestRule.getActivity().getScore();

        GameObject currentGameObject = gameTestRule.getActivity().getCurrentGameObject();
        int oldScore = score;

        onView(withId(R.id.guessEditText)).perform(typeText(currentGameObject.getName()), closeSoftKeyboard());

        Thread.sleep(1000);

        onView(withId(R.id.guessButton)).perform(click());

        score = gameTestRule.getActivity().getScore();


        assertThat("score", score, greaterThan(oldScore));

    }
}
