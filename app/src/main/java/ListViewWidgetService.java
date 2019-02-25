import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.github.johntinashe.hooked.R;

import java.util.ArrayList;

public class ListViewWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteViewsFactory(this.getApplicationContext(), intent);

    }

    class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;

        private ArrayList<String> favorites;


        public ListViewRemoteViewsFactory(Context context, Intent intent) {

            mContext = context;

        }

        // Initialize the data set.

        public void onCreate() {

            // In onCreate() you set up any connections / cursors to your data source. Heavy lifting,

            // for example downloading or creating content etc, should be deferred to onDataSetChanged()

            // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.

            favorites = new ArrayList<String>();
            favorites.add("John");
            favorites.add("Mike");
            favorites.add("Peter");


        }

        @Override
        public void onDataSetChanged() {

        }

        // Given the position (index) of a WidgetItem in the array, use the item's text value in

        // combination with the app widget item XML file to construct a RemoteViews object.

        public RemoteViews getViewAt(int position) {

            // position will always range from 0 to getCount() - 1.

            // Construct a RemoteViews item based on the app widget item XML file, and set the

            // text based on the position.

            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

            // feed row

            String data = favorites.get(position);

            rv.setTextViewText(R.id.item, data);

            // end feed row

            // Next, set a fill-intent, which will be used to fill in the pending intent template

            // that is set on the collection view in ListViewWidgetProvider.

//            Bundle extras = new Bundle();
//
//            extras.putInt(MyWidgetProvider.EXTRA_ITEM, position);
//
//            Intent fillInIntent = new Intent();
//
//            fillInIntent.putExtra("homescreen_meeting",data);
//
//            fillInIntent.putExtras(extras);
//
//            // Make it possible to distinguish the individual on-click
//
//            // action of a given item
//
//            rv.setOnClickFillInIntent(R.id.item_layout, fillInIntent);

            // Return the RemoteViews object.

            return rv;

        }

        public int getCount() {

            return favorites != null ? favorites.size() : 0;

        }


        public int getViewTypeCount() {

            return 1;

        }

        public long getItemId(int position) {

            return position;

        }

        public void onDestroy() {

            if (favorites != null) favorites.clear();

        }

        public boolean hasStableIds() {

            return true;

        }

        public RemoteViews getLoadingView() {

            return null;

        }

    }

}
