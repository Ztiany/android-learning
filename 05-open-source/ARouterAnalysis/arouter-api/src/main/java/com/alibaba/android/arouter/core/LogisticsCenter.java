package com.alibaba.android.arouter.core;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.alibaba.android.arouter.exception.HandlerException;
import com.alibaba.android.arouter.exception.NoRouteFoundException;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.enums.TypeKind;
import com.alibaba.android.arouter.facade.model.RouteMeta;
import com.alibaba.android.arouter.facade.template.IInterceptorGroup;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.alibaba.android.arouter.facade.template.IProviderGroup;
import com.alibaba.android.arouter.facade.template.IRouteGroup;
import com.alibaba.android.arouter.facade.template.IRouteRoot;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.arouter.utils.ClassUtils;
import com.alibaba.android.arouter.utils.Consts;
import com.alibaba.android.arouter.utils.MapUtils;
import com.alibaba.android.arouter.utils.PackageUtils;
import com.alibaba.android.arouter.utils.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

import static com.alibaba.android.arouter.launcher.ARouter.logger;
import static com.alibaba.android.arouter.utils.Consts.AROUTER_SP_CACHE_KEY;
import static com.alibaba.android.arouter.utils.Consts.AROUTER_SP_KEY_MAP;
import static com.alibaba.android.arouter.utils.Consts.DOT;
import static com.alibaba.android.arouter.utils.Consts.ROUTE_ROOT_PAKCAGE;
import static com.alibaba.android.arouter.utils.Consts.SDK_NAME;
import static com.alibaba.android.arouter.utils.Consts.SEPARATOR;
import static com.alibaba.android.arouter.utils.Consts.SUFFIX_INTERCEPTORS;
import static com.alibaba.android.arouter.utils.Consts.SUFFIX_PROVIDERS;
import static com.alibaba.android.arouter.utils.Consts.SUFFIX_ROOT;
import static com.alibaba.android.arouter.utils.Consts.TAG;

/**
 * LogisticsCenter contains all of the map.
 * <p>
 * 1. Creates instance when it is first used.
 * 2. Handler Multi-Module relationship map(*)
 * 3. Complex logic to solve duplicate group definition
 * 后勤中心包含所有的 map
 * 1. 当对象第一次被使用时创建它。
 * 2. 处理多个模块之间的关系的 map。
 * 3. 用于处理重复 group 定义的复杂逻辑。
 *
 * @author Alex <a href="mailto:zhilong.liu@aliyun.com">Contact me.</a>
 * @version 1.0
 * @since 16/8/23 15:02
 */
public class LogisticsCenter {

    private static Context mContext;

    static ThreadPoolExecutor executor;

    /**
     * 表示路由信息由编译插件自动初始化冷
     */
    private static boolean registerByPlugin;

    /**
     * arouter-auto-register plugin will generate code inside this method
     * call this method to register all Routers, Interceptors and Providers
     * <p>
     * 默认实现是将 registerByPlugin 设置为 false，之后的流程就会因为 registerByPlugin == false 而去执行真正的加载逻辑，但是如果使用了自动注册插件，那么在编译期间，就会修改这个方法的逻辑，插入
     * 加载路由的代码。【应该是编译期间生成的代码更加高效。】
     */
    private static void loadRouterMap() {
        registerByPlugin = false;
        // auto generate register code by gradle plugin: arouter-auto-register
        // looks like below:
        // registerRouteRoot(new ARouter..Root..modulejava());
        // registerRouteRoot(new ARouter..Root..modulekotlin());
    }

    /**
     * register by class name
     * Sacrificing a bit of efficiency to solve
     * the problem that the main dex file size is too large
     */
    private static void register(String className) {
        if (!TextUtils.isEmpty(className)) {
            try {
                Class<?> clazz = Class.forName(className);
                Object obj = clazz.getConstructor().newInstance();
                if (obj instanceof IRouteRoot) {
                    registerRouteRoot((IRouteRoot) obj);
                } else if (obj instanceof IProviderGroup) {
                    registerProvider((IProviderGroup) obj);
                } else if (obj instanceof IInterceptorGroup) {
                    registerInterceptor((IInterceptorGroup) obj);
                } else {
                    logger.info(TAG, "register failed, class name: " + className
                            + " should implements one of IRouteRoot/IProviderGroup/IInterceptorGroup.");
                }
            } catch (Exception e) {
                logger.error(TAG, "register class error:" + className);
            }
        }
    }

