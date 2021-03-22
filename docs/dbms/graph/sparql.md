# Note of SPARQL 1.1


|时间|内容|
|:---|:---|
|20190226|kick off.|
|20190408|add SPARQL 1.1 outline.|

## 术语

<!-- 记录阅读过程中出现的关键字及其简单的解释. -->

## 介绍

<!-- 描述书籍阐述观点的来源、拟解决的关键性问题和采用的方法论等. -->

## 动机

<!-- 描述阅读书籍的动机, 要达到什么目的等. -->

## 概念结构

<!-- 描述书籍的行文结构, 核心主题和子主题的内容结构和关系. -->

### Detailed Interpretation

通用的命名空间

|Prefix|IRI|
|:---|:---|
|rdf|http://www.w3.org/1999/02/22-rdf-syntax-ns# |
||xsd|http://www.w3.org/2001/XMLSchema# |
rdfs|http://www.w3.org/2000/01/rdf-schema# |
|fn|http://www.w3.org/2005/xpath-functions# |
|sfn|http://www.w3.org/ns/sparql# |

文档中使用Turtle数据格式展示triple

绑定: (变量, RDF项)

RDF相关概念: IRI, literal, lexical form, plain literal, language tag, typed literal, datatype IRI, blank node[^1]

- RDF Term: which includes IRIs, blank nodes and literals
- Simple Literal: which covers literals without language tag or datatype IRI

[^1]: https://www.w3.org/TR/2004/REC-rdf-concepts-20040210/

基本图模式(basic graph pattern): 一组triple模式

空节点(blank node): :_加上空节点标签

复杂图模式的构建块:

- 基本图模式: 必须匹配一组triple; 使用子图匹配方法
- 分组图模式: 必须全部匹配一组图模式; 由`{}`标识<br/>
  可选图模式: 额外模式可以扩展结果<br/>
  备选图模式: 尝试两个或者更多的模式<br/>
  命名图模式: 模式与命名图匹配

包含可选值:

OPTIONAL是左结合的

### Query Form Example

#### SELECT

```
@prefix  foaf:  <http://xmlns.com/foaf/0.1/> .

_:a	foaf:name   "Alice" .
_:a	foaf:knows  _:b .
_:a	foaf:knows  _:c .

_:b	foaf:name   "Bob" .

_:c	foaf:name   "Clare" .
_:c	foaf:nick   "CT" .


PREFIX foaf:	<http://xmlns.com/foaf/0.1/>
SELECT ?nameX ?nameY ?nickY
WHERE
  { ?x foaf:knows ?y ;
   	foaf:name ?nameX .
	?y foaf:name ?nameY .
	OPTIONAL { ?y foaf:nick ?nickY }
  }
```

```
nameX nameY nickY
"Alice"    "Clare"    "CT"
"Alice"    "Bob"   
```

#### CONSTRUCT

```
PREFIX foaf:    <http://xmlns.com/foaf/0.1/>
PREFIX vcard:   <http://www.w3.org/2001/vcard-rdf/3.0#>
CONSTRUCT   { <http://example.org/person#Alice> vcard:FN ?name }
WHERE       { ?x foaf:name ?name;
                    foaf:nick "CT"}
```

```
Subject Predicate Object Graph
Alice    FN    "Clare"    (null)
```

#### ASK

```
PREFIX foaf:    <http://xmlns.com/foaf/0.1/>
ASK  { ?x foaf:name  "Alice" }
```

```
True
```

#### DESCRIBE

```
PREFIX foaf:   <http://xmlns.com/foaf/0.1/>
DESCRIBE ?x
WHERE { ?x foaf:name  "Alice" }
```

```
Subject                  Predicate    Object    Graph
_:b391EA535x4    foaf:name    "Alice"    (null)
_:b391EA535x4    foaf:knows    _:b391EA535x5    (null)
_:b391EA535x4    foaf:knows    _:b391EA535x6    (null)
_:b391EA535x6    foaf:name    "Clare"    (null)
_:b391EA535x6    foaf:nick    "CT"    (null)
_:b391EA535x5    foaf:name    "Bob"    (null)
```

### SPARQL 1.1 Query Language 目录

