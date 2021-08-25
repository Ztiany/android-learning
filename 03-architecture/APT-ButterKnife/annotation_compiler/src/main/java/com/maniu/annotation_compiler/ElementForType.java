package com.maniu.annotation_compiler;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

/**
 * 装载一个组件里面所有的用到了注解的节点
 */
public class ElementForType {
    //所有绑定View的成员变量的节点的集合
    List<VariableElement> viewElements;
    //所有点击时间方法的节点的集合
    List<ExecutableElement> methodElements;
    //.....如果还有其他的注解  每个注解的节点对应一个集合

    public List<VariableElement> getViewElements() {
        return viewElements;
    }

    public void setViewElements(List<VariableElement> viewElements) {
        this.viewElements = viewElements;
    }

    public List<ExecutableElement> getMethodElements() {
        return methodElements;
    }

    public void setMethodElements(List<ExecutableElement> methodElements) {
        this.methodElements = methodElements;
    }
}
