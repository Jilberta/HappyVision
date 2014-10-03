package views;

/**
 * Created by Jay on 10/2/2014.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import happyvision.jilberta.leavingstone.happyvision.BuildConfig;
import happyvision.jilberta.leavingstone.happyvision.R;

public class CircleViewTest extends ListView implements OnScrollListener {

    private static final String TAG = CircleViewTest.class.getSimpleName();
    private static final boolean DEBUG = BuildConfig.DEBUG && false;

    private static final int DEFAULT_SCROLL_DURATION = 200;

    public static final int DEFAULT_SELECTION = Integer.MAX_VALUE / 2;

    private boolean mIsForceCentering;

    private ViewModifier mViewModifier;

    private final CenterRunnable mCenterRunnable = new CenterRunnable();
    private int mScrollDuration = DEFAULT_SCROLL_DURATION;
    private OnItemCenteredListener mOnItemCenteredListener;

    public CircleViewTest(Context context, AttributeSet attrs) {
        super(context, attrs);

        super.setOnScrollListener(this);

        setOverscrollFooter(null);
        setOverscrollHeader(null);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        setSelection(DEFAULT_SELECTION);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mViewModifier != null) {

            final int count = getChildCount();
            for (int i = 0; i < count; ++i) {
                View v = getChildAt(i);

                mViewModifier.applyToView(v, this);
            }
        }
    }

    /**
     * Sets the current {@link ViewModifier} for this list.
     *
     * @param modifier implementation of the {@link ViewModifier} class
     * @see ViewModifier
     */
    public void setViewModifier(ViewModifier modifier) {
        mViewModifier = modifier;
    }

    /**
     * Sets the smooth scroll duration.
     *
     * @param duration the number of milliseconds the scrolling animation will take
     */
    public void setScrollDuration(int duration) {
        mScrollDuration = duration;
    }

    /**
     * Sets the listener object that will be notified when the list centers on an item.
     *
     * @param listener the {@link OnItemCenteredListener} implementation
     */
    public void setOnItemCenteredListener(OnItemCenteredListener listener) {
        mOnItemCenteredListener = listener;
    }

    /**
     * Smoothly scrolls the  so the {@link View} v is in the middle.
     *
     * @param v the {@link View} that should be in the center after the scroll
     *          <p/>
     *          how long will the scroll take
     */
    public void smoothScrollToView(final View v) {
        final float y = v.getY() + v.getHeight() * 0.5f;
        final float halfHeight = getHeight() * 0.5f;
        final int distance = (int) (y - halfHeight);

        smoothScrollBy(distance, mScrollDuration);
        post(new Runnable() {
            @Override
            public void run() {
                v.findViewById(R.id.play_button).setVisibility(VISIBLE);
                v.findViewById(R.id.stuff).setVisibility(VISIBLE);
//                v.findViewWithTag("Opa").setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        throw new UnsupportedOperationException();
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {

            if (!mIsForceCentering) {
                // Start centering the view

                mIsForceCentering = true;

                final View childView = findViewAtCenter();

                if (childView != null) {
//                    if (DEBUG) {
//                        TextView tv = (TextView) childView.findViewById(R.id.label_app_name);
//                        Log.d(TAG, "Application in center: " + tv.getText().toString());
//                    }


                    if (mOnItemCenteredListener != null) {
                        mOnItemCenteredListener.onItemCentered(childView);
                    }

                    mCenterRunnable.setView(childView);
                    postOnAnimation(mCenterRunnable);
                }
            }
        } else {
            final View childView = findViewAtCenter();
            if (childView != null) {
                childView.findViewById(R.id.play_button).setVisibility(INVISIBLE);
                childView.findViewById(R.id.stuff).setVisibility(INVISIBLE);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        removeCallbacks(mCenterRunnable);

        mIsForceCentering = false;

        return super.onTouchEvent(ev);
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * Finds a View located at the specified point.
     *
     * @param x x-coordinate of the point
     * @param y y-coordinate of the point
     * @return {@link View} located at (x, y) or <code>null</code> if not found
     */
//    public View findViewAt(int x, int y) {
//        final int count = getChildCount();
//        for (int i = 0; i < count; ++i) {
//            final View v = getChildAt(i);
//            final int x0 = v.getLeft();
//            final int y0 = v.getTop();
//            final int x1 = v.getWidth() - x0;
//            final int y1 = v.getHeight() + y0;
//
//            if (x >= x0 && x <= x1 && y >= y0 && y <= y1) {
//                return v;
//            }
//        }
//
//        return null;
//    }
    public View findViewAt(int x, int y) {
        final int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            final View v = getChildAt(i);
            final int x0 = v.getLeft();
            final int y0 = v.getTop();
            final int x1 = v.getWidth() - x0;
            final int y1 = v.getHeight() + y0;

            if (x >= x0 && x <= x1 && y >= y0 && y <= y1) {
                return v;
            }
        }

        return null;
    }

    /**
     * Finds a {@link View} located in the center of the.
     *
     * @return the {@link View} at the center of the
     */
    public View findViewAtCenter() {
        return findViewAt(0, getHeight() / 2);
    }

    public class CenterRunnable implements Runnable {

        private View mView;

        public void setView(View v) {
            mView = v;
        }

        public void run() {
            smoothScrollToView(mView);

            mIsForceCentering = true;
        }
    }

    /**
     * Handles the event occurring when the list centers on an item.
     */
    public interface OnItemCenteredListener {
        public void onItemCentered(View v);
    }
}