```
1 Introduction
    1.1 Document Outline
    1.2 Document Conventions
        1.2.1 Namespaces
        1.2.2 Data Descriptions
        1.2.3 Result Descriptions
        1.2.4 Terminology
2 Making Simple Queries (Informative)                                           - 执行简单查询
    2.1 Writing a Simple Query
    2.2 Multiple Matches
    2.3 Matching RDF Literals
        2.3.1 Matching Literals with Language Tags
        2.3.2 Matching Literals with Numeric Types
        2.3.3 Matching Literals with Arbitrary Datatypes
    2.4 Blank Node Labels in Query Results
    2.5 Creating Values with Expressions
    2.6 Building RDF Graphs
3 RDF Term Constraints (Informative)                                            - RDF项约束
    3.1 Restricting the Value of Strings
    3.2 Restricting Numeric Values
    3.3 Other Term Constraints

4 SPARQL Syntax                                                                 - SPARQL语法
    4.1 RDF Term Syntax
        4.1.1 Syntax for IRIs
            4.1.1.1 Prefixed Names
            4.1.1.2 Relative IRIs
        4.1.2 Syntax for Literals
        4.1.3 Syntax for Query Variables
        4.1.4 Syntax for Blank Nodes
    4.2 Syntax for Triple Patterns
        4.2.1 Predicate-Object Lists
        4.2.2 Object Lists
        4.2.3 RDF Collections
        4.2.4 rdf:type

5 Graph Patterns                                                                - 图模式
    5.1 Basic Graph Patterns
        5.1.1 Blank Node Labels
        5.1.2 Extending Basic Graph Pattern Matching
    5.2 Group Graph Patterns
        5.2.1 Empty Group Pattern
        5.2.2 Scope of Filters
        5.2.3 Group Graph Pattern Examples

6 Including Optional Values                                                     - 包含可选值
    6.1 Optional Pattern Matching
    6.2 Constraints in Optional Pattern Matching
    6.3 Multiple Optional Graph Patterns

7 Matching Alternatives                                                         - 匹配备选

8 Negation - 补
    8.1 Filtering Using Graph Patterns
        8.1.1 Testing For the Absence of a Pattern
        8.1.2 Testing For the Presence of a Pattern
    8.2 Removing Possible Solutions
    8.3 Relationship and differences between NOT EXISTS and MINUS
        8.3.1 Example: Sharing of variables
        8.3.2 Example: Fixed pattern
        8.3.3 Example: Inner FILTERs

9 Property Paths                                                                - 属性路径
    9.1 Property Path Syntax
    9.2 Examples
    9.3 Property Paths and Equivalent Patterns
    9.4 Arbitrary Length Path Matching

10 Assignment                                                                   - 赋值
    10.1 BIND: Assigning to Variables
    10.2 VALUES: Providing inline data
        10.2.1 VALUES syntax
        10.2.2 VALUES Examples

11 Aggregates                                                                   - 聚合
    11.1 Aggregate Example
    11.2 GROUP BY
    11.3 HAVING
    11.4 Aggregate Projection Restrictions
    11.5 Aggregate Example (with errors)

12 Subqueries                                                                   - 子查询

13 RDF Dataset                                                                  - RDF数据集
    13.1 Examples of RDF Datasets
    13.2 Specifying RDF Datasets
        13.2.1 Specifying the Default Graph
        13.2.2 Specifying Named Graphs
        13.2.3 Combining FROM and FROM NAMED
    13.3 Querying the Dataset
        13.3.1 Accessing Graph Names
        13.3.2 Restricting by Graph IRI
        13.3.3 Restricting Possible Graph IRIs
        13.3.4 Named and Default Graphs

14 Basic Federated Query                                                        - 基本联合查询

15 Solution Sequences and Modifiers
    15.1 ORDER BY
    15.2 Projection
    15.3 Duplicate Solutions
    15.4 OFFSET
    15.5 LIMIT

16 Query Forms                                                                  - 查询形式
    16.1 SELECT
        16.1.1 Projection
        16.1.2 SELECT Expressions
    16.2 CONSTRUCT
        16.2.1 Templates with Blank Nodes
        16.2.2 Accessing Graphs in the RDF Dataset
        16.2.3 Solution Modifiers and CONSTRUCT
        16.2.4 CONSTRUCT WHERE
    16.3 ASK
    16.4 DESCRIBE (Informative)
        16.4.1 Explicit IRIs
        16.4.2 Identifying Resources
        16.4.3 Descriptions of Resources

17 Expressions and Testing Values                                               - 表达式和测试值
    17.1 Operand Data Types                      				                        - 操作数数据类型
    17.2 Filter Evaluation                        				                      - 过滤器求值
        17.2.1 Invocation
        17.2.2 Effective Boolean Value (EBV)
    17.3 Operator Mapping                         				                      - 操作符映射
        17.3.1 Operator Extensibility
    17.4 Function Definitions                     				                      - 函数定义
        17.4.1 Functional Forms                   				                      -- 函数形式
            17.4.1.1 bound
            17.4.1.2 IF
            17.4.1.3 COALESCE
            17.4.1.4 NOT EXISTS and EXISTS
            17.4.1.5 logical-or
            17.4.1.6 logical-and
            17.4.1.7 RDFterm-equal
            17.4.1.8 sameTerm
            17.4.1.9 IN
            17.4.1.10 NOT IN
        17.4.2 Functions on RDF Terms             				                      -- RDF项上函数
            17.4.2.1 isIRI
            17.4.2.2 isBlank
            17.4.2.3 isLiteral
            17.4.2.4 isNumeric
            17.4.2.5 str
            17.4.2.6 lang
            17.4.2.7 datatype
            17.4.2.8 IRI
            17.4.2.9 BNODE
            17.4.2.10 STRDT
            17.4.2.11 STRLANG
            17.4.2.12 UUID
            17.4.2.13 STRUUID
        17.4.3 Functions on Strings               				                      -- 字符串函数
            17.4.3.1 Strings in SPARQL Functions
                17.4.3.1.1 String arguments
                17.4.3.1.2 Argument Compatibility Rules
                17.4.3.1.3 String Literal Return Type
            17.4.3.2 STRLEN
            17.4.3.3 SUBSTR
            17.4.3.4 UCASE
            17.4.3.5 LCASE
            17.4.3.6 STRSTARTS
            17.4.3.7 STRENDS
            17.4.3.8 CONTAINS
            17.4.3.9 STRBEFORE
            17.4.3.10 STRAFTER
            17.4.3.11 ENCODE_FOR_URI
            17.4.3.12 CONCAT
            17.4.3.13 langMatches
            17.4.3.14 REGEX
            17.4.3.15 REPLACE
        17.4.4 Functions on Numerics              				                      -- 数值函数
            17.4.4.1 abs
            17.4.4.2 round
            17.4.4.3 ceil
            17.4.4.4 floor
            17.4.4.5 RAND
        17.4.5 Functions on Dates and Times       			                        -- 日期和时间函数
            17.4.5.1 now
            17.4.5.2 year
            17.4.5.3 month
            17.4.5.4 day
            17.4.5.5 hours
            17.4.5.6 minutes
            17.4.5.7 seconds
            17.4.5.8 timezone
            17.4.5.9 tz
        17.4.6 Hash Functions                     				                      -- 哈希函数
            17.4.6.1 MD5
            17.4.6.2 SHA1
            17.4.6.3 SHA256
            17.4.6.4 SHA384
            17.4.6.5 SHA512
    17.5 XPath Constructor Functions              				                      -- XPath构造函数
    17.6 Extensible Value Testing                 				                      -- 自定义扩展函数

18 Definition of SPARQL                                                         - SPARQL定义
    18.1 Initial Definitions                      					                    -- 初始定义
        18.1.1 RDF Terms
        18.1.2 Simple Literal
        18.1.3 RDF Dataset
        18.1.4 Query Variables
        18.1.5 Triple Patterns
        18.1.6 Basic Graph Patterns
        18.1.7 Property Path Patterns
        18.1.8 Solution Mapping
        18.1.9 Solution Sequence Modifiers
        18.1.10 SPARQL Query
    18.2 Translation to the SPARQL Algebra        			                        -- 翻译成SPARQL代数
        18.2.1 Variable Scope
        18.2.2 Converting Graph Patterns
            18.2.2.1 Expand Syntax Forms
            18.2.2.2 Collect FILTER Elements
            18.2.2.3 Translate Property Path Expressions
            18.2.2.4 Translate Property Path Patterns
            18.2.2.5 Translate Basic Graph Patterns
            18.2.2.6 Translate Graph Patterns
            18.2.2.7 Filters of Group
            18.2.2.8 Simplification step
        18.2.3 Examples of Mapped Graph Patterns
        18.2.4 Converting Groups, Aggregates, HAVING, final VALUES clause and SELECT Expressions
            18.2.4.1 Grouping and Aggregation
            18.2.4.2 HAVING
            18.2.4.3 VALUES
            18.2.4.4 SELECT Expressions
        18.2.5 Converting Solution Modifiers
            18.2.5.1 ORDER BY
            18.2.5.2 Projection
            18.2.5.3 DISTINCT
            18.2.5.4 REDUCED
            18.2.5.5 OFFSET and LIMIT
            18.2.5.6 Final Algebra Expression
    18.3 Basic Graph Patterns                     			                       	-- 基本的图模式
        18.3.1 SPARQL Basic Graph Pattern Matching
        18.3.2 Treatment of Blank Nodes
    18.4 Property Path Patterns                   				                      -- 属性路径模式
    18.5 SPARQL Algebra                           				                      -- SPARQL代数
        18.5.1 Aggregate Algebra
            18.5.1.1 Set Functions
            18.5.1.2 Count
            18.5.1.3 Sum
            18.5.1.4 Avg
            18.5.1.5 Min
            18.5.1.6 Max
            18.5.1.7 GroupConcat
            18.5.1.8 Sample
    18.6 Evaluation Semantics                     				                      -- 求值语义
    18.7 Extending SPARQL Basic Graph Matching    			                        -- 扩展SPARQL基本的图匹配
        18.7.1 Notes

19 SPARQL Grammar - SPARQL文法
    19.1 SPARQL Request String
    19.2 Codepoint Escape Sequences
    19.3 White Space
    19.4 Comments
    19.5 IRI References
    19.6 Blank Nodes and Blank Node Labels
    19.7 Escape sequences in strings
    19.8 Grammar

20 Conformance

21 Security Considerations (Informative)

22 Internet Media Type, File Extension and Macintosh File Type
```


