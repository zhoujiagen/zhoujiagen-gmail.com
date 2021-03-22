package com.spike.giantdataanalysis.rdfstore.sql;

/**
 * 
 */
public interface TestConstants {
  String TEST_QUERY_DIR = "src/test/resources/sql";
  String TEST_QUERY_FULL_DIR = "src/test/resources/sql/full";

  String alert_table_path = TEST_QUERY_DIR + "/alert_table.sql";
  String create_table_path = TEST_QUERY_DIR + "/create_table.sql";
  String data_path = TEST_QUERY_DIR + "/data.sql";
  String all_path = TEST_QUERY_DIR + "/all.sql";
  String test_path = TEST_QUERY_DIR + "/test.sql";

  String SAMPLE_path = TEST_QUERY_DIR + "/all.sql";
  String SAMPLE_procedure_path = TEST_QUERY_DIR + "/create_procedure.sql";
  // ddl
  // dml
  String DIR_DML = TEST_QUERY_FULL_DIR + "/dml";
  String SELECT_syntax_path = DIR_DML + "/SELECT_syntax.sql";
  String DELETE_syntax_path = DIR_DML + "/DELETE_syntax.sql";
  // ...

  /** from 'sample.sql' */
  public static class SampleSQL {

    // ---------------------------------------------------------------------------
    // Simple Queries
    // ---------------------------------------------------------------------------
    public static String Simple_Queries() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT * ").append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("WHERE studioName = 'Disney' AND year = 1990;").append(System.lineSeparator());
      return sb.toString();
    }

