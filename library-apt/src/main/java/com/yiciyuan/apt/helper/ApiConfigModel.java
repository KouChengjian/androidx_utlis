package com.yiciyuan.apt.helper;

import javax.lang.model.element.TypeElement;

public class ApiConfigModel {

    private TypeElement element; // com.example.kernel.net.api.UserApi
    private int resultSucceed = 200;
    private String resultCode = "Code";
    private String resultMsg = "Msg";
    private String resultData = "Data";
    private String resultListTotal = "Total";
    private String resultListPages = "Pages";
    private String resultListData = "Data";

    public TypeElement getElement() {
        return element;
    }

    public void setElement(TypeElement element) {
        this.element = element;
    }

    public int getResultSucceed() {
        return resultSucceed;
    }

    public void setResultSucceed(int resultSucceed) {
        this.resultSucceed = resultSucceed;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public String getResultListTotal() {
        return resultListTotal;
    }

    public void setResultListTotal(String resultListTotal) {
        this.resultListTotal = resultListTotal;
    }

    public String getResultListPages() {
        return resultListPages;
    }

    public void setResultListPages(String resultListPages) {
        this.resultListPages = resultListPages;
    }

    public String getResultListData() {
        return resultListData;
    }

    public void setResultListData(String resultListData) {
        this.resultListData = resultListData;
    }
}
