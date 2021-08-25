package com.ztiany.base.di;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-23 09:59
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface ActivityScope {

}