    // ------------------------------------- Projection
    public static String Projection1() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT title, length").append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("WHERE studioName = 'Disney' AND year = 1990;").append(System.lineSeparator());
      return sb.toString();
    }

    public static String Projection2() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT title AS name, length AS duration").append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("WHERE studioName = 'Disney' AND year = 1990;").append(System.lineSeparator());
      return sb.toString();
    }

    public static String Projection3() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT title AS name, length*0.016667 AS lengthInHours")
          .append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("WHERE studioName = 'Disney' AND year = 1990;").append(System.lineSeparator());
      return sb.toString();
    }

    public static String Projection4() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT title, length*0.016667 AS length, 'hrs.' AS inHours")
          .append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("WHERE studioName = 'Disney' AND year = 1990;").append(System.lineSeparator());
      return sb.toString();
    }

    // ------------------------------------- Selection

    public static String Selection() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT title").append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("WHERE (year > 1970 OR length < 90) AND studioName = 'MGM';")
          .append(System.lineSeparator());
      return sb.toString();
    }

    // ------------------------------------- Comparson of Strings

    public static String Comparson_of_Strings() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT title").append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("WHERE title < 'Disney';").append(System.lineSeparator());
      return sb.toString();
    }

    // ------------------------------------- Pattern Matching in SQL

    public static String Pattern_Matching_in_SQL1() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT title").append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("WHERE title LIKE 'Star ____';").append(System.lineSeparator());
      return sb.toString();
    }

    public static String Pattern_Matching_in_SQL2() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT title").append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("WHERE title LIKE '%''s%';").append(System.lineSeparator());
      return sb.toString();
    }

    // ------------------------------------- Ordering

    public static String Ordering1() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT *").append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("WHERE studioName = 'Disney' AND year = 1990").append(System.lineSeparator());
      sb.append("ORDER BY length, title;").append(System.lineSeparator());
      return sb.toString();
    }

    public static String Ordering2() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT *").append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("WHERE studioName = 'Disney' AND year = 1990").append(System.lineSeparator());
      sb.append("ORDER BY CONCAT(length, title) DESC;").append(System.lineSeparator());
      return sb.toString();
    }

    // ---------------------------------------------------------------------------
    // Multiple Relations Queries
    // ---------------------------------------------------------------------------

    // ------------------------------------- Disambiguating Attributes
    public static String Disambiguating_Attributes1() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT name").append(System.lineSeparator());
      sb.append("FROM Movies, MovieExec").append(System.lineSeparator());
      sb.append("WHERE title = 'Star Wars' AND `producerC#` = `cert#`;")
          .append(System.lineSeparator());
      return sb.toString();
    }

    public static String Disambiguating_Attributes2() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT MovieExec.name").append(System.lineSeparator());
      sb.append("FROM Movies, MovieExec").append(System.lineSeparator());
      sb.append("WHERE Movies.title = 'Star Wars' AND Movies.`producerC#` = MovieExec.`cert#`;")
          .append(System.lineSeparator());
      return sb.toString();
    }

    public static String Disambiguating_Attributes3() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT MovieStar.name, MovieExec.name").append(System.lineSeparator());
      sb.append("FROM MovieStar, MovieExec").append(System.lineSeparator());
      sb.append("WHERE MovieStar.address = MovieExec.address;").append(System.lineSeparator());
      return sb.toString();
    }

    // ------------------------------------- Tuple Variables

    public static String Tuple_Variables() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT Star1.name, Star2.name").append(System.lineSeparator());
      sb.append("FROM MovieStar Star1, MovieStar Star2").append(System.lineSeparator());
      sb.append("WHERE Star1.address = Star2.address").append(System.lineSeparator());
      sb.append(" AND Star1.name < Star2.name;").append(System.lineSeparator());
      return sb.toString();
    }

    // ------------------------------------- Union, Intersection, and Difference of Queries

    public static String Union_Intersection_and_Difference_of_Queries1() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT v1.name, v1.address").append(System.lineSeparator());
      sb.append("FROM ").append(System.lineSeparator());
      sb.append("(SELECT name, address").append(System.lineSeparator());
      sb.append("FROM MovieStar").append(System.lineSeparator());
      sb.append("WHERE gender = 'F') v1").append(System.lineSeparator());
      sb.append(",").append(System.lineSeparator());
      sb.append("(SELECT name, address").append(System.lineSeparator());
      sb.append("FROM MovieExec").append(System.lineSeparator());
      sb.append("WHERE netWorth > 1000000) v2").append(System.lineSeparator());
      sb.append("WHERE v1.name = v2.name AND v1.address = v2.address;")
          .append(System.lineSeparator());
      return sb.toString();
    }

    public static String Union_Intersection_and_Difference_of_Queries2() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT name, address").append(System.lineSeparator());
      sb.append("FROM MovieStar").append(System.lineSeparator());
      sb.append("WHERE (name, address) NOT IN").append(System.lineSeparator());
      sb.append("(SELECT name, address").append(System.lineSeparator());
      sb.append("FROM MovieExec);").append(System.lineSeparator());
      return sb.toString();
    }

    public static String Union_Intersection_and_Difference_of_Queries3() {
      StringBuilder sb = new StringBuilder();
      sb.append("(SELECT title, year FROM Movies)").append(System.lineSeparator());
      sb.append("UNION").append(System.lineSeparator());
      sb.append("(SELECT movieTitle, movieYear AS year FROM StarsIn);")
          .append(System.lineSeparator());
      return sb.toString();
    }

    // ---------------------------------------------------------------------------
    // Sub Queries
    // ---------------------------------------------------------------------------

    // ------------------------------------- Subqueries that Produce Scalar Values

    public static String Subqueries_that_Produce_Scalar_Values1() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT name").append(System.lineSeparator());
      sb.append("FROM Movies, MovieExec").append(System.lineSeparator());
      sb.append("WHERE title = 'Star Wars' AND `producerC#` = `cert#`;")
          .append(System.lineSeparator());
      return sb.toString();
    }

    public static String Subqueries_that_Produce_Scalar_Values2() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT name").append(System.lineSeparator());
      sb.append("FROM MovieExec").append(System.lineSeparator());
      sb.append("WHERE `cert#` = ").append(System.lineSeparator());
      sb.append(" (SELECT `producerC#`").append(System.lineSeparator());
      sb.append("    FROM Movies").append(System.lineSeparator());
      sb.append("    WHERE title = 'Star Wars');").append(System.lineSeparator());
      return sb.toString();
    }

    public static String Subqueries_that_Produce_Scalar_Values3() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT name").append(System.lineSeparator());
      sb.append("FROM MovieExec").append(System.lineSeparator());
      sb.append("WHERE `cert#` = 12345;").append(System.lineSeparator());
      return sb.toString();
    }

    // ------------------------------------- Conditions Involving Relations

    // -- EXISTS, NOT EXISTS
    public static String Conditions_Involving_Relations1() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT `producerC#`").append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("WHERE EXISTS (SELECT * FROM MovieExec WHERE `cert#` = 12345);")
          .append(System.lineSeparator());
      return sb.toString();
    }

    public static String Conditions_Involving_Relations2() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT `producerC#`").append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("WHERE EXISTS (SELECT * FROM MovieExec WHERE `cert#` = `producerC#`);")
          .append(System.lineSeparator());
      return sb.toString();
    }

    // -- IN, NOT IN

    public static String Conditions_Involving_Relations3() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT `producerC#`").append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("WHERE `producerC#` IN (SELECT `cert#` FROM MovieExec);")
          .append(System.lineSeparator());
      return sb.toString();
    }

    // -- ANY/SOME, ALL
    // -- comparison_operator: = > < >= <= <> !=
    public static String Conditions_Involving_Relations4() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT `producerC#`").append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("WHERE `producerC#` = ANY (SELECT `cert#` FROM MovieExec);")
          .append(System.lineSeparator());
      return sb.toString();
    }

    public static String Conditions_Involving_Relations5() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT `producerC#`").append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("WHERE `producerC#` > SOME (SELECT `cert#` FROM MovieExec);")
          .append(System.lineSeparator());
      return sb.toString();
    }

    public static String Conditions_Involving_Relations6() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT `producerC#`").append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("WHERE `producerC#` > ALL (SELECT `cert#` FROM MovieExec);")
          .append(System.lineSeparator());
      return sb.toString();
    }

    /**
     * Scoping rule: MySQL evaluates from inside to outside. For example: SELECT column1 FROM t1 AS
     * x WHERE x.column1 = (SELECT column1 FROM t2 AS x WHERE x.column1 = (SELECT column1 FROM t3
     * WHERE x.column2 = t3.column1));
     */

    // ------------------------------------- Conditions Involving Tuples
    public static String Conditions_Involving_Tuples1() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT name").append(System.lineSeparator());
      sb.append("FROM MovieExec").append(System.lineSeparator());
      sb.append("WHERE `cert#` IN ").append(System.lineSeparator());
      sb.append(" (SELECT `producerC#`").append(System.lineSeparator());
      sb.append("    FROM Movies").append(System.lineSeparator());
      sb.append("    WHERE (title, year) IN").append(System.lineSeparator());
      sb.append("   (SELECT movieTitle, movieYear").append(System.lineSeparator());
      sb.append("        FROM StarsIn").append(System.lineSeparator());
      sb.append("        WHERE starName = 'Harrison Ford')").append(System.lineSeparator());
      sb.append(" );").append(System.lineSeparator());
      return sb.toString();
    }

    // -- 不使用子查询
    public static String Conditions_Involving_Tuples2() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT name").append(System.lineSeparator());
      sb.append("FROM MovieExec, Movies, StarsIn").append(System.lineSeparator());
      sb.append("WHERE `cert#` = `producerC#`").append(System.lineSeparator());
      sb.append(" AND title = movieTitle").append(System.lineSeparator());
      sb.append("    AND year = movieYear").append(System.lineSeparator());
      sb.append("    AND starName = 'Harrison Ford';").append(System.lineSeparator());
      return sb.toString();
    }

    // ------------------------------------- Correlated Subqueries

    /**
     * A correlated subquery is a subquery that contains a reference to a table that also appears in
     * the outer query. For example: SELECT * FROM t1 WHERE column1 = ANY (SELECT column1 FROM t2
     * WHERE t2.column2 = t1.column2);
     */
    public static String Correlated_Subqueries() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT title").append(System.lineSeparator());
      sb.append("FROm Movies Old").append(System.lineSeparator());
      sb.append("WHERE year < ANY ").append(System.lineSeparator());
      sb.append(" (SELECT year").append(System.lineSeparator());
      sb.append("    FROM Movies").append(System.lineSeparator());
      sb.append("    WHERE title = Old.title);").append(System.lineSeparator());
      return sb.toString();
    }

    // ------------------------------------- Subqueries in FROM Clauses

    public static String Subqueries_in_FROM_Clauses() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT name").append(System.lineSeparator());
      sb.append("FROM MovieExec, (SELECT `producerC#`").append(System.lineSeparator());
      sb.append("               FROM Movies, StarsIn").append(System.lineSeparator());
      sb.append("                                WHERE title = movieTitle")
          .append(System.lineSeparator());
      sb.append("                 AND year = movieYear").append(System.lineSeparator());
      sb.append("                                    AND starName = 'Harrison Ford') Prod")
          .append(System.lineSeparator());
      sb.append("WHERE `cert#` = Prod.`producerC#`;").append(System.lineSeparator());
      return sb.toString();
    }

    // ------------------------------------- SQL Join Expressions

    public static String SQL_Join_Expressions() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT title, year, length, genre, studioName, `producerC#`, starName")
          .append(System.lineSeparator());
      sb.append("FROM Movies JOIN StarsIn ON title = movieTitle AND year = movieYear;")
          .append(System.lineSeparator());
      return sb.toString();
    }

    /**
     * STRAIGHT_JOIN is similar to JOIN, except that the left table is always read before the right
     * table. This can be used for those (few) cases for which the join optimizer processes the
     * tables in a suboptimal order.
     */

    // ------------------------------------- Natural Joins

    public static String Natural_Joins() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT title, year, length, genre, studioName, `producerC#`, starName")
          .append(System.lineSeparator());
      sb.append("FROM Movies NATURAL JOIN StarsIn;").append(System.lineSeparator());
      return sb.toString();
    }

    // ------------------------------------- Outerjoins

    public static String Outerjoins1() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT title, year, length, genre, studioName, `producerC#`, starName")
          .append(System.lineSeparator());
      sb.append("FROM Movies LEFT OUTER JOIN StarsIn ON title = movieTitle AND year = movieYear;")
          .append(System.lineSeparator());
      return sb.toString();
    }

    /**
     * The NATURAL [LEFT] JOIN of two tables is defined to be semantically equivalent to an INNER
     * JOIN or a LEFT JOIN with a USING clause that names all columns that exist in both tables.
     */
    public static String Outerjoins2() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT title, year, length, genre, studioName, `producerC#`, starName")
          .append(System.lineSeparator());
      sb.append("FROM Movies NATURAL RIGHT OUTER JOIN StarsIn;").append(System.lineSeparator());
      return sb.toString();
    }

    // ---------------------------------------------------------------------------
    // Full Relation Operations
    // ---------------------------------------------------------------------------

    // ------------------------------------- Eliminating Duplicates
    public static String Eliminating_Duplicates() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT DISTINCT title").append(System.lineSeparator());
      sb.append("FROM Movies;").append(System.lineSeparator());
      return sb.toString();
    }

    // ------------------------------------- Duplicates in Unions, Intersections, and Differences

    public static String Duplicates_in_Unions_Intersections_and_Differences1() {
      StringBuilder sb = new StringBuilder();
      sb.append("(SELECT title, year FROM Movies)").append(System.lineSeparator());
      sb.append("UNION ").append(System.lineSeparator());
      sb.append("(SELECT movieTitle AS title, movieYear AS year FROM StarsIn);")
          .append(System.lineSeparator());
      return sb.toString();
    }

    public static String Duplicates_in_Unions_Intersections_and_Differences2() {
      StringBuilder sb = new StringBuilder();
      sb.append("(SELECT title, year FROM Movies)").append(System.lineSeparator());
      sb.append("UNION ALL").append(System.lineSeparator());
      sb.append("(SELECT movieTitle AS title, movieYear AS year FROM StarsIn);")
          .append(System.lineSeparator());
      return sb.toString();
    }

    public static String Duplicates_in_Unions_Intersections_and_Differences3() {
      StringBuilder sb = new StringBuilder();
      sb.append("(SELECT title, year FROM Movies)").append(System.lineSeparator());
      sb.append("UNION DISTINCT").append(System.lineSeparator());
      sb.append("(SELECT movieTitle AS title, movieYear AS year FROM StarsIn);")
          .append(System.lineSeparator());
      return sb.toString();
    }

    /**
     * FIXME(zhoujiagen) fix grammar 'MySqlParser.g4':
     * 
     * <pre>
     * functionNameBase : ... | REPEAT
     * </pre>
     * 
     * @return
     */
    public static String Duplicates_in_Unions_Intersections_and_Differences4() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT REPEAT('a',1) UNION SELECT REPEAT('a',1);").append(System.lineSeparator());
      return sb.toString();
    }

    public static String Duplicates_in_Unions_Intersections_and_Differences5() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT REPEAT('a',1) UNION ALL SELECT REPEAT('a',1);")
          .append(System.lineSeparator());
      return sb.toString();
    }

    public static String Duplicates_in_Unions_Intersections_and_Differences6() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT REPEAT('a',1) UNION DISTINCT SELECT REPEAT('a',1);")
          .append(System.lineSeparator());
      return sb.toString();
    }

    // ------------------------------------- Aggregation Operators

    public static String Aggregation_Operators1() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT AVG(netWorth)").append(System.lineSeparator());
      sb.append("FROM MovieExec;").append(System.lineSeparator());
      return sb.toString();
    }

    public static String Aggregation_Operators2() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT COUNT(*)").append(System.lineSeparator());
      sb.append("FROM StarsIn;").append(System.lineSeparator());
      return sb.toString();
    }

    public static String Aggregation_Operators3() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT COUNT(starName)").append(System.lineSeparator());
      sb.append("FROM StarsIn;").append(System.lineSeparator());
      return sb.toString();
    }

    public static String Aggregation_Operators4() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT COUNT(DISTINCT starName)").append(System.lineSeparator());
      sb.append("FROM StarsIn;").append(System.lineSeparator());
      return sb.toString();
    }

    // ------------------------------------- Grouping

    public static String Grouping1() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT studioName, SUM(length)").append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("GROUP BY studioName;").append(System.lineSeparator());
      return sb.toString();
    }

    public static String Grouping2() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT studioName").append(System.lineSeparator());
      sb.append("FROM Movies").append(System.lineSeparator());
      sb.append("GROUP BY studioName;").append(System.lineSeparator());
      sb.append("-- equals to").append(System.lineSeparator());
      sb.append("SELECT DISTINCT studioName").append(System.lineSeparator());
      sb.append("FROM Movies;").append(System.lineSeparator());
      return sb.toString();
    }

    public static String Grouping3() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT name, SUM(length)").append(System.lineSeparator());
      sb.append("FROM MovieExec, Movies").append(System.lineSeparator());
      sb.append("WHERE `producerC#` = `cert#`").append(System.lineSeparator());
      sb.append("GROUP BY name;").append(System.lineSeparator());
      return sb.toString();
    }

    // ------------------------------------- HAVING Clauses
    public static String HAVING_Clauses() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT name, SUM(length)").append(System.lineSeparator());
      sb.append("FROM MovieExec, Movies").append(System.lineSeparator());
      sb.append("WHERE `producerC#` = `cert#`").append(System.lineSeparator());
      sb.append("GROUP BY name").append(System.lineSeparator());
      sb.append("HAVING MIN(year) < 1930;").append(System.lineSeparator());
      return sb.toString();
    }
    // ---------------------------------------------------------------------------
    // Database System Implementation
    // ---------------------------------------------------------------------------
    // ------------------------------------- Chapter 5 查询编译器

    // 不相关的子查询
    public static String DSI5_1() {

      StringBuilder sb = new StringBuilder();
      sb.append("SELECT movieTitle").append(System.lineSeparator());
      sb.append("FROM StarsIn").append(System.lineSeparator());
      sb.append("WHERE starName IN (").append(System.lineSeparator());
      sb.append(" SELECT name").append(System.lineSeparator());
      sb.append("    FROM MovieStar").append(System.lineSeparator());
      sb.append("    WHERE birthdate LIKE '%1960');").append(System.lineSeparator());
      return sb.toString();
    }

    // 等价的查询: 移除了子查询
    public static String DSI5_2() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT movieTitle").append(System.lineSeparator());
      sb.append("FROM StarsIn, MovieStar").append(System.lineSeparator());
      sb.append("WHERE starName = name").append(System.lineSeparator());
      sb.append(" AND birthdate LIKE '%1960';").append(System.lineSeparator());
      return sb.toString();
    }

    // 相关子查询
    public static String DSI5_3() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT DISTINCT m1.movieTitle, m1.movieYear").append(System.lineSeparator());
      sb.append("FROM StarsIn m1").append(System.lineSeparator());
      sb.append("WHERE m1.movieYear - 40 <= (").append(System.lineSeparator());
      sb.append(" SELECT AVG(birthdate)").append(System.lineSeparator());
      sb.append("    FROM StarsIn m2, MovieStar s").append(System.lineSeparator());
      sb.append("    WHERE m2.starName = s.name").append(System.lineSeparator());
      sb.append("   AND m1.movieTitle = m2.movieTitle").append(System.lineSeparator());
      sb.append("        AND m1.movieYear = m2.movieYear);").append(System.lineSeparator());
      return sb.toString();
    }

  }
}
