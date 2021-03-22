# Notes of Jena Codes

|时间|内容|
|:---|:---|
|20200819|kick off.|

## 术语

<!-- 记录阅读过程中出现的关键字及其简单的解释. -->

- blank node: 空节点, 表示没有指定URI的资源, 扮演了一阶逻辑中存在限定变量
- literal: 字符串字面量, 可以是属性的值
- property: 属性, 是资源的特征
- resource: 一些实体, 用URI标识, 可以表示Web资源、具体的物理事物、抽象概念
- statement: RDF模型中
- subject:
- predicate:
- object:
- triple:


## 介绍

<!-- 描述软件的来源、特性、解决的关键性问题等. -->

构成模块:

```
1. RDF
1.1 RDF API
1.2 ARQ(SPARQL)
2. Triple Store
2.1 TDB
2.2 Fuseki
3. OWL
3.1 Ontology API
3.2 Inference API
```

## 动机

<!-- 描述阅读软件源码的动机, 要达到什么目的等. -->

- RDF/OWL数据表示的实现方法: 1.1, 3.1
- RDFS/OWL推理机的实现方法: 3.2
- SPARQL查询的实现方法: 1.2
- Triple存储的实现方法: 2.1


## 系统结构

<!-- 描述软件的系统结构, 核心和辅助组件的结构; 系统较复杂时细分展示. -->

项目结构:

版本: 3.17.0-SNAPSHOT


