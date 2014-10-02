package views;

/**
 * Created by Jay on 10/2/2014.
 */
import android.view.View;
import android.widget.AbsListView;

/**
 * Abstract class defining a transformation that will be applied to a View.
 *
 * @author Artur Stepniewski <a.stepniewsk@samsung.com>
 *
 */
public abstract class ViewModifier {
    abstract void applyToView(View v, AbsListView parent);
}
