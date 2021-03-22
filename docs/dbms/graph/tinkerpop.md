# Notes of Apache TinerPop

|时间|内容|
|:---|:---|
|20190412|kick off. found SPARQL-Gremlin|

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

### SQL2Gremlin

创建图和Traversal

```
$ bin/gremlin.sh -i ../northwind.groovy
gremlin> graph = NorthwindFactory.createGraph()
==>tinkergraph[vertices:3209 edges:6177]
gremlin> g = graph.traversal()
==>graphtraversalsource[tinkergraph[vertices:3209 edges:6177], standard]
```

探查节点

```
gremlin> g.V().count()
==>3209
gremlin> g.V().label().dedup()
==>category
==>customer
==>region
==>country
==>employee
==>order
==>item
==>product
gremlin> g.V().groupCount().by(label)
==>[country:21,item:2155,product:77,category:8,region:18,employee:9,customer:91,order:830]
gremlin> g.V().hasLabel('category').properties().key().dedup()
==>name
==>description
gremlin> g.V().hasLabel('customer').properties().key().dedup()
==>address
==>phone
==>city
==>postalCode
==>name
==>customerId
==>company
==>fax
==>title
gremlin> g.V().hasLabel('region').properties().key().dedup()
==>name
gremlin> g.V().hasLabel('country').properties().key().dedup()
==>name
gremlin> g.V().hasLabel('employee').properties().key().dedup()
==>lastName
==>firstName
==>hireDate
==>extension
==>address
==>notes
==>city
==>homePhone
==>postalCode
==>title
==>titleOfCourtesy
gremlin> g.V().hasLabel('order').properties().key().dedup()
==>shipCity
==>freight
==>requiredDate
==>shipName
==>shipPostalCode
==>shippedDate
==>orderDate
==>shipAddress
gremlin> g.V().hasLabel('item').properties().key().dedup()
==>unitPrice
==>quantity
==>discount
gremlin> g.V().hasLabel('product').properties().key().dedup()
==>unitsInStock
==>reorderLevel
==>unitPrice
==>name
==>discontinued
==>type
==>unitsOnOrder
```


探查边

```
gremlin> g.E().count()
==>6177
gremlin> g.E().label().dedup()
==>contains
==>is
==>sold
==>ordered
==>inCategory
==>reportsTo
==>livesInRegion
==>livesInCountry
gremlin> g.E().groupCount().by(label)
==>[sold:830,ordered:830,contains:2155,livesInCountry:91,livesInRegion:31,inCategory:77,is:2155,reportsTo:8]
gremlin> g.E().hasLabel('contains').properties().key().dedup()
gremlin> g.E().hasLabel('is').properties().key().dedup()
gremlin> g.E().hasLabel('sold').properties().key().dedup()
gremlin> g.E().hasLabel('ordered').properties().key().dedup()
gremlin> g.E().hasLabel('inCategory').properties().key().dedup()
gremlin> g.E().hasLabel('reportsTo').properties().key().dedup()
gremlin> g.E().hasLabel('livesInRegion').properties().key().dedup()
gremlin> g.E().hasLabel('livesInCountry').properties().key().dedup()
```


TODO:

Select

Filtering

Ordering

Paging

Grouping

Joining

Miscellaneous
  Concatenate
  Create, Update and Delete



#### CTE : Common Table Expressions

MySQL WITH: https://dev.mysql.com/doc/refman/8.0/en/with.html

```
  CREATE TABLE `employees` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    `reportsTo` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

  INSERT INTO employees(id, `name`, reportsTo)
  VALUES
  ('1', 'Andrew Fuller', NULL),
  ('2', 'Nancy Davolio', '1'),
  ('3', 'Janet Leverling', '1'),
  ('4', 'Margaret Peacock', '1'),
  ('5', 'Steven Buchanan', '1'),
  ('6', 'Laura Callahan', '1'),
  ('7', 'Robert King', '10'),
  ('8', 'Anne Dodsworth', '10'),
  ('9', 'Michael Suyama', '10'),
  ('10', 'Steven Buchanan', NULL);

  WITH RECURSIVE
  EmployeeHierarchy (id, name, reportsTo, HierarchyLevel) AS
  (
      SELECT id
           , name
           , reportsTo
           , 1 as HierarchyLevel
        FROM employees
       WHERE reportsTo IS NULL

       UNION ALL

      SELECT e.id
           , e.name
           , e.reportsTo
           , eh.HierarchyLevel + 1 AS HierarchyLevel
        FROM employees e
  INNER JOIN EmployeeHierarchy eh
          ON e.reportsTo = eh.id
  )
  SELECT *
  FROM EmployeeHierarchy
  ORDER BY HierarchyLevel, name
  ;
```


  hierarchical

```  
  ==>graphtraversalsource[tinkergraph[vertices:3209 edges:6177], standard]
  gremlin> g.V().hasLabel("employee").where(__.not(out("reportsTo"))).
  ......1>                repeat(__.in("reportsTo")).emit().tree().by(map {
  ......2>                  def employee = it.get()
  ......3>                  employee.value("firstName") + " " + employee.value("lastName")
  ......4>                }).next()
  ==>Andrew Fuller={Margaret Peacock={}, Janet Leverling={}, Nancy Davolio={}, Steven Buchanan={Anne Dodsworth={}, Michael Suyama={}, Robert King={}}, Laura Callahan={}}
```

  拆分步骤

