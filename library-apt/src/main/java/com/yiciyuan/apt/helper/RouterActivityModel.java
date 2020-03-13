package com.yiciyuan.apt.helper;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Created by baixiaokang on 16/12/30.
 */

public class RouterActivityModel {
    private boolean mIsNeedBind;//是否需要对目标类进行数据绑定，只有有传参数的和有转场需要初始化数据绑定
    private TypeElement mElement;//当前的Activity
    private String mActionName;//当前Activity的ActionName
    private String mSceneTransitionElementName;//转场元素View的名称
    private Element mSceneTransitionElement;//转场元素View的Element
    private List<Element> mExtraElements;//需要传递的参数的Extra元素Element列表
    private List<String> mExtraElementKeys;//需要传递的参数的Extra元素Element列表的Key列表

    public String getSceneTransitionElementName() {
        return mSceneTransitionElementName;
    }

    public void setSceneTransitionElementName(String sceneTransitionElementName) {
        mSceneTransitionElementName = sceneTransitionElementName;
    }

    public List<String> getExtraElementKeys() {
        return mExtraElementKeys;
    }

    public void setExtraElementKeys(List<String> extraElementKeys) {
        mExtraElementKeys = extraElementKeys;
    }

    public List<Element> getExtraElements() {
        return mExtraElements;
    }

    public void setExtraElements(List<Element> extraElements) {
        mExtraElements = extraElements;
    }

    public Element getSceneTransitionElement() {
        return mSceneTransitionElement;
    }

    public void setSceneTransitionElement(Element sceneTransitionElement) {
        mSceneTransitionElement = sceneTransitionElement;
    }

    public TypeElement getElement() {
        return mElement;
    }

    public void setElement(TypeElement mElement) {
        this.mElement = mElement;
    }

    public String getActionName() {
        return mActionName;
    }

    public void setActionName(String mActionName) {
        this.mActionName = mActionName;
    }

    public boolean isNeedBind() {
        return mIsNeedBind;
    }

    public void setNeedBind(boolean needBind) {
        mIsNeedBind = needBind;
    }
}
