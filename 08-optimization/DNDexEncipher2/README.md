# 动脑学院-->性能优化-->APK加固：Dex加固

工程说明：

- app：将要被被加固的 app
- proxy_core：加固 sdk，app 依赖 proxy_core，使用其解密 dex 功能。
- proxy_tools：加固工具，java工程，用于对 app 中的 dex 进行加密并重新打包和签名。

Dex加固，在阶段 1 的基础上进行了以下优化

- 被加固的 App 中的 Application 无需继承 ProxyApplication。
- ProxyApplication 启动后，首先对 Dex 进行解密，然后通过反射实例化  App 中的 Application，并将其替换到 SDK 中去。
