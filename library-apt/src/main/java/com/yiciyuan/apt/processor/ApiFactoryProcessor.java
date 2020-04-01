package com.yiciyuan.apt.processor;


import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.yiciyuan.annotation.apt.ApiFactory;
import com.yiciyuan.annotation.apt.ApiParams;
import com.yiciyuan.annotation.enums.ApiRequestType;
import com.yiciyuan.annotation.enums.ApiResponseType;
import com.yiciyuan.apt.BaseProcessor;
import com.yiciyuan.apt.helper.ApiConfigModel;
import com.yiciyuan.apt.helper.ApiFactoryModel;
import com.yiciyuan.apt.utils.Utils;

import java.io.File;
import java.io.Serializable;
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
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

@AutoService(Processor.class)//自动生成 javax.annotation.processing.IProcessor 文件
@SupportedSourceVersion(SourceVersion.RELEASE_8)//java版本支持
public class ApiFactoryProcessor extends BaseProcessor<ApiFactory> {

    private static final Class<ApiFactory> ApiFactory = ApiFactory.class;
    private List<ClassName> mList = new ArrayList<>();
    private List<ApiFactoryModel> mApiFactoryModels = new ArrayList<>();
    private boolean mIsNewCode = false;

    private ApiConfigModel mApiConfigModel = new ApiConfigModel();

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
//            ExecutableElement executableElement = (ExecutableElement) childElement;
//            printMessage(Diagnostic.Kind.NOTE, executableElement.getReturnType().toString() + " " + executableElement.toString(), "");
//            printMessage(Diagnostic.Kind.NOTE, executableElement.getReturnType().toString(), "");
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
        ClassName JSONObject = ClassName.get("org.json", "JSONObject");
        ClassName JSONArray = ClassName.get("org.json", "JSONArray");
        ClassName Single = ClassName.get("io.reactivex", "Single");

        ClassName ApiHelper = ClassName.get(mApiFactoryModels.get(0).getAnnotationValue() + ".net", "ApiHelper");

        ClassName ResultTransformer = ClassName.get(Utils.PackageName + ".net.transformer", "ResultTransformer");
        ClassName ResultJsonTransformer = ClassName.get(Utils.PackageName + ".net.transformer", "ResultJsonTransformer");
        ClassName ResultJsonListTransformer = ClassName.get(Utils.PackageName + ".net.transformer", "ResultJsonListTransformer");

