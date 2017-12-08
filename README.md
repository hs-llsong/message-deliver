# message-deliver
一个能够实时推送消息到微信、ios APP、android APP的服务端JAVA程序
- 前端可以通过redis 缓存直接发送待发消息，也可以通过http 协议从ali ons发送消息，最终都会汇聚到message-deliver从容分发。
- 目前移动推送渠道使用的是通过阿里的移动推送
- 消息传递管道使用的是阿里消息队队
