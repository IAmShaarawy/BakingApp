package net.elshaarawy.bakingapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.elshaarawy.bakingapp.Data.Entities.IngredientEntity;
import net.elshaarawy.bakingapp.Data.Entities.StepEntity;
import net.elshaarawy.bakingapp.Fragments.RecipeFragment;
import net.elshaarawy.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    private static final String EXTRA_INGREDIENTS = "extra_ingredients";
    private static final String EXTRA_STEPS = "extra_steps";
    private static final String FRAGMENT_TAG = "fragment_tag";

    private List<IngredientEntity> mIngredientEntities;
    private List<StepEntity> mStepEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        mIngredientEntities = intent.getParcelableArrayListExtra(EXTRA_INGREDIENTS);
        mStepEntities = intent.getParcelableArrayListExtra(EXTRA_STEPS);

        if (findViewById(R.id.recipe_portrait) != null) {
            RecipeFragment.attachMe(getSupportFragmentManager(), R.id.recipe_portrait, mIngredientEntities, mStepEntities);
        }
    }

    public static void starMe(Context context, List<IngredientEntity> ingredientEntities, List<StepEntity> stepEntities) {
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_INGREDIENTS, (ArrayList) ingredientEntities);
        intent.putParcelableArrayListExtra(EXTRA_STEPS, (ArrayList) stepEntities);
        context.startActivity(intent);
    }
}
