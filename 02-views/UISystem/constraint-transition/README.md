# [HenCoder-Plus](https://plus.hencoder.com) transition 学习代码

在学习 MotionLayout 之前，学习 API18 的 transition 动画，以帮助理解。

1. `ObjectAnimatorActivity/ObjectAnimator2Activity`：用 “属性动画实现位移/旋转等动画” 与用 “transition 框架实现位移/旋转等动画” 的区别。
2. `Go`：用 transition 框架实现转场动画。【实现相对麻烦：需要写两套布局，需要重新绑定数据】
3. `ConstraintSetExample`：使用 transition 框架 +  ConstraintLayout 实现转场动画。【实现相对麻烦：需要写两套布局，不能停留在动画的任一位置，不能有触目反馈】
4. `MotionActivity`：使用 MotionLayout 实现动画，可以解决上面所有问题。

工具：

- CycleEditor 用于编辑动画生成 ConstrainsMotion 的 KeyFrameSet 集合。

