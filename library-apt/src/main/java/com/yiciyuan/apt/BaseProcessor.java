package com.yiciyuan.apt;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;

public abstract class BaseProcessor<T extends Annotation> extends AbstractProcessor {

    protected Filer mFiler; //文件相关的辅助类
    protected Elements mElements; //元素相关的辅助类
    protected final Class<T> mSupportedAnnotation;

    protected void printMessage(Kind kind, String format, Object... arguments) {
        String message = String.format(format, arguments);
        processingEnv.getMessager().printMessage(kind, message);
    }

    protected void printMessage(Kind kind, Element element, String format, Object... arguments) {
        String message = String.format(format, arguments);
        processingEnv.getMessager().printMessage(kind, message, element);
    }

    protected void printMessage(Kind kind, Element element, AnnotationMirror annotation, String format, Object... arguments) {
        String message = String.format(format, arguments);
        processingEnv.getMessager().printMessage(kind, message, element, annotation);
    }

    protected void printMessage(Kind kind, Element element, AnnotationMirror annotation, AnnotationValue annotationValue, String format, Object... arguments) {
        String message = String.format(format, arguments);
        processingEnv.getMessager().printMessage(kind, message, element, annotation, annotationValue);
    }

    public BaseProcessor(Class<T> supportedAnnotation) {
        mSupportedAnnotation = supportedAnnotation;
    }

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElements = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {
        if (roundEnvironment.processingOver()) {
            return false;
        }
        printMessage(Kind.NOTE, " %s START -------------------------",mSupportedAnnotation.getName());
        Set<TypeElement> allTypeElements = new HashSet<>();
        for (Iterator<? extends TypeElement> iterator = annotations.iterator(); iterator.hasNext(); ) {
            Set<? extends Element> set = roundEnvironment.getElementsAnnotatedWith(iterator.next());
            allTypeElements.addAll(ElementFilter.typesIn(set));
        }
        for (Iterator<TypeElement> iterator = allTypeElements.iterator(); iterator.hasNext(); ) {
            TypeElement element = iterator.next();
            try {
                T annotation = element.getAnnotation(mSupportedAnnotation);
                handleEach(element, annotation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boolean isEnd = true;
        try {
            isEnd = processComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isEnd;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedAnnotations = new HashSet<>();
        supportedAnnotations.add(mSupportedAnnotation.getName());
        return supportedAnnotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    protected abstract void handleEach(TypeElement element, T annotation);

    protected abstract boolean processComplete();
}