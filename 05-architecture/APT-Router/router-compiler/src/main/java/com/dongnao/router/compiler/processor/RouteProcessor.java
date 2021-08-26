package com.dongnao.router.compiler.processor;

/**
 * @author Lance
 * @date 2018/2/22
 */

import com.dongnao.router.annotation.Route;
import com.dongnao.router.annotation.model.RouteMeta;
import com.dongnao.router.compiler.utils.Consts;
import com.dongnao.router.compiler.utils.Log;
import com.dongnao.router.compiler.utils.Utils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static javax.lang.model.element.Modifier.PUBLIC;


@AutoService(Processor.class)
/**
 * 处理器接收的参数 替代 {@link AbstractProcessor#getSupportedOptions()} 函数
 */
@SupportedOptions(Consts.ARGUMENTS_NAME)
/**
 * 指定使用的Java版本 替代 {@link AbstractProcessor#getSupportedSourceVersion()} 函数
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
/**
 * 注册给哪些注解的  替代 {@link AbstractProcessor#getSupportedAnnotationTypes()} 函数
 */
@SupportedAnnotationTypes({Consts.ANN_TYPE_ROUTE})
public class RouteProcessor extends AbstractProcessor {


    /**
     * key:组名 value:类名
     */
    private Map<String, String> rootMap = new TreeMap<>();
    /**
     * 分组 key:组名 value:对应组的路由信息
     */
    private Map<String, List<RouteMeta>> groupMap = new HashMap<>();
    /**
     * 节点工具类 (类、函数、属性都是节点)
     */
    private Elements elementUtils;

    /**
     * type(类信息)工具类
     */
    private Types typeUtils;
    /**
     * 文件生成器 类/资源
     */
    private Filer filerUtils;
    /**
     * 参数
     */
    private String moduleName;

    private Log log;

    /**
     * 初始化 从 {@link ProcessingEnvironment} 中获得一系列处理器工具
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //获得apt的日志输出
        log = Log.newLog(processingEnvironment.getMessager());
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        filerUtils = processingEnv.getFiler();
        //参数是模块名 为了防止多模块/组件化开发的时候 生成相同的 xx$$ROOT$$文件
        Map<String, String> options = processingEnv.getOptions();
        if (!Utils.isEmpty(options)) {
            moduleName = options.get(Consts.ARGUMENTS_NAME);
        }
        log.i("RouteProcessor Parameters: " + moduleName);
        if (Utils.isEmpty(moduleName)) {
            throw new RuntimeException("Not set Processor Parameters.");
        }
    }

    /**
     * 相当于main函数，正式处理注解
     *
     * @param set              使用了支持处理注解  的节点集合
     * @param roundEnvironment 表示当前或是之前的运行环境,可以通过该对象查找找到的注解。
     * @return true 表示后续处理器不会再处理(已经处理)
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //使用了需要处理的注解
        if (!Utils.isEmpty(set)) {
            //获取所有被 Route 注解的元素集合
            Set<? extends Element> routeElements = roundEnvironment.getElementsAnnotatedWith(Route.class);
            //处理 Route 注解
            if (!Utils.isEmpty(routeElements)) {
                try {
                    parseRoutes(routeElements);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    private void parseRoutes(Set<? extends Element> routeElements) throws IOException {
        //支持配置路由类的类型
        TypeElement activity = elementUtils.getTypeElement(Consts.ACTIVITY);
        //节点自描述 Mirror
        TypeMirror type_Activity = activity.asType();

        TypeElement iService = elementUtils.getTypeElement(Consts.ISERVICE);
        TypeMirror type_IService = iService.asType();

        /*
         * groupMap(组名:路由信息)集合
         */
        //声明 Route 注解的节点 (需要处理的节点 Activity/IService)
        for (Element element : routeElements) {
            //路由信息
            RouteMeta routeMeta;
            // 使用Route注解的类信息
            TypeMirror tm = element.asType();
            log.i("Route Class: " + tm.toString());
            Route route = element.getAnnotation(Route.class);
            //是否是 Activity 使用了Route注解
            if (typeUtils.isSubtype(tm, type_Activity)) {
                routeMeta = new RouteMeta(RouteMeta.Type.ACTIVITY, route, element);
            } else if (typeUtils.isSubtype(tm, type_IService)) {
                routeMeta = new RouteMeta(RouteMeta.Type.ISERVICE, route, element);
            } else {
                throw new RuntimeException("[Just Support Activity/IService Route] :" + element);
            }
            //分组信息记录  groupMap <Group分组,RouteMeta路由信息> 集合
            categories(routeMeta);
        }

        //生成类需要实现的接口
        TypeElement iRouteGroup = elementUtils.getTypeElement(Consts.IROUTE_GROUP);
        TypeElement iRouteRoot = elementUtils.getTypeElement(Consts.IROUTE_ROOT);

