# Paging and sorting

### Overview 
* Paging:  is a way to get a page of data from a long list of values
  * For example page 3 with 100 records
  * Commonly used on websites search results or catalogs
* Sorting : is how the data is ordered
  * can be natural (order from database)
  * or by one or more columns

### SQL Paging
* SQL Paging uses SQL clauses of limit and offset
  * limit: limit the number of rows returned
  * offset: limit the number of row to skip over
  * Simple example: 30 records and 10 per page
    * page 1: limit 10, offset 0
    * page 2: limit 10, offset 10
    * page 2: limit 10, offset 20

### SQL Sorting
* uses SQL order by clause
  * ASC (default)
  * DESC: descending
* physical order: when no sort clause id provided, whatever order the records are stored in the database
* often will return rows in the same order, but it is not guaranteed

### Paging and Sorting performance
would it be faster to do perform paging and sorting on the application server ?
* generally no
* modern databases are very efficient with paging and sorting
* plus need to transfer large data set between database and server
* there are edge case where offloading the computational workload does make sense





