# 自定义Gradle插件

## 发布到本地仓库

```groovy
uploadArchives {
    repositories {
        mavenDeployer {
            //设置插件的GAV参数
            pom.groupId = 'com.ztiany'
            pom.artifactId = 'hello-plugin'
            pom.version = 1.0
            //文件发布到下面目录
            repository(url: uri('../release'))
        }
    }
}
```

## 此时配置根目录脚本如下

```groovy
buildscript {

    repositories {
        jcenter()
        maven { url uri('repo') }
        google()
    }
    dependencies {
        //android
        classpath 'com.android.tools.build:gradle:3.0.1'
        classpath 'com.ztiany:hello-plugin:1.0'
    }
}

```

## META-INF

META-INF中的properties文件名由开发者定义，必须以.properties结尾，并且文件名即为其他项目所引用的插件名称。比如这里的文件名是`hello-plugin.properties`，在其他项目使用该名称引用插件：*apply plugin :'hello-plugin'*
