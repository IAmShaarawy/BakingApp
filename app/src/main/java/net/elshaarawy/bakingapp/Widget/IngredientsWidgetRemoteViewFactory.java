package net.elshaarawy.bakingapp.Widget;

import android.content.Context;
import android.database.Cursor;
import android.text.NoCopySpan;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import net.elshaarawy.bakingapp.R;
import net.elshaarawy.bakingapp.Utils.PreferenceUtil;

import static net.elshaarawy.bakingapp.Data.BakingContract.IngredientsColumns;
import static net.elshaarawy.bakingapp.Data.BakingContract.ProviderUris.CONTENT_INGREDIENTS;
import static net.elshaarawy.bakingapp.Utils.PreferenceUtil.DefaultKeys;

/**
 * Created by elshaarawy on 20-May-17.
 */

public class IngredientsWidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor mCursor;
    private PreferenceUtil mPreferenceUtil;
    private String id;

    public IngredientsWidgetRemoteViewFactory(Context mContext) {
        this.mContext = mContext;
        mPreferenceUtil = new PreferenceUtil(mContext, DefaultKeys.DEFAULT_SHARED_PREFERENCE);
        id = mPreferenceUtil.getString(DefaultKeys.PREF_IS_DESIRED);
        if (id == null || id.equals("")) {
            id = "0";
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        String selection = IngredientsColumns.TABLE_NAME_INGREDIENTS + "." + IngredientsColumns._ID + "=?";
        String[] selectionArgs = new String[]{id};
        mCursor = mContext.getContentResolver().query(CONTENT_INGREDIENTS, null, selection, selectionArgs, null);
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_widget);

        mCursor.moveToPosition(position);

        CharSequence s = mCursor.getString(IngredientsColumns.INDEX_INGREDIENTS_QUANTITY) + " " +
                mCursor.getString(IngredientsColumns.INDEX_INGREDIENTS_MEASURE) + " -> " +
                mCursor.getString(IngredientsColumns.INDEX_INGREDIENTS_INGREDIENT);

        views.setTextViewText(R.id.item_widget,s);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
