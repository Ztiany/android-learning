# 动脑学院-->性能优化-->APK加固：Dex加固

工程说明：

- app：将要被被加固的 app
- proxy_core：加固 sdk，app 依赖 proxy_core，使用其解密 dex 功能。
- proxy_tools：加固工具，java工程，用于对 app 中的 dex 进行加密并重新打包和签名。

Dex加固，阶段 1 存在的缺点：

- 被加固的 App 中的 Application  必须继承 ProxyApplication。
