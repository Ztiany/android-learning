package com.dongnao.base;

import com.dongnao.router.core.template.IService;

/**
 * @author Lance
 * @date 2018/3/6
 * <p>
 * <p>
 * 需要组件共享的服务需要将服务在此暴露
 */
public interface TestService extends IService {

    void test();
}