### 主题1

#### 子主题1

#### 子主题2

## 总结

<!-- 概要记录书籍中如何解决关键性问题的. -->

## 应用

<!-- 记录如何使用书籍中方法论解决你自己的问题. -->

### AllegroGraph

```
# https://trello.com/c/4hloexWe/26-allegrograph

/home/zhoujiagen/tmp/ag6.4.2/bin/agraph-control --config /home/zhoujiagen/tmp/ag6.4.2/lib/agraph.cfg start
/home/zhoujiagen/tmp/ag6.4.2/bin/agraph-control --config /home/zhoujiagen/tmp/ag6.4.2/lib/agraph.cfg stop
```

Play with [sparql-kernel](https://github.com/paulovn/sparql-kernel)

```
pip install sparqlkernel
jupyter sparqlkernel install

%endpoint http://192.168.56.110:10035/repositories/demo-sparql
%auth basic zhoujiagen zhoujiagen

PREFIX foaf:    <http://xmlns.com/foaf/0.1/>
SELECT ?nameX ?nameY ?nickY
WHERE
  { ?x foaf:knows ?y ;
       foaf:name ?nameX .
    ?y foaf:name ?nameY .
    OPTIONAL { ?y foaf:nick ?nickY }
  }

```

## 文献引用

<!-- 记录相关的和进一步阅读资料: 文献、网页链接等. -->


- SPARQL Query Language for RDF: https://www.w3.org/TR/rdf-sparql-query/
- Virtuoso SPARQL Query Editor: https://dbpedia.org/sparql
- Simple SPARQL client: https://sparqlclient.eionet.europa.eu/


|#|Title|Description|Link|
|:---|:---|:---|:---|
|1|SPARQL 1.1 Overview|概述|http://www.w3.org/TR/2013/REC-sparql11-overview-20130321 |
|2|SPARQL 1.1 Query Language|查询语言|http://www.w3.org/TR/2013/REC-sparql11-query-20130321 |
|3|SPARQL 1.1 Update|更新|http://www.w3.org/TR/2013/REC-sparql11-update-20130321 |
|4|SPARQL1.1 Service Description|服务描述|http://www.w3.org/TR/2013/REC-sparql11-service-description-20130321 |
|5|SPARQL 1.1 Federated Query|联合查询(在多个端点间查询)|http://www.w3.org/TR/2013/REC-sparql11-federated-query-20130321 |
|6|SPARQL 1.1 Query Results JSON Format|查询结果: JSON格式|http://www.w3.org/TR/2013/REC-sparql11-results-json-20130321 |
|7|SPARQL 1.1 Query Results CSV and TSV Formats|查询结果: CSV, TSV格式|http://www.w3.org/TR/2013/REC-sparql11-results-csv-tsv-20130321 |
|8|SPARQL Query Results XML Format (Second Edition)|查询结果: XML格式|http://www.w3.org/TR/2013/REC-rdf-sparql-XMLres-20130321 |
|9|SPARQL 1.1 Entailment Regimes|蕴含管理方法|http://www.w3.org/TR/2013/REC-sparql11-entailment-20130321 |
|10|SPARQL 1.1 Protocol|协议|http://www.w3.org/TR/2013/REC-sparql11-protocol-20130321 |
|11|SPARQL 1.1 Graph Store HTTP Protocol|图存储HTTP协议|http://www.w3.org/TR/2013/REC-sparql11-http-rdf-update-20130321 |
|12|SPARQL1.1: Test case structure|测试案例(目前不完整)|https://www.w3.org/2009/sparql/docs/tests/ |

An **entailment regime** specifies how an entailment relation such as RDF Schema entailment can be used to ==redefine the evaluation of basic graph patterns from a SPARQL query== making use of SPARQL's **extension point for basic graph pattern matching**. In order to satisfy the conditions that SPARQL places on extensions to basic graph pattern matching, an entailment regime specifies conditions that limit the number of entailments that contribute solutions for a basic graph pattern. For example, only a finite number of the infinitely many axiomatic triples can contribute solutions under the RDF Schema entailment regime. The entailment relations used in this document are common semantic web entailment relations: **RDF entailment**, **RDF Schema entailment**, **D-Entailment**, **OWL 2 RDF-Based Semantics entailment**, **OWL 2 Direct Semantics entailment**, and **RIF-Simple entailment**.


## 其他备注
