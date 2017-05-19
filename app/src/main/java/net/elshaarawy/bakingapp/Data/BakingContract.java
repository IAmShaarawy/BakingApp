package net.elshaarawy.bakingapp.Data;

import android.content.ContentResolver;
import android.graphics.Path;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by elshaarawy on 18-May-17.
 */

public class BakingContract {


    public static final String AUTHORITY = "net.elshaarawy.bakingapp";

    public static final Uri BASE_CONTENT_URI = new Uri.Builder().scheme("content").authority(AUTHORITY).build();


    public interface RecipesColumns extends BaseColumns {

        String TABLE_NAME_RECIPES = "table_name_recipes";

        String _ID = "r_id";
        String RECIPE_NAME = "recipe_name";
        String RECIPE_SERVINGS = "recipe_servings";
        String RECIPE_IMAGE = "recipe_image";

        int INDEX_ID = 0;
        int INDEX_RECIPE_NAME = 1;
        int INDEX_RECIPE_SERVINGS = 2;
        int INDEX_RECIPE_IMAGE = 3;

    }

    public interface IngredientsColumns extends BaseColumns {

        String TABLE_NAME_INGREDIENTS = "table_name_ingredients";

        String _ID = "i_id";
        String INGREDIENTS_QUANTITY = "ingredients_quantity";
        String INGREDIENTS_MEASURE = "ingredients_measure";
        String INGREDIENTS_INGREDIENT = "ingredients_ingredient";

        int INDEX_ID = 0;
        int INDEX_INGREDIENTS_QUANTITY = 1;
        int INDEX_INGREDIENTS_MEASURE = 2;
        int INDEX_INGREDIENTS_INGREDIENT = 3;
    }

    public interface StepsColumns extends BaseColumns {

        String TABLE_NAME_STEPS = "table_name_steps";

        String _ID = "s_id";
        String STEP_RECIPE_ID = "steps_recipe_id";
        String STEP_SHORT_DESCRIPTION = "steps_shortDescription";
        String STEP_DESCRIPTION = "steps_description";
        String STEP_VIDEO_URL = "step_video_url";
        String STEP_THUMBNAIL_URL = "step_thumbnail_url";

        int INDEX_ID = 0;
        int INDEX_STEP_RECIPE_ID = 1;
        int INDEX_STEP_SHORT_DESCRIPTION = 2;
        int INDEX_STEP_DESCRIPTION =3 ;
        int INDEX_STEP_VIDEO_URL = 4;
        int INDEX_STEP_THUMBNAIL_URL = 5;
    }

    public interface Paths {
        String RECIPES = "recipes";
        String INGREDIENTS = "ingredients";
        String STEPS = "steps";
    }

    public interface ProviderUris {
        Uri CONTENT_RECIPES = BASE_CONTENT_URI.buildUpon().appendPath(Paths.RECIPES).build();
        Uri CONTENT_INGREDIENTS = BASE_CONTENT_URI.buildUpon().appendPath(Paths.INGREDIENTS).build();
        Uri CONTENT_STEPS  = BASE_CONTENT_URI.buildUpon().appendPath(Paths.STEPS ).build();
    }

    public interface MatchingCodes {
        int RECIPES = 100;
        int RECIPES_ITEM = 101;

        int INGREDIENTS = 110;
        int INGREDIENTS_ITEM = 111;

        int STEPS = 120;
        int STEPS_ITEM = 121;
    }

    public interface MimeTypes {
        String RECIPES_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + Paths.RECIPES;
        String RECIPES_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + Paths.RECIPES;

        String INGREDIENTS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + Paths.INGREDIENTS;
        String INGREDIENTS_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + Paths.INGREDIENTS;

        String STEPS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + Paths.STEPS;
        String STEPS_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + Paths.STEPS;
    }

}