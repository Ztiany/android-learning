package com.maniu.mn_vip_upload_point.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 区分操作类型的注解的基类，用来标记 ，每个行为的注解的作用。
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommonAnnotationBase {

    //类型
    String type();

    //类型对应的ID
    String actionId();

}
