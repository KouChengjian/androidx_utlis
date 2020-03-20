package com.yiciyuan.apt.processor;


import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.yiciyuan.annotation.apt.ApiFactory;
import com.yiciyuan.annotation.apt.ApiParams;
import com.yiciyuan.annotation.enums.ApiRequestType;
import com.yiciyuan.annotation.enums.ApiResponseType;
import com.yiciyuan.apt.BaseProcessor;
import com.yiciyuan.apt.helper.ApiFactoryModel;
import com.yiciyuan.apt.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
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
//        try {
//            printMessage(Diagnostic.Kind.NOTE, Utils.getPackageName(mElements, element), "");
//        } catch (Exception e) {
//
//        }
        for (Element childElement : element.getEnclosedElements()) {
            ApiFactoryModel apiFactoryModel = new ApiFactoryModel();
            apiFactoryModel.setElement(element);
            apiFactoryModel.setAnnotationValue(annotation.value());
            apiFactoryModel.setChildElement(childElement);
//            ExecutableElement executableElement = (ExecutableElement) childElement;
//            printMessage(Diagnostic.Kind.NOTE, TypeName.get(executableElement.getReturnType()).toString(), "");
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

    private ParameterizedTypeName getParameterized(ParameterizedTypeName parameterizedTypeName, ClassName[] classNames) {
        if (parameterizedTypeName == null) {
            ClassName className2 = classNames[classNames.length - 2];
            ClassName className1 = classNames[classNames.length - 1];
            ClassName[] classs = new ClassName[classNames.length - 2];
            ParameterizedTypeName parameterized = ParameterizedTypeName.get(className2, className1);
            for (int j = 0; j < classNames.length - 2; j++) {
                classs[j] = classNames[j];
            }
            return getParameterized(parameterized, classs);
        } else {
            if (classNames.length != 0) {
                ClassName className = classNames[classNames.length - 1];
                ParameterizedTypeName parameterized = ParameterizedTypeName.get(className, parameterizedTypeName);
                ClassName[] classs = new ClassName[classNames.length - 1];
                for (int j = 0; j < classNames.length - 1; j++) {
                    classs[j] = classNames[j];
                }
                return getParameterized(parameterized, classs);
            }
            return parameterizedTypeName;
        }
    }

    private ParameterizedTypeName getHttpResultReturn(String returnType) {
        returnType = returnType.replace(">", "");
        String[] returnTypes = returnType.split("<");
        ClassName classNameHeader = null;
        ClassName[] classNames = new ClassName[returnTypes.length - 2];
        for (int i = 0; i < returnTypes.length; i++) {
            String re = returnTypes[i];
            if (re.contains("HttpResult")) {
                continue;
            }
            ClassName className = Utils.getType(re);
            if (i == 0) {
                classNameHeader = className;
            } else {
                classNames[i - 2] = className;
            }
        }
        if (classNames.length == 1) {
            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(classNameHeader, classNames);
            return parameterizedTypeName;
        } else {
            ParameterizedTypeName parameterized = getParameterized(null, classNames);
            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(classNameHeader, parameterized);
            return parameterizedTypeName;
        }
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

            // 请求参数
            String paramsString = "";
            ApiParams apiParams = childElement.getAnnotation(ApiParams.class);
            if (apiParams != null) {
                List<String> paramNames = Arrays.asList(apiParams.paramName());
                if (paramNames.size() != 0) {
                    List<? extends TypeMirror> paramTypes = new ArrayList<>();
                    try {
                        apiParams.paramType();
                    } catch (MirroredTypesException mte) {
                        paramTypes = mte.getTypeMirrors();
                    }

                    for (int i = 0; i < paramNames.size(); i++) {
                        String paramName = paramNames.get(i);
                        paramsString += paramName + ",";
                        if (paramNames.size() == paramTypes.size()) {
                            String paramType = paramTypes.get(i).toString();
                            methodBuilder.addParameter(Utils.getType(paramType), paramName);
                        } else if (paramTypes.size() == 1) {
                            String paramType = paramTypes.get(0).toString();
                            methodBuilder.addParameter(Utils.getType(paramType), paramName);
                        } else {
                            methodBuilder.addParameter(Utils.getType("java.lang.String"), paramName);
                        }
                    }
                } else {
                    for (VariableElement ep : executableElement.getParameters()) {
                        methodBuilder.addParameter(TypeName.get(ep.asType()), ep.getSimpleName().toString());
                        paramsString += ep.getSimpleName().toString() + ",";
                    }
                }

            } else {
                for (VariableElement ep : executableElement.getParameters()) {
                    methodBuilder.addParameter(TypeName.get(ep.asType()), ep.getSimpleName().toString());
                    paramsString += ep.getSimpleName().toString() + ",";
                }
            }

            // 返回内容
            String returnType = TypeName.get(executableElement.getReturnType()).toString();
            if (returnType.contains("HttpResult")) {
                methodBuilder.returns(getHttpResultReturn(returnType));
            } else {
                if (apiParams != null) {
                    ClassName className = apiParams.response() == ApiResponseType.JSONObject ?
                            ClassName.get("org.json", "JSONObject") :
                            ClassName.get("org.json", "JSONArray");
                    ParameterizedTypeName typeName = ParameterizedTypeName.get(ClassName.get("io.reactivex", "Single"), className);
                    methodBuilder.returns(typeName);
                } else {
                    methodBuilder.returns(TypeName.get(executableElement.getReturnType()));
                }
            }

            // 执行函数
            if (apiParams != null) {
                if (apiParams.request() == ApiRequestType.APPLICATIONJSON) {
                    methodBuilder.addStatement("$T<String, Object> params = new $T<>()", Map.class, HashMap.class);
                    List<String> paramsList = Arrays.asList(paramsString.split(","));
                    for (String str : paramsList) {
                        if (str != null && str != "") {
                            methodBuilder.addStatement("params.put(\"" + str + "\"," + str + ")");
                        }
                    }
                    methodBuilder.addStatement("$T requestBody = ApiHelper.getInstance().createRequestBody(params)", Utils.getType("okhttp3.RequestBody"));
                }

                if (apiParams.response() == ApiResponseType.NONE) {
                    methodBuilder.addStatement(
                            "return $T.getInstance()" +
                                    ".get$L().$L($L)" +
                                    ".compose(new $T<>())" +
                                    ".map($T::get)"
                            , ClassName.get(apiFactoryModel.getAnnotationValue() + ".net", "ApiHelper")
                            , apiFactoryModel.getElement().getSimpleName().toString()
                            , apiFactoryModel.getChildElement().getSimpleName().toString()
                            , paramsString.substring(0, paramsString.length() - 1)
                            , ClassName.get(apiFactoryModel.getAnnotationValue() + ".net.transformer", "ResultTransformer")
                            , ClassName.get(apiFactoryModel.getAnnotationValue() + ".net.result", "Taker"));
                } else {
                    methodBuilder.addStatement(
                            "return $T.getInstance()" +
                                    ".get$L().$L($L)" +
                                    ".compose(new $T<>())" +
                                    ".map($T::get)"
                            , ClassName.get(apiFactoryModel.getAnnotationValue() + ".net", "ApiHelper")
                            , apiFactoryModel.getElement().getSimpleName().toString()
                            , apiFactoryModel.getChildElement().getSimpleName().toString()
                            , apiParams.request() == ApiRequestType.APPLICATIONJSON ?
                                    "requestBody" :
                                    paramsString.substring(0, paramsString.length() - 1)
                            , apiParams.response() == ApiResponseType.JSONObject ?
                                    ClassName.get(apiFactoryModel.getAnnotationValue() + ".net.transformer", "ResultJsonTransformer") :
                                    ClassName.get(apiFactoryModel.getAnnotationValue() + ".net.transformer", "ResultJsonListTransformer")
                            , ClassName.get(apiFactoryModel.getAnnotationValue() + ".net.result", "Taker"));

                }
            } else {
                methodBuilder.addStatement(
                        "return $T.getInstance()" +
                                ".get$L().$L($L)" +
                                ".compose(new $T<>())" +
                                ".map($T::get)"
                        , ClassName.get(apiFactoryModel.getAnnotationValue() + ".net", "ApiHelper")
                        , apiFactoryModel.getElement().getSimpleName().toString()
                        , apiFactoryModel.getChildElement().getSimpleName().toString()
                        , paramsString.substring(0, paramsString.length() - 1)
                        , ClassName.get(apiFactoryModel.getAnnotationValue() + ".net.transformer", "ResultTransformer")
                        , ClassName.get(apiFactoryModel.getAnnotationValue() + ".net.result", "Taker"));
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
