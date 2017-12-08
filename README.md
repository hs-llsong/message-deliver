# message-deliver
一个能够实时推送消息到微信、ios APP、android APP的服务端JAVA程序
- 前端可以通过redis 缓存直接发送待发消息，也可以通过http 协议从ali ons发送消息，最终都会汇聚到message-deliver从容分发。
- 目前移动推送渠道使用的是通过阿里的移动推送
- 消息传递管道使用的是阿里消息队列
- 配置文件 conf/config.properites
# 配置文件示例
#Consumer topics,多个以,号隔开

ConsumeTopics=templatemessage

#和topic对应的channel name 格式= UPCASE(topic)_CHANNEL_NAME

TEMPLATEMESSAGE_CHANNEL_NAME=NotificationTest

#每个channel 的配置

NotificationTestConsumerId=CID-message001
NotificationTestProducerId=PID-message001

#每个channel对应的阿里access 权限配置

NotificationTestAccessKey=
NotificationTestSecretKey=
NotificationTestTopic=templatemessage

#每个channel consumer 关注的tag

NotificationTestConsumerTag=heishi||iosapp||androidapp||weixinapp

#阿里接口访问access key

AccessKey=
SecretKey=

#微信的token获取相关配置

WeixinAppId=
WeixinAppSecret=

#redis 配置

RedisHost=heishimaster.redis
RedisPort=6379
RedisAuth=

#是否只读访问微信accessToken
- 如果true,会自动定时从微信接口刷新token到redis

WxAccessTokenReadOnly=false
RedisTokenKey=heimarket_wx_token
RedisTicketKey=heimarket_wx_ticket

#是否开启从redis获取待推送的消息
- true 打开redis 监听
- false 关闭

RedisCacheListening=false
RedisHsMessageCacheKey=the_queue_of_aliyun_os_message

#redis 获取消息后推送到阿里ons 的Topic名

RedisHsMessageProducerTopic=templatemessage

#对应的微信消息模板

#
COLOR_APPROVAL=#16C0B4
TEMPLATE_ID_APPROVAL=4bKgCso86d6grW5nQjUYEuyOCvQqarei7Ke3_PFsIyk
JUMP_URL_APPROVAL=url
#
COLOR_REPLAY=#67CB87
TEMPLATE_ID_REPLAY=zXuwCzmHK_V0aXpeRUtL6H5OjrwPNHcLNwG-7gT-6vo
JUMP_URL_REPLAY=url
#
COLOR_TRADING=#F53D84
TEMPLATE_ID_TRADING=KF5CCA2T9-8uuoeiepEcw4oJ1TPJKE58nlWkcWF0haY
JUMP_URL_TRADING=url
#
COLOR_DELIVER=#f1c232
TEMPLATE_ID_DELIVER=fLAsYuCySkKrtOloigiJDW6HSIiIVTbc6vUTOu2fDLs
JUMP_URL_DELIVER=url
#
COLOR_UPDATE=#1C86EE
TEMPLATE_ID_UPDATE=PQxo56t5fcS1LP1kOAXFCgebGSNbbIKNs5HthcB_PBQ
JUMP_URL_UPDATE=url

COLOR_SYSTME_NOTIFY=#030303
TEMPLATE_ID_SYSTEM_NOTIFY=cUkeDhqstEws1VH6ZBRgR40IuAA6KFdc6I_ulQwFzLA
JUMP_URL_SYSTEM_NOTIFY=url

#阿里移动推送配置

AMSRegion=cn-hangzhou
AMSAppkey=
AMSApnsEnv=DEV
