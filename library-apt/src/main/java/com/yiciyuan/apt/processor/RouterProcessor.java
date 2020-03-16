package com.yiciyuan.apt.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.yiciyuan.annotation.apt.Extra;
import com.yiciyuan.annotation.apt.Router;
import com.yiciyuan.annotation.apt.SceneTransition;
import com.yiciyuan.apt.BaseProcessor;
import com.yiciyuan.apt.helper.RouterActivityModel;
import com.yiciyuan.apt.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static javax.lang.model.element.Modifier.VOLATILE;


@AutoService(Processor.class)//自动生成 javax.annotation.processing.IProcessor 文件
@SupportedSourceVersion(SourceVersion.RELEASE_8)//java版本支持
public class RouterProcessor extends BaseProcessor<com.yiciyuan.annotation.apt.Router> {
    private static final Class<Router> Router = Router.class;
    private List<ClassName> mList = new ArrayList<>();
    private List<RouterActivityModel> mRouterActivityModels = new ArrayList<>();
    private boolean mIsNewCode = false;

    public RouterProcessor() {
        super(Router);
    }

    @Override
    protected void handleEach(TypeElement element, Router annotation) {
        ClassName currentType = ClassName.get(element);
        printMessage(Diagnostic.Kind.NOTE, currentType.toString(), "");
        if (mList.contains(currentType)) {
            return;
        }
        mIsNewCode = true;
        mList.add(currentType);
        RouterActivityModel routerActivityModel = new RouterActivityModel();
        routerActivityModel.setElement(element);
        routerActivityModel.setActionName(annotation.value());
        List<Element> mExtraElements = new ArrayList<>();
        List<String> mExtraElementKeys = new ArrayList<>();
        for (Element childElement : element.getEnclosedElements()) {
            SceneTransition mSceneTransitionAnnotation = childElement.getAnnotation(SceneTransition.class);
            if (mSceneTransitionAnnotation != null) {
                routerActivityModel.setSceneTransitionElementName(mSceneTransitionAnnotation.value());
                routerActivityModel.setSceneTransitionElement(childElement);
            }
            Extra mExtraAnnotation = childElement.getAnnotation(Extra.class);
            if (mExtraAnnotation != null) {
                mExtraElementKeys.add(mExtraAnnotation.value());
                mExtraElements.add(childElement);
            }
        }
        routerActivityModel.setExtraElementKeys(mExtraElementKeys);
        routerActivityModel.setExtraElements(mExtraElements);
        boolean isNeedBind = (mExtraElementKeys != null && mExtraElementKeys.size() > 0
                || routerActivityModel.getSceneTransitionElement() != null);
        routerActivityModel.setNeedBind(isNeedBind);
        mRouterActivityModels.add(routerActivityModel);
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
        String CLASS_NAME = "TRouter";
        TypeSpec.Builder tb = classBuilder(CLASS_NAME).addModifiers(PUBLIC, FINAL).addJavadoc("@ 全局路由器 此类由apt自动生成");

        FieldSpec contextField = FieldSpec.builder(ClassName.get("android.content", "Context"), "mContext")
                .addModifiers(PRIVATE)
                .build();
        tb.addField(contextField);

        FieldSpec bitSetField = FieldSpec.builder(ClassName.get("java.util", "BitSet"), "IGNORE_ENCODING")
                .addModifiers(PRIVATE, STATIC)
                .build();
        tb.addField(bitSetField);

        FieldSpec routerField = FieldSpec.builder(ClassName.get(Utils.PackageName, CLASS_NAME), "mTRouter")
                .addModifiers(PRIVATE, STATIC, VOLATILE)
                .build();
        tb.addField(routerField);


        MethodSpec.Builder initMethodBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);
        CodeBlock.Builder initCodeBlock = CodeBlock.builder();
        initCodeBlock.addStatement("IGNORE_ENCODING = new BitSet(256)");
        initCodeBlock.addStatement("int i");
        initCodeBlock.beginControlFlow("for (i = 'a'; i <= 'z'; i++)");
        initCodeBlock.addStatement("IGNORE_ENCODING.set(i)");
        initCodeBlock.endControlFlow();
        initCodeBlock.beginControlFlow("for (i = 'A'; i <= 'Z'; i++)");
        initCodeBlock.addStatement("IGNORE_ENCODING.set(i)");
        initCodeBlock.endControlFlow();
        initCodeBlock.beginControlFlow("for (i = '0'; i <= '9'; i++)");
        initCodeBlock.addStatement("IGNORE_ENCODING.set(i)");
        initCodeBlock.endControlFlow();
        initCodeBlock.addStatement("IGNORE_ENCODING.set('*')");
        initCodeBlock.addStatement("IGNORE_ENCODING.set('~')");
        initCodeBlock.addStatement("IGNORE_ENCODING.set('!')");
        initCodeBlock.addStatement("IGNORE_ENCODING.set('(')");
        initCodeBlock.addStatement("IGNORE_ENCODING.set(')')");
        initCodeBlock.addStatement("IGNORE_ENCODING.set('\\'')");
        initMethodBuilder.addCode(initCodeBlock.build());
        tb.addMethod(initMethodBuilder.build());

