package net.elshaarawy.bakingapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.elshaarawy.bakingapp.Data.Entities.IngredientEntity;
import net.elshaarawy.bakingapp.Data.Entities.StepEntity;
import net.elshaarawy.bakingapp.Fragments.StepFragment;
import net.elshaarawy.bakingapp.Fragments.RecipeFragment;
import net.elshaarawy.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.RecipeFragmentCallbacks {

    private static final String EXTRA_INGREDIENTS = "extra_ingredients";
    private static final String EXTRA_STEPS = "extra_steps";
    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_ID = "extra_liked";

    private List<IngredientEntity> mIngredientEntities;
    private List<StepEntity> mStepEntities;
    private String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        mIngredientEntities = intent.getParcelableArrayListExtra(EXTRA_INGREDIENTS);
        mStepEntities = intent.getParcelableArrayListExtra(EXTRA_STEPS);
        itemId  = intent.getStringExtra(EXTRA_ID);
        getSupportActionBar().setTitle(intent.getStringExtra(EXTRA_TITLE));

        if (findViewById(R.id.recipe_portrait) != null) {
            RecipeFragment.attachMe(getSupportFragmentManager(),
                    R.id.recipe_portrait,
                    mIngredientEntities,
                    mStepEntities, this,false,itemId);
        } else if (findViewById(R.id.recipe_land) != null) {
            RecipeFragment.attachMe(getSupportFragmentManager(),
                    R.id.recipe_land_master,
                    mIngredientEntities,
                    mStepEntities, this,true,itemId);
            StepFragment.attachMe(getSupportFragmentManager(),
                    R.id.recipe_land_detail,
                    mStepEntities.get(0));
            getSupportActionBar().hide();
        } else if (findViewById(R.id.recipe_xl) != null) {
            RecipeFragment.attachMe(getSupportFragmentManager(),
                    R.id.recipe_xl_master,
                    mIngredientEntities,
                    mStepEntities, this,true,itemId);
            StepFragment.attachMe(getSupportFragmentManager(),
                    R.id.recipe_xl_detail,
                    mStepEntities.get(0));
            getSupportActionBar().hide();
        }
    }

    @Override
    public void recipeFragmentItemClickListener(int position) {
        if (findViewById(R.id.recipe_portrait) != null) {
            StepActivity.startMe(this, mStepEntities, position);

        } else if (findViewById(R.id.recipe_land) != null) {
            StepFragment.attachMe(getSupportFragmentManager(),
                    R.id.recipe_land_detail,
                    mStepEntities.get(position));

        } else if (findViewById(R.id.recipe_xl) != null) {
            StepFragment.attachMe(getSupportFragmentManager(),
                    R.id.recipe_xl_detail,
                    mStepEntities.get(position));
        }
    }

    public static void starMe(Context context,String itemId ,String recipeTitle,
                              List<IngredientEntity> ingredientEntities,
                              List<StepEntity> stepEntities) {
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(EXTRA_TITLE, recipeTitle);
        intent.putParcelableArrayListExtra(EXTRA_INGREDIENTS, (ArrayList) ingredientEntities);
        intent.putParcelableArrayListExtra(EXTRA_STEPS, (ArrayList) stepEntities);
        intent.putExtra(EXTRA_ID,itemId);
        context.startActivity(intent);
    }


}
