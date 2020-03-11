package com.example.test.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.test.R;


/**
 * Created with Android Studio.
 * User: kcj
 * Date: 2018/7/7 14:15
 * Description:
 */
public class TipLayoutView extends RelativeLayout implements View.OnClickListener {

    private Context context;
    private ViewStub mLayoutLoading, mLayoutError;
    private LinearLayout mLLTipViewLoading, mLLTipViewError;
    private ImageView mTvTipViewErrorPic;
    private TextView mTvTipViewErrorMsg;
    private BGButton mReloadButton;
    private OnReloadClick mOnReloadClick;

    public TipLayoutView(Context context) {
        this(context, null);
    }

    public TipLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TipLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.include_tiplayoutview, this);
        view.setOnClickListener(null);
        mLayoutLoading = findViewById(R.id.layout_loading);
        mLayoutError = findViewById(R.id.layout_error);
        showLoading();
    }

    public void resetStatus() {
        if (mLLTipViewError != null) {
            mLLTipViewError.setVisibility(View.GONE);
        }
        if (mLLTipViewLoading != null) {
            mLLTipViewLoading.setVisibility(View.GONE);
        }
    }

    public void showContent() {
        if (this.getVisibility() == View.VISIBLE) {
            this.setVisibility(GONE);
        }
    }

    public void showLoading() {
        this.inflateLoadingLayout();
        if (mLLTipViewLoading.getVisibility() == View.GONE) {
            mLLTipViewLoading.setVisibility(View.VISIBLE);
        }
    }

    public void showEmpty() {
        this.inflateErrorLayout();
        if (mLLTipViewError.getVisibility() == View.GONE) {
            mLLTipViewError.setVisibility(View.VISIBLE);
        }
        mReloadButton.setVisibility(View.GONE);
    }

    /**
     * 显示空数据且存在刷新按钮
     */
    public void showEmptyOrRefresh() {
        showEmptyOrRefresh(false);
    }

    /**
     * 显示空数据且存在刷新按钮
     */
    public void showEmptyOrRefresh(boolean isTop) {
        this.inflateErrorLayout();
        if (isTop) {
//            mLLTipViewError.setGravity(Gravity.CENTER | Gravity.TOP);
//            mLLTipViewError.setPadding(0, ScreenUtil.dp2px(context, 50), 0, 0);
        }
        mTvTipViewErrorPic.setImageResource(R.mipmap.bg_loading_data_null);
        mTvTipViewErrorMsg.setText("");
        mTvTipViewErrorMsg.setVisibility(View.GONE);
        mReloadButton.setText("点击加载");
        if (mLLTipViewError.getVisibility() == View.GONE) {
            mLLTipViewError.setVisibility(View.VISIBLE);
        }
    }

    public void showNetError() {
        this.inflateErrorLayout();
        if (mLLTipViewError.getVisibility() == View.GONE) {
            mLLTipViewError.setVisibility(View.VISIBLE);
        }
    }

    public void showCustomError(int id, String msg, String btn) {
        this.inflateErrorLayout();
        mTvTipViewErrorPic.setImageResource(id);
        mTvTipViewErrorMsg.setText(msg);
        if (btn != null && !btn.isEmpty()) {
            mReloadButton.setText(btn);
            mReloadButton.setVisibility(View.VISIBLE);
        } else {
            mReloadButton.setVisibility(View.GONE);
        }
        if (mLLTipViewError.getVisibility() == View.GONE) {
            mLLTipViewError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_refush) {
            if (mOnReloadClick != null) {
                showLoading();
                mOnReloadClick.onReload();
            }
        }
    }

    private void inflateLoadingLayout() {
        if (mLLTipViewLoading != null) {
            return;
        }
        View view = mLayoutLoading.inflate();
        mLLTipViewLoading = view.findViewById(R.id.ll_tipview_loading);
    }

    private void inflateErrorLayout() {
        if (mLLTipViewError != null) {
            return;
        }
        View view = mLayoutError.inflate();
        mLLTipViewError = view.findViewById(R.id.ll_tipview_error);
        mTvTipViewErrorPic = view.findViewById(R.id.tv_tiplayout_pic);
        mTvTipViewErrorMsg = view.findViewById(R.id.tv_tiplayout_msg);
        mReloadButton = view.findViewById(R.id.btn_refush);
        mReloadButton.setOnClickListener(this);
    }

    /**
     * 重新加载点击事件
     */
    public void setOnReloadClick(OnReloadClick onReloadClick) {
        this.mOnReloadClick = onReloadClick;
    }

    public interface OnReloadClick {
        void onReload();
    }
}
