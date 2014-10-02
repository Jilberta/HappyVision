package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import happyvision.jilberta.leavingstone.happyvision.R;

/**
 * Created by Jay on 10/2/2014.
 */
public class ListviewAdapter extends BaseAdapter {
    private ArrayList<String> songList;
    private Context context;

    public ListviewAdapter(Context context, ArrayList<String> songList){
        this.context = context;
        this.songList = songList;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = convertView;
        if(newView == null){
            newView = View.inflate(context, R.layout.listview_item, null);
        }

        TextView view = (TextView) newView.findViewById(R.id.item);
        view.setText((CharSequence) getItem(position));

        return view;
    }
}
