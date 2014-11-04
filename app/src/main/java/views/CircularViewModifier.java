package views;

/**
 * Created by Jay on 10/2/2014.
 */
import android.util.FloatMath;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * A simple implementation of the {@link ViewModifier} that rotates the {@link ListView} child {@link View Views} by an
 * angle.
 *
 * @author Artur Stepniewski <a.stepniewsk@samsung.com>
 *
 */
public class CircularViewModifier extends ViewModifier {
    private static final int CIRCLE_OFFSET = 1000;
    private static final float DEGTORAD = 1.0f / 180.0f * (float) Math.PI;
    private static final float SCALING_RATIO = 0.00065f;
    private static final float TRANSLATION_RATIO = 0.09f;

    @Override
    void applyToView(final View v, final AbsListView parent) {
        final float halfHeight = v.getHeight() * 0.5f;
        final float parentHalfHeight = parent.getHeight() * 0.5f;
        final float y = v.getY();
        final float rot = parentHalfHeight - halfHeight - y;

        v.setPivotX(0.0f);
        v.setPivotY(halfHeight);
//        v.setRotation(rot * 0.05f);
        v.setTranslationX((-FloatMath.cos(rot * TRANSLATION_RATIO * DEGTORAD) + 1) * CIRCLE_OFFSET + v.getWidth() * 0.2f);

        final float scale = 1.0f - Math.abs(parentHalfHeight - halfHeight) * SCALING_RATIO;
        v.setScaleX(scale);
        v.setScaleY(scale);
    }
}
