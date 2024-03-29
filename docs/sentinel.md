## 降级熔断

### 降级策略
- 慢调用比例/RT(平均响应时间)

  选择以慢调用比例作为阈值，需要设置允许的慢调用 RT（即最大的响应时间），请求的响应时间大于该值`最大RT`则统计为慢调用。当单位`统计时长`（statIntervalMs）内请求数目大于设置的`最小请求数`目，并且慢调用的比例大于`比例阈值`，则接下来的`熔断时长`内请求会自动被熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求响应时间小于设置的慢调用 RT 则结束熔断，若大于设置的慢调用 RT 则会再次被熔断

## 流控

### 流控规则

- 针对来源:Sentinel可以针对调用者进行限流，填写微服务名

- 阈值类型

  1. QPS:  每秒钟的请求数量，当调用该api的QPS达到阈值时，进行限流操作

  2. 线程数: 当调用该api的请求线程数达到阈值时，触发限流

- 是否集群：不需要集群

- 流控模式：

  1. 直接：API达到限流条件时，直接限流
  2. 关联：当关联的资源达到阈值时，就限流当前资源
  3. 链路：只记录指定链路上的流量（指定资源从入口资源进来的流量，如果达到阈值，就进行限流）【API级别的针对来源】。
- 流控效果：

  1. 快速失败：直接失败，抛出异常
  2. Warm up：根据Code Factor的值（冷加载因子，默认3），从阈值/code Factor，经过预热时长，才达到设置的QPS阈值。
  3. 排队等待：匀速排队，让请求以匀速通过，阈值类型必须设置为QPS，否则无效。 