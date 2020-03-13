package com.yiciyuan.apt.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.yiciyuan.apt.BaseProcessor;
import com.yiciyuan.apt.annotation.Extra;
import com.yiciyuan.apt.annotation.Router;
import com.yiciyuan.apt.annotation.SceneTransition;
import com.yiciyuan.apt.helper.RouterActivityModel;
import com.yiciyuan.apt.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
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
public class RouterProcessor extends BaseProcessor<Router> {
    private static final Class<Router> Router = Router.class;
    List<ClassName> mList = new ArrayList<>();
    List<RouterActivityModel> mRouterActivityModels = new ArrayList<>();
    private boolean mIsNewCode = false;

    public RouterProcessor() {
        super(Router);
    }

    private void grenCode() {
        String CLASS_NAME = "TRouter";
        TypeSpec.Builder tb = classBuilder(CLASS_NAME).addModifiers(PUBLIC, FINAL).addJavadoc("@ 全局路由器 此类由apt自动生成");
        FieldSpec contextField = FieldSpec.builder(ClassName.get("android.content", "Context"), "mContext")
            .addModifiers(PRIVATE)
            .build();
        tb.addField(contextField);

        FieldSpec routerField = FieldSpec.builder(ClassName.get(Utils.PackageName, CLASS_NAME), "mTRouter")
            .addModifiers(PRIVATE, STATIC, VOLATILE)
            .build();
        tb.addField(routerField);

        MethodSpec initMethodBuilder = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .build();
        tb.addMethod(initMethodBuilder);

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

        MethodSpec.Builder goMethodBuilder1 = MethodSpec.methodBuilder("go")
            .addJavadoc("@此方法由apt自动生成")
            .addModifiers(PUBLIC)
            .addParameter(String.class, "name")
            .addCode("go(name, 0);\n");
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

    @Override
    protected void handleEach(TypeElement element, Router annotation) {
        ClassName currentType = ClassName.get(element);
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
}