    /**
     * method for arouter-auto-register plugin to register Routers
     *
     * @param routeRoot IRouteRoot implementation class in the package: com.alibaba.android.arouter.core.routers
     */
    private static void registerRouteRoot(IRouteRoot routeRoot) {
        markRegisteredByPlugin();
        if (routeRoot != null) {
            routeRoot.loadInto(Warehouse.groupsIndex);
        }
    }

    /**
     * method for arouter-auto-register plugin to register Interceptors
     *
     * @param interceptorGroup IInterceptorGroup implementation class in the package: com.alibaba.android.arouter.core.routers
     */
    private static void registerInterceptor(IInterceptorGroup interceptorGroup) {
        markRegisteredByPlugin();
        if (interceptorGroup != null) {
            interceptorGroup.loadInto(Warehouse.interceptorsIndex);
        }
    }

    /**
     * method for arouter-auto-register plugin to register Providers
     *
     * @param providerGroup IProviderGroup implementation class in the package: com.alibaba.android.arouter.core.routers
     */
    private static void registerProvider(IProviderGroup providerGroup) {
        markRegisteredByPlugin();
        if (providerGroup != null) {
            providerGroup.loadInto(Warehouse.providersIndex);
        }
    }

    /**
     * mark already registered by arouter-auto-register plugin
     */
    private static void markRegisteredByPlugin() {
        if (!registerByPlugin) {
            registerByPlugin = true;
        }
    }

    /**
     * LogisticsCenter init, load all metas in memory. Demand initialization.
     * <p>
     * 后勤中心初始化，加载所有的路由信息到内存中。
     */
    public synchronized static void init(Context context, ThreadPoolExecutor tpe) throws HandlerException {
        mContext = context;
        executor = tpe;

        try {
            long startInit = System.currentTimeMillis();

            //load by plugin first
            //尝试用插件生成的代码加载路由信息
            loadRouterMap();

            if (registerByPlugin) {
                logger.info(TAG, "Load router map by arouter-auto-register plugin.");
                Log.d(TAG, "1");
            } else {
                Set<String> routerMap;
                Log.d(TAG, "2");

                // It will rebuild router map every times when debuggable.
                if (ARouter.debuggable() || PackageUtils.isNewVersion(context)) {
                    logger.info(TAG, "Run with debug mode or new install, rebuild router map.");
                    Log.d(TAG, "Run with debug mode or new install, rebuild router map.");
                    Log.d(TAG, "3");
                    // These class was generated by arouter-compiler.
                    // 扫描指定包下面包含的所有的 ClassName 并存入 map 中。
                    // ARouter 的 APT 插件会将所有路由类放在 com.alibaba.android.arouter.routes 包下
                    routerMap = ClassUtils.getFileNameByPackageName(mContext, ROUTE_ROOT_PAKCAGE);

                    /*缓存起来，下次就不用加载了*/
                    if (!routerMap.isEmpty()) {
                        context.getSharedPreferences(AROUTER_SP_CACHE_KEY, Context.MODE_PRIVATE).edit().putStringSet(AROUTER_SP_KEY_MAP, routerMap).apply();
                    }

                    PackageUtils.updateVersion(context);    // Save new version name when router map update finishes.
                } else {
                    Log.d(TAG, "4");
                    logger.info(TAG, "Load router map from cache.");
                    routerMap = new HashSet<>(context.getSharedPreferences(AROUTER_SP_CACHE_KEY, Context.MODE_PRIVATE).getStringSet(AROUTER_SP_KEY_MAP, new HashSet<String>()));
                }

                logger.info(TAG, "Find router map finished, map size = " + routerMap.size() + ", cost " + (System.currentTimeMillis() - startInit) + " ms.");

                startInit = System.currentTimeMillis();

                for (String className : routerMap) {
                    /*所有的路由组*/
                    if (className.startsWith(ROUTE_ROOT_PAKCAGE + DOT + SDK_NAME + SEPARATOR + SUFFIX_ROOT)) {
                        // This one of root elements, load root.
                        ((IRouteRoot) (Class.forName(className).getConstructor().newInstance())).loadInto(Warehouse.groupsIndex);
                    }
                    /*所有的拦截器*/
                    else if (className.startsWith(ROUTE_ROOT_PAKCAGE + DOT + SDK_NAME + SEPARATOR + SUFFIX_INTERCEPTORS)) {
                        // Load interceptorMeta
                        ((IInterceptorGroup) (Class.forName(className).getConstructor().newInstance())).loadInto(Warehouse.interceptorsIndex);
                    }
                    /*所有的 Provider*/
                    else if (className.startsWith(ROUTE_ROOT_PAKCAGE + DOT + SDK_NAME + SEPARATOR + SUFFIX_PROVIDERS)) {
                        // Load providerIndex
                        ((IProviderGroup) (Class.forName(className).getConstructor().newInstance())).loadInto(Warehouse.providersIndex);
                    }
                }
            }

            logger.info(TAG, "Load root element finished, cost " + (System.currentTimeMillis() - startInit) + " ms.");

            if (Warehouse.groupsIndex.size() == 0) {
                logger.error(TAG, "No mapping files were found, check your configuration please!");
            }

            if (ARouter.debuggable()) {
                logger.debug(TAG, String.format(Locale.getDefault(), "LogisticsCenter has already been loaded, GroupIndex[%d], InterceptorIndex[%d], ProviderIndex[%d]", Warehouse.groupsIndex.size(), Warehouse.interceptorsIndex.size(), Warehouse.providersIndex.size()));
            }
        } catch (Exception e) {
            throw new HandlerException(TAG + "ARouter init logistics center exception! [" + e.getMessage() + "]");
        }
    }

