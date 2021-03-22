# Notes of AllegroGraph

|时间|内容|
|:---|:---|
|20190226|kick off|

## 术语

<!-- 记录阅读过程中出现的关键字及其简单的解释. -->

## 介绍

<!-- 描述软件的来源、特性、解决的关键性问题等. -->

AllegroGraph was developed to meet W3C standards for the **RDF**. It is a reference implementation for the **SPARQL** protocol.

The first version of AllegroGraph was made available at the end of 2004.

The free version lets you load a maximum of 5,000,000 triples, and has no expiration date.

## 动机

<!-- 描述阅读软件源码的动机, 要达到什么目的等. -->

## 系统结构

<!-- 描述软件的系统结构, 核心和辅助组件的结构; 系统较复杂时细分展示. -->

## 使用

<!-- 记录软件如何使用. -->

安装

```
cd /home/zhoujiagen
mkdir -p tmp/ag6.4.2
cd /home/zhoujiagen/agraph-6.4.2
./install-agraph /home/zhoujiagen/tmp/ag6.4.2/
netstat -an | grep 10035
```

启停

```
/home/zhoujiagen/tmp/ag6.4.2/bin/agraph-control --config /home/zhoujiagen/tmp/ag6.4.2/lib/agraph.cfg start
/home/zhoujiagen/tmp/ag6.4.2/bin/agraph-control --config /home/zhoujiagen/tmp/ag6.4.2/lib/agraph.cfg stop
```

数据

- 中国旅游景点知识图谱 http://openkg.cn/dataset/tourist-attraction

示例SPARQL查询

```
# t: http://www.brain-inspired-cognitive-engine.org/knowledge-engine/cas-kb/
# 所属地区
#SELECT DISTINCT ?l1 ?l2
#WHERE { ?s rdfs:label ?l1.
#  ?s t:suoshudiqu ?o.
#      ?o rdfs:label ?l2.
#}

# 实例
SELECT DISTINCT ?sl ?ol
WHERE {
?s rdfs:label ?sl.
  ?o t:shili ?s. # techan
  ?o rdfs:label ?ol
}
```



## 数据结构和算法

<!-- 描述软件中重要的数据结构和算法, 支撑过程部分的记录. -->

## 过程

<!-- 描述软件中重要的过程性内容, 例如服务器的启动、服务器响应客户端请求、服务器背景活动等. -->

## 文献引用

<!-- 记录软件相关和进一步阅读资料: 文献、网页链接等. -->

- AllegroGraph 6.4.2 Documentation Index https://franz.com/agraph/support/documentation/current/index.html


## 其他备注
