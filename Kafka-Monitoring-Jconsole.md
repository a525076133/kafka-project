# 监控Kafka和Kafka Connector

该部分为可选项，不影响sink task正常工作，开启后可监控kafka和kafka connector的相关参数和指标。

## 启动流程

该部分为可选项，可以监控kafka和kafka connector的相关参数和指标。

**1.启动Kafka**

之前启动kafka的命令为`bin/kafka-server-start.sh config/server.properties`。

若要监控kafka相关指标，需要在启动kafka命令前加上`JMX_PORT=${port}`，`${port}`为端口号，可以为任何未被占用的端口。例如9988。

现在启动命令变为：`JMX_PORT=9988 bin/kafka-server-start.sh config/server.properties`。

**2.监控kafka**

打开终端，输入`jconsole`，出现如下窗口。

<img src="https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220831173016953.png" alt="image-20220831173016953" style="zoom:50%;" />

选择远程进程，输入`localhost:port`，port为设置的端口号。

<img src="https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220831173415143.png" alt="image-20220831173415143" style="zoom:50%;" />

点击MBean，即可查看kafka相关信息。

**3.启动Kafka Connect Worker**

之前启动Kafka Connect Worker的命令为`bin/connect-distributed.sh config/connect-distributed.properties`。

若要监控Connector相关指标，需要在启动命令前加上`JMX_PORT=${port}`，`${port}`为端口号，可以为任何未被占用的端口。例如9989。

现在启动命令变为：`JMX_PORT=9989 bin/connect-distributed.sh config/connect-distributed.properties`。

**4.监控kafka Connector**

打开终端，输入`jconsole`，出现如下窗口。

<img src="https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220831173931245.png" alt="image-20220831173931245" style="zoom:50%;" />

选择远程进程，输入`localhost:port`，port为设置的端口号。

![image-20220831174018748](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220831174018748.png)

点击MBean，即可查看kafka connector相关信息。

例如，下图显示名为reporter的connector的sink-task-metrics相关的属性和数值。

<img src="https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220831174059919.png" alt="image-20220831174059919" style="zoom:50%;" />

*监控kafka和监控kafka connector相互独立，不必先监控kafka再监控kafka connector。监控kafka connector可以单独启动。*

## 参数说明

监控指标中的属性描述请参考：

https://docs.confluent.io/platform/current/connect/monitoring.html

以sink task metrics为例介绍一些常用指标。

### Sink task metrics

| 指标                         | 说明                                                         |
| :--------------------------- | ------------------------------------------------------------ |
| sink-record-read-rate        | 从 Kafka 读取的平均每秒记录数                                |
| sink-record-read-total       | 指定sink connector的任务产生或轮询的记录总数（自上次重新启动任务以来） |
| sink-record-send-rate        | 从转换输出并发送到工作器中属于指定接收器连接器的任务的平均每秒记录数（不包括被transformation过滤掉的任何记录） |
| sink-record-send-total       | transformation输出并发送到指定任务的记录总数（自上次重新启动任务以来） |
| sink-record-active-count     | 从Kafka读取但尚未被sink task完全提交、刷新或确认的最新记录数 |
| sink-record-active-count-max | 从Kafka读取但尚未被sink task完全提交、刷新或确认的最大记录数 |
| sink-record-active-count-avg | 从Kafka读取但尚未被sink task完全提交、刷新或确认的平均记录数 |
| partition-count              | 分配给task的topic分区数                                      |
| put-batch-max-time-ms        | 此task放置一批sink记录所用的最长时间（以毫秒为单位）         |
| put-batch-avg-time-ms        | 此task放置一批 sink记录所用的平均时间（以毫秒为单位）        |



