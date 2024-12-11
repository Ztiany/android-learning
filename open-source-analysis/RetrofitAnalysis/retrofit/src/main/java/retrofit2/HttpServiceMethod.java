/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package retrofit2;

import static retrofit2.Utils.getRawType;
import static retrofit2.Utils.methodError;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import kotlin.coroutines.Continuation;
import okhttp3.ResponseBody;

/**
 * Adapts an invocation of an interface method into an HTTP call.
 */
abstract class HttpServiceMethod<ResponseT, ReturnT> extends ServiceMethod<ReturnT> {

    private final RequestFactory requestFactory;
    private final okhttp3.Call.Factory callFactory;
    private final Converter<ResponseBody, ResponseT> responseConverter;

    HttpServiceMethod(
            RequestFactory requestFactory,
            okhttp3.Call.Factory callFactory,
            Converter<ResponseBody, ResponseT> responseConverter
    ) {
        this.requestFactory = requestFactory;
        this.callFactory = callFactory;
        this.responseConverter = responseConverter;
    }

    @Override
    final @Nullable ReturnT invoke(Object[] args) {
        Call<ResponseT> call = new OkHttpCall<>(requestFactory, args, callFactory, responseConverter);
        return adapt(call, args);
    }

    protected abstract @Nullable ReturnT adapt(Call<ResponseT> call, Object[] args);

    /**
     * Inspects the annotations on an interface method to construct a reusable service method that
     * speaks HTTP. This requires potentially-expensive reflection so it is best to build each service
     * method only once and reuse it.
     */
    static <ResponseT, ReturnT> HttpServiceMethod<ResponseT, ReturnT> parseAnnotations(
            Retrofit retrofit,
            Method method,
            RequestFactory requestFactory
    ) {

        // 是不是 Kotlin 的 suspend 函数，如果方法的参数列表的最后一个参数是 Continuation 类型的，那么就是 Kotlin 的 suspend 函数。
        boolean isKotlinSuspendFunction = requestFactory.isKotlinSuspendFunction;
        boolean continuationWantsResponse = false;
        boolean continuationBodyNullable = false;

        // 解析出 adapterType
        Annotation[] annotations = method.getAnnotations();
        Type adapterType;
        if (isKotlinSuspendFunction) {
            Type[] parameterTypes = method.getGenericParameterTypes();
            Type responseType = Utils.getParameterLowerBound(0, (ParameterizedType) parameterTypes[parameterTypes.length - 1]);
            // 返回类型是 Response<T> 的话，需要解包
            if (getRawType(responseType) == Response.class && responseType instanceof ParameterizedType) {
                // Unwrap the actual body type from Response<T>.
                responseType = Utils.getParameterUpperBound(0, (ParameterizedType) responseType);
                continuationWantsResponse = true;
            } else {
                // TODO figure out if type is nullable or not
                // Metadata metadata = method.getDeclaringClass().getAnnotation(Metadata.class)
                // Find the entry for method
                // Determine if return type is nullable or not
            }

            adapterType = new Utils.ParameterizedTypeImpl(null, Call.class, responseType);
            annotations = SkipCallbackExecutorImpl.ensurePresent(annotations);
        } else {
            adapterType = method.getGenericReturnType();
        }

        // 根据解析到的 adapterType 创建 CallAdapter。
        System.out.println("adapterType: " + adapterType);
        CallAdapter<ResponseT, ReturnT> callAdapter = createCallAdapter(retrofit, method, adapterType, annotations);

        // 解析出 responseType
        Type responseType = callAdapter.responseType();
        if (responseType == okhttp3.Response.class) {
            throw methodError(
                    method,
                    "'"
                            + getRawType(responseType).getName()
                            + "' is not a valid response body type. Did you mean ResponseBody?");
        }

        if (responseType == Response.class) {
            throw methodError(method, "Response must include generic type (e.g., Response<String>)");
        }

        // TODO support Unit for Kotlin?
        if (requestFactory.httpMethod.equals("HEAD") && !Void.class.equals(responseType)) {
            throw methodError(method, "HEAD method must use Void as response type.");
        }

        // 根据解析到的 responseType 创建 Converter。
        System.out.println("responseType: " + responseType);
        Converter<ResponseBody, ResponseT> responseConverter = createResponseConverter(retrofit, method, responseType);

        okhttp3.Call.Factory callFactory = retrofit.callFactory;

        // 最后根据解析到的信息创建 HttpServiceMethod 实例。
        if (!isKotlinSuspendFunction) {
            return new CallAdapted<>(requestFactory, callFactory, responseConverter, callAdapter);
        } else if (continuationWantsResponse) {
            //noinspection unchecked Kotlin compiler guarantees ReturnT to be Object.
            return (HttpServiceMethod<ResponseT, ReturnT>)
                    new SuspendForResponse<>(
                            requestFactory,
                            callFactory,
                            responseConverter,
                            (CallAdapter<ResponseT, Call<ResponseT>>) callAdapter);
        } else {
            //noinspection unchecked Kotlin compiler guarantees ReturnT to be Object.
            return (HttpServiceMethod<ResponseT, ReturnT>)
                    new SuspendForBody<>(
                            requestFactory,
                            callFactory,
                            responseConverter,
                            (CallAdapter<ResponseT, Call<ResponseT>>) callAdapter,
                            continuationBodyNullable);
        }
    }

