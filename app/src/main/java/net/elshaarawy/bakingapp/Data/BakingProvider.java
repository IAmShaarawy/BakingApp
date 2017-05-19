package net.elshaarawy.bakingapp.Data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static net.elshaarawy.bakingapp.Data.BakingContract.*;

/**
 * Created by elshaarawy on 18-May-17.
 */

public class BakingProvider extends ContentProvider {

    private BakingAppDbHelper mDbHelper;
    private static UriMatcher sMatcher;


    @Override
    public boolean onCreate() {
        mDbHelper = new BakingAppDbHelper(getContext());
        sMatcher = bakingAppMatcher();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        switch (sMatcher.match(uri)) {
            case MatchingCodes.RECIPES:
                cursor = database.query(RecipesColumns.TABLE_NAME_RECIPES,
                        projection,
                        selection,
                        selectionArgs,null,null,
                        sortOrder);
                break;
            case MatchingCodes.INGREDIENTS:
                cursor = database.query(IngredientsColumns.TABLE_NAME_INGREDIENTS,
                        projection,
                        selection,
                        selectionArgs,null,null,
                        sortOrder);
                break;
            case MatchingCodes.STEPS:
                cursor = database.query(StepsColumns.TABLE_NAME_STEPS,
                        projection,
                        selection,
                        selectionArgs,null,null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        } else throw new SQLiteException("Unsupported Operation");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sMatcher.match(uri)) {

            case MatchingCodes.RECIPES:
                return MimeTypes.RECIPES_TYPE;

            case MatchingCodes.RECIPES_ITEM:
                return MimeTypes.RECIPES_ITEM_TYPE;

            case MatchingCodes.INGREDIENTS:
                return MimeTypes.INGREDIENTS_TYPE;

            case MatchingCodes.INGREDIENTS_ITEM:
                return MimeTypes.INGREDIENTS_ITEM_TYPE;

            case MatchingCodes.STEPS:
                return MimeTypes.STEPS_TYPE;

            case MatchingCodes.STEPS_ITEM:
                return MimeTypes.STEPS_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (selection == null) {
            throw new IllegalArgumentException("You shouldn't pass null, you will delete all the table entries if you want that pass empty string \"\"");
        }

        final SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int effected;
        switch (sMatcher.match(uri)) {
            case MatchingCodes.RECIPES:
                effected = database.delete(RecipesColumns.TABLE_NAME_RECIPES, selection, selectionArgs);
                break;

            case MatchingCodes.INGREDIENTS:
                effected = database.delete(IngredientsColumns.TABLE_NAME_INGREDIENTS, selection, selectionArgs);
                break;

            case MatchingCodes.STEPS:
                effected = database.delete(StepsColumns.TABLE_NAME_STEPS, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        if (effected == 1) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        database.close();
        return effected;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase database = mDbHelper.getWritableDatabase();
        ContentResolver resolver = getContext().getContentResolver();
        switch (sMatcher.match(uri)) {
            case MatchingCodes.RECIPES:
                return multiInsertion(database, RecipesColumns.TABLE_NAME_RECIPES, values, resolver, uri);

            case MatchingCodes.INGREDIENTS:
                return multiInsertion(database,IngredientsColumns.TABLE_NAME_INGREDIENTS,values,resolver,uri);

            case MatchingCodes.STEPS:
                return multiInsertion(database,StepsColumns.TABLE_NAME_STEPS,values,resolver,uri);

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();
        mDbHelper.close();
    }

    private static int multiInsertion(SQLiteDatabase db, String tableName, ContentValues[] values, ContentResolver resolver, Uri _uri) {
        int insertedRows = 0;
        db.beginTransaction();
        try {
            for (ContentValues value : values) {
                Long _id = db.insert(tableName, null, value);
                if (_id != -1)
                    insertedRows++;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        if (insertedRows > 0) {
            resolver.notifyChange(_uri, null);
        }
        return insertedRows;
    }

    private static UriMatcher bakingAppMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, Paths.RECIPES, MatchingCodes.RECIPES);
        uriMatcher.addURI(AUTHORITY, Paths.RECIPES + "/#", MatchingCodes.RECIPES_ITEM);

        uriMatcher.addURI(AUTHORITY, Paths.INGREDIENTS, MatchingCodes.INGREDIENTS);
        uriMatcher.addURI(AUTHORITY, Paths.INGREDIENTS + "/#", MatchingCodes.INGREDIENTS_ITEM);

        uriMatcher.addURI(AUTHORITY, Paths.STEPS, MatchingCodes.STEPS);
        uriMatcher.addURI(AUTHORITY, Paths.STEPS + "/#", MatchingCodes.STEPS_ITEM);

        return uriMatcher;
    }
}
