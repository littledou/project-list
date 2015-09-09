package cafe.wdiget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

/**
 * Created by mac on 15/9/1.
 */
public class VerticalLinearLayout extends ViewGroup {

    private final static String TAG = "VerticalLinearLayout";
    /**
     * 屏幕高度
     */
    private int mScreenHeight;
    /**
     * 手指按下时的 getScrollY
     */
    private int mScrollStart;
    /**
     * 手指抬起时的 getScrollY
     */
    private int mScrollEnd;
    /**
     * 记录移动时的Y
     */
    private int mLastY;
    /**
     * 滚动的辅助类
     */
    private Scroller mScroller;
    /**
     * 检测是否在滚动
     */
    private boolean isScrolling;
    /**
     * 加速度检测
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 记录当前页
     */
    private int currentPage = 0;
    private OnPagerListener mOnPagerListener;
    private int childCount = 0;

    public VerticalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**
         * 获取屏幕高度
         */
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        mScroller = new Scroller(context);
        setMotionEventSplittingEnabled(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, mScreenHeight);
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            childCount = getChildCount();
            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
            lp.height = mScreenHeight * childCount;
            setLayoutParams(lp);
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != View.GONE) {
                    child.layout(l, i * mScreenHeight, r, (i + 1) * mScreenHeight);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isScrolling)
            return super.onTouchEvent(event);
        int action = event.getAction();
        int y = (int) event.getY();
        int dy = 0;
        obtainVelocity(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScrollStart = getScrollY();
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                dy = mLastY - y;
                if (getScrollY() < 0 || getScrollY() > mScreenHeight * (childCount - 1)) {//首段终端拉伸阻力加大
                    dy = dy / 3;
                }

                scrollBy(0, dy);
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:

                if (getScrollY() < 0) {
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                } else if (getScrollY() > mScreenHeight * (childCount - 1)) {
                    mScroller.startScroll(0, getScrollY(), 0, mScreenHeight * (childCount - 1) - getScrollY());
                } else {
                    mScrollEnd = getScrollY();
                    int dScrollY = mScrollEnd - mScrollStart;
                    if (mScrollEnd > mScrollStart) {
                        if (shouldScroll()) {
                            mScroller.startScroll(0, getScrollY(), 0, mScreenHeight - dScrollY);
                        } else {
                            mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                        }
                    } else {
                        if (shouldScroll()) {
                            mScroller.startScroll(0, getScrollY(), 0, -mScreenHeight - dScrollY);
                        } else {
                            mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                        }
                    }
                }
                isScrolling = true;
                postInvalidate();
                recycleVelocity();
                break;

        }

        return true;
    }

    private void recycleVelocity() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private boolean shouldScroll() {
        return Math.abs(mScrollEnd - mScrollStart) > mScreenHeight / 2 || Math.abs(getVelocity()) > 600;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        } else {
            int position = getScrollY() / mScreenHeight;
            if (position != currentPage) {
                if (mOnPagerListener != null) {
                    mOnPagerListener.onPageChange(position,getChildAt(position));
                }
            }
        }
        isScrolling = false;
    }

    private void obtainVelocity(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private float getVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        return (int) mVelocityTracker.getYVelocity();
    }

    public void setmOnPagerListener(OnPagerListener mOnPagerListener) {
        this.mOnPagerListener = mOnPagerListener;
    }

    public interface OnPagerListener {
        void onPageChange(int currentPage,View view);
    }
}
