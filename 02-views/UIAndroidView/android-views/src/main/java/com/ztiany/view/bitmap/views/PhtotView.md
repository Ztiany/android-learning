注意：对事件分发原理不了解的，建议学习下之前课程讲解的事件分发原理。

# PhotoView项目实战

我们要实现的效果是显示一张图片，然后对其进行双击放大缩小，滑动，双指放大缩小。

## 1.绘制图片到屏幕中间

```java
protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    
    // 求原始偏移量，为了让图片居中
    originalOffsetX = ( getWidth() - bitmap.getWidth()) / 2f;
    originalOffsetY = ( getHeight() - bitmap.getHeight()) / 2f;
}

protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    // 绘制图片居中
    canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint);
}
```

## 2.缩放和比例计算

```java
protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
	
    ...
    
    // if命中：宽为全屏时为小缩放
    if ((float) bitmap.getWidth() / bitmap.getHeight() > (float) getWidth() / getHeight()) {
        // 放大后一边全屏，一边留白叫小缩放
        smallScale = (float) getWidth() / bitmap.getWidth();
        // 放大后一边全屏，一边超出界面叫大缩放
        bigScale = (float) getHeight() / bitmap.getHeight() * OVER_SCALE_FACTOR;
    } else {
        smallScale = (float) getHeight() / bitmap.getHeight();
        bigScale = (float) getWidth() / bitmap.getWidth() * OVER_SCALE_FACTOR;
    }
    currentScale = smallScale;
}
```

OVER_SCALE_FACTOR：bigScale的时候多放大点，这样四个方向就都可以滑动了。

```java
protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    // 缩放图片
    canvas.scale(currentScale, currentScale, getWidth() / 2f, getHeight() / 2f);
    // 绘制图片居中
    canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint);
}
```

## 3.手势处理

### 3-1.OnDoubleTapListener

300ms内点击两次才算双击。

#### 3-1-1.onSingleTapConfirmed--仅单击

```java
// 发生单次点击时通知
boolean onSingleTapConfirmed(MotionEvent e);
```

单击事件为什么不直接用onClick方法？

1. onClickListener中的onClick方法与该方法冲突
2. 用onClick方法，不管单击还是双击都会触发（双击就触发两次）

单击按下时触发，双击时不会触发。

#### 3-1-2.onDoubleTap--双击

```java
// 发生双击时通知
boolean onDoubleTap(MotionEvent e);
```

双击第二次点击按下时发生回调。

#### 3-1-3.onDoubleTapEvent--双击手势事件

```java
// 在双击手势中发生事件时通知，包括按下、移动和抬起事件
boolean onDoubleTapEvent(MotionEvent e);
```

双击第二次点击时的按下，移动和抬起事件都会回调。

#### 3-1-4.注意点

双击必须设置监听，要么自己调用方法`setOnDoubleTapListener`设置，要么系统帮忙调用。

```java
// 如果参数 listener属于 OnDoubleTapListener的对象则系统帮忙创建
public GestureDetector(Context context, OnGestureListener listener, Handler handler) {
    if (listener instanceof OnDoubleTapListener) {
        setOnDoubleTapListener((OnDoubleTapListener) listener);
    }
}
```

所以我们一般直接继承 SimpleOnGestureListener，这样系统就会为我们调用双击监听方法。

### 3-2.OnGestureListener

可以重写其中的各种方法（单击事件、双击事件等等），就可以监听到单击、双击、滑动等事件，然后直接在回调方法中处理即可。

#### 3-2-1.onDown--按下

```java
// 按下
boolean onDown(MotionEvent e);
```

#### 3-2-2.onShowPress--触摸反馈

```java
// 触摸反馈
void onShowPress(MotionEvent e);
```

它是在 View 被点击（按下）时调用，其作用是给用户一个视觉反馈，让用户知道我这个控件被点击了，这样的效果我们也可以用 Material design 的 ripple 实现，或者直接 drawable 写个背景也行。

它是一种延时回调，延迟时间是 100 ms。也就是说用户手指按下后，如果立即抬起或者事件立即被拦截，时间没有超过 100 ms的话，这条消息会被 remove 掉，也就不会触发这个回调。

#### 3-2-3.onSingleTapUp--单击抬起

```java
// 单击抬起
boolean onSingleTapUp(MotionEvent e);
```

单击抬起时触发，且只在双击的第一次抬起时触发。（连续点击三次，则会触发两次）

#### 3-2-4.onScroll--移动

```java
// 手指按下后移动，类似 move事件
// e1：手指按下时的 MotionEvent
// e2：手指当前的 MotionEvent
// distanceX：在X轴上划过的距离 --- 旧位置 减去 新位置
// distanceY：在Y轴上划过的距离
boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);
```

#### 3-2-5.onLongPress--长按

```java
// 长按事件回调
void onLongPress(MotionEvent e);
```

取消长按响应

```java
GestureDetector.setIsLongpressEnabled(false);
```

#### 3-2-6.onFling--抛掷

处理惯性滑动。最小滑动速度50dip/s（dp=dip）。最大8000dp/s。

