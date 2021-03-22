# Notes of WordNet

|时间|内容|
|:---|:---|
|yyyymmdd||

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

安装:

```
$ brew info wordnet
wordnet: stable 3.1 (bottled)
Lexical database for the English language
https://wordnet.princeton.edu/
/usr/local/Cellar/wordnet/3.1 (181 files, 53.9MB) *
  Poured from bottle on 2019-03-29 at 17:30:59
From: https://mirrors.tuna.tsinghua.edu.cn/git/homebrew/homebrew-core.git/Formula/wordnet.rb
==> Requirements
Required: x11 ✔
```

## 数据结构和算法

<!-- 描述软件中重要的数据结构和算法, 支撑过程部分的记录. -->

### WordNet Identifiers

Words are identified by means of the following URI scheme: - 单词

```
http://wordnet-rdf.princeton.edu/wn31/word-[nvarsp]
```

Where [nvarsp] is the part-of-speech. Similarly synsets are identified as follows - 同义词集

```
http://wordnet-rdf.princeton.edu/id/[8 digit code]-[nvarsp]
```

Where the 8-digit code is the identifier of the synset.

Each file may be downloaded in a given format by adding a file extension to the URL. For example, adding .json to the URL gives the JSON version of the word or synset, e.g.,

```
http://wordnet-rdf.princeton.edu/id/00001740-n.json
```

The following formats are supported:

- .html: HTML+RDFa
- .rdf: RDF/XML
- .ttl: Turtle
- .json: JSON

It is also possible to use synset identifiers from WordNet 2.0 and WordNet 3.0. The URIs use the 8-digit synset code and the part-of-speech letter code, for example as follows:

```
http://wordnet-rdf.princeton.edu/pwn20/00001740-a
http://wordnet-rdf.princeton.edu/pwn30/00001740-a
```

## 过程

<!-- 描述软件中重要的过程性内容, 例如服务器的启动、服务器响应客户端请求、服务器背景活动等. -->

## 文献引用

<!-- 记录软件相关和进一步阅读资料: 文献、网页链接等. -->

- WordNet - A Lexical Database for English: https://wordnet.princeton.edu/
- Princeton WordNet RDF Ontology: http://wordnet-rdf.princeton.edu/about


## 其他备注

### 关联的外部资源

#### VerbNet - 动词表

VerbNet (VN) (Kipper-Schuler 2006) is the largest on-line verb lexicon currently available for English. It is a hierarchical domain-independent, broad-coverage verb lexicon with mappings to other lexical resources such as WordNet (Miller, 1990; Fellbaum, 1998), Xtag (XTAG Research Group, 2001), and FrameNet (Baker et al., 1998). VerbNet is organized into verb classes extending Levin (1993) classes through refinement and addition of subclasses to achieve syntactic and semantic coherence among members of a class. Each verb class in VN is completely described by thematic roles, selectional restrictions on the arguments, and frames consisting of a syntactic description and semantic predicates with a temporal function, in a manner similar to the event decomposition of Moens and Steedman (1988).

> http://verbs.colorado.edu/~mpalmer/projects/verbnet.html

#### Lexicon Model for Ontologies: Community Report, 10 May 2016 - W3C 词典模型

This document describes the lexicon model for ontologies (lemon) as a main outcome of the work of the Ontology Lexicon (Ontolex) community group.

> https://www.w3.org/2016/05/ontolex/

The data is structured according to the OntoLex-Lemon model.

#### RDF/OWL Representation of WordNet - W3C WordNet的RDF/OWL表示

This document presents a standard conversion of Princeton WordNet to RDF/OWL. It describes how it was converted and gives examples of how it may be queried for use in Semantic Web applications.

> https://www.w3.org/TR/wordnet-rdf/


#### ISOcat - 语言学相关概念和资源中心库, 被DatCatInfo替代

ISOcat is a central registry for all concepts relevant in linguistics and the domain of language resources, including metadata categories etc.
The ISOcat web interface allows you to search for data categories you are interested in, select the ones you want to use, and collect them in your own Data Category Selection (DCS). You can export this DCS for your own application using several export formats.

> ISOcat is no longer developed by TLA
> http://www.isocat.org/

> DatCatInfo is the Data Category Repository (DCR) that replaces ISOcat.
> http://www.datcatinfo.net/#/history

#### OLiA ontologies - 标注模型/引用模型/链接模型

The OLiA architecture is a set of modular OWL/DL ontologies with ontological models of annotation schemes (Annotation Models) on the one hand, an ontology of reference terms (Reference Model) on the other hand, and ontologies (Linking Models) that implement subClassOf relationships between them.

> http://nachhalt.sfb632.uni-potsdam.de/owl/

#### LexInfo - 为Lemon模型提供数据

LexInfo is an ontology that was defined during the Monnet Project to provide data categories for the Lemon model. It has since since been updated with the new OntoLex-Lemon model of the OntoLex community group

> https://lexinfo.net/
