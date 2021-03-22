# Notes of JansusGraph Codes

|时间|内容|
|:---|:---|
|20200819||

## 术语

<!-- 记录阅读过程中出现的关键字及其简单的解释. -->

## 介绍

<!-- 描述软件的来源、特性、解决的关键性问题等. -->

## 动机

<!-- 描述阅读软件源码的动机, 要达到什么目的等. -->

## 系统结构

<!-- 描述软件的系统结构, 核心和辅助组件的结构; 系统较复杂时细分展示. -->

## 使用

<!-- 记录软件如何使用. -->

## 数据结构和算法

<!-- 描述软件中重要的数据结构和算法, 支撑过程部分的记录. -->

### 5 Schema and Data Modeling

Edge Label Property Key: key-value pairs on Vertex or Edge Vertex Label

Relation Type: Edge Label + Property Key

advance

- Static Vertex: 不可在创建它们的事务外修改
- TTL on Edge and Vertex: Cassandra支持
- single-valued property annotation: property on property, maybe for provenance information
- unidirected edge: 只能向out-going方向遍历; 构成超边(hyper-edges), 可加在Edge或Property上

### 8 Index

(1) graph index

从一组通过Property值标识的Edge或Vertex开始查询

分composite index和mixed index

(1.1) composite index

特定的已定义的属性组合上的等值查找

(1.2) mixed index

索引键上任意组合, 多个条件谓词

(2) vertex centric index

加速图遍历

Index Parameter and full-text search - 20

full text search field mapping

direct Index Query - 21

直接查询后端索引

Index Management - 28

手动重建索引

移除索引


### 33 Data Model

Example: [GraphApp.java](https://github.com/JanusGraph/janusgraph/blob/master/janusgraph-examples/example-common/src/main/java/org/janusgraph/example/GraphApp.java)

![](./images/jansusgraph-example-data.png)


Vertex Property: name, age, type

Edge Property: time, place, reason

- (a) 加粗键: 图索引; - age, reason, place
- (b) 加粗键带星号: 图索引, 唯一值; - name
- (c) 下划线键: Vertex索引 - time
- (d) 空箭头边: 函数唯一边/无重复 - father, mother
- (e) 十字边: 单向边/只能单向遍历 - lives


## 过程

<!-- 描述软件中重要的过程性内容, 例如服务器的启动、服务器响应客户端请求、服务器背景活动等. -->

## 文献引用

<!-- 记录软件相关和进一步阅读资料: 文献、网页链接等. -->

## 其他备注


```
=============================================================================== JanusGraph

=== 版本兼容性
https://docs.janusgraph.org/latest/version-compat.html


=== 后端存储
类似于BigTable数据表示的数据存储
org.janusgraph.diskstorage.keycolumnvalue.KeyColumnValueStore

--- Cassandra 3.11.2
基于Netflix astyanax Cassandra客户端的实现
https://github.com/Netflix/astyanax/wiki

=== 索引后端
--- ES: 6.2.4; 9200
https://www.elastic.co/guide/en/elasticsearch/reference/6.2/index.html

=============================================================================== Cache

there is a fucking cache!!!


=== 事务级别缓存: cache.tx-cache-size
Vertex缓存: Vertex和连接列表
Index缓存: Index的查询结果; 最大为一半

on heap

=== 数据库级别缓存: cache.db-cache, cache.db-cache-time, cache.db-cache-size, cache.db-cache-clean-wait

on heap

=== 存储后端自身的缓存

may off heap

=============================================================================== Cassandra

txlog                     - 事务日志
systemlog                 - 系统日志
janusgraph_ids            - ID
edgestore                 - 边
edgestore_lock_
graphindex                - 图索引
graphindex_lock_
system_properties         - 系统属性???
system_properties_lock_


gremlin.graph=org.janusgraph.core.JanusGraphFactory

org.apache.tinkerpop.gremlin.structure.util.GraphFactory.open(Configuration)
org.apache.tinkerpop.gremlin.structure.Graph.GRAPH - gremlin.graph
  org.janusgraph.core.JanusGraphFactory - 配置
    org.janusgraph.graphdb.database.StandardJanusGraph.StandardJanusGraph(GraphDatabaseConfiguration)

-- 事务提交
org.janusgraph.graphdb.transaction.StandardJanusGraphTx.commit()
  org.janusgraph.graphdb.database.StandardJanusGraph.commit(Collection<InternalRelation>, Collection<InternalRelation>, StandardJanusGraphTx)


JanusGraphTransaction: StandardJanusGraphTx


TODO(zhoujiagen) 以GraphOfTheGodsFactory为例, 整理调用链

[ENTRANCE: example or org.janusgraph.example.GraphOfTheGodsFactory.load(JanusGraph, String, boolean)]
org.janusgraph.example.JanusGraphApp.createElements()
  org.janusgraph.example.GraphApp.createElements()
    org.apache.tinkerpop.gremlin.structure.util.AbstractTransaction.commit()
      org.apache.tinkerpop.gremlin.structure.util.AbstractTransaction.doCommit()
        org.janusgraph.graphdb.transaction.StandardJanusGraphTx.commit()
          org.janusgraph.graphdb.database.StandardJanusGraph.commit(Collection<InternalRelation>, Collection<InternalRelation>, StandardJanusGraphTx)


-- 添加Vertex
org.janusgraph.graphdb.transaction.StandardJanusGraphTx.addVertex(Long, VertexLabel)
  org.janusgraph.graphdb.transaction.StandardJanusGraphTx.addProperty(JanusGraphVertex, PropertyKey, Object)
  org.janusgraph.graphdb.transaction.StandardJanusGraphTx.addEdge(JanusGraphVertex, JanusGraphVertex, EdgeLabel)


=============================================================================== ES

```