        MethodSpec.Builder withMethodBuilder = MethodSpec.methodBuilder("with")
                .addJavadoc("@此方法由apt自动生成")
                .returns(ClassName.get(Utils.PackageName, CLASS_NAME))
                .addModifiers(PUBLIC, STATIC)
                .addParameter(ClassName.get("android.content", "Context"), "context");
        CodeBlock.Builder blockBuilderWith = CodeBlock.builder();
        blockBuilderWith.beginControlFlow("if (mTRouter == null)");
        blockBuilderWith.addStatement("mTRouter = new TRouter()");
        blockBuilderWith.endControlFlow();
        blockBuilderWith.addStatement("mTRouter.mContext = context");
        blockBuilderWith.addStatement("return mTRouter");
        withMethodBuilder.addCode(blockBuilderWith.build());
        tb.addMethod(withMethodBuilder.build());

        MethodSpec.Builder goMethodBuilder1 = MethodSpec.methodBuilder("go")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC)
                .addParameter(String.class, "url")
                .addStatement("if (url == null || url.isEmpty()) return")
                .beginControlFlow("if (!url.contains(\"?\"))")
                .addStatement("go(url, 0)")
                .addStatement("return")
                .endControlFlow()
                .addStatement("String[] str = url.split(\"\\\\?\")")
                .addStatement("String name = str[0]")
                .addStatement("String params = str[1]")
                .addStatement("Bundle bundle = new Bundle()")
                .addStatement("Map<String, String> hashMap = toMap(params)")
                .addCode("go(name, bundle);\n");
        tb.addMethod(goMethodBuilder1.build());

        MethodSpec.Builder goMethodBuilder2 = MethodSpec.methodBuilder("go")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC)
                .addParameter(String.class, "name")
                .addParameter(int.class, "requestCode")
                .addCode("go(name, null, requestCode);\n");
        tb.addMethod(goMethodBuilder2.build());

        MethodSpec.Builder goMethodBuilder3 = MethodSpec.methodBuilder("go")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC)
                .addParameter(String.class, "name")
                .addParameter(ClassName.get("android.os", "Bundle"), "bundle")
                .addCode("go(name, bundle, 0);\n");
        tb.addMethod(goMethodBuilder3.build());

        MethodSpec.Builder goMethodBuilder4 = MethodSpec.methodBuilder("go")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC)
                .addParameter(String.class, "name")
                .addParameter(ClassName.get("android.os", "Bundle"), "bundle")
                .addParameter(int.class, "requestCode");
        CodeBlock.Builder blockBuilderGo4 = CodeBlock.builder();
        blockBuilderGo4.addStatement("if (name == null || name.isEmpty()) return");
        blockBuilderGo4.beginControlFlow(" switch (name)");//括号开始
        for (RouterActivityModel item : mRouterActivityModels) {
            blockBuilderGo4.add("case $S: \n", item.getActionName());
            blockBuilderGo4.add("starActivity($T.class , bundle , requestCode);", item.getElement());
            blockBuilderGo4.addStatement("\nbreak");
        }
        blockBuilderGo4.addStatement("default: \nbreak");
        blockBuilderGo4.endControlFlow();
        goMethodBuilder4.addCode(blockBuilderGo4.build());
        tb.addMethod(goMethodBuilder4.build());

        ClassName mIntentClassName = ClassName.get("android.content", "Intent");
        ClassName mActivityClassName = ClassName.get("android.app", "Activity");
        MethodSpec.Builder starActivityMethodBuilder = MethodSpec.methodBuilder("starActivity")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PRIVATE)
                .addParameter(ClassName.get("java.lang", "Class"), "cls")
                .addParameter(ClassName.get("android.os", "Bundle"), "bundle")
                .addParameter(int.class, "requestCode")
                .addStatement("$T intent = new $T(mContext, cls)", mIntentClassName, mIntentClassName)
                .addStatement("intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)")
                .beginControlFlow("if (bundle != null)")
                .addStatement("intent.putExtras(bundle)")
                .endControlFlow()
                .beginControlFlow("if (requestCode != 0)")
                .addStatement("(($T) mContext).startActivityForResult(intent, requestCode)", mActivityClassName)
                .nextControlFlow("else")
                .addStatement(" mContext.startActivity(intent)", mActivityClassName)
                .endControlFlow()
                .addStatement("mContext = null");

        tb.addMethod(starActivityMethodBuilder.build());

        ClassName mURLDecoderClassName = ClassName.get("java.net", "URLDecoder");
        ClassName mExceptionClassName = ClassName.get("java.io", "UnsupportedEncodingException");
        MethodSpec.Builder decodeMethodBuilder = MethodSpec.methodBuilder("decode")
                .addJavadoc("@此方法由apt自动生成")
                .returns(ClassName.get("java.lang", "String"))
                .addModifiers(PRIVATE)
                .addParameter(ClassName.get("java.lang", "String"), "string")
                .beginControlFlow("try")
                .addStatement("return $T.decode(string, \"utf-8\")", mURLDecoderClassName)
                .nextControlFlow("catch($T e)", mExceptionClassName)
                .addStatement("e.printStackTrace()")
                .endControlFlow()
                .addStatement("return \"\"", mURLDecoderClassName);
        tb.addMethod(decodeMethodBuilder.build());

        MethodSpec.Builder isIgnoreMethodBuilder = MethodSpec.methodBuilder("isIgnore")
                .addJavadoc("@此方法由apt自动生成")
                .addJavadoc("\n判断c是否是16进制的字符")
                .returns(ClassName.get("java.lang", "Boolean"))
                .addModifiers(PRIVATE)
                .addParameter(int.class, "c")
                .beginControlFlow("if (IGNORE_ENCODING.get((int) c))")
                .addStatement("return true")
                .nextControlFlow("else")
                .addStatement("return false")
                .endControlFlow();
        tb.addMethod(isIgnoreMethodBuilder.build());

        MethodSpec.Builder hasEncodeURIComponentMethodBuilder = MethodSpec.methodBuilder("hasEncodeURIComponent")
                .addJavadoc("@此方法由apt自动生成")
                .addJavadoc("\n支持JAVA的URLEncoder.encode出来的string做判断。 即: 将' '转成'+' ")
                .addJavadoc("\n0-9a-zA-Z保留 ")
                .addJavadoc("\n'-'，'_'，'.'，'*'保留 ")
                .addJavadoc("\n其他字符转成%XX的格式，X是16进制的大写字符，范围是[0-9A-F]")
                .returns(boolean.class)
                .addModifiers(PRIVATE)
                .addParameter(String.class, "str")
                .addStatement("boolean needEncode = false")
                .beginControlFlow("for (int i = 0; i < str.length(); i++)")
                .addStatement("char c = str.charAt(i)")
                .beginControlFlow("if (IGNORE_ENCODING.get((int) c))")
                .addStatement("continue")
                .endControlFlow()
                .beginControlFlow("if (c == '%' && (i + 2) < str.length())")
                .addStatement("char c1 = str.charAt(++i)")
                .addStatement("char c2 = str.charAt(++i)")
                .beginControlFlow("if (isIgnore(c1) && isIgnore(c2))")
                .addStatement("continue")
                .endControlFlow()
                .endControlFlow()
                .addStatement("needEncode = true")
                .addStatement("break")
                .endControlFlow()
                .addStatement("return !needEncode");
        tb.addMethod(hasEncodeURIComponentMethodBuilder.build());

        MethodSpec.Builder toMapMethodBuilder = MethodSpec.methodBuilder("toMap")
                .addJavadoc("@此方法由apt自动生成")
                .returns(Map.class)
                .addModifiers(PRIVATE)
                .addParameter(String.class, "url")
                .addStatement("$T<String, String> map = new $T<>()", Map.class, HashMap.class)
                .beginControlFlow("if (url != null && url.contains(\"=\"))")
                .addStatement("String[] arrTemp = url.split(\"&\")")
                .beginControlFlow("for (String str : arrTemp)")
                .addStatement("String[] qs = str.split(\"=\")")
                .addStatement("String s = qs[1]")
                .beginControlFlow("if (hasEncodeURIComponent(s))")
                .addStatement("s = decode(s)")
                .endControlFlow()
                .addStatement("map.put(qs[0], s)")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return map");
        tb.addMethod(toMapMethodBuilder.build());

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