```
  gremlin> v1 = g.V().hasLabel("employee").where(__.not(out("reportsTo")))
  ==>v[139]

  gremlin> g.V().hasLabel("employee").where(__.not(out("reportsTo"))).
  ......1> repeat(__.in("reportsTo")).emit().tree()
  ==>[v[139]:[v[145]:[],v[138]:[],v[140]:[],v[141]:[],v[142]:[v[144]:[],v[146]:[],v[143]:[]]]]

  gremlin> g.V().hasLabel("employee").where(__.not(out("reportsTo"))).
  ......1> repeat(__.in("reportsTo")).emit().tree().by(map {
  ......2> def employee = it.get()
  ......3>  employee.value("firstName") + " " + employee.value("lastName")
  ......4> })
  ==>[Andrew Fuller:[Margaret Peacock:[],Janet Leverling:[],Nancy Davolio:[],Steven Buchanan:[Anne Dodsworth:[],Michael Suyama:[],Robert King:[]],Laura Callahan:[]]]
```

  tabular

```  
  gremlin> g.V().hasLabel("employee").where(__.not(out("reportsTo"))).
  ......1>                repeat(__.as("reportsTo").in("reportsTo").as("employee")).emit().
  ......2>                select(last, "reportsTo", "employee").by(map {
  ......3>                  def employee = it.get()
  ......4>                  employee.value("firstName") + " " + employee.value("lastName")
  ......5>                })
  ==>[reportsTo:Andrew Fuller,employee:Nancy Davolio]
  ==>[reportsTo:Andrew Fuller,employee:Janet Leverling]
  ==>[reportsTo:Andrew Fuller,employee:Margaret Peacock]
  ==>[reportsTo:Andrew Fuller,employee:Steven Buchanan]
  ==>[reportsTo:Andrew Fuller,employee:Laura Callahan]
  ==>[reportsTo:Steven Buchanan,employee:Robert King]
  ==>[reportsTo:Steven Buchanan,employee:Anne Dodsworth]
  ==>[reportsTo:Steven Buchanan,employee:Michael Suyama]
```

  拆分步骤

```
  gremlin>  g.V().hasLabel("employee").where(__.not(out("reportsTo")))
  ==>v[139]

  gremlin> g.V().hasLabel("employee").where(__.not(out("reportsTo"))).
  ......1> repeat(__.as("reportsTo").in("reportsTo").as("employee")).emit()
  ==>v[138]
  ==>v[140]
  ==>v[141]
  ==>v[142]
  ==>v[145]
  ==>v[144]
  ==>v[146]
  ==>v[143]

  gremlin> g.V().hasLabel("employee").where(__.not(out("reportsTo"))).
  ......1> repeat(__.as("reportsTo").in("reportsTo").as("employee")).emit().
  ......2> select(last, "reportsTo", "employee")
  ==>[reportsTo:v[139],employee:v[138]]
  ==>[reportsTo:v[139],employee:v[140]]
  ==>[reportsTo:v[139],employee:v[141]]
  ==>[reportsTo:v[139],employee:v[142]]
  ==>[reportsTo:v[139],employee:v[145]]
  ==>[reportsTo:v[142],employee:v[144]]
  ==>[reportsTo:v[142],employee:v[146]]
  ==>[reportsTo:v[142],employee:v[143]]
```


### SPARQL-Gremlin

安装和加载插件

```
gremlin> :install org.apache.tinkerpop sparql-gremlin 3.4.1
==>Loaded: [org.apache.tinkerpop, sparql-gremlin, 3.4.1]
gremlin> :plugin use tinkerpop.sparql
==>tinkerpop.sparql activated
```


创建SPARQL Traversal

```
gremlin> graph = NorthwindFactory.createGraph()
==>tinkergraph[vertices:3209 edges:6177]
gremlin> g = graph.traversal(SparqlTraversalSource)
==>sparqltraversalsource[tinkergraph[vertices:3209 edges:6177], standard]
```

查询示例

```
gremlin> g.sparql(""" SELECT DISTINCT ?name WHERE {
......1> ?c v:label "customer".
......2> ?c v:name ?name
......3> }
......4> LIMIT 10""")
==>Maria Anders
==>Ana Trujillo
==>Antonio Moreno
==>Thomas Hardy
==>Christina Berglund
==>Hanna Moos
==>Frédérique Citeaux
==>Martín Sommer
==>Laurence Lebihan
==>Elizabeth Lincoln
```




## 数据结构和算法

<!-- 描述软件中重要的数据结构和算法, 支撑过程部分的记录. -->

## 过程

<!-- 描述软件中重要的过程性内容, 例如服务器的启动、服务器响应客户端请求、服务器背景活动等. -->

## 文献引用

<!-- 记录软件相关和进一步阅读资料: 文献、网页链接等. -->

- TinkerPop Documentation: http://tinkerpop.apache.org/docs/current/reference
- SQL2Gremlin: http://sql2gremlin.com
- SPARQL-Gremlin: A Stitch and in Time and Saves Nine and – SPARQL and Querying of Property and Graphs using Gremlin and Traversals.


## 其他备注