    private static <ResponseT, ReturnT> CallAdapter<ResponseT, ReturnT> createCallAdapter(
            Retrofit retrofit,
            Method method,
            Type returnType,
            Annotation[] annotations
    ) {
        try {
            //noinspection unchecked
            return (CallAdapter<ResponseT, ReturnT>) retrofit.callAdapter(returnType, annotations);
        } catch (RuntimeException e) { // Wide exception range because factories are user code.
            throw methodError(method, e, "Unable to create call adapter for %s", returnType);
        }
    }

    private static <ResponseT> Converter<ResponseBody, ResponseT> createResponseConverter(
            Retrofit retrofit,
            Method method,
            Type responseType
    ) {
        Annotation[] annotations = method.getAnnotations();
        try {
            return retrofit.responseBodyConverter(responseType, annotations);
        } catch (RuntimeException e) { // Wide exception range because factories are user code.
            throw methodError(method, e, "Unable to create converter for %s", responseType);
        }
    }



    static final class CallAdapted<ResponseT, ReturnT> extends HttpServiceMethod<ResponseT, ReturnT> {

        private final CallAdapter<ResponseT, ReturnT> callAdapter;

        CallAdapted(
                RequestFactory requestFactory,
                okhttp3.Call.Factory callFactory,
                Converter<ResponseBody, ResponseT> responseConverter,
                CallAdapter<ResponseT, ReturnT> callAdapter
        ) {
            super(requestFactory, callFactory, responseConverter);
            this.callAdapter = callAdapter;
        }

        @Override
        protected ReturnT adapt(Call<ResponseT> call, Object[] args) {
            return callAdapter.adapt(call);
        }
    }

    static final class SuspendForResponse<ResponseT> extends HttpServiceMethod<ResponseT, Object> {

        private final CallAdapter<ResponseT, Call<ResponseT>> callAdapter;

        SuspendForResponse(
                RequestFactory requestFactory,
                okhttp3.Call.Factory callFactory,
                Converter<ResponseBody, ResponseT> responseConverter,
                CallAdapter<ResponseT, Call<ResponseT>> callAdapter) {
            super(requestFactory, callFactory, responseConverter);
            this.callAdapter = callAdapter;
        }

        @Override
        protected Object adapt(Call<ResponseT> call, Object[] args) {
            call = callAdapter.adapt(call);

            //noinspection unchecked Checked by reflection inside RequestFactory.
            Continuation<Response<ResponseT>> continuation = (Continuation<Response<ResponseT>>) args[args.length - 1];

            // See SuspendForBody for explanation about this try/catch.
            try {
                return KotlinExtensions.awaitResponse(call, continuation);
            } catch (Exception e) {
                return KotlinExtensions.suspendAndThrow(e, continuation);
            }
        }
    }

    static final class SuspendForBody<ResponseT> extends HttpServiceMethod<ResponseT, Object> {
        private final CallAdapter<ResponseT, Call<ResponseT>> callAdapter;
        private final boolean isNullable;

        SuspendForBody(
                RequestFactory requestFactory,
                okhttp3.Call.Factory callFactory,
                Converter<ResponseBody, ResponseT> responseConverter,
                CallAdapter<ResponseT, Call<ResponseT>> callAdapter,
                boolean isNullable) {
            super(requestFactory, callFactory, responseConverter);
            this.callAdapter = callAdapter;
            this.isNullable = isNullable;
        }

        @Override
        protected Object adapt(Call<ResponseT> call, Object[] args) {
            call = callAdapter.adapt(call);

            //noinspection unchecked Checked by reflection inside RequestFactory.
            Continuation<ResponseT> continuation = (Continuation<ResponseT>) args[args.length - 1];

            // Calls to OkHttp Call.enqueue() like those inside await and awaitNullable can sometimes
            // invoke the supplied callback with an exception before the invoking stack frame can return.
            // Coroutines will intercept the subsequent invocation of the Continuation and throw the
            // exception synchronously. A Java Proxy cannot throw checked exceptions without them being
            // declared on the interface method. To avoid the synchronous checked exception being wrapped
            // in an UndeclaredThrowableException, it is intercepted and supplied to a helper which will
            // force suspension to occur so that it can be instead delivered to the continuation to
            // bypass this restriction.
            try {
                return isNullable
                        ? KotlinExtensions.awaitNullable(call, continuation)
                        : KotlinExtensions.await(call, continuation);
            } catch (Exception e) {
                return KotlinExtensions.suspendAndThrow(e, continuation);
            }
        }

    }

}