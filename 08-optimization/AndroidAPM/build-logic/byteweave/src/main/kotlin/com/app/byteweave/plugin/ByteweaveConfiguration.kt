package com.app.byteweave.plugin

import java.io.Serializable

open class ByteweaveConfiguration : Serializable {

    internal var legalImage = LegalImageConfiguration()
    internal var viewThrottle = ViewThrottleConfiguration()

    fun legalImage(action: LegalImageConfiguration.() -> Unit) {
        legalImage = LegalImageConfiguration().apply(action)
    }

    fun viewThrottle(action: ViewThrottleConfiguration.() -> Unit) {
        viewThrottle = ViewThrottleConfiguration().apply(action)
    }

    override fun toString(): String {
        return "ByteweaveConfiguration(legalImage=$legalImage, viewThrottle=$viewThrottle)"
    }
}

class LegalImageConfiguration : Serializable {

    interface Editor {

        /**
         * Replace the class with the target class. For example:
         *
         * ```
         *  "android.widget.ImageView" replaceWith "me.ztiany.apm.aspect.bitmap.MonitorImageView"
         * ```
         *
         * means that all `ImageView` will be replaced with `MonitorImageView`.
         */
        infix fun String.replaceWith(target: String)
    }

    var enabled = false

    internal val replacements = mutableMapOf<String, String>()

    /**
     * add rules to replace the class.
     */
    fun rules(edit: Editor.() -> Unit) {
        object : Editor {
            override infix fun String.replaceWith(target: String) {
                replacements[this.replace(".", "/")] = target.replace(".", "/")
            }
        }.edit()
    }

    override fun toString(): String {
        return "LegalImageConfiguration(universalImageViewClass='$replacements', enabled=$enabled)"
    }

    internal fun isAvailable(): Boolean {
        return enabled && replacements.isNotEmpty()
    }

}

class ViewThrottleConfiguration : Serializable {

    internal data class ViewClickHookPoint(
        val className: String,
        val methodName: String,
        val nameWithDesc: String,
    ) : Serializable {
        val interfaceSignSuffix = "L$className;"
    }

    internal data class Checker(
        val className: String,
        val methodName: String,
    ) : Serializable

    var enabled = false

    internal var checker: Checker = Checker("", "")

    internal var includeAnnotationName: String = ""
    internal var excludeAnnotationName: String = ""

    internal val basePackages = mutableListOf<String>()

    internal var hookPoints = mutableListOf<ViewClickHookPoint>()

    /**
     * define the checker class and method.
     *
     * - [className] the class name of the checker. for example: `com.app.aspect.ThrottleChecker`
     * - [methodName] the method name of the checker. for example: `check`
     *
     * Note: the checker method must be static, and take a `View` as the parameter and return a `boolean`. returning `true` means that the click event is legal.
     */
    fun checker(className: String, methodName: String) {
        checker = Checker(className.internalName, methodName)
    }

    /**
     * All methods annotated with the [checkAnnotationName] will be processed.
     *
     * [checkAnnotationName] the annotation name of the checker. for example: `com.app.aspect.ClickThrottle`
     */
    fun includeAnnotation(checkAnnotationName: String) {
        this.includeAnnotationName = checkAnnotationName.descriptor
    }

    /**
     * All methods annotated with the [skipAnnotationName] will be skipped.
     *
     * [skipAnnotationName] the annotation name of the checker. for example: `com.app.aspect.SkipClickThrottle`
     */
    fun excludeAnnotation(skipAnnotationName: String) {
        this.excludeAnnotationName = skipAnnotationName.descriptor
    }

    /**
     * Define the base packages that need to be processed.
     *
     * [basePackages] the base packages that need to be processed. for example: `com.app`, `com.app.aspect`
     */
    fun include(vararg basePackages: String) {
        this.basePackages.addAll(basePackages)
    }

    /**
     * Include all classes.
     */
    fun includeAll() {
        basePackages.add("*")
    }

    /**
     * Add custom hook point.
     *
     * - [className] the class name of the hook point. for example: `android.view.View$OnClickListener`
     * - [methodName] the method name of the hook point. for example: `onClick`
     * - [nameWithDesc] the method name with desc of the hook point. for example: `onClick(Landroid/view/View;)V`. you can use `javap -s your.class` to get it.
     */
    fun addCustomHookPoint(className: String, methodName: String, nameWithDesc: String) {
        hookPoints.add(ViewClickHookPoint(className.internalName, methodName, nameWithDesc))
    }

    /**
     * Add the hook point of the `android.view.View.OnClickListener`.
     */
    fun addViewOnClickListenerHookPoint() {
        addCustomHookPoint("android/view/View\$OnClickListener", "onClick", "onClick(Landroid/view/View;)V")
    }

    internal fun isAvailable(): Boolean {
        return enabled && checker.className.isNotEmpty() && checker.methodName.isNotEmpty() && basePackages.isNotEmpty() && (hookPoints.isNotEmpty() || includeAnnotationName.isNotEmpty())
    }

    internal fun isIncluded(className: String): Boolean {
        return basePackages.contains("*") || basePackages.any { className.startsWith(it) }
    }

    override fun toString(): String {
        return "ViewThrottleConfiguration(enabled=$enabled, checker=$checker, checkAnnotationName='$includeAnnotationName', skipAnnotationName='$excludeAnnotationName', basePackages=$basePackages, hookPoints=$hookPoints)"
    }

}