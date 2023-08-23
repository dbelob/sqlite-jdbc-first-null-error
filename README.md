# Examples to reproduce [issue #948](https://github.com/xerial/sqlite-jdbc/issues/948) for SQLite JDBC Driver

There ara two classes:
1. *JdbcTemplateTest* contains tests when used *Spring JdbcTemplate* methods 
2. *PreparedStatementTest* contains tests when used *JDBC PreparedStatement* methods

In both cases *updateWithFirstNull* test fails with *NullPointerException* since *SQLite JDBC 3.41.2.2*
