/*
* Example SQL cited from "A First Course in Database Systems".
*/

###############################################################################
# Simple Queries
###############################################################################
SELECT * 
FROM Movies
WHERE studioName = 'Disney' AND year = 1990;

####################################### Projection
SELECT title, length
FROM Movies
WHERE studioName = 'Disney' AND year = 1990;

SELECT title AS name, length AS duration
FROM Movies
WHERE studioName = 'Disney' AND year = 1990;


SELECT title AS name, length*0.016667 AS lengthInHours
FROM Movies
WHERE studioName = 'Disney' AND year = 1990;

SELECT title, length*0.016667 AS length, 'hrs.' AS inHours
FROM Movies
WHERE studioName = 'Disney' AND year = 1990;

####################################### Selection

SELECT title
FROM Movies
WHERE (year > 1970 OR length < 90) AND studioName = 'MGM';

####################################### Comparson of Strings

SELECT title
FROM Movies
WHERE title < 'Disney';


####################################### Pattern Matching in SQL

SELECT title
FROM Movies
WHERE title LIKE 'Star ____';

SELECT title
FROM Movies
WHERE title LIKE '%''s%';

####################################### Ordering

SELECT *
FROM Movies
WHERE studioName = 'Disney' AND year = 1990
ORDER BY length, title;

SELECT *
FROM Movies
WHERE studioName = 'Disney' AND year = 1990
ORDER BY CONCAT(length, title) DESC;


###############################################################################
# Multiple Relations Queries
###############################################################################

####################################### Disambiguating Attributes
SELECT name
FROM Movies, MovieExec
WHERE title = 'Star Wars' AND `producerC#` = `cert#`;

SELECT MovieExec.name
FROM Movies, MovieExec
WHERE Movies.title = 'Star Wars' AND Movies.`producerC#` = MovieExec.`cert#`;


SELECT MovieStar.name, MovieExec.name
FROM MovieStar, MovieExec
WHERE MovieStar.address = MovieExec.address;

####################################### Tuple Variables
SELECT Star1.name, Star2.name
FROM MovieStar Star1, MovieStar Star2
WHERE Star1.address = Star2.address
	AND Star1.name < Star2.name;

####################################### Union, Intersection, and Difference of Queries


SELECT v1.name, v1.address
FROM 
(SELECT name, address
FROM MovieStar
WHERE gender = 'F') v1
,
(SELECT name, address
FROM MovieExec
WHERE netWorth > 1000000) v2
WHERE v1.name = v2.name AND v1.address = v2.address;


SELECT name, address
FROM MovieStar
WHERE (name, address) NOT IN
(SELECT name, address
FROM MovieExec);

(SELECT title, year FROM Movies)
UNION
(SELECT movieTitle, movieYear AS year FROM StarsIn);


###############################################################################
# Sub Queries
###############################################################################

####################################### Subqueries that Produce Scalar Values

SELECT name
FROM Movies, MovieExec
WHERE title = 'Star Wars' AND `producerC#` = `cert#`;

SELECT name
FROM MovieExec
WHERE `cert#` = 
	(SELECT `producerC#`
    FROM Movies
    WHERE title = 'Star Wars');

SELECT name
FROM MovieExec
WHERE `cert#` = 12345;

####################################### Conditions Involving Relations

-- EXISTS, NOT EXISTS

SELECT `producerC#`
FROM Movies
WHERE EXISTS (SELECT * FROM MovieExec WHERE `cert#` = 12345);


SELECT `producerC#`
FROM Movies
WHERE EXISTS (SELECT * FROM MovieExec WHERE `cert#` = `producerC#`);

-- IN, NOT IN

SELECT `producerC#`
FROM Movies
WHERE `producerC#` IN (SELECT `cert#` FROM MovieExec);

-- ANY/SOME, ALL
-- comparison_operator: =  >  <  >=  <=  <>  !=

SELECT `producerC#`
FROM Movies
WHERE `producerC#` = ANY (SELECT `cert#` FROM MovieExec);

SELECT `producerC#`
FROM Movies
WHERE `producerC#` > SOME (SELECT `cert#` FROM MovieExec);

SELECT `producerC#`
FROM Movies
WHERE `producerC#` > ALL (SELECT `cert#` FROM MovieExec);


/**
Scoping rule: MySQL evaluates from inside to outside. For example:

SELECT column1 FROM t1 AS x
  WHERE x.column1 = (SELECT column1 FROM t2 AS x
    WHERE x.column1 = (SELECT column1 FROM t3
      WHERE x.column2 = t3.column1));
*/

####################################### Conditions Involving Tuples
SELECT name
FROM MovieExec
WHERE `cert#` IN 
	(SELECT `producerC#`
    FROM Movies
    WHERE (title, year) IN
		(SELECT movieTitle, movieYear
        FROM StarsIn
        WHERE starName = 'Harrison Ford')
	);
-- 不使用子查询
SELECT name
FROM MovieExec, Movies, StarsIn
WHERE `cert#` = `producerC#`
	AND title = movieTitle
    AND year = movieYear
    AND starName = 'Harrison Ford';


####################################### Correlated Subqueries

/**
A correlated subquery is a subquery that contains a reference to a table that also appears in the outer query. For example:

SELECT * FROM t1
  WHERE column1 = ANY (SELECT column1 FROM t2
                       WHERE t2.column2 = t1.column2);
*/

