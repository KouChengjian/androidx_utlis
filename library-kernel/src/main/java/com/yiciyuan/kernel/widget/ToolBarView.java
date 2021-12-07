package com.yiciyuan.kernel.widget;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.yiciyuan.kernel.R;

/**
 * Created with Android Studio.
 */
public class ToolBarView extends FrameLayout {

    private Context mContext;
    private View mToolbarStatusBar;
    private Toolbar mToolbar;
    private RelativeLayout mToolbarContainer;
    private TextView mToolbarTitle;
    private TextView mToolbarRightText;

    private int toolbarBackgroundBarColor;
    private int toolbarBackgroundColor = 0xff46D29C;
    private int toolbarTitleTextColor = 0xffffffff;
    private int toolbarSubtitleTextColor = 0xffffffff;

    private int navigationIcon = R.mipmap.ic_action_back;

    private int titleTextColor = 0xffffffff;
    private int titleTextSize;
    private int titleTextStyle;

    private int rightTextColor = 0xffffffff;
    private int rightTextSize;
    private int rightTextStyle;

    public ToolBarView(@NonNull Context context) {
        this(context, null);
    }

    public ToolBarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolBarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        titleTextSize = dp2px(20);
        rightTextSize = dp2px(16);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ycy_ToolBarView);
        toolbarBackgroundBarColor = ta.getColor(R.styleable.ycy_ToolBarView_tb_toolbar_background_bar_color, toolbarBackgroundColor);
        toolbarBackgroundColor = ta.getColor(R.styleable.ycy_ToolBarView_tb_toolbar_background_color, toolbarBackgroundColor);
        toolbarTitleTextColor = ta.getColor(R.styleable.ycy_ToolBarView_tb_toolbar_title_text_color, toolbarTitleTextColor);
        toolbarSubtitleTextColor = ta.getColor(R.styleable.ycy_ToolBarView_tb_toolbar_subtitle_text_color, toolbarSubtitleTextColor);

        navigationIcon = ta.getResourceId(R.styleable.ycy_ToolBarView_tb_navigationIcon, navigationIcon);

        titleTextColor = ta.getColor(R.styleable.ycy_ToolBarView_tb_titleTextColor, titleTextColor);
        titleTextSize = ta.getColor(R.styleable.ycy_ToolBarView_tb_titleTextSize, titleTextSize);
//        titleTextStyle = ta.getInt(R.styleable.ycy_ToolBarView_tb_titleTextStyle, titleTextStyle);

        rightTextColor = ta.getColor(R.styleable.ycy_ToolBarView_tb_rightTextColor, rightTextColor);
        rightTextSize = ta.getColor(R.styleable.ycy_ToolBarView_tb_rightTextSize, rightTextSize);
//        rightTextStyle = ta.getInt(R.styleable.ycy_ToolBarView_tb_rightTextStyle, rightTextStyle);

        ta.recycle();
        initViews();
        initDatas();
        initEvent();

    }

    private void initViews() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.include_action_bar, this);
        mToolbarStatusBar = view.findViewById(R.id.toolbarStatusBar);
        mToolbar = view.findViewById(R.id.toolbar);
        mToolbarContainer = view.findViewById(R.id.toolbarContainer);
        mToolbarTitle = view.findViewById(R.id.toolbarTitle);
        mToolbarRightText = view.findViewById(R.id.toolbarRightText);
    }

    private void initDatas() {
        mToolbarStatusBar.setBackgroundColor(toolbarBackgroundBarColor);
        mToolbar.setBackgroundColor(toolbarBackgroundColor);
        mToolbar.setTitleTextColor(toolbarTitleTextColor);
        mToolbar.setSubtitleTextColor(toolbarSubtitleTextColor);

        mToolbarTitle.setTextColor(titleTextColor);
        mToolbarTitle.setTextSize(COMPLEX_UNIT_PX, titleTextSize);

        mToolbarRightText.setTextColor(rightTextColor);
        mToolbarRightText.setTextSize(COMPLEX_UNIT_PX, rightTextSize);
    }

    private void initEvent() {

    }

    private int dp2px(float value) {
        final float scale = mContext.getResources().getDisplayMetrics().densityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public Toolbar getToolbar() {
        return mToolbar;
    }

    public TextView getToolbarTitle() {
        return mToolbarTitle;
    }

    public RelativeLayout getToolbarContainer() {
        return mToolbarContainer;
    }

    public TextView getToolbarRightText() {
        return mToolbarRightText;
    }

    public int resetToolbarStatusBarHeight() {
        int height = getStatusBarHeight();
        if (height > 0) {
            setToolbarStatusBarHeight(height);
        }
        return height;
    }

    public void setToolbarStatusBarHeight(int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        mToolbarStatusBar.setLayoutParams(params);
    }

    public void setBackground(int color) {
        mToolbar.setBackgroundColor(color);
        mToolbarStatusBar.setBackgroundColor(color);
    }

    public void setNavigationIcon() {
        setNavigationIcon(navigationIcon);
    }

    public void setNavigationIcon(int rId) {
        mToolbar.setNavigationIcon(rId);
    }

    public void setNavigationTitle(String title) {
        mToolbar.setTitle(title);
    }

    public void setNavigationOrBreak(String title) {
        setNavigationOrBreak(navigationIcon, title);
    }

    public void setNavigationOrBreak(int rId, String title) {
        setNavigationIcon(rId);
        setNavigationTitle(title);
    }

    public void setTitleOrBreak(String s) {
        setTitleOrBreak(s, navigationIcon);
    }

    public void setTitleOrBreak(String s, int resId) {
        setNavigationIcon(resId);
        setToolbarTitle(s);
    }

    public void setToolbarTitle(String s) {
        mToolbarTitle.setText(s);
    }

    public void addToolbarContainer(View view) {
        mToolbarContainer.removeAllViews();
        mToolbarContainer.addView(view);
    }

    public void setToolbarRightMenus(int rid) {
        mToolbar.inflateMenu(rid);
    }

    public void setToolbarRightText(String s) {
        mToolbarRightText.setText(s);
    }

    public void setAlpha(int a) {
        mToolbarStatusBar.getBackground().mutate().setAlpha(a);
        mToolbar.getBackground().mutate().setAlpha(a);
    }

    public void setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener onMenuItemClickListener) {
        mToolbar.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    public void setNavigationOnClickListener(OnClickListener onClickListener) {
        mToolbar.setNavigationOnClickListener(onClickListener);
    }
}
