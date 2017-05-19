package net.elshaarawy.bakingapp.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import net.elshaarawy.bakingapp.Adapter.RecipesAdapter;
import net.elshaarawy.bakingapp.Data.BakingContract;
import net.elshaarawy.bakingapp.Data.Entities.IngredientEntity;
import net.elshaarawy.bakingapp.Data.Entities.RecipeEntity;
import net.elshaarawy.bakingapp.Data.Entities.StepEntity;
import net.elshaarawy.bakingapp.R;
import net.elshaarawy.bakingapp.Services.BakingIntentService;
import net.elshaarawy.bakingapp.Utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import static net.elshaarawy.bakingapp.Data.BakingContract.ProviderUris.CONTENT_STEPS;
import static net.elshaarawy.bakingapp.Utils.PreferenceUtil.DefaultKeys;
import static net.elshaarawy.bakingapp.Data.BakingContract.ProviderUris.CONTENT_RECIPES;
import static net.elshaarawy.bakingapp.Data.BakingContract.ProviderUris.CONTENT_INGREDIENTS;
import static net.elshaarawy.bakingapp.Data.BakingContract.RecipesColumns;
import static net.elshaarawy.bakingapp.Data.BakingContract.IngredientsColumns;
import static net.elshaarawy.bakingapp.Data.BakingContract.StepsColumns;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, RecipesAdapter.RecipeItemClickListener {

    private static final String BROADCAST_ACTION = "net.elshaarawy.bakingapp.activities.MainActivity";
    private static final String KEY_POSITION = "key_position";
    PreferenceUtil mPreferenceUtil;
    private LoaderManager mLoaderManager;
    private static final int LOADER_ID_RECIPES = 101;
    private static final int LOADER_ID_INGREDIENTS = 102;
    private static final int LOADER_ID_STEPS = 103;
    String mCurrentItemId = null;
    List<IngredientEntity> mIngredientEntities = new ArrayList<>();
    List<StepEntity> mStepEntities = new ArrayList<>();


    RecyclerView mRecipesRecyclerView;
    GridLayoutManager mGridLayoutManager;
    RecipesAdapter mRecipesAdapter;


    private LocalBroadcastManager localBroadcastManager;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPreferenceUtil = new PreferenceUtil(this, DefaultKeys.DEFAULT_SHARED_PREFERENCE);

        mLoaderManager = getSupportLoaderManager();
        mLoaderManager.initLoader(LOADER_ID_RECIPES, null, this);

        if (findViewById(R.id.recipesRV)!=null){
            mRecipesRecyclerView = (RecyclerView) findViewById(R.id.recipesRV);
             mGridLayoutManager = new GridLayoutManager(this, 1);
        }else if (findViewById(R.id.recipesRVLand) != null){
            mRecipesRecyclerView = (RecyclerView) findViewById(R.id.recipesRVLand);
            mGridLayoutManager = new GridLayoutManager(this, 2);
        }else if (findViewById(R.id.recipesRVXL) != null){
            mRecipesRecyclerView = (RecyclerView) findViewById(R.id.recipesRVXL);
            mGridLayoutManager = new GridLayoutManager(this, 3);
        }


        mRecipesRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecipesAdapter = new RecipesAdapter(new ArrayList<RecipeEntity>(), this);
        mRecipesRecyclerView.setAdapter(mRecipesAdapter);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Recipes...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isSuccess = intent.getBooleanExtra(Intent.EXTRA_RESULT_RECEIVER, false);
            if (isSuccess) {
                mPreferenceUtil.editValue(DefaultKeys.PREF_IS_FIRST_TIME, true);
                progressDialog.hide();
                mLoaderManager.restartLoader(LOADER_ID_RECIPES, null, MainActivity.this);
            } else {
                progressDialog.hide();
                Toast.makeText(MainActivity.this, "check your connection", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (!mPreferenceUtil.getBoolean(DefaultKeys.PREF_IS_FIRST_TIME)) {
            loadData();
            IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
            localBroadcastManager.registerReceiver(broadcastReceiver, filter);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection;
        String[] selectionArgs;
        switch (id) {
            case LOADER_ID_RECIPES:
                return new CursorLoader(this, CONTENT_RECIPES, null, null, null, null);

            case LOADER_ID_INGREDIENTS:
                selection = IngredientsColumns.TABLE_NAME_INGREDIENTS + "." + IngredientsColumns._ID + "=?";
                selectionArgs = new String[]{mCurrentItemId};
                return new CursorLoader(this, CONTENT_INGREDIENTS, null, selection, selectionArgs, null);

            case LOADER_ID_STEPS:
                selection = BakingContract.StepsColumns.TABLE_NAME_STEPS + "." + BakingContract.StepsColumns.STEP_RECIPE_ID + "=?";
                selectionArgs = new String[]{mCurrentItemId};
                return new CursorLoader(this, CONTENT_STEPS, null, selection, selectionArgs, null);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID_RECIPES:
                loadRecipes(data);
                break;
            case LOADER_ID_INGREDIENTS:
                loadIngredients(data);
                break;
            case LOADER_ID_STEPS:
                loadSteps(data);
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void loadRecipes(Cursor data) {
        int rowCount = data.getCount();
        int columnCount = data.getColumnCount();

        data.moveToFirst();
        if (rowCount >= 0 && columnCount > 0) {

            List<RecipeEntity> recipeEntities = new ArrayList<>(rowCount);
            RecipeEntity recipeEntity;

            for (int i = 0; i < rowCount; i++) {
                recipeEntity = new RecipeEntity(
                        String.valueOf(data.getInt(RecipesColumns.INDEX_ID)),
                        data.getString(RecipesColumns.INDEX_RECIPE_NAME),
                        null, null,
                        data.getString(RecipesColumns.INDEX_RECIPE_SERVINGS),
                        data.getString(RecipesColumns.INDEX_RECIPE_IMAGE));
                recipeEntities.add(recipeEntity);
                data.moveToNext();
            }
            mRecipesAdapter.restData(recipeEntities);
        }
    }

    private void loadIngredients(Cursor data) {
        int rowCount = data.getCount();
        int columnCount = data.getColumnCount();

        data.moveToFirst();
        if (rowCount >= 0 && columnCount > 0) {
            List<IngredientEntity> ingredientEntities = new ArrayList<>(rowCount);
            IngredientEntity ingredientEntity;

            for (int i = 0; i < rowCount; i++) {
                ingredientEntity = new IngredientEntity(
                        data.getString(IngredientsColumns.INDEX_INGREDIENTS_QUANTITY),
                        data.getString(IngredientsColumns.INDEX_INGREDIENTS_MEASURE),
                        data.getString(IngredientsColumns.INDEX_INGREDIENTS_INGREDIENT));

                ingredientEntities.add(ingredientEntity);
                data.moveToNext();
            }
            mIngredientEntities.clear();
            mIngredientEntities.addAll(ingredientEntities);

            mLoaderManager.restartLoader(LOADER_ID_STEPS, null, this);
        }
    }

    private void loadSteps(Cursor data) {
        int rowCount = data.getCount();
        int columnCount = data.getColumnCount();

        data.moveToFirst();
        if (rowCount >= 0 && columnCount > 0) {
            List<StepEntity> stepEntities = new ArrayList<>(rowCount);
            StepEntity stepEntity;

            for (int i = 0; i < rowCount; i++) {
                stepEntity = new StepEntity(
                        data.getString(StepsColumns.INDEX_ID),
                        data.getString(StepsColumns.INDEX_STEP_SHORT_DESCRIPTION),
                        data.getString(StepsColumns.INDEX_STEP_DESCRIPTION),
                        data.getString(StepsColumns.INDEX_STEP_VIDEO_URL),
                        data.getString(StepsColumns.INDEX_STEP_THUMBNAIL_URL));

                stepEntities.add(stepEntity);
                data.moveToNext();
            }
            mStepEntities.clear();
            mStepEntities.addAll(stepEntities);

            Toast.makeText(this,mIngredientEntities.size()+"\n"+mStepEntities.size(),Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        BakingIntentService.startMe(this);
        progressDialog.show();
    }

    public static void sendBroadCastToMe(Context context, boolean isSuccess) {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra(Intent.EXTRA_RESULT_RECEIVER, isSuccess);
        broadcastManager.sendBroadcast(intent);
    }


    @Override
    public void onItemClick(String recipeId) {
        mCurrentItemId = recipeId;
        mLoaderManager.restartLoader(LOADER_ID_INGREDIENTS, null, this);
    }
}