SELECT title
FROm Movies Old
WHERE year < ANY 
	(SELECT year
    FROM Movies
    WHERE title = Old.title);

####################################### Subqueries in FROM Clauses

SELECT name
FROM MovieExec, (SELECT `producerC#`
								FROM Movies, StarsIn
                                WHERE title = movieTitle
									AND year = movieYear
                                    AND starName = 'Harrison Ford') Prod
WHERE `cert#` = Prod.`producerC#`;

####################################### SQL Join Expressions

SELECT title, year, length, genre, studioName, `producerC#`, starName
FROM Movies JOIN StarsIn ON title = movieTitle AND year = movieYear;

/**
STRAIGHT_JOIN is similar to JOIN, except that the left table is always read before the right table. This can be used for those (few) cases for which the join optimizer processes the tables in a suboptimal order.
*/

####################################### Natural Joins

SELECT title, year, length, genre, studioName, `producerC#`, starName
FROM Movies NATURAL JOIN StarsIn;

####################################### Outerjoins

SELECT title, year, length, genre, studioName, `producerC#`, starName
FROM Movies LEFT OUTER JOIN StarsIn ON title = movieTitle AND year = movieYear;

/**
The NATURAL [LEFT] JOIN of two tables is defined to be semantically equivalent to 
an INNER JOIN or a LEFT JOIN with a USING clause that names all columns that exist in both tables.
*/
SELECT title, year, length, genre, studioName, `producerC#`, starName
FROM Movies NATURAL RIGHT OUTER JOIN StarsIn;

###############################################################################
# Full Relation Operations
###############################################################################

####################################### Eliminating Duplicates
SELECT DISTINCT title
FROM Movies;

####################################### Duplicates in Unions, Intersections, and Differences

(SELECT title, year FROM Movies)
UNION 
(SELECT movieTitle AS title, movieYear AS year FROM StarsIn);

(SELECT title, year FROM Movies)
UNION ALL
(SELECT movieTitle AS title, movieYear AS year FROM StarsIn);

(SELECT title, year FROM Movies)
UNION DISTINCT
(SELECT movieTitle AS title, movieYear AS year FROM StarsIn);


SELECT REPEAT('a',1) UNION SELECT REPEAT('a',1);
SELECT REPEAT('a',1) UNION ALL SELECT REPEAT('a',1);
SELECT REPEAT('a',1) UNION DISTINCT SELECT REPEAT('a',1);

####################################### Aggregation Operators

SELECT AVG(netWorth)
FROM MovieExec;

SELECT COUNT(*)
FROM StarsIn;

SELECT COUNT(starName)
FROM StarsIn;

SELECT COUNT(DISTINCT starName)
FROM StarsIn;

####################################### Grouping

SELECT studioName, SUM(length)
FROM Movies
GROUP BY studioName;

SELECT studioName
FROM Movies
GROUP BY studioName;
-- equals to
SELECT DISTINCT studioName
FROM Movies;

SELECT name, SUM(length)
FROM MovieExec, Movies
WHERE `producerC#` = `cert#`
GROUP BY name;

####################################### HAVING Clauses

SELECT name, SUM(length)
FROM MovieExec, Movies
WHERE `producerC#` = `cert#`
GROUP BY name
HAVING MIN(year) < 1930;

###############################################################################
# Updates
###############################################################################

###############################################################################
# Database System Implementation
###############################################################################
####################################### Chapter 5 查询编译器

-- 不相关的子查询
SELECT movieTitle
FROM StarsIn
WHERE starName IN (
	SELECT name
    FROM MovieStar
    WHERE birthdate LIKE '%1960');
-- 等价的查询: 移除了子查询
SELECT movieTitle
FROM StarsIn, MovieStar
WHERE starName = name
	AND birthdate LIKE '%1960';

-- 相关子查询
SELECT DISTINCT m1.movieTitle, m1.movieYear
FROM StarsIn m1
WHERE m1.movieYear - 40 <= (
	SELECT AVG(birthdate)
    FROM StarsIn m2, MovieStar s
    WHERE m2.starName = s.name
		AND m1.movieTitle = m2.movieTitle
        AND m1.movieYear = m2.movieYear);


###############################################################################
# Schemas
###############################################################################

/*

CREATE DATABASE `movies` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE `Movies` (
  `title` varchar(100) NOT NULL,
  `year` int(11) NOT NULL,
  `length` int(11) DEFAULT NULL,
  `genre` varchar(10) DEFAULT NULL,
  `studioName` varchar(30) DEFAULT NULL,
  `producerC#` int(11) DEFAULT NULL,
  PRIMARY KEY (`title`,`year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `MovieStar` (
  `name` varchar(30) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `gender` char(1) DEFAULT NULL,
  `birthdate` datetime DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `StarsIn` (
  `movieTitle` varchar(100) NOT NULL,
  `movieYear` int(11) NOT NULL,
  `starName` varchar(30) NOT NULL,
  PRIMARY KEY (`movieTitle`,`movieYear`,`starName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `MovieExec` (
  `name` varchar(100) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `cert#` int(11) NOT NULL,
  `netWorth` int(11) DEFAULT NULL,
  PRIMARY KEY (`cert#`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='movie executives';


CREATE TABLE `Studio` (
  `name` varchar(30) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `presC#` int(11) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

*/