|项目|说明|
|:---|:---|
|apache-jena|Apache Jena - Distribution<br/>Apache Jena is an API and toolkit for working with semantic web technologies such as RDF and SPARQL using Java.  This artifact represents the source and binary distribution packages generated for releases.|
|apache-jena-libs|Apache Jena - Libraries POM<br/>A POM artifact that references all the standard Jena Libraries with a single dependency.|
|apache-jena-osgi|Apache Jena - OSGi<br/>Apache Jena OSGi distribution and test|
|jena-arq|Apache Jena - ARQ (SPARQL 1.1 Query Engine)<br/>ARQ is a SPARQL 1.1 query engine for Apache Jena|
|jena-base|Apache Jena - Base Common Environment<br/>This module contains non-RDF library code and the common system runtime.|
|jena-cmds|Apache Jena - Command line tools|
|jena-core|Apache Jena - Core<br/>Jena is a Java framework for building Semantic Web applications. It provides a programmatic environment for RDF, RDFS and OWL, SPARQL and includes a rule-based inference engine.|
|jena-csv|The last release of this module was with Jena 3.9.0 on 2018-10-27.|
|jena-db|Apache Jena - Database Operation Environment<br/><br/>jena-dboe-base: Apache Jena - DBOE Base<br/>jena-dboe-index: Apache Jena - DBOE Indexes<br/>jena-dboe-index-test: Apache Jena - DBOE Index test suite<br/>jena-dboe-storage: Apache Jena - DBOE Storage<br/>  Triplestore database storage<br/>jena-dboe-trans-data: Apache Jena - DBOE Transactional Datastructures<br/>jena-dboe-transaction: Apache Jena - DBOE Transactions<br/>jena-tdb2: Apache Jena - TDB2|
|jena-elephas|Apache Jena - Elephas<br/>A collection of tools for working with RDF on the Hadoop platform<br/><br/>jena-elephas-common: Apache Jena - Elephas - Common API<br/>  Common code for RDF on Hadoop such as writable types for RDF primitives<br/>jena-elephas-io: Apache Jena - Elephas - I/O<br/>  RDF Input/Output formats library for Hadoop<br/>jena-elephas-mapreduce: Apache Jena - Elephas - Map/Reduce<br/>  Contains some basic Map/Reduce implementations for working with RDF on Hadoop<br/>jena-elephas-stats: Apache Jena - Elephas - Statistics Demo App<br/>  A demo application that can be run on Hadoop to produce a statistical analysis on arbitrary RDF inputs|
|jena-examples|Apache Jena - Code Examples<br/>A collection of example code illustrating uses of Apache Jena|
|jena-extras|Apache Jena - Extras<br/>Extra packages for Jena development.<br/><br/>jena-commonsrdf: Apache Jena - CommonsRDF for Jena<br/>  Apache CommonsRDF Java library for Apache Jena<br/>jena-querybuilder: Apache Jena - Extras - Query Builder<br/>  A utility package to simplify the building of ARQ queries in code.  Provides both a simple builder interface for queries as well as simple prepared statement processing.|
|jena-fuseki1|The last release of Fuseki1 was with Jena 3.9.0 on 2018-10-27.|
|jena-fuseki2|Apache Jena - Fuseki - A SPARQL 1.1 Server<br/>Fuseki is a SPARQL 1.1 Server which provides the SPARQL query, SPARQL update and SPARQL graph store protocols.<br/><br/>apache-jena-fuseki: Apache Jena - Fuseki Binary Distribution<br/>jena-fuseki-access: Apache Jena - Fuseki Data Access Control<br/>jena-fuseki-core: Apache Jena - Fuseki Core Engine<br/>jena-fuseki-fulljar: Apache Jena - Fuseki Server Standalone Jar<br/>  Fuseki server - combined jar with built-in webserver.<br/>jena-fuseki-geosparql: Apache Jena - Fuseki with GeoSPARQL Engine<br/>jena-fuseki-main: Apache Jena - Fuseki Server<br/>jena-fuseki-server: Apache Jena - Fuseki Server Jar<br/>jena-fuseki-war: Apache Jena - Fuseki WAR File<br/>jena-fuseki-webapp: Apache Jena - Fuseki Webapp|
|jena-geosparql|Apache Jena - GeoSPARQL Engine<br/>GeoSPARQL implementation for Apache Jena|
|jena-integration-tests|Apache Jena - Integration Testing<br/>Apache Jena - Integration testing and test tools|
|jena-iri|Apache Jena - IRI<br/>The IRI module provides an implementation of the IRI and URI specifications (RFC 3987 and 3986) which are used across Jena in order to comply with relevant W3C specifications for RDF and SPARQL which require conformance to these specifications.|
|jena-jdbc|Apache Jena - JDBC Parent<br>This is the parent module for the Jena JDBC modules.  These modules provide JDBC Type 4 drivers for in-memory and TDB datasets as well as remote SPARQL endpoints.<br>**jena-jdbc-core**: Apache Jena - JDBC Core API<br>  This library provides core functionality for Jena JDBC<br>**jena-jdbc-driver-bundle**: Apache Jena - JDBC Driver Bundle<br>  An artifact which bundles up the standard Jena JDBC drivers into a single shaded JAR file to provide a convenience dependency<br>**jena-jdbc-driver-mem**: Apache Jena - JDBC In-Memory Driver<br>  A Jena JDBC driver that uses an ARQ in-memory endpoint, intended primarily for testing purposes.<br>**jena-jdbc-driver-remote**: Apache Jena - JDBC Remote Endpoint Driver<br>  A Jena JDBC driver for use with remote SPARQL endpoints.<br>**jena-jdbc-driver-tdb**: Apache Jena - JDBC TDB Driver<br>  A Jena JDBC driver for use with the Jena TDB backend|
|jena-maven-tools|The last release of this module was with Jena 3.6.0 on 2017-12-13.|
|jena-permissions|Apache Jena - Security Permissions<br>Security Permissions wrapper around Jena RDF implementation.|
|jena-rdfconnection|Apache Jena - RDF Connection<br>RDF Connection|
|jena-sdb|Apache Jena - SDB (SQL based triple store)<br/>SDB is a persistence layer for use with Apache Jena that uses an SQL database to store triples/quads.|
|jena-shacl|Apache Jena - SHACL<br>SHACL engine for Apache Jena|
|jena-shaded-guava|Apache Jena - Shadowed external libraries<br/>This module shades Google's Guava. Direct use can lead to versioning problems  as some systems are dependent on specific versions of guava.  This module uses the Shade plugin to  re-package them under the package name org.apache.jena.ext.com.google....|
|jena-spatial|Apache Jena - SPARQL Spatial Search|
|jena-tdb|Apache Jena - TDB1 (Native Triple Store)<br/>TDB is a storage subsystem for Jena and ARQ, it is a native triple store providing persisent disk based storage of triples/quads.|
|jena-text|Apache Jena - SPARQL Text Search|
|jena-text-es|Apache Jena - SPARQL Text Search - Elasticsearch|

