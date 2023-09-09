## QueryBuilder

Please use it if you need it. However, be aware that there may be errors or insufficient functions

I was making a native query in the project, and I wanted to use it as Java code than Mybatis, and I made the query sentence more easy to see, so I made it like this.



QueryBuilder is a class of utilities designed to help Java developers organize SQL queries in a chain and legible manner rather than in pure strings.
This Builder reduces the likelihood of errors when configuring direct queries and provides a more structured way to create and correct Query statements.

Features
Fluent API: Easily chain method calls to construct a DELETE statement.
Parameter Management: Safely manage parameters associated with the DELETE query.
Extensive Condition Support: Easily add conditions such as equals, less than, greater than, in, between, etc., with dedicated methods.
Readable Code: Enhance code readability by abstracting SQL string concatenation.

## How to Use

### DeleteBuilder

### InsertBuilder

### SelectBuilder

### UpdateBuilder
