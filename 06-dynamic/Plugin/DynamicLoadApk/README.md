# 分析笔记

## 1 知识点

### PackageManager

`PackageInfo getPackageArchiveInfo(String archiveFilePath, int flags)`：检索在包存档文件中定义的应用程序包的总体信息

- 掌握PackageManager的api
- ActivityInfo

### 2 需要掌握的知识

1. APK打包、APK中包含哪些资源
2. APK安装过程、系统加载APK中的各种资源，路径是如何初始化的
3. 类加载机制、Android资源加载机制

## 3 DL流程分析

1. 宿主工程的Manifest中需要定义lib中的代理组件，Activity和Service
2. DLPluginManager的DL的门面类，提供了所有功能的API接口
    - 加载插件`DLPluginPackage loadApk(String dexPath)`
    - 启动Activity
    - 启动Service
    
### 3.1 加载插件的流程
