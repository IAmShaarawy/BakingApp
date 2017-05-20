package net.elshaarawy.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import net.elshaarawy.bakingapp.Activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by elshaarawy on 20-May-17.
 */

@RunWith(AndroidJUnit4.class)
public class StepTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void selectStep() throws InterruptedException {


        onView(withId(R.id.recipesRV))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_portrait)).check(matches(isDisplayed()));

        Thread.sleep(2000);


        onView(withId(R.id.recipe_steps_RV))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        Thread.sleep(2000);



        pressBack();


    }

}