    /**
     * Build postcard by serviceName
     *
     * @param serviceName interfaceName
     * @return postcard
     */
    public static Postcard buildProvider(String serviceName) {
        RouteMeta meta = Warehouse.providersIndex.get(serviceName);

        if (null == meta) {
            return null;
        } else {
            return new Postcard(meta.getPath(), meta.getGroup());
        }
    }

    /**
     * Completion the postcard by route metas
     * 处理 FRAGMENT, PROVIDER 两种跳转类型，给 Postcard 使之对应的 Provider
     *
     * @param postcard Incomplete postcard, should complete by this method.
     */
    public synchronized static void completion(Postcard postcard) {
        if (null == postcard) {
            throw new NoRouteFoundException(TAG + "No postcard!");
        }

        /*获取路由信息：即将我们传递的字符串路由转变为包含实际路由信息的 RouteMeta*/
        RouteMeta routeMeta = Warehouse.routes.get(postcard.getPath());

        if (null == routeMeta) {
            // Maybe its does't exist, or didn't load.
            if (!Warehouse.groupsIndex.containsKey(postcard.getGroup())) {
                throw new NoRouteFoundException(TAG + "There is no route match the path [" + postcard.getPath() + "], in group [" + postcard.getGroup() + "]");
            } else {
                // Load route and cache it into memory, then delete from metas.
                try {
                    if (ARouter.debuggable()) {
                        logger.debug(TAG, String.format(Locale.getDefault(), "The group [%s] starts loading, trigger by [%s]", postcard.getGroup(), postcard.getPath()));
                    }

                    /*在第一次使用时初始化路由组：IRouteGroup-->RouteMeta 的转变*/
                    addRouteGroupDynamic(postcard.getGroup(), null);

                    if (ARouter.debuggable()) {
                        logger.debug(TAG, String.format(Locale.getDefault(), "The group [%s] has already been loaded, trigger by [%s]", postcard.getGroup(), postcard.getPath()));
                    }

                } catch (Exception e) {
                    throw new HandlerException(TAG + "Fatal exception when loading group meta. [" + e.getMessage() + "]");
                }
                completion(postcard);   // Reload
            }

        } else {

            postcard.setDestination(routeMeta.getDestination());
            postcard.setType(routeMeta.getType());
            postcard.setPriority(routeMeta.getPriority());
            postcard.setExtra(routeMeta.getExtra());

            /*------------------------用于支持 web 页面的 URI 跳转支持，会自动解析 url 上的参数，然后设置给 Postcard---------------------------------*/
            Uri rawUri = postcard.getUri();

            if (null != rawUri) {   // Try to set params into bundle.

                Map<String, String> resultMap = TextUtils.splitQueryParameters(rawUri);
                Map<String, Integer> paramsType = routeMeta.getParamsType();

                if (MapUtils.isNotEmpty(paramsType)) {

                    // Set value by its type, just for params which annotation by @Param
                    for (Map.Entry<String, Integer> params : paramsType.entrySet()) {
                        setValue(postcard,
                                params.getValue(),
                                params.getKey(),
                                resultMap.get(params.getKey()));
                    }

                    // Save params name which need auto inject.
                    postcard.getExtras().putStringArray(ARouter.AUTO_INJECT, paramsType.keySet().toArray(new String[]{}));
                }

                // Save raw uri
                postcard.withString(ARouter.RAW_URI, rawUri.toString());
            }
            /*------------------------URI跳转支持---------------------------------*/

            switch (routeMeta.getType()) {

                case PROVIDER:  // if the route is provider, should find its instance
                    // Its provider, so it must implement IProvider
                    Class<? extends IProvider> providerMeta = (Class<? extends IProvider>) routeMeta.getDestination();
                    IProvider instance = Warehouse.providers.get(providerMeta);
                    if (null == instance) { // There's no instance of this provider
                        IProvider provider;
                        try {
                            provider = providerMeta.getConstructor().newInstance();
                            provider.init(mContext);
                            Warehouse.providers.put(providerMeta, provider);
                            instance = provider;
                        } catch (Exception e) {
                            throw new HandlerException("Init provider failed! " + e.getMessage());
                        }
                    }
                    postcard.setProvider(instance);
                    postcard.greenChannel();    // Provider should skip all of interceptors
                    break;

                case FRAGMENT:
                    postcard.greenChannel();    // Fragment needn't interceptors
                default:
                    break;
            }
        }
    }

