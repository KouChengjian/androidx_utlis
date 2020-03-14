package com.yiciyuan.apt.processor;


import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.yiciyuan.apt.BaseProcessor;
import com.yiciyuan.apt.annotation.ApiFactory;
import com.yiciyuan.apt.helper.ApiFactoryModel;
import com.yiciyuan.apt.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

@AutoService(Processor.class)//自动生成 javax.annotation.processing.IProcessor 文件
@SupportedSourceVersion(SourceVersion.RELEASE_8)//java版本支持
public class ApiFactoryProcessor extends BaseProcessor<ApiFactory> {

    private static final Class<ApiFactory> ApiFactory = ApiFactory.class;
    private List<ClassName> mList = new ArrayList<>();
    private List<ApiFactoryModel> mApiFactoryModels = new ArrayList<>();
    private boolean mIsNewCode = false;

    public ApiFactoryProcessor() {
        super(ApiFactory);
    }

    @Override
    protected void handleEach(TypeElement element, ApiFactory annotation) {
        ClassName currentType = ClassName.get(element);
        printMessage(Diagnostic.Kind.NOTE, currentType.toString(), "");
        if (mList.contains(currentType)) {
            return;
        }
        mIsNewCode = true;
        mList.add(currentType);
        for (Element childElement : element.getEnclosedElements()) {
            ApiFactoryModel apiFactoryModel = new ApiFactoryModel();
            apiFactoryModel.setElement(element);
            apiFactoryModel.setAnnotationValue(annotation.value());
            apiFactoryModel.setChildElement(childElement);
//            printMessage(Diagnostic.Kind.NOTE, childElement.toString(), "");
            ExecutableElement executableElement = (ExecutableElement) childElement;
            printMessage(Diagnostic.Kind.NOTE, TypeName.get(executableElement.getReturnType()).toString(), "");
//            for (AnnotationMirror annotationMirror :TypeName.get(executableElement.getReturnType()).getClass().getSimpleName()){
//                printMessage(Diagnostic.Kind.NOTE, annotationMirror.toString(), "");
//            }
            mApiFactoryModels.add(apiFactoryModel);
        }
    }

    @Override
    protected boolean processComplete() {
        if (mIsNewCode) {
            grenCode();
            return true;
        }
        return false;
    }

    private void grenCode() {
        String CLASS_NAME = "ApiFactory";
        TypeSpec.Builder tb = classBuilder(CLASS_NAME).addModifiers(PUBLIC, FINAL).addJavadoc("@ 全局路由器 此类由apt自动生成");

        for (ApiFactoryModel apiFactoryModel : mApiFactoryModels) {
            Element childElement = apiFactoryModel.getChildElement();
            ExecutableElement executableElement = (ExecutableElement) childElement;
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(childElement.getSimpleName().toString())
                    .addJavadoc("@此方法由apt自动生成")
                    .addModifiers(PUBLIC, STATIC);

            // 处理返回内容
            String returnType = TypeName.get(executableElement.getReturnType()).toString();
            if (returnType.contains("HttpResult")) {
//                executableElement.getReturnType()
            } else {
//                methodBuilder.returns(TypeName.get(executableElement.getReturnType()));
            }

            tb.addMethod(methodBuilder.build());
        }

        JavaFile javaFile = JavaFile.builder(Utils.PackageName, tb.build()).build();// 生成源代码

        try {
            FileObject cc = mFiler.getResource(StandardLocation.SOURCE_OUTPUT, "", javaFile.toJavaFileObject().toUri().toString());

            if (cc != null) {
                File file = new File(cc.getName());
                {
                    cc.delete();
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
            javaFile.writeTo(mFiler);// 在 app module/build/generated/source/apt 生成一份源代码
            mIsNewCode = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
