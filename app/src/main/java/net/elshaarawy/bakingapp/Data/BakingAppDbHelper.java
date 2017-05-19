package net.elshaarawy.bakingapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static net.elshaarawy.bakingapp.Data.BakingContract.*;

/**
 * Created by elshaarawy on 18-May-17.
 */

public class BakingAppDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BakingApp.db";

    public BakingAppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLTableCreator.RECIPES);
        db.execSQL(SQLTableCreator.INGREDIENTS);
        db.execSQL(SQLTableCreator.STEPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+RecipesColumns.TABLE_NAME_RECIPES );
        db.execSQL("DROP TABLE IF EXISTS "+IngredientsColumns.TABLE_NAME_INGREDIENTS );
        db.execSQL("DROP TABLE IF EXISTS "+StepsColumns.TABLE_NAME_STEPS );

        this.onCreate(db);
    }

    private interface SQLTableCreator{

        String RECIPES = "CREATE TABLE "+RecipesColumns.TABLE_NAME_RECIPES+" ( "+
                RecipesColumns._ID+ " INTEGER PRIMARY KEY , "+
                RecipesColumns.RECIPE_NAME +" TEXT NOT NULL , "+
                RecipesColumns.RECIPE_SERVINGS+" TEXT NOT NULL , "+
                RecipesColumns.RECIPE_IMAGE+" TEXT NOT NULL ); ";

        String INGREDIENTS = "CREATE TABLE "+IngredientsColumns.TABLE_NAME_INGREDIENTS+" ( "+
                IngredientsColumns._ID + " INTEGER , "+
                IngredientsColumns.INGREDIENTS_QUANTITY+" TEXT NOT NULL , "+
                IngredientsColumns.INGREDIENTS_MEASURE+" TEXT NOT NULL , "+
                IngredientsColumns.INGREDIENTS_INGREDIENT+" TEXT NOT NULL ); ";

        String STEPS = "CREATE TABLE "+StepsColumns.TABLE_NAME_STEPS+" ( "+
                StepsColumns._ID + " INTEGER , "+
                StepsColumns.STEP_RECIPE_ID + " INTEGER , "+
                StepsColumns.STEP_SHORT_DESCRIPTION+" TEXT NOT NULL , "+
                StepsColumns.STEP_DESCRIPTION+" TEXT NOT NULL , "+
                StepsColumns.STEP_VIDEO_URL+" TEXT NOT NULL , "+
                StepsColumns.STEP_THUMBNAIL_URL+" TEXT NOT NULL ); ";

    }
}