    /**
     * Set value by known type
     *
     * @param postcard postcard
     * @param typeDef  type
     * @param key      key
     * @param value    value
     */
    private static void setValue(Postcard postcard, Integer typeDef, String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return;
        }

        try {
            if (null != typeDef) {
                if (typeDef == TypeKind.BOOLEAN.ordinal()) {
                    postcard.withBoolean(key, Boolean.parseBoolean(value));
                } else if (typeDef == TypeKind.BYTE.ordinal()) {
                    postcard.withByte(key, Byte.parseByte(value));
                } else if (typeDef == TypeKind.SHORT.ordinal()) {
                    postcard.withShort(key, Short.parseShort(value));
                } else if (typeDef == TypeKind.INT.ordinal()) {
                    postcard.withInt(key, Integer.parseInt(value));
                } else if (typeDef == TypeKind.LONG.ordinal()) {
                    postcard.withLong(key, Long.parseLong(value));
                } else if (typeDef == TypeKind.FLOAT.ordinal()) {
                    postcard.withFloat(key, Float.parseFloat(value));
                } else if (typeDef == TypeKind.DOUBLE.ordinal()) {
                    postcard.withDouble(key, Double.parseDouble(value));
                } else if (typeDef == TypeKind.STRING.ordinal()) {
                    postcard.withString(key, value);
                } else if (typeDef == TypeKind.PARCELABLE.ordinal()) {
                    // TODO : How to description parcelable value with string?
                } else if (typeDef == TypeKind.OBJECT.ordinal()) {
                    postcard.withString(key, value);
                } else {    // Compatible compiler sdk 1.0.3, in that version, the string type = 18
                    postcard.withString(key, value);
                }
            } else {
                postcard.withString(key, value);
            }
        } catch (Throwable ex) {
            logger.warning(Consts.TAG, "LogisticsCenter setValue failed! " + ex.getMessage());
        }
    }

    /**
     * Suspend business, clear cache.
     */
    public static void suspend() {
        Warehouse.clear();
    }

    public synchronized static void addRouteGroupDynamic(String groupName, IRouteGroup group) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (Warehouse.groupsIndex.containsKey(groupName)) {
            // If this group is included, but it has not been loaded
            // load this group first, because dynamic route has high priority.
            Warehouse.groupsIndex.get(groupName).getConstructor().newInstance().loadInto(Warehouse.routes);
            Warehouse.groupsIndex.remove(groupName);
        }
        // cover old group.
        if (null != group) {
            group.loadInto(Warehouse.routes);
        }
    }

}
