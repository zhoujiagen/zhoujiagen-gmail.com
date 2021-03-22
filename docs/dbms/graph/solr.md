# Notes of Apache Solr

|时间|内容|
|:---|:---|
|20181206| kick off.|

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


### 启动

```
# clean
bin/solr stop -all ; rm -Rf example/cloud/

#启动Solr Server
bin/solr start -e cloud -noprompt
```

### 索引文件

```
bin/post -c gettingstarted docs/
  -c gettingstarted: name of the collection to index into
  docs/: a relative path of the Solr install docs/ directory
```
索引操作的输入格式和技术

- Solr XML

```
# 使用SimplePostTool
bin/post -c gettingstarted example/exampledocs/*.xml
```
- Solr JSON

```
bin/post -c gettingstarted example/exampledocs/books.json
```

- csv

```
bin/post -c gettingstarted example/exampledocs/books.csv
```

- Data Import Handler (DIH) 数据库
- 客户端, SolrJ等
- Admin UI

### 更新和删除文档

uniqueKey字段

```
bin/post -c gettingstarted -d "SP2514N"
```

### 搜索

#### 1 Basics

(1) term查询q

```
curl "http://localhost:8983/solr/gettingstarted/select?wt=json&indent=true&q=foundation"
```

指定查询字段(fl)

```
curl "http://localhost:8983/solr/gettingstarted/select?wt=json&indent=true&q=foundation&fl=id"
curl "http://localhost:8983/solr/gettingstarted/select?wt=json&indent=true&q=name:foundation"
```

(2) Phrase search

```
curl "http://localhost:8983/solr/gettingstarted/select?wt=json&indent=true&q=\"CAS+latency\""
```

(3) combining search

```
+one +three
curl "http://localhost:8983/solr/gettingstarted/select?wt=json&indent=true&q=%2Bone+%2Bthree"
+two -one
curl "http://localhost:8983/solr/gettingstarted/select?wt=json&indent=true&q=%2Btwo+-one"
```

Search in Depth: https://cwiki.apache.org/confluence/display/solr/Searching

#### 2 Faceting

(1) field facets

```
rows=0指定只返回facets不返回文档
curl http://localhost:8983/solr/gettingstarted/select?wt=json&indent=true&q=*:*&rows=0 &facet=true&facet.field=manu_id_s
```

(2) range facets

查询串参数可以放在AdminUI的Raw Query Parameters中:

```
curl http://localhost:8983/solr/gettingstarted/select?q=*:*&wt=json&indent=on&rows=0&facet=true &facet.range=price &f.price.facet.range.start=0 &f.price.facet.range.end=600 &f.price.facet.range.gap=50 &facet.range.other=after
```

(3) pivot facets - decison tree

```
curl http://localhost:8983/solr/gettingstarted/select?q=*:*&rows=0&wt=json&indent=on &facet=on&facet.pivot=cat,inStock
```

more information: https://cwiki.apache.org/confluence/display/solr/Faceting

### 应用配置

(1) Standalone Mode

```
<solr-home-directory>/
 solr.xml
 core_name1/
    core.properties
    conf/
       solrconfig.xml
       schema.xml
    data/
 core_name2/
    core.properties
    conf/
       solrconfig.xml
       schema.xml
    data/
```

(2) SolrCloud Mode
￼
```
<solr-home-directory>/
 solr.xml
 core_name1/
    core.properties
    data/
 core_name2/
    core.properties
    data/
```

## 数据结构和算法

<!-- 描述软件中重要的数据结构和算法, 支撑过程部分的记录. -->

## 过程

<!-- 描述软件中重要的过程性内容, 例如服务器的启动、服务器响应客户端请求、服务器背景活动等. -->

## 文献引用

<!-- 记录软件相关和进一步阅读资料: 文献、网页链接等. -->

## 其他备注