        /**
         *  生成Group类 作用:记录 <地址,RouteMeta路由信息(Class文件等信息)>
         */
        generatedGroup(iRouteGroup);
        /**
         * 生成Root类 作用:记录 <分组，对应的Group类>
         */
        generatedRoot(iRouteRoot, iRouteGroup);


    }

    private void generatedGroup(TypeElement iRouteGroup) throws IOException {
        //参数  Map<String,RouteMeta>
        ParameterizedTypeName atlas = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouteMeta.class)
        );
        //参数 Map<String,RouteMeta> atlas
        ParameterSpec groupParamSpec = ParameterSpec.builder(atlas, "atlas")
                .build();

        //遍历分组,每一个分组创建一个 $$Group$$ 类
        for (Map.Entry<String, List<RouteMeta>> entry : groupMap.entrySet()) {
            /**
             * 类成员函数loadInfo声明构建
             */
            //函数 public void loadInfo(Map<String,RouteMeta> atlas)
            MethodSpec.Builder loadIntoMethodOfGroupBuilder = MethodSpec.methodBuilder
                    (Consts.METHOD_LOAD_INTO)
                    .addAnnotation(Override.class)
                    .addModifiers(PUBLIC)
                    .addParameter(groupParamSpec);

            //分组名 与 对应分组中的信息
            String groupName = entry.getKey();
            List<RouteMeta> groupData = entry.getValue();
            //遍历分组中的条目 数据
            for (RouteMeta routeMeta : groupData) {
                // 组装函数体:
                // atlas.put(地址,RouteMeta.build(Class,path,group))
                // $S https://github.com/square/javapoet#s-for-strings
                // $T https://github.com/square/javapoet#t-for-types
                loadIntoMethodOfGroupBuilder.addStatement(
                        "atlas.put($S, $T.build($T.$L,$T.class, $S, $S))",
                        routeMeta.getPath(),
                        ClassName.get(RouteMeta.class),
                        ClassName.get(RouteMeta.Type.class),
                        routeMeta.getType(),
                        ClassName.get((TypeElement) routeMeta.getElement()),
                        routeMeta.getPath().toLowerCase(),
                        routeMeta.getGroup().toLowerCase());
            }
            // 创建java文件($$Group$$)  组
            String groupClassName = Consts.NAME_OF_GROUP + groupName;
            JavaFile.builder(Consts.PACKAGE_OF_GENERATE_FILE,
                    TypeSpec.classBuilder(groupClassName)
                            .addSuperinterface(ClassName.get(iRouteGroup))
                            .addModifiers(PUBLIC)
                            .addMethod(loadIntoMethodOfGroupBuilder.build())
                            .build()
            ).build().writeTo(filerUtils);
            log.i("Generated RouteGroup: " + Consts.PACKAGE_OF_GENERATE_FILE + "." +
                    groupClassName);
            //分组名和生成的对应的Group类类名
            rootMap.put(groupName, groupClassName);
        }
    }

    private void generatedRoot(TypeElement iRouteRoot, TypeElement iRouteGroup) throws IOException {
        //类型 Map<String,Class<? extends IRouteGroup>> routes>
        //Wildcard 通配符
        ParameterizedTypeName routes = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(
                        ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(iRouteGroup))
                )
        );

        //参数 Map<String,Class<? extends IRouteGroup>> routes> routes
        ParameterSpec rootParamSpec = ParameterSpec.builder(routes, "routes")
                .build();
        //函数 public void loadInfo(Map<String,Class<? extends IRouteGroup>> routes> routes)
        MethodSpec.Builder loadIntoMethodOfRootBuilder = MethodSpec.methodBuilder
                (Consts.METHOD_LOAD_INTO)
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .addParameter(rootParamSpec);

        //函数体
        for (Map.Entry<String, String> entry : rootMap.entrySet()) {
            loadIntoMethodOfRootBuilder.addStatement("routes.put($S, $T.class)", entry
                    .getKey(), ClassName.get(Consts.PACKAGE_OF_GENERATE_FILE, entry.getValue
                    ()));
        }
        //生成 $Root$类
        String rootClassName = Consts.NAME_OF_ROOT + moduleName;
        JavaFile.builder(Consts.PACKAGE_OF_GENERATE_FILE,
                TypeSpec.classBuilder(rootClassName)
                        .addSuperinterface(ClassName.get(iRouteRoot))
                        .addModifiers(PUBLIC)
                        .addMethod(loadIntoMethodOfRootBuilder.build())
                        .build()
        ).build().writeTo(filerUtils);

        log.i("Generated RouteRoot: " + Consts.PACKAGE_OF_GENERATE_FILE + "." + rootClassName);
    }

    private void categories(RouteMeta routeMeta) {
        if (routeVerify(routeMeta)) {
            log.i("Group Info, Group Name = " + routeMeta.getGroup() + ", Path = " + routeMeta.getPath());

            List<RouteMeta> routeMetas = groupMap.get(routeMeta.getGroup());
            //如果未记录分组则创建
            if (Utils.isEmpty(routeMetas)) {
                List<RouteMeta> routeMetaSet = new ArrayList<>();
                routeMetaSet.add(routeMeta);
                groupMap.put(routeMeta.getGroup(), routeMetaSet);
            } else {
                routeMetas.add(routeMeta);
            }
        } else {
            log.i("Group Info Error: " + routeMeta.getPath());
        }
    }

    /**
     * 验证路由信息必须存在path(并且设置分组)
     *
     * @param meta raw meta
     */
    private boolean routeVerify(RouteMeta meta) {
        String path = meta.getPath();
        String group = meta.getGroup();
        //路由地址必须以 / 开头
        if (Utils.isEmpty(path) || !path.startsWith("/")) {
            return false;
        }
        //如果没有设置分组,以第一个 / 后的节点为分组(所以必须path两个/)
        if (Utils.isEmpty(group)) {
            String defaultGroup = path.substring(1, path.indexOf("/", 1));
            if (Utils.isEmpty(defaultGroup)) {
                return false;
            }
            meta.setGroup(defaultGroup);
            return true;
        }
        return true;
    }

}