        ClassName Taker = ClassName.get(Utils.PackageName + ".net.result", "Taker");

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
                    ClassName className = apiParams.response() == ApiResponseType.JSONObject ? JSONObject : JSONArray;
                    ParameterizedTypeName typeName = ParameterizedTypeName.get(Single, className);
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
            }
            paramsString = paramsString == "" ? "" : paramsString.substring(0, paramsString.length() - 1);
            methodBuilder.addStatement(
                    "return $T.getInstance()" +
                            ".get$L().$L($L)" +
                            ".compose(new $T<>())" +
                            ".map($T::get)"
                    , ApiHelper
                    , apiFactoryModel.getElement().getSimpleName().toString()
                    , apiFactoryModel.getChildElement().getSimpleName().toString()
                    , apiParams == null ? paramsString : apiParams.request() == ApiRequestType.APPLICATIONJSON ? "requestBody" : paramsString
                    , apiParams == null ? ResultTransformer : apiParams.response() == ApiResponseType.JSONObject ? ResultJsonTransformer : apiParams.response() == ApiResponseType.JSONArray ? ResultJsonListTransformer : ResultTransformer
                    , Taker);

            tb.addMethod(methodBuilder.build());
        }

        JavaFile javaFile = JavaFile.builder(Utils.PackageName + ".net", tb.build()).build();// 生成源代码
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

    private void createResultJsonListTransformer() {
        ClassName singleTransformer = ClassName.get("io.reactivex", "SingleTransformer");
        ClassName singleSource = ClassName.get("io.reactivex", "SingleSource");
        ClassName single = ClassName.get("io.reactivex", "Single");
        ClassName JSONObject = ClassName.get("org.json", "JSONObject");
        ClassName JSONArray = ClassName.get("org.json", "JSONArray");
        ClassName ResponseBody = ClassName.get("okhttp3", "ResponseBody");
        ClassName httpResult = ClassName.get(Utils.PackageName + ".net.result", "HttpResult");
        ClassName taker = ClassName.get(Utils.PackageName + ".net.result", "Taker");
        ClassName jsonParse = ClassName.get(Utils.PackageName + ".net.result", "JsonParse");
        ClassName apiException = ClassName.get(Utils.PackageName + ".net.exception", "ApiException");
        ClassName resultCode = ClassName.get(Utils.PackageName + ".net.exception", "ResultCode");
        ParameterizedTypeName parameterizedHttpResult = ParameterizedTypeName.get(httpResult, ResponseBody);
        ParameterizedTypeName parameterizedTaker = ParameterizedTypeName.get(taker, JSONArray);
        // SingleTransformer<HttpResult<T>, Taker<T>>
        ParameterizedTypeName parameterized = ParameterizedTypeName.get(singleTransformer, ResponseBody, parameterizedTaker);
        // SingleSource<Taker<JSONObject>>
        ParameterizedTypeName parameterizedSingleSource = ParameterizedTypeName.get(singleSource, parameterizedTaker);
        // Single<HttpResult<T>>
        ParameterizedTypeName parameterizedSingle = ParameterizedTypeName.get(single, ResponseBody);


        String CLASS_NAME = "ResultJsonListTransformer";
        TypeSpec.Builder tb = classBuilder(CLASS_NAME)
                .addModifiers(PUBLIC)
                .addTypeVariable(TypeVariableName.get("T"))
                .addSuperinterface(parameterized)
                .addJavadoc("@ 全局路由器 此类由apt自动生成");

        MethodSpec.Builder applyMethodBuilder = MethodSpec.methodBuilder("apply")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC)
                .addAnnotation(Override.class)
                .returns(parameterizedSingleSource)
                .addParameter(parameterizedSingle, "upstream")
                .addStatement(
                        "return upstream"
                                + ".subscribeOn($T.io())"
                                + ".flatMap(($T<ResponseBody, SingleSource<Taker<JSONArray>>>) result -> {"
                                + "$T jsonObject = $T.createJSONObject(result.string());"
                                + "int code = JsonParse.getInt(jsonObject ,$S);"
                                + "String msg = JsonParse.getString(jsonObject ,$S);"
                                + "if (code == $T.SUCCESS) {"
                                + "JSONArray data = JsonParse.getJSONArray(jsonObject , $S);"
                                + "return Single.just(Taker.ofNullable(data));"
                                + "} else {"
                                + "return Single.error(new $T(code, msg));"
                                + "}"
                                + "})"
                                + ".observeOn($T.mainThread())"
                        , ClassName.get("io.reactivex.schedulers", "Schedulers")
                        , ClassName.get("io.reactivex.functions", "Function")
                        , JSONObject
                        , jsonParse
                        , mApiConfigModel.getResultCode()
                        , mApiConfigModel.getResultMsg()
                        , resultCode
                        , mApiConfigModel.getResultData()
                        , apiException
                        , ClassName.get("io.reactivex.android.schedulers", "AndroidSchedulers")
                );
        tb.addMethod(applyMethodBuilder.build());

        JavaFile javaFile = JavaFile.builder(Utils.PackageName + ".net.transformer", tb.build()).build();// 生成源代码
        createFile(javaFile);
    }

    private void createResultJsonTransformer() {
        ClassName singleTransformer = ClassName.get("io.reactivex", "SingleTransformer");
        ClassName singleSource = ClassName.get("io.reactivex", "SingleSource");
        ClassName single = ClassName.get("io.reactivex", "Single");
        ClassName JSONObject = ClassName.get("org.json", "JSONObject");
        ClassName ResponseBody = ClassName.get("okhttp3", "ResponseBody");
        ClassName httpResult = ClassName.get(Utils.PackageName + ".net.result", "HttpResult");
        ClassName taker = ClassName.get(Utils.PackageName + ".net.result", "Taker");
        ClassName jsonParse = ClassName.get(Utils.PackageName + ".net.result", "JsonParse");
        ClassName apiException = ClassName.get(Utils.PackageName + ".net.exception", "ApiException");
        ClassName resultCode = ClassName.get(Utils.PackageName + ".net.exception", "ResultCode");
        ParameterizedTypeName parameterizedHttpResult = ParameterizedTypeName.get(httpResult, ResponseBody);
        ParameterizedTypeName parameterizedTaker = ParameterizedTypeName.get(taker, JSONObject);
        // SingleTransformer<HttpResult<T>, Taker<T>>
        ParameterizedTypeName parameterized = ParameterizedTypeName.get(singleTransformer, ResponseBody, parameterizedTaker);
        // SingleSource<Taker<JSONObject>>
        ParameterizedTypeName parameterizedSingleSource = ParameterizedTypeName.get(singleSource, parameterizedTaker);
        // Single<HttpResult<T>>
        ParameterizedTypeName parameterizedSingle = ParameterizedTypeName.get(single, ResponseBody);


        String CLASS_NAME = "ResultJsonTransformer";
        TypeSpec.Builder tb = classBuilder(CLASS_NAME)
                .addModifiers(PUBLIC)
                .addTypeVariable(TypeVariableName.get("T"))
                .addSuperinterface(parameterized)
                .addJavadoc("@ 全局路由器 此类由apt自动生成");

        MethodSpec.Builder applyMethodBuilder = MethodSpec.methodBuilder("apply")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC)
                .addAnnotation(Override.class)
                .returns(parameterizedSingleSource)
                .addParameter(parameterizedSingle, "upstream")
                .addStatement(
                        "return upstream"
                                + ".subscribeOn($T.io())"
                                + ".flatMap(($T<ResponseBody, SingleSource<Taker<JSONObject>>>) result -> {"
                                + "JSONObject jsonObject = $T.createJSONObject(result.string());"
                                + "int code = JsonParse.getInt(jsonObject ,$S);"
                                + "String msg = JsonParse.getString(jsonObject ,$S);"
                                + "if (code == $T.SUCCESS) {"
                                + "JSONObject data = JsonParse.getJSONObject(jsonObject , $S);"
                                + "return Single.just(Taker.ofNullable(data));"
                                + "} else {"
                                + "return Single.error(new $T(code, msg));"
                                + "}"
                                + "})"
                                + ".observeOn($T.mainThread())"
                        , ClassName.get("io.reactivex.schedulers", "Schedulers")
                        , ClassName.get("io.reactivex.functions", "Function")
                        , jsonParse
                        , mApiConfigModel.getResultCode()
                        , mApiConfigModel.getResultMsg()
                        , resultCode
                        , mApiConfigModel.getResultData()
                        , apiException
                        , ClassName.get("io.reactivex.android.schedulers", "AndroidSchedulers")
                );
        tb.addMethod(applyMethodBuilder.build());

        JavaFile javaFile = JavaFile.builder(Utils.PackageName + ".net.transformer", tb.build()).build();// 生成源代码
        createFile(javaFile);
    }

    private void createResultTransformer() {
        ClassName singleTransformer = ClassName.get("io.reactivex", "SingleTransformer");
        ClassName singleSource = ClassName.get("io.reactivex", "SingleSource");
        ClassName single = ClassName.get("io.reactivex", "Single");
        ClassName httpResult = ClassName.get(Utils.PackageName + ".net.result", "HttpResult");
        ClassName taker = ClassName.get(Utils.PackageName + ".net.result", "Taker");
        ClassName apiException = ClassName.get(Utils.PackageName + ".net.exception", "ApiException");
        ClassName resultCode = ClassName.get(Utils.PackageName + ".net.exception", "ResultCode");
        ParameterizedTypeName parameterizedHttpResult = ParameterizedTypeName.get(httpResult, TypeVariableName.get("T"));
        ParameterizedTypeName parameterizedTaker = ParameterizedTypeName.get(taker, TypeVariableName.get("T"));
        // SingleTransformer<HttpResult<T>, Taker<T>>
        ParameterizedTypeName parameterized = ParameterizedTypeName.get(singleTransformer, parameterizedHttpResult, parameterizedTaker);
        // SingleSource<Taker<T>>
        ParameterizedTypeName parameterizedSingleSource = ParameterizedTypeName.get(singleSource, parameterizedTaker);
        // Single<HttpResult<T>>
        ParameterizedTypeName parameterizedSingle = ParameterizedTypeName.get(single, parameterizedHttpResult);


        String CLASS_NAME = "ResultTransformer";
        TypeSpec.Builder tb = classBuilder(CLASS_NAME)
                .addModifiers(PUBLIC)
                .addTypeVariable(TypeVariableName.get("T"))
                .addSuperinterface(parameterized)
                .addJavadoc("@ 全局路由器 此类由apt自动生成");

        MethodSpec.Builder applyMethodBuilder = MethodSpec.methodBuilder("apply")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC)
                .addAnnotation(Override.class)
                .returns(parameterizedSingleSource)
                .addParameter(parameterizedSingle, "upstream")
                .addStatement(
                        "return upstream"
                                + ".subscribeOn($T.io())"
                                + ".flatMap(($T<HttpResult<T>, SingleSource<Taker<T>>>) result -> {"
                                + "int code = result." + mApiConfigModel.getResultCode() + ";"
                                + "if (code == $T.SUCCESS) {"
                                + "return Single.just(Taker.ofNullable(result." + mApiConfigModel.getResultData() + "));"
                                + "} else {"
                                + "return Single.error(new $T(result." + mApiConfigModel.getResultCode() + ",  result." + mApiConfigModel.getResultMsg() + "));"
                                + "}"
                                + "})"
                                + ".observeOn($T.mainThread())"
                        , ClassName.get("io.reactivex.schedulers", "Schedulers")
                        , ClassName.get("io.reactivex.functions", "Function")
                        , resultCode
                        , apiException
                        , ClassName.get("io.reactivex.android.schedulers", "AndroidSchedulers")
                );
        tb.addMethod(applyMethodBuilder.build());

        JavaFile javaFile = JavaFile.builder(Utils.PackageName + ".net.transformer", tb.build()).build();// 生成源代码
        createFile(javaFile);
    }

    private void createResultCode() {
        String CLASS_NAME = "ResultCode";
        TypeSpec.Builder tb = classBuilder(CLASS_NAME)
                .addModifiers(PUBLIC)
                .addJavadoc("@ 全局路由器 此类由apt自动生成");

        FieldSpec codeField = FieldSpec.builder(int.class, "SUCCESS")
                .addModifiers(PUBLIC, STATIC)
                .initializer(mApiConfigModel.getResultSucceed() + "")
                .build();
        tb.addField(codeField);

        JavaFile javaFile = JavaFile.builder(Utils.PackageName + ".net.exception", tb.build()).build();// 生成源代码
        createFile(javaFile);
    }

    private void createApiException() {
        String CLASS_NAME = "ApiException";
        TypeSpec.Builder tb = classBuilder(CLASS_NAME)
                .addModifiers(PUBLIC)
                .superclass(RuntimeException.class)
                .addJavadoc("@ 全局路由器 此类由apt自动生成");

        FieldSpec codeField = FieldSpec.builder(int.class, "code")
                .addModifiers(PUBLIC)
                .build();
        tb.addField(codeField);

        FieldSpec messageField = FieldSpec.builder(String.class, "message")
                .addModifiers(PUBLIC)
                .build();
        tb.addField(messageField);

        MethodSpec.Builder initMethodBuilder = MethodSpec.constructorBuilder()
                .addModifiers(PUBLIC)
                .addParameter(int.class, "code")
                .addParameter(String.class, "message")
                .addStatement("super(message)")
                .addStatement("this.code = code")
                .addStatement("this.message = message");
        tb.addMethod(initMethodBuilder.build());

        MethodSpec.Builder getCodeMethodBuilder = MethodSpec.methodBuilder("getCode")
                .addModifiers(PUBLIC)
                .returns(int.class)
                .addStatement("return code");
        tb.addMethod(getCodeMethodBuilder.build());

        MethodSpec.Builder getMessageMethodBuilder = MethodSpec.methodBuilder("getMessage")
                .addModifiers(PUBLIC)
                .addAnnotation(Override.class)
                .returns(String.class)
                .addStatement("return message");
        tb.addMethod(getMessageMethodBuilder.build());

        JavaFile javaFile = JavaFile.builder(Utils.PackageName + ".net.exception", tb.build()).build();// 生成源代码
        createFile(javaFile);
    }

    private void createJsonParse() {
        ClassName Gson = ClassName.get("com.google.gson", "Gson");
        ClassName JsonParser = ClassName.get("com.google.gson", "JsonParser");
        ClassName JsonArray = ClassName.get("com.google.gson", "JsonArray");
        ClassName JsonElement = ClassName.get("com.google.gson", "JsonElement");

        ClassName JSONObject = ClassName.get("org.json", "JSONObject");
        ClassName JSONArray = ClassName.get("org.json", "JSONArray");

        ClassName Class = ClassName.get("java.lang", "Class");
        ClassName Exception = ClassName.get("java.lang", "Exception");
        ClassName List = ClassName.get("java.util", "List");
        ClassName ArrayList = ClassName.get("java.util", "ArrayList");

        ParameterizedTypeName parameterizedClass = ParameterizedTypeName.get(Class, TypeVariableName.get("T"));

        String CLASS_NAME = "JsonParse";
        TypeSpec.Builder tb = classBuilder(CLASS_NAME)
                .addModifiers(PUBLIC, FINAL)
                .addJavadoc("@ 全局路由器 此类由apt自动生成");

        FieldSpec valueField = FieldSpec.builder(Gson, "gson")
                .addModifiers(PRIVATE, STATIC)
                .initializer("new $T()", Gson)
                .build();
        tb.addField(valueField);

        MethodSpec.Builder fromJsonMethodBuilder = MethodSpec.methodBuilder("fromJson")
                .addJavadoc("@此方法由apt自动生成")
                .returns(TypeVariableName.get("<T> T"))
                .addParameter(String.class, "content")
                .addParameter(parameterizedClass, "clazz")
                .addModifiers(PUBLIC, STATIC)
                .beginControlFlow("if (content.isEmpty() || clazz == null)")
                .addStatement("return null")
                .endControlFlow()
                .beginControlFlow("try")
                .addStatement("return gson.fromJson(content, clazz)")
                .nextControlFlow("catch ($T e)", Exception)
                .addStatement("return null")
                .endControlFlow();
        tb.addMethod(fromJsonMethodBuilder.build());

        MethodSpec.Builder fromListJsonMethodBuilder = MethodSpec.methodBuilder("fromListJson")
                .addJavadoc("@此方法由apt自动生成")
                .returns(TypeVariableName.get("<T> List<T>"))
                .addParameter(String.class, "string")
                .addParameter(parameterizedClass, "T")
                .addModifiers(PUBLIC, STATIC)
                .beginControlFlow("try")
                .addStatement("$T array = new $T().parse(string).getAsJsonArray()", JsonArray, JsonParser)
                .addStatement("return fromListJson(array, T)")
                .nextControlFlow("catch ($T e)", Exception)
                .addStatement("return null")
                .endControlFlow();
        tb.addMethod(fromListJsonMethodBuilder.build());

        MethodSpec.Builder fromJsonMethodBuilder1 = MethodSpec.methodBuilder("fromListJson")
                .addJavadoc("@此方法由apt自动生成")
                .returns(TypeVariableName.get("<T> List<T>"))
                .addParameter(JsonArray, "array")
                .addParameter(parameterizedClass, "T")
                .addModifiers(PUBLIC, STATIC)
                .beginControlFlow("try")
                .addStatement("$T<T> lst = new $T<>()", List, ArrayList)
                .beginControlFlow("for (final $T element : array)", JsonElement)
                .addStatement("lst.add(gson.fromJson(element, T))")
                .endControlFlow()
                .addStatement("return lst")
                .nextControlFlow("catch ($T e)", Exception)
                .addStatement("return null")
                .endControlFlow();
        tb.addMethod(fromJsonMethodBuilder1.build());

        MethodSpec.Builder getStringMethodBuilder1 = MethodSpec.methodBuilder("getString")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC, STATIC)
                .returns(String.class)
                .addParameter(JSONObject, "json")
                .addParameter(String.class, "key")
                .beginControlFlow("try")
                .beginControlFlow("if (!json.isNull(key))")
                .addStatement("String s = json.getString(key)")
                .beginControlFlow("if (s == null)")
                .addStatement("return \"\"")
                .endControlFlow()
                .addStatement("return s")
                .endControlFlow()
                .addStatement("return \"\"")
                .nextControlFlow("catch ($T e)", Exception)
                .addStatement("return \"\"")
                .endControlFlow();
        tb.addMethod(getStringMethodBuilder1.build());

        MethodSpec.Builder getStringMethodBuilder2 = MethodSpec.methodBuilder("getString")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC, STATIC)
                .returns(String.class)
                .addParameter(JSONArray, "json")
                .addParameter(int.class, "position")
                .beginControlFlow("try")
                .addStatement("return json.getString(position)")
                .nextControlFlow("catch ($T e)", Exception)
                .addStatement("return \"\"")
                .endControlFlow();
        tb.addMethod(getStringMethodBuilder2.build());

        MethodSpec.Builder getIntMethodBuilder = MethodSpec.methodBuilder("getInt")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC, STATIC)
                .returns(int.class)
                .addParameter(JSONObject, "json")
                .addParameter(String.class, "key")
                .beginControlFlow("try")
                .beginControlFlow("if (!json.isNull(key))")
                .addStatement("return json.getInt(key)")
                .nextControlFlow("else")
                .addStatement("return 0")
                .endControlFlow()
                .nextControlFlow("catch ($T e)", Exception)
                .addStatement("return 0")
                .endControlFlow();
        tb.addMethod(getIntMethodBuilder.build());

        MethodSpec.Builder getLongMethodBuilder = MethodSpec.methodBuilder("getLong")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC, STATIC)
                .returns(long.class)
                .addParameter(JSONObject, "json")
                .addParameter(String.class, "key")
                .beginControlFlow("try")
                .beginControlFlow("if (!json.isNull(key))")
                .addStatement("return json.getLong(key)")
                .nextControlFlow("else")
                .addStatement("return 0")
                .endControlFlow()
                .nextControlFlow("catch ($T e)", Exception)
                .addStatement("return 0")
                .endControlFlow();
        tb.addMethod(getLongMethodBuilder.build());

        MethodSpec.Builder getDoubleMethodBuilder = MethodSpec.methodBuilder("getDouble")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC, STATIC)
                .returns(double.class)
                .addParameter(JSONObject, "json")
                .addParameter(String.class, "key")
                .beginControlFlow("try")
                .beginControlFlow("if (!json.isNull(key))")
                .addStatement("return json.getDouble(key)")
                .nextControlFlow("else")
                .addStatement("return 0")
                .endControlFlow()
                .nextControlFlow("catch ($T e)", Exception)
                .addStatement("return 0")
                .endControlFlow();
        tb.addMethod(getDoubleMethodBuilder.build());

        MethodSpec.Builder getJSONObjectMethodBuilder = MethodSpec.methodBuilder("getJSONObject")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC, STATIC)
                .returns(JSONObject)
                .addParameter(JSONObject, "json")
                .addParameter(String.class, "key")
                .beginControlFlow("try")
                .beginControlFlow("if (!json.isNull(key))")
                .addStatement("return json.getJSONObject(key)")
                .nextControlFlow("else")
                .addStatement("return new JSONObject()")
                .endControlFlow()
                .nextControlFlow("catch ($T e)", Exception)
                .addStatement("return new JSONObject()")
                .endControlFlow();
        tb.addMethod(getJSONObjectMethodBuilder.build());

        MethodSpec.Builder getJSONObjectMethodBuilder2 = MethodSpec.methodBuilder("getJSONObject")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC, STATIC)
                .returns(JSONObject)
                .addParameter(JSONArray, "array")
                .addParameter(int.class, "position")
                .beginControlFlow("try")
                .addStatement("return array.getJSONObject(position)")
                .nextControlFlow("catch ($T e)", Exception)
                .addStatement("return new JSONObject()")
                .endControlFlow();
        tb.addMethod(getJSONObjectMethodBuilder2.build());

        MethodSpec.Builder getJSONArrayMethodBuilder = MethodSpec.methodBuilder("getJSONArray")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC, STATIC)
                .returns(JSONArray)
                .addParameter(JSONObject, "json")
                .addParameter(String.class, "key")
                .beginControlFlow("try")
                .beginControlFlow("if (!json.isNull(key))")
                .addStatement("return json.getJSONArray(key)")
                .nextControlFlow("else")
                .addStatement("return new JSONArray()")
                .endControlFlow()
                .nextControlFlow("catch ($T e)", Exception)
                .addStatement("return new JSONArray()")
                .endControlFlow();
        tb.addMethod(getJSONArrayMethodBuilder.build());

        MethodSpec.Builder createJSONObjectMethodBuilder = MethodSpec.methodBuilder("createJSONObject")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC, STATIC)
                .returns(JSONObject)
                .addParameter(String.class, "str")
                .beginControlFlow("try")
                .addStatement("return new JSONObject(str)")
                .nextControlFlow("catch ($T e)", Exception)
                .addStatement("return new JSONObject()")
                .endControlFlow();
        tb.addMethod(createJSONObjectMethodBuilder.build());

        MethodSpec.Builder createJSONArrayMethodBuilder = MethodSpec.methodBuilder("createJSONArray")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC, STATIC)
                .returns(JSONArray)
                .addParameter(String.class, "str")
                .beginControlFlow("try")
                .addStatement("return new JSONArray(str)")
                .nextControlFlow("catch ($T e)", Exception)
                .addStatement("return new JSONArray()")
                .endControlFlow();
        tb.addMethod(createJSONArrayMethodBuilder.build());

        JavaFile javaFile = JavaFile.builder(Utils.PackageName + ".net.result", tb.build()).build();// 生成源代码
        createFile(javaFile);
    }

    private void createTaker() {
        String CLASS_NAME = "Taker";
        TypeSpec.Builder tb = classBuilder(CLASS_NAME)
                .addModifiers(PUBLIC, FINAL)
                .addTypeVariable(TypeVariableName.get("T"))
                .addJavadoc("@ 全局路由器 此类由apt自动生成");

        FieldSpec valueField = FieldSpec.builder(TypeVariableName.get("T"), "value")
                .addModifiers(PRIVATE)
                .build();
        tb.addField(valueField);

        ClassName taker = ClassName.get(Utils.PackageName + ".net.result", "Taker");
        FieldSpec takerField = FieldSpec.builder(taker, "EMPTY")
                .addModifiers(PRIVATE, STATIC, FINAL)
                .initializer("new $T()", taker)
                .build();
        tb.addField(takerField);

        MethodSpec.Builder initMethodBuilder = MethodSpec.constructorBuilder()
                .addModifiers(PRIVATE);
        initMethodBuilder.addStatement("this.value = null");
        tb.addMethod(initMethodBuilder.build());

        ClassName objects = ClassName.get("java.util", "Objects");
        MethodSpec.Builder initMethodBuilder2 = MethodSpec.constructorBuilder()
                .addModifiers(PRIVATE)
                .addParameter(TypeVariableName.get("T"), "value")
                .addStatement("this.value = $T.requireNonNull(value)", objects);
        tb.addMethod(initMethodBuilder2.build());

        MethodSpec.Builder emptyMethodBuilder = MethodSpec.methodBuilder("empty")
                .addJavadoc("@此方法由apt自动生成")
                .returns(TypeVariableName.get("<T> Taker<T>"))
                .addModifiers(PUBLIC, STATIC)
                .addStatement("Taker<T> t = (Taker<T>) EMPTY")
                .addStatement("return t");
        tb.addMethod(emptyMethodBuilder.build());

        MethodSpec.Builder ofMethodBuilder = MethodSpec.methodBuilder("of")
                .addJavadoc("@此方法由apt自动生成")
                .addParameter(TypeVariableName.get("T"), "value")
                .returns(TypeVariableName.get("<T> Taker<T>"))
                .addModifiers(PUBLIC, STATIC)
                .addStatement("return new Taker<>(value)");
        tb.addMethod(ofMethodBuilder.build());

        MethodSpec.Builder ofNullableMethodBuilder = MethodSpec.methodBuilder("ofNullable")
                .addJavadoc("@此方法由apt自动生成")
                .addParameter(TypeVariableName.get("T"), "value")
                .returns(TypeVariableName.get("<T> Taker<T>"))
                .addModifiers(PUBLIC, STATIC)
                .addStatement("return value == null ? (Taker<T>) empty() : of(value)");
        tb.addMethod(ofNullableMethodBuilder.build());

        MethodSpec.Builder getMethodBuilder = MethodSpec.methodBuilder("get")
                .addJavadoc("@此方法由apt自动生成")
                .returns(TypeVariableName.get("T"))
                .addModifiers(PUBLIC)
                .beginControlFlow("if (value == null)")
                .addStatement("throw new $T(\"No value present\")", ClassName.get("java.util", "NoSuchElementException"))
                .endControlFlow()
                .addStatement("return value");
        tb.addMethod(getMethodBuilder.build());

        MethodSpec.Builder isPresentMethodBuilder = MethodSpec.methodBuilder("isPresent")
                .addJavadoc("@此方法由apt自动生成")
                .returns(boolean.class)
                .addModifiers(PUBLIC)
                .addStatement("return value != null");
        tb.addMethod(isPresentMethodBuilder.build());

        JavaFile javaFile = JavaFile.builder(Utils.PackageName + ".net.result", tb.build()).build();// 生成源代码
        createFile(javaFile);
    }

    private void createHttpListResult() {
        String CLASS_NAME = "HttpListResult";
        TypeSpec.Builder tb = classBuilder(CLASS_NAME)
                .addModifiers(PUBLIC, FINAL)
                .addTypeVariable(TypeVariableName.get("T"))
                .addSuperinterface(Serializable.class)
                .addJavadoc("@ 全局路由器 此类由apt自动生成");

        FieldSpec resultCodeField = FieldSpec.builder(int.class, mApiConfigModel.getResultListTotal())
                .addModifiers(PUBLIC)
                .build();
        tb.addField(resultCodeField);

        FieldSpec resultMsgField = FieldSpec.builder(int.class, mApiConfigModel.getResultListPages())
                .addModifiers(PUBLIC)
                .build();
        tb.addField(resultMsgField);

        FieldSpec resultDataField = FieldSpec.builder(TypeVariableName.get("T"), mApiConfigModel.getResultListData())
                .addModifiers(PUBLIC)
                .build();
        tb.addField(resultDataField);

        JavaFile javaFile = JavaFile.builder(Utils.PackageName + ".net.result", tb.build()).build();// 生成源代码
        createFile(javaFile);
    }

    private void createHttpResult() {
        String CLASS_NAME = "HttpResult";
        TypeSpec.Builder tb = classBuilder(CLASS_NAME)
                .addModifiers(PUBLIC, FINAL)
                .addTypeVariable(TypeVariableName.get("T"))
                .addJavadoc("@ 全局路由器 此类由apt自动生成");

        FieldSpec resultCodeField = FieldSpec.builder(int.class, mApiConfigModel.getResultCode())
                .addModifiers(PUBLIC)
                .build();
        tb.addField(resultCodeField);

        FieldSpec resultMsgField = FieldSpec.builder(String.class, mApiConfigModel.getResultMsg())
                .addModifiers(PUBLIC)
                .build();
        tb.addField(resultMsgField);

        FieldSpec resultDataField = FieldSpec.builder(TypeVariableName.get("T"), mApiConfigModel.getResultData())
                .addModifiers(PUBLIC)
                .build();
        tb.addField(resultDataField);

        JavaFile javaFile = JavaFile.builder(Utils.PackageName + ".net.result", tb.build()).build();// 生成源代码
        createFile(javaFile);
    }

    private void createEmpty() {
        String CLASS_NAME = "Empty";
        TypeSpec.Builder tb = classBuilder(CLASS_NAME).addModifiers(PUBLIC).addJavadoc("@ 全局路由器 此类由apt自动生成");

        JavaFile javaFile = JavaFile.builder(Utils.PackageName + ".net.result", tb.build()).build();// 生成源代码
        createFile(javaFile);
    }

    private void createFile(JavaFile javaFile) {
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
