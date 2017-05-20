package net.elshaarawy.bakingapp.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by elshaarawy on 20-May-17.
 */

public class IngredientsWidgetRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsWidgetRemoteViewFactory(getApplicationContext());
    }
}
