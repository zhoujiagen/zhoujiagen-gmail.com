# Notes of ElasticSearch Codes

|时间|内容|
|:---|:---|
|20181206|kick off.|

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

> 参考 Elasticsearch服务器开发(第2版) 的目录.

### 2 索引

#### 分片和副本

#### 映射配置
- 设置映射: https://www.elastic.co/guide/en/elasticsearch/reference/6.2/indices-put-mapping.html
- 获取映射: https://www.elastic.co/guide/en/elasticsearch/reference/6.2/indices-get-mapping.html
- 获取字段映射: https://www.elastic.co/guide/en/elasticsearch/reference/6.2/indices-get-field-mapping.html
https://www.elastic.co/guide/en/elasticsearch/reference/6.2/mapping.html

例子: 创建索引时指定映射类型(mapping type)

```
PUT "/my_index"
{
 "mappings": {
   "doc": {
     "properties": {
       "title":    { "type": "text"  },
       "name":     { "type": "text"  },
       "age":      { "type": "integer" },
       "created":  {
         "type":   "date",
         "format": "strict_date_optional_time||epoch_millis"
       }
     }
   }
 }
}
```

#### 批量索引
#### 扩展索引结构的内部信息
#### 段合并
### 3 搜索
#### 查询入门
#### 理解查询过程
#### 基本查询
https://www.elastic.co/guide/en/elasticsearch/reference/6.2/query-dsl.html

- 词条查询: term
- 多词条查询
- match_all查询
- 常用词查询: common
- match查询: 分析后查询, 不支持Lucene查询语法, 例:

- 布尔匹配查询;
- match_phrase查询: 从分析后的文本中构建短语查询;
- match_phrase_prefix查询: 允许查询文本的最后一个词条只做前缀匹配
- multi_match查询: 通过fields参数针对多个字段查询
- query_string查询: 支持全部的Lucene查询语法
- simple_query_string查询: 类似于query_string, 遇到解析错误时不抛出异常而是丢弃无效部分执行其余部分

- ids标识符查询
- prefix查询: 特定字段以给定的前缀开始
- fuzzy_like_this查询: 类似于more_like_this, 但利用模糊字符串并选择生成的最佳差分词条
- fuzzy_like_this_field查询: 类似于fuzzy_like_this, 但只能对应单个字段
- fuzzy查询: 基于编辑距离算法匹配文档
- wildcard通配符查询: *?
- more_like_this查询: 得到与提供的文本类似的文档
- more_like_this_field查询: 类似于more_like_this, 但只能对应单个字段
- range范围查询: 找到某一字段(数值或字符串)在某个范围内的文档, 只能对应单个字段
- dismax最大分查询: 生成由所有子查询返回的文档组成的并集, 可以控制较低得分的子查询对文档最后得分的影响
- egexp正则表达式查询: 如果正则表达式匹配许多词条, 查询将很慢

#### 复合查询

- bool布尔查询: should, must, must_not
- boosting加权查询: positive, negative, negative_boost
- constant_score查询: 封装另一个查询或过滤, 并为返回的文档返回一个常量得分
- indices索引查询: query, no_match_query

#### 查询结果过滤

过滤器不影响评分, 应用在整个索引的内容上, 可以被缓存 ES在内部为每个过滤器都建立bitset结构

- post_filter: 后于查询执行
- filtered: 先于查询执行

过滤器:

- range范围过滤器: gt, lt, gte, lte; 变种: numeric_filter
- exists过滤器: 过滤掉给定字段上没有值的文档
- missing过滤器: 过滤掉给定字段上有值的文档
- script脚本过滤器: 通过计算值过滤文档
- type类型过滤器: 限制文档的类型
- limit限定过滤器: 限定单个分片返回的文档数量
- ids标识符过滤器: 过滤成具体的文档

可以在过滤器中封装几乎所有查询, 返回的每个文档得分都是1.0 专用的过滤器版本: bool, geo_shape, has_child, has_parent, ids, indices, match_all nested, prefix, range, regexp, term, terms

组合过滤器: bool; and/or/not

过滤器的缓存: _cache 结果总被缓存的过滤器: exists, missing, range, term, terms; 缓存无效的过滤器: ids, match_all, limit

可以使用filtered给过滤器命名, 名字将随匹配文档返回

#### 高亮显示

Lucene的高亮实现:

- 标准类型: 字段设为stored, 或字段包含在_source字段中
- FastVectorHighlighter: term_vector=with_positions_offsets
- PostingsHighlighter: index_options=offsets

#### 验证查询

```
/_search => /_validate/query?pretty&explain
```

#### 数据排序

```
sort: []
```

#### 查询重写

找到相关的词条, 将查询重写为性能更好的查询

rewrite:

- scoring_boolean: 将生成的每个词条翻译未bool查询的一个should子句, CPU密集型
- constant_score_boolean: 类似于scoring_boolean, 但不需要计算得分, CPU要求低
- constant_score_filter: 创建一个私有过滤器来重写查询
- top_terms_N: 与scoring_boolean类似, 但只保留N个得分最高的词条
- top_terms_boost_N: 类似于top_terms_N, 但分数只由加权计算而来, 而不是查询

### 4 扩展索引结构

#### 索引树形结构
#### 索引非扁平数据
#### 使用嵌套对象
#### 使用父子关系
#### 更新索引结构API

### 5 更好的搜索

### 6 超越全文检索

### 7 深入ES集群

### 8 集群管理


## 数据结构和算法

<!-- 描述软件中重要的数据结构和算法, 支撑过程部分的记录. -->

## 过程

<!-- 描述软件中重要的过程性内容, 例如服务器的启动、服务器响应客户端请求、服务器背景活动等. -->

## 文献引用

<!-- 记录软件相关和进一步阅读资料: 文献、网页链接等. -->


- 【词向量介绍】《king - man + woman is queen; but why?》by Piotr Migdał http://p.migdal.pl/2017/01/06/king-man-woman-queen-why.html
- 词向量可视化浏览器word2viz https://lamyiowce.github.io/word2viz/
- 实践版本: 6.2.4 https://www.elastic.co/guide/en/elasticsearch/reference/6.2/index.html
- Elasticsearch Clients: https://www.elastic.co/guide/en/elasticsearch/client/index.html

## 其他备注
