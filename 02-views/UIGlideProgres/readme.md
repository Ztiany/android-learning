# OkHttp Progress


##  1 一般的Http上传下载进度

- 使用OKHttp进行文件上传下载
- 提供方法对OkHttp的request和response进行包装，从而获取上传下载的进度。

##  2 图片加载进度

- 使用Glide加载图片
- 使用OkHttp作为网络加载器
- 提供对响应体进行包装的分发，从而从响应体的读操作中获取进度
- 通过URL进行进度监听，处理好通过加载同一个URL地址
- 提供ImageViewLayout。处理好加载时的进度展示

## 3 引用

- [ProgressManager](https://github.com/JessYanCoding/ProgressManager)
- [OkHttpUtils](https://github.com/hongyangAndroid/okhttputils)
- [GlideImageView](https://github.com/sfsheng0322/GlideImageView)
- [XSnow](https://github.com/xiaoyaoyou1212/XSnow)
- [Glide4.0使用浅解](http://www.jianshu.com/p/ab97d6bda8ec)
- [support_232_progress](https://github.com/TWiStErRob/glide-support/tree/master/src/glide3/java/com/bumptech/glide/supportapp/github/_232_progress)