```java
// 抛掷（惯性滑动）
// e1：手指按下时的 MotionEvent，可以知道按下位置等
// e2：手指当前的 MotionEvent
// velocityX：在X轴上的运动速度（像素/秒）
// velocityY：在Y轴上的运动速度（像素/秒）
boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);
```

### 3-3.SimpleOnGestureListener

实现了OnGestureListener, OnDoubleTapListener, OnContextClickListener这三个接口，并重写了接口的方法。所以我们可以 new 一个 SimpleOnGestureListener 对象，这样就不用重写接口的所有方法，而只写自己需要的方法即可。

## 4.双击缩放

```java
// 创建 GestureDetector对象
gestureDetector = new GestureDetector(context, new PhotoGestureListener());
```

```java
class PhotoGestureListener extends GestureDetector.SimpleOnGestureListener {

    // 必须为true才表示消费事件
    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    // 双击处理缩放
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        isEnlarge = !isEnlarge;
        if (isEnlarge) {
            currentScale = bigScale;
        } else {
            currentScale = smallScale;
        }
        invalidate();
        return super.onDoubleTap(e);
    }
}
```

必须重写onTouchEvent，因为GestureDetector里面自己重写了事件处理。

```java
@Override
public boolean onTouchEvent(MotionEvent event) {
    return gestureDetector.onTouchEvent(event);
}
```

### 4-1.缩放效果更平滑

使用属性动画是缩放效果更平滑。

```java
private ObjectAnimator getScaleAnimator() {
    if (scaleAnimator == null) {
        // values必须要有，否则运行时报错
        scaleAnimator = ObjectAnimator.ofFloat(this,
                "currentScale", 0);
    }
    scaleAnimator.setFloatValues(smallScale, bigScale);
    return scaleAnimator;
}

public float getCurrentScale() {
    return currentScale;
}

public void setCurrentScale(float currentScale) {
    this.currentScale = currentScale;
    // 属性动画变化是刷新界面
    invalidate();
}
```

```java
public boolean onDoubleTap(MotionEvent e) {
    isEnlarge = !isEnlarge;
    if (isEnlarge) {
        getScaleAnimator().start();
    } else {
        getScaleAnimator().reverse();
    }
    return super.onDoubleTap(e);
}
```

## 5.手指滑动

只有放大时可以移动。

```java
canvas.translate(offsetX, offsetY);
```

```java
public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    if (isEnlarge) {
        offsetX -= distanceX;
        offsetY -= distanceY;
        // 处理边界问题
        fixOffsets();
        invalidate();
    }
    return super.onScroll(e1, e2, distanceX, distanceY);
}
```

```java
private void fixOffsets() {
    offsetX = Math.min(offsetX, (bitmap.getWidth() * bigScale - getWidth()) / 2);
    offsetX = Math.max(offsetX, -(bitmap.getWidth() * bigScale - getWidth()) / 2);
    offsetY = Math.min(offsetY, (bitmap.getHeight() * bigScale - getHeight()) / 2);
    offsetY = Math.max(offsetY, -(bitmap.getHeight() * bigScale - getHeight()) / 2);
}
```

## 6.惯性滑动

```java
public boolean onFling(MotionEvent down, MotionEvent event, float velocityX, float velocityY) {
    if (isEnlarge) {
        overScroller.fling((int) offsetX, (int) offsetY, (int) velocityX, (int) velocityY,
                -(int) (bitmap.getWidth() * bigScale - getWidth()) / 2,
                (int) (bitmap.getWidth() * bigScale - getWidth()) / 2,
                -(int) (bitmap.getHeight() * bigScale - getHeight()) / 2,
                (int) (bitmap.getHeight() * bigScale - getHeight()) / 2);
        postOnAnimation(flingRunner);
    }
    return false;
}
```

```java
class FlingRunner implements Runnable {

        @Override
        public void run() {
            // 动画还在执行，返回true
            if (overScroller.computeScrollOffset()) {
                offsetX = overScroller.getCurrX();
                offsetY = overScroller.getCurrY();
                invalidate();
                postOnAnimation(this);
            }
        }
    }
```

## 7.双指缩放 -- ScaleGestureDetector

```java
scaleGestureListener = new PhotoScaleGestureListener();
scaleGestureDetector = new ScaleGestureDetector(context, scaleGestureListener);
```

```java
public boolean onTouchEvent(MotionEvent event) {
    // 双指缩放操作优先处理事件
    boolean result = scaleGestureDetector.onTouchEvent(event);
    // 如果不是双指缩放才处理手势事件
    if (!scaleGestureDetector.isInProgress()) {
        result = gestureDetector.onTouchEvent(event);
    }

    return result;
}
```

```java
class PhotoScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

    float initialScale;

    // 缩放中回调 -- 倍数，焦点
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        // getScaleFactor：将比例因子从上一个缩放事件返回到当前事件
        currentScale = initialScale * detector.getScaleFactor();
        invalidate();
        return false;
    }

    // 缩放前回调,返回true 消费这个缩放事件
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        initialScale = currentScale;
        return true;
    }

    // 缩放后回调
    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
}
```

## 8.优化问题

1. 放大移动后再缩小位置偏移了
2. 双指直接放大后移动不了
3. 其他
