package adapter;

import android.content.res.ColorStateList;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import happyvision.jilberta.leavingstone.happyvision.R;
import item.SongItem;

/**
 * Created by Jay on 10/2/2014.
 */
public class TestAdapter implements ListAdapter {
    public List<SongItem> list = new ArrayList<SongItem>();

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        LinearLayout ll = new LinearLayout(parent.getContext());
//        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        ll.setLayoutParams(params2);
//        ll.setOrientation(LinearLayout.HORIZONTAL);

        SongItem song = (SongItem) getItem(position);

        ImageView img = new ImageView(parent.getContext());
//            LayoutParams params = new LayoutParams(144, 144);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        img.setLayoutParams(params);
        img.setImageResource(song.getImageId());
//        ll.addView(img);
//
//        TextView tv = new TextView(parent.getContext());
//        tv.setText("Avoeeeeeee");
//        tv.setTextColor(Color.WHITE);
//        tv.setTextSize(50);
//        tv.setVisibility(View.GONE);
//        tv.setTag("Opa");
//
//        ll.addView(tv);

        convertView = img;

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

}
