package net.elshaarawy.bakingapp.Services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import net.elshaarawy.bakingapp.Data.BakingContract;
import net.elshaarawy.bakingapp.Data.Entities.IngredientEntity;
import net.elshaarawy.bakingapp.Data.Entities.RecipeEntity;
import net.elshaarawy.bakingapp.Data.Entities.StepEntity;
import net.elshaarawy.bakingapp.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import static net.elshaarawy.bakingapp.Activities.MainActivity.sendBroadCastToMe;
import static net.elshaarawy.bakingapp.Data.BakingContract.ProviderUris.*;
import static net.elshaarawy.bakingapp.Data.BakingContract.RecipesColumns;
import static net.elshaarawy.bakingapp.Data.BakingContract.IngredientsColumns;
import static net.elshaarawy.bakingapp.Data.BakingContract.StepsColumns;

/**
 * Created by elshaarawy on 18-May-17.
 */

public class BakingIntentService extends IntentService implements Callback<List<RecipeEntity>> {

    private Retrofit mRetrofit;

    public BakingIntentService() {
        super("BakingIntentService");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_call))
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Recipes recipes = mRetrofit.create(Recipes.class);
        Call<List<RecipeEntity>> call = recipes.getRecipes();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<RecipeEntity>> call, Response<List<RecipeEntity>> response) {

        sendBroadCastToMe(this,response.isSuccessful());

        //Content Resolver to access Content Provider
        ContentResolver resolver = getContentResolver();

        // variables used to Extract recipes
        List<RecipeEntity> recipeEntities = response.body();
        RecipeEntity recipeEntity;
        int r_id;
        String r_name,r_servings,r_image;
        ContentValues[] recipesValues = new ContentValues[recipeEntities.size()];
        ContentValues recipeValue;

        // variables used to Extract Ingredient
        List<IngredientEntity> ingredientEntities;
        IngredientEntity ingredientEntity;
        int i_id;
        String i_quantity,i_measure,i_ingredient;
        ContentValues[] ingredientsValues;
        ContentValues ingredientValue;

        // variables used to Extract steps
        List<StepEntity> stepEntities;
        StepEntity stepEntity;
        int s_id,s_r_id;
        String s_short_desc,s_desc,s_video_url,s_thumbnail_url;
        ContentValues[] stepsValues;
        ContentValues stepValue;


        for (int i = 0; i < recipeEntities.size(); i++) {

            recipeEntity = recipeEntities.get(i);
            r_id = Integer.parseInt(recipeEntity.getId());
            r_name = recipeEntity.getName();
            r_servings  = recipeEntity.getServings();
            r_image = recipeEntity.getImage();

            recipeValue = new ContentValues();
            recipeValue.put(RecipesColumns._ID,r_id);
            recipeValue.put(RecipesColumns.RECIPE_NAME,r_name);
            recipeValue.put(RecipesColumns.RECIPE_SERVINGS,r_servings);
            recipeValue.put(RecipesColumns.RECIPE_IMAGE,r_image);
            recipesValues[i] = recipeValue;

            //Extract and Insert Steps Ingredient
            ingredientEntities = recipeEntity.getIngredients();
            ingredientsValues = new ContentValues[ingredientEntities.size()];

            for (int j = 0; j <ingredientEntities.size() ; j++) {
                ingredientEntity = ingredientEntities.get(j);
                i_id = r_id;
                i_quantity = ingredientEntity.getQuantity();
                i_measure = ingredientEntity.getMeasure();
                i_ingredient = ingredientEntity.getIngredient();

                ingredientValue = new ContentValues();
                ingredientValue.put(IngredientsColumns._ID,i_id);
                ingredientValue.put(IngredientsColumns.INGREDIENTS_QUANTITY,i_quantity);
                ingredientValue.put(IngredientsColumns.INGREDIENTS_MEASURE,i_measure);
                ingredientValue.put(IngredientsColumns.INGREDIENTS_INGREDIENT,i_ingredient);
                ingredientsValues[j] = ingredientValue;
            }
            resolver.bulkInsert(CONTENT_INGREDIENTS,ingredientsValues);


            //Extract and Insert Steps
            stepEntities = recipeEntity.getSteps();
            stepsValues = new ContentValues[stepEntities.size()];

            for (int j = 0; j < stepEntities.size() ; j++) {
                stepEntity = stepEntities.get(j);
                s_id = Integer.parseInt(stepEntity.getId());
                s_r_id = r_id;
                s_short_desc = stepEntity.getShortDescription();
                s_desc = stepEntity.getDescription();
                s_video_url = stepEntity.getVideoURL();
                s_thumbnail_url = stepEntity.getThumbnailURL();

                stepValue = new ContentValues();
                stepValue.put(StepsColumns._ID,s_id);
                stepValue.put(StepsColumns.STEP_RECIPE_ID,s_r_id);
                stepValue.put(StepsColumns.STEP_SHORT_DESCRIPTION,s_short_desc);
                stepValue.put(StepsColumns.STEP_DESCRIPTION,s_desc);
                stepValue.put(StepsColumns.STEP_VIDEO_URL,s_video_url);
                stepValue.put(StepsColumns.STEP_THUMBNAIL_URL,s_thumbnail_url);
                stepsValues[j]=stepValue;

            }
            resolver.bulkInsert(CONTENT_STEPS,stepsValues);
        }
        resolver.bulkInsert(CONTENT_RECIPES, recipesValues);

    }

    @Override
    public void onFailure(Call<List<RecipeEntity>> call, Throwable t) {
        sendBroadCastToMe(this,false);
    }


    private interface Recipes {
        @GET("baking.json")
        Call<List<RecipeEntity>> getRecipes();
    }

    public static void startMe(Context context){
        Intent intent = new Intent(context,BakingIntentService.class);
        context.startService(intent);
    }
}
