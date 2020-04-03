package com.yiciyuan.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


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

    private int mLoadingNoWifi = R.mipmap.bg_loading_no_wifi;
    private int mLoadingDataNull = R.mipmap.bg_loading_data_null;

    public TipLayoutView(Context context) {
        this(context, null);
    }

    public TipLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TipLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TipLayoutView);
        mLoadingNoWifi = ta.getColor(R.styleable.TipLayoutView_tlv_loading_nowifi, mLoadingNoWifi);
        mLoadingDataNull = ta.getColor(R.styleable.TipLayoutView_tlv_loading_datanull, mLoadingDataNull);
        ta.recycle();
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.include_tiplayout_view, this);
        view.setOnClickListener(null);
        mLayoutLoading = findViewById(R.id.layout_loading);
        mLayoutError = findViewById(R.id.layout_error);
        showLoading();
    }

    public void showContent() {
        if (this.getVisibility() == View.VISIBLE) {
            this.setVisibility(GONE);
        }
    }

    public void showLoading() {
        this.inflateLoadingLayout();
        this.resetLoadingLayout();
    }

    public void showEmpty() {
        this.inflateErrorLayout();
        this.resetErrorLayout();
        mTvTipViewErrorPic.setImageResource(mLoadingDataNull);
        mTvTipViewErrorMsg.setText("");
        mTvTipViewErrorMsg.setVisibility(View.GONE);
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
        this.resetErrorLayout();
        if (isTop) {
            mLLTipViewError.setGravity(Gravity.CENTER | Gravity.TOP);
            mLLTipViewError.setPadding(0, 100, 0, 0);
        }
        mTvTipViewErrorPic.setImageResource(mLoadingDataNull);
        mTvTipViewErrorMsg.setText("");
        mTvTipViewErrorMsg.setVisibility(View.GONE);
        mReloadButton.setText("点击加载");
    }

    public void showNetError() {
        this.inflateErrorLayout();
        this.resetErrorLayout();
        mTvTipViewErrorPic.setImageResource(mLoadingNoWifi);
        mTvTipViewErrorMsg.setText("加载失败，请检查网络");
        mReloadButton.setText("点击加载");
        mReloadButton.setVisibility(View.VISIBLE);
    }

    public void showCustomError(int id, String msg, String btn) {
        this.inflateErrorLayout();
        this.resetErrorLayout();
        mTvTipViewErrorPic.setImageResource(id);
        mTvTipViewErrorMsg.setText(msg);
        if (btn != null && !btn.isEmpty()) {
            mReloadButton.setText(btn);
            mReloadButton.setVisibility(View.VISIBLE);
        } else {
            mReloadButton.setVisibility(View.GONE);
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

    public void resetLoadingLayout() {
        if (this.getVisibility() == View.GONE) {
            this.setVisibility(VISIBLE);
        }
        if (mLLTipViewLoading.getVisibility() == View.GONE) {
            mLLTipViewLoading.setVisibility(View.VISIBLE);
        }
        if (mLLTipViewError != null) {
            if (mLLTipViewError.getVisibility() == View.VISIBLE) {
                mLLTipViewError.setVisibility(View.GONE);
            }
        }
    }

    public void resetErrorLayout() {
        if (this.getVisibility() == View.GONE) {
            this.setVisibility(VISIBLE);
        }
        if (mLLTipViewError.getVisibility() == View.GONE) {
            mLLTipViewError.setVisibility(View.VISIBLE);
        }
        if (mLLTipViewLoading != null) {
            if (mLLTipViewLoading.getVisibility() == View.VISIBLE) {
                mLLTipViewLoading.setVisibility(View.GONE);
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
