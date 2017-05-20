package net.elshaarawy.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import net.elshaarawy.bakingapp.Activities.MainActivity;
import net.elshaarawy.bakingapp.Data.Entities.RecipeEntity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by elshaarawy on 20-May-17.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void selectRecipe() throws InterruptedException {

        for (int i = 0; i < 3; i++) {
            onView(withId(R.id.recipesRV))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));
            onView(withId(R.id.recipe_portrait)).check(matches(isDisplayed()));

            Thread.sleep(2000);

            pressBack();
        }
    }
}
