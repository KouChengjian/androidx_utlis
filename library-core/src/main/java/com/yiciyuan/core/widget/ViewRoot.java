package com.yiciyuan.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.yiciyuan.core.R;
import com.yiciyuan.core.utils.AnimationUtils;

/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2019-12-16 14:58
 * Description:
 */
public class ViewRoot extends FrameLayout {

    private boolean init = false;

    private int mLayoutResource;
    private int mBackgroundResource;

    private Context context;
    private RelativeLayout pbRl;
    private ProgressBar progressBar;
    private ImageView imageView;
    private ViewStub viewStub;

    public ViewRoot(Context context) {
        this(context, null);
    }

    public ViewRoot(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewRoot(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewRootDelayed);
        mLayoutResource = array.getResourceId(R.styleable.ViewRootDelayed_root_layout, 0);
        mBackgroundResource = array.getResourceId(R.styleable.ViewRootDelayed_root_background, 0);
        array.recycle();
        initViews();
    }

    private void initViews() {
        init = false;
        if (mBackgroundResource == 0) {
            showLoadingView();
        } else {
            showBackgroundResource();
        }
    }

    private void showBackgroundResource() {
        if (imageView == null) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(mBackgroundResource);
            imageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            pbRl = new RelativeLayout(context);
            pbRl.setGravity(Gravity.CENTER);

            pbRl.addView(imageView);
            addView(pbRl, params);
        }
        AnimationUtils.showAndHiddenAnimation(imageView, AnimationUtils.AnimationState.STATE_SHOW, 200, null);
    }

    private void removeBackgroundResource() {
        if (pbRl != null) {
            removeView(pbRl);
        }
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        }
        imageView = null;
    }

    private void showLoadingView() {
        if (progressBar == null) {
            progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
            progressBar.setIndeterminate(true);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            pbRl = new RelativeLayout(context);
            pbRl.setGravity(Gravity.CENTER);
            pbRl.addView(progressBar);
            addView(pbRl, params);
        }
    }

    private void removeLoadingView() {
        if (pbRl != null) {
            removeView(pbRl);
        }
    }

    private void removeAllView() {
        removeLoadingView();
        if (imageView != null) {
            AnimationUtils.showAndHiddenAnimation(imageView, AnimationUtils.AnimationState.STATE_HIDDEN, 1000, new AnimationUtils.OnAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    removeBackgroundResource();
                }
            });
        } else {
            removeBackgroundResource();
        }

    }

    public void removeImageView() {
        ImageView imageView = null;
        int total = getChildCount();
        for (int i = 0; i < total; i++) {
            View view = getChildAt(i);
            if (view instanceof ImageView) {
                imageView = (ImageView) view;
                break;
            }
        }

        final ImageView finalImageView = imageView;
        AnimationUtils.showAndHiddenAnimation(imageView, AnimationUtils.AnimationState.STATE_HIDDEN, 1500, new AnimationUtils.OnAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                removeView(finalImageView);
            }
        });
        imageView = null;
    }

    // TODO ：修改三思而行
    public View inflate(ViewStub viewRootChild) {
        init = true;
        removeAllView();
        if (viewRootChild == null) {
            throw new RuntimeException("viewStub Not Null");
        }
        View view = viewRootChild.inflate();
        return view;
    }

    public boolean isInit() {
        return init;
    }
}
