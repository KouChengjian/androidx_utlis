package com.yiciyuan.apt.helper;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class ApiFactoryModel {

    private TypeElement element; // com.example.kernel.net.api.UserApi
    private String annotationValue;
    private Element childElement;

    public TypeElement getElement() {
        return element;
    }

    public void setElement(TypeElement element) {
        this.element = element;
    }

    public String getAnnotationValue() {
        return annotationValue;
    }

    public void setAnnotationValue(String annotationValue) {
        this.annotationValue = annotationValue;
    }

    public Element getChildElement() {
        return childElement;
    }

    public void setChildElement(Element childElement) {
        this.childElement = childElement;
    }
}
