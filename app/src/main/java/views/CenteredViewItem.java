package views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import happyvision.jilberta.leavingstone.happyvision.R;

/**
 * Created by Jay on 10/3/2014.
 */
public class CenteredViewItem extends LinearLayout {
    LayoutInflater inflater;
    View view;

    public CenteredViewItem(Context context) {
        super(context);
        initViews();
    }

    private void initViews() {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_item_view, this, true);
    }
}
