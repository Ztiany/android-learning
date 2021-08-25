package com.maniu.annotation_compiler;

import com.google.auto.service.AutoService;
import com.maniu.annotation.BindView;
import com.maniu.annotation.OnClick;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class AnnotationCompiler extends AbstractProcessor {
    //生成文件的对象
    Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //初始化生成文件的对象
        filer = processingEnvironment.getFiler();
    }

    /**
     * 声明注解处理器要处理的注解
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(OnClick.class.getCanonicalName());
        types.add(BindView.class.getCanonicalName());
        return types;
    }

    /**
     * 声明注解处理器支持的java源版本
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //获取到程序中的注解 并且和它所在的类对象一一对应起来
        Map<TypeElement, ElementForType> andParseTargets = findAndParseTargets(roundEnvironment);
        //写文件
        if(andParseTargets.size()>0){
            Iterator<TypeElement> iterator = andParseTargets.keySet().iterator();
            Writer writer = null;
            while (iterator.hasNext()){
                TypeElement typeElement = iterator.next();
                ElementForType elementForType = andParseTargets.get(typeElement);
                String activityName = typeElement.getSimpleName().toString();
                //获取到新类名
                String newClazzName = activityName+"$ViewBinder";
                //获取到包名
                String packageName = getPackageName(typeElement);
                //创建java文件
                try {
                    JavaFileObject sourceFile = filer.createSourceFile(
                            packageName+ "." + newClazzName);
                    writer = sourceFile.openWriter();
                    StringBuffer stringBuffer = getStringBuffer(packageName, newClazzName, typeElement, elementForType);
                    writer.write(stringBuffer.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(writer != null){
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取到所有的注解 以及将注解和Activity一一对应起来
     * @param roundEnvironment
     */
    private Map<TypeElement, ElementForType> findAndParseTargets(RoundEnvironment roundEnvironment) {
        Map<TypeElement, ElementForType> map = new HashMap<>();

        //获取到模块中所有用到了BindView的节点
        Set<? extends Element> viewElelments = roundEnvironment.
                getElementsAnnotatedWith(BindView.class);
        //获取到模块中所有用到了OnClick的节点
        Set<? extends Element> methodElements = roundEnvironment.
                getElementsAnnotatedWith(OnClick.class);
        //遍历所有的成员变量节点  一一对应的封装到ElementForType对象中
        for (Element viewElelment : viewElelments) {
            //转换
            VariableElement variableElement = (VariableElement) viewElelment;
            //获取到它的上一个节点  成员变量节点的上一个节点  就是类节点
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            //获取到类节点在map中所对应的value
            ElementForType elementForType = map.get(typeElement);
            List<VariableElement> viewElements;
            //如果这个类节点在map中已经存在
            if(elementForType !=null){
                //获取到类节点所对应的值中的控件节点的集合
                viewElements = elementForType.getViewElements();
                //如果集合为空就创建一个新的集合  然后放置到类节点所对应的值中
                if(viewElements == null){
                    viewElements = new ArrayList<>();
                    elementForType.setViewElements(viewElements);
                }
            }else{
                //如果elementForType为空  就创建一个新的
                elementForType = new ElementForType();
                //同时创建一个新的控件的节点的集合
                viewElements = new ArrayList<>();
                elementForType.setViewElements(viewElements);
                if(!map.containsKey(typeElement)){
                    map.put(typeElement,elementForType);
                }
            }
            //最后  将遍历到的这个控件的节点的对象放置到控件的节点封装类中
            viewElements.add(variableElement);
        }
        //遍历所有的点击事件的方法的节点  并且封装在对象中
        for (Element methodElement : methodElements) {
            ExecutableElement executableElement = (ExecutableElement) methodElement;
            TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
            ElementForType elementForType = map.get(typeElement);
            List<ExecutableElement> executableElements;
            logUtil(elementForType+"");
            if(elementForType !=null){
                executableElements = elementForType.getMethodElements();
                if(executableElements == null){
                    executableElements = new ArrayList<>();
                    elementForType.setMethodElements(executableElements);
                }
            }else{
                elementForType = new ElementForType();
                executableElements = new ArrayList<>();
                elementForType.setMethodElements(executableElements);
                if(!map.containsKey(typeElement)){
                    map.put(typeElement,elementForType);
                }
            }
            executableElements.add(executableElement);
        }
        return map;
    }

    /**
     * 获取包名的方法
     * @param typeElement
     */
    public String getPackageName(Element typeElement){
        //获取包名
        PackageElement packageOf = processingEnv.getElementUtils().getPackageOf(typeElement);
        Name qualifiedName = packageOf.getQualifiedName();
        return qualifiedName.toString();
    }

    public void logUtil(String message){
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE,message);
    }

    /**
     * 获取到类的拼装语句的方法
     * @param packageName
     * @param newClazzName
     * @param typeElement
     * @param elementForType
     * @return
     */
    public StringBuffer getStringBuffer(String packageName,String newClazzName,
                                        TypeElement typeElement,ElementForType elementForType ){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("package "+packageName+";\n");
        stringBuffer.append("import android.view.View;\n");
        stringBuffer.append("public class "+newClazzName+"{\n");
        stringBuffer.append("public "+newClazzName+"(final "+typeElement.getQualifiedName()+" target){\n");
        if(elementForType!=null && elementForType.getViewElements()!=null && elementForType.getViewElements().size()>0){
            List<VariableElement> viewElements = elementForType.getViewElements();
            for (VariableElement viewElement : viewElements) {
                //获取到类型
                TypeMirror typeMirror = viewElement.asType();
                //获取到控件的名字
                Name simpleName = viewElement.getSimpleName();
                //获取到资源ID
                int resId = viewElement.getAnnotation(BindView.class).value();
                stringBuffer.append("target."+simpleName +" =(" +typeMirror+")target.findViewById("+resId+");\n");
            }
        }

        if(elementForType!=null && elementForType.getMethodElements()!=null && elementForType.getMethodElements().size()>0){
            List<ExecutableElement> methodElements = elementForType.getMethodElements();
            for (ExecutableElement methodElement : methodElements) {
                int[] resIds = methodElement.getAnnotation(OnClick.class).value();
                String methodName = methodElement.getSimpleName().toString();
                for (int resId : resIds) {
                    stringBuffer.append("(target.findViewById("+resId+")).setOnClickListener(new View.OnClickListener() {\n");
                    stringBuffer.append("public void onClick(View p0) {\n");
                    stringBuffer.append("target."+methodName+"(p0);\n");
                    stringBuffer.append("}\n});\n");
                }
            }
        }
        stringBuffer.append("}\n}\n");
        return stringBuffer;
    }

}
