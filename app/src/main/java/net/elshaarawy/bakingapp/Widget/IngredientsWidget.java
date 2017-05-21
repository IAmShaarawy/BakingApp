package net.elshaarawy.bakingapp.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import net.elshaarawy.bakingapp.Activities.MainActivity;
import net.elshaarawy.bakingapp.R;
import net.elshaarawy.bakingapp.Utils.PreferenceUtil;

import static net.elshaarawy.bakingapp.Utils.PreferenceUtil.DefaultKeys;
import static net.elshaarawy.bakingapp.Data.BakingContract.RecipesColumns;
import static net.elshaarawy.bakingapp.Data.BakingContract.ProviderUris.CONTENT_RECIPES;
/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        PreferenceUtil preferenceUtil = new PreferenceUtil(context,DefaultKeys.DEFAULT_SHARED_PREFERENCE);
        String id = preferenceUtil.getString(DefaultKeys.PREF_IS_DESIRED);
        if (id == null ||id.equals("") ) {
            id = "0";
        }
        if (!id.equals("0")){

            String selection = RecipesColumns.TABLE_NAME_RECIPES+"."+RecipesColumns._ID+"=?";
            String selectionArgs [] = new String[]{id};
            Cursor cursor = context
                    .getContentResolver()
                    .query(CONTENT_RECIPES,null,selection,selectionArgs,null);
            cursor.moveToFirst();

            views.setTextViewText(R.id.widget_title, cursor.getString(RecipesColumns.INDEX_RECIPE_NAME));

            views.setRemoteAdapter(R.id.widget_list,new Intent(context,IngredientsWidgetRemoteViewService.class));

            views.setViewVisibility(R.id.widget_list,View.VISIBLE);
            views.setViewVisibility(R.id.widget_placeholder, View.GONE);
        }else {
            views.setTextViewText(R.id.widget_title,context.getString(R.string.app_name));

            views.setViewVisibility(R.id.widget_list,View.GONE);
            views.setViewVisibility(R.id.widget_placeholder,View.VISIBLE);

            Intent  intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
            views.setOnClickPendingIntent(R.id.widget_placeholder,pendingIntent);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, IngredientsWidget.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widget_list);
            this.onUpdate(context,mgr,mgr.getAppWidgetIds(cn));
        }
    }
}