### jena-core


|包|描述|
|:---|:---|
|assembler|An Assembler creates objects from their RDF descriptions.|
|datatypes|Provides the core interfaces through which datatypes are described to Jena.|
|enhanced|This package defines the enhanced node and graph classes; an enhanced node is one embedded in a particular enhanced graph.|
|ext.xerces|???|
|graph|This package defines the Graph and Node family of classes, which form the underlying datatypes of the Jena system.|
|mem|Various memory-based implementations of interfaces, specifically GraphMem for memory-based Graphs.|
|n3|Jena N3 Parser, RDF Reader and Writer.|
|ontology|Provides a set of abstractions and convenience classes for accessing and manipluating ontologies represented in RDF.|
|rdf|model: A package for creating and manipulating RDF graphs.<br/>listeners: A package defining some useful implementations of ModelChangedListener, for listening to:<br/>(a) all triples added or removed, exploding composite objects,<br/>(b) all objects added or removed, as themselves,<br/>(c) notification of additions/removals, but no details,<br/>(d) accepting but ignoring all changes, as a base-class to be extended.|
|rdfxml|xmlinput: A parser for RDF/XML.<br/>xmloutput: Writing RDF/XML.|
|reasoner|The Jena2 reasoner subsystem is designed to allow a range of inference engines to be plugged into Jena.|
|shared|This package defines some classes common to the Jena API and SPI levels, in particular the JenaException class from which all Jena-specific exceptions hang, and the interface PrefixMapping for translation to and from QNames.|
|sys|Jena "system" - simple controls for ensuring components are loaded and initialized.|
|util|Miscellaneous collection of utility classes.|
|vocabulary|A package containing constant classes with predefined constant objects for classes and properties defined in well known vocabularies.|

### jena-arq


|包|描述|
|:---|:---|
|atlas|csv<br/>data<br/>event<br/>json<br/>test<br/>web|
|query|ARQ - A query engine for Jena, implementing SPARQL.|
|riot|RIOT|
|sparql|algebra<br/>core<br/>engine|
|expr|function<br/>graph<br/>lang<br/>lib<br/>mgt<br/>modify<br/>path<br/>pfunction<br/>procedure<br/>resultset|
|system||
|update||
|web||


## 使用

<!-- 记录软件如何使用. -->

Jena JavaDoc[^1]

[^1]: https://jena.apache.org/documentation/javadoc.html

RDF API: RDFAPITutorial.java[^2]

[^2]: https://github.com/zhoujiagen/snippets/blob/master/codes/java/example-jena/src/main/java/com/spike/rtfsc/jena/RDFAPITutorial.java


## 数据结构和算法

<!-- 描述软件中重要的数据结构和算法, 支撑过程部分的记录. -->

- RDF API

rtfsc-jena-graph-model.gliffy

`Graph`, `Model`, `Resource`, `Property`, `Statement`

- ARQ

rtfsc-jena-arq.gliffy

`Dataset`, `Query`, `QueryExecution`, `ResultSet`


## 过程

<!-- 描述软件中重要的过程性内容, 例如服务器的启动、服务器响应客户端请求、服务器背景活动等. -->

## 文献引用

<!-- 记录软件相关和进一步阅读资料: 文献、网页链接等. -->

Apache Jena[^3]: A free and open source Java framework for building Semantic Web and Linked Data applications.

[^3]: https://jena.apache.org/

## 其他备注
