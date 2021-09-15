# rongcloud-android-imdemo-userinfo&groupinfo

rongcloud-android-imdemo-voicetotext 是基于融云 IMKit SDK 的语音转文字功能，使用了融云最新的 5.0 SDK。


# 使用步骤

## 参数配置

开发者需配置项目中的变量 `Appkey` 和 `Token`，`科大讯飞appkey`，

[AppKey 获取地址](https://developer.rongcloud.cn/app/appkey/0vMK99Huzz-qw40Ybv4NDA)

[Token 获取地址](https://developer.rongcloud.cn/apitool/Mw8EsJmV43kZBugTMSAZXg)

## 前置条件

1. 为了配合 Demo 中的代码，需要执行下面步骤：
   * 使用 [Token 获取地址](https://developer.rongcloud.cn/apitool/Mw8EsJmV43kZBugTMSAZXg) 获取 Token，userId 需要使用 “tester1”，将该 Token 配置到 MainActivity 中进行连接。

2. 新用户默认是空的会话列表，是无法进入会话页面的。开发者可在 [开发者后台 - 服务管理 - API 调用 - 消息服务](https://developer.rongcloud.cn/apitool/kNUDHRczlPHkECa0SJ8X3Q)  中调用发送系统消息接口给当前用户 tester1 发送消息。

   发送成功后，接收方的会话列表展示会话。

## 功能实现

Demo 实现了语音转文字功能，为了方便演示，代码中写死了用户id，需要向用户发送消息 展示会话页面后，向对方发送语音消息，长按后出现弹框"语音转文字"，点击后即可看到效果。




# 更多

[融云官网](https://www.rongcloud.cn/)

[融云文档](https://docs.rongcloud.cn/v4/?version=5.x)

[科大讯飞文档](https://www.xfyun.cn/service/lfasr)
