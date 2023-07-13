# Spring data jpa queries

## Defining Query Methods
The repository proxy has two ways to derive a store-specific query from the method name:

By deriving the query from the method name directly.

By using a manually defined query.

Available options depend on the actual store. However, there must be a strategy that decides what actual query is created. The next section describes the available options.

###  Query Lookup Strategies
The following strategies are available for the repository infrastructure to resolve the query. With XML configuration, you can configure the strategy at the namespace through the query-lookup-strategy attribute. For Java configuration, you can use the queryLookupStrategy attribute of the EnableJpaRepositories annotation. Some strategies may not be supported for particular datastores.

* CREATE attempts to construct a store-specific query from the query method name. The general approach is to remove a given set of well known prefixes from the method name and parse the rest of the method.

* USE_DECLARED_QUERY tries to find a declared query and throws an exception if it cannot find one. The query can be defined by an annotation somewhere or declared by other means. See the documentation of the specific store to find available options for that store. If the repository infrastructure does not find a declared query for the method at bootstrap time, it fails.

* CREATE_IF_NOT_FOUND (the default) combines CREATE and USE_DECLARED_QUERY. It looks up a declared query first, and, if no declared query is found, it creates a custom method name-based query. This is the default lookup strategy and, thus, is used if you do not configure anything explicitly. It allows quick query definition by method names but also custom-tuning of these queries by introducing declared queries as needed.

### Query Creation
The query builder mechanism built into the Spring Data repository infrastructure is useful for building constraining queries over entities of the repository.

The following example shows how to create a number of queries:

  ```
  interface PersonRepository extends Repository<Person, Long> {
  
  List<Person> findByEmailAddressAndLastname(EmailAddress emailAddress, String lastname);
  
  // Enables the distinct flag for the query
  List<Person> findDistinctPeopleByLastnameOrFirstname(String lastname, String firstname);
  List<Person> findPeopleDistinctByLastnameOrFirstname(String lastname, String firstname);
  
  // Enabling ignoring case for an individual property
  List<Person> findByLastnameIgnoreCase(String lastname);
  // Enabling ignoring case for all suitable properties
  List<Person> findByLastnameAndFirstnameAllIgnoreCase(String lastname, String firstname);
  
  // Enabling static ORDER BY for a query
  List<Person> findByLastnameOrderByFirstnameAsc(String lastname);
  List<Person> findByLastnameOrderByFirstnameDesc(String lastname);
  }
  ```
  
Parsing query method names is divided into subject and predicate. The first part (find…By, exists…By) defines the subject of the query, the second part forms the predicate. The introducing clause (subject) can contain further expressions. Any text between find (or other introducing keywords) and By is considered to be descriptive unless using one of the result-limiting keywords such as a Distinct to set a distinct flag on the query to be created or Top/First to limit query results.

The appendix contains the full list of query method subject keywords and query method predicate keywords including sorting and letter-casing modifiers. However, the first By acts as a delimiter to indicate the start of the actual criteria predicate. At a very basic level, you can define conditions on entity properties and concatenate them with And and Or.

The actual result of parsing the method depends on the persistence store for which you create the query. However, there are some general things to notice:

The expressions are usually property traversals combined with operators that can be concatenated. You can combine property expressions with AND and OR. You also get support for operators such as Between, LessThan, GreaterThan, and Like for the property expressions. The supported operators can vary by datastore, so consult the appropriate part of your reference documentation.

The method parser supports setting an IgnoreCase flag for individual properties (for example, findByLastnameIgnoreCase(…)) or for all properties of a type that supports ignoring case (usually String instances — for example, findByLastnameAndFirstnameAllIgnoreCase(…)). Whether ignoring cases is supported may vary by store, so consult the relevant sections in the reference documentation for the store-specific query method.

You can apply static ordering by appending an OrderBy clause to the query method that references a property and by providing a sorting direction (Asc or Desc). To create a query method that supports dynamic sorting, see “Paging, Iterating Large Results, Sorting”.


###  Null Handling of Repository Methods
As of Spring Data 2.0, repository CRUD methods that return an individual aggregate instance use Java 8’s Optional to indicate the potential absence of a value. Besides that, Spring Data supports returning the following wrapper types on query methods:

com.google.common.base.Optional

scala.Option

io.vavr.control.Option

Alternatively, query methods can choose not to use a wrapper type at all. The absence of a query result is then indicated by returning null. Repository methods returning collections, collection alternatives, wrappers, and streams are guaranteed never to return null but rather the corresponding empty representation.

### Nullability Annotations
You can express nullability constraints for repository methods by using Spring Framework’s nullability annotations. They provide a tooling-friendly approach and opt-in null checks during runtime, as follows:

* @NonNullApi: Used on the package level to declare that the default behavior for parameters and return values is, respectively, neither to accept nor to produce null values.
* @NonNull: Used on a parameter or return value that must not be null (not needed on a parameter and return value where @NonNullApi applies).
* @Nullable: Used on a parameter or return value that can be null.

Spring annotations are meta-annotated with JSR 305 annotations (a dormant but widely used JSR). JSR 305 meta-annotations let tooling vendors (such as IDEA, Eclipse, and Kotlin) provide null-safety support in a generic way, without having to hard-code support for Spring annotations. To enable runtime checking of nullability constraints for query methods, you need to activate non-nullability on the package level by using Spring’s @NonNullApi in package-info.java, as shown in the following example:

````
@org.springframework.lang.NonNullApi
package com.acme;
````
Once non-null defaulting is in place, repository query method invocations get validated at runtime for nullability constraints. If a query result violates the defined constraint, an exception is thrown. This happens when the method would return null but is declared as non-nullable (the default with the annotation defined on the package in which the repository resides). If you want to opt-in to nullable results again, selectively use @Nullable on individual methods. Using the result wrapper types mentioned at the start of this section continues to work as expected: an empty result is translated into the value that represents absence.


e following example shows a number of the techniques just described:

Using different nullability constraints
package com.acme;

    interface UserRepository extends Repository<User, Long> {
    
    User getByEmailAddress(EmailAddress emailAddress);
    
    @Nullable
    User findByEmailAddress(@Nullable EmailAddress emailAdress);
    
    Optional<User> findOptionalByEmailAddress(EmailAddress emailAddress);
    }

The repository resides in a package (or sub-package) for which we have defined non-null behavior.
Throws an EmptyResultDataAccessException when the query does not produce a result. Throws an IllegalArgumentException when the emailAddress handed to the method is null.
Returns null when the query does not produce a result. Also accepts null as the value for emailAddress.
Returns Optional.empty() when the query does not produce a result. Throws an IllegalArgumentException when the emailAddress handed to the method is null.
