import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.github.johntinashe.hooked.R;

public class MyWidgetProvider extends AppWidgetProvider {

    public static final String UPDATE_MEETING_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";

    public static final String EXTRA_ITEM = "EXTRA_ITEM";


    public void onReceive(Context context, Intent intent) {

        AppWidgetManager mgr = AppWidgetManager.getInstance(context);

        if (intent.getAction().equals(UPDATE_MEETING_ACTION)) {

            int appWidgetIds[] = mgr.getAppWidgetIds(new ComponentName(context, MyWidgetProvider.class));

            mgr.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view);

        }

        super.onReceive(context, intent);

    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager,

                         int[] appWidgetIds) {


        // update each of the app widgets with the remote adapter

        for (int appWidgetId : appWidgetIds) {


            // Set up the intent that starts the ListViewService, which will

            // provide the views for this collection.

            Intent intent = new Intent(context, ListViewWidgetService.class);

            // Add the app widget ID to the intent extras.

            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // Instantiate the RemoteViews object for the app widget layout.

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            // Set up the RemoteViews object to use a RemoteViews adapter.

            // This adapter connects

            // to a RemoteViewsService  through the specified intent.

            // This is how you populate the data.

            rv.setRemoteAdapter(appWidgetId, R.id.list_view, intent);


            // The empty view is displayed when the collection has no items.

            // It should be in the same layout used to instantiate the RemoteViews  object above.

            rv.setEmptyView(R.id.list_view, R.id.empty_view);

            //

            // Do additional processing specific to this app widget...

            //

            appWidgetManager.updateAppWidget(appWidgetId, rv);

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

}
