package com.maniu.mn_vip_upload_point.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 这个注解是专门用来标记 ，用来切我们上传用户行为统计的注解。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@CommonAnnotationBase(type = "Exception",actionId = "1002")
public @interface MaiDianData2 {
    String value() default "";
}
