# Notes of Dgraph Codes

|时间|内容|
|:---|:---|
|2021-02-5|kick off.|

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

### 实例

Dgraph cluster consists of different nodes (Zero, Alpha & Ratel), and each node serves a different purpose.

- **Dgraph Zero** controls the Dgraph cluster, assigns servers to a group, and re-balances data between server groups.
- **Dgraph Alpha** hosts predicates and indexes. Predicates are either the properties associated with a node or the relationship between two nodes. Indexes are the tokenizers that can be associated with the predicates to enable filtering using appropriate functions.
- **Ratel** serves the UI to run queries, mutations & altering schema.

Default ports used by different nodes[^1][^2]:

|Dgraph Node Type|gRPC internal private|gRPC external private|gRPC external public|HTTP external private|HTTP external public|
|:---|:---|:---|:---|:---|:---|
|zero|50801|50801||60802||
|alpha|7080||9080||8080|
|ratel|||||8000|

[^1]: Dgraph Zero uses port 5080 for internal communication within the cluster, and to support the fast data loading tools: Dgraph Live Loader and Dgraph Bulk Loader.
[^2]: Dgraph Zero uses port 6080 for administrative operations. Dgraph clients cannot access this port.


```
docker run --rm -it \
  -p 8088:8080 \
  -p 9088:9080 \
  -p 8009:8000 \
  -v .:/dgraph dgraph/standalone:v20.11.1
```

- `/graphql` is where you’ll find the GraphQL API for the types you’ve added. That is the single GraphQL entry point for your apps to Dgraph.
- `/admin` is where you’ll find an admin API for administering your GraphQL instance. That’s where you can update your GraphQL schema, perform health checks of your backend, and more.
	application/graphql
	application/json

### 概念

GraphQL

- Introduction to GraphQL: https://graphql.org/learn/
- Spec: http://spec.graphql.org/
- Overview: https://dgraph.io/docs/graphql/overview/

- Schema Overview: https://dgraph.io/docs/graphql/schema/schema-overview/
- Index of Directives: https://dgraph.io/docs/graphql/directives/
- Administrative API: https://dgraph.io/docs/graphql/admin/


DQL

- Get Started - Quickstart Guide: https://dgraph.io/docs/get-started/

Client

- Python: https://dgraph.io/docs/clients/python/


## 数据结构和算法

<!-- 描述软件中重要的数据结构和算法, 支撑过程部分的记录. -->

## 过程

<!-- 描述软件中重要的过程性内容, 例如服务器的启动、服务器响应客户端请求、服务器背景活动等. -->

## 文献引用

<!-- 记录软件相关和进一步阅读资料: 文献、网页链接等. -->

https://dgraph.io/dgraph

## 其他备注
