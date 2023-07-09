# Getting Started

### Requirements

We are going to use liquibase for this case. 
Liquibase is used for migrations: 
* migrations are the process of moving programming code from a system to another
* this is fairly large and complex topic of maintaining computer applications
* databases migrations typically need to occur prior to or in conjunction with application code
* can lead to run time errors if databases doen't match what is expected
* database migrations are a very important part of the process of moving application code to production
* keep in mind, that migration is not the role of the developer usually

--------------

### Why using a migration tool

* hibernate can manage my schema fine, so why using a migration tool ?
* management many environments and databases: 
  * h2, CI/DO; QA, UAT, Production
* development and CI/CD databases are easy, just rebuild each time
* QA, UAT, Production are permanent databases
  * what state they're in ?
  * has a script applied ?
  * how to create a new database to a release ?

### Liquibase and flyway 
* common features 
  * command line tools
  * integration with maven and gradle
  * springboot integration
  * use script files which can be versioned and tracked
  * use database table to track changes
  * have commercial support

### springboot integration 
* spring boot offers support for both liquibase and flyway
* fundamentally spring boot will apply change sets
* spring boot's support is narrow in scope
* both tools have additional capabilities available from the command line or build tool plugins
* the spring boot integration is convenient for migrations
* both tools can be used independently of spring boot

### liquibase vs flyway
* liquibase and flyway are similar in terms of functionality
* share same concepts, slightly different terminology
* liquibase supports change scripts in SQL, XML, YAML and JSON
* flyway supports SQL and java only
* liquibase is a larger and more robust product
* flyway seems to have more popularity
* both are mature and widely used

### which to use
* liquibase is probably a better solution for large enterprises with complex environments
* flyway is good for 90% of applications which don't need the additional capabilities
* flyway is slightly easier to use than liquibase

### Some terminology
* migrate: migrate to latest version
* clean: drops all database object (not for production)
* info: prints info about migration 
* validate: validates applied migration against available
* undo: reverts most recently applied migration
* baseline: baselines an existing database
* repair: used to fix problems with schema history table

### Running Flyway
* with CLI (command line interface): available for windows, MacOs and Linux (not covered)
* Maven / gradle plugins (not covered)
* Spring boot: run flyway at startup to update configured database to latest changeset

### next step 
* configure spring boot support for flyway
* alter existing table with flyway 
* add new table with flyway

### why not baseline ?
* baseline is for introducing flyway to an existing database schema
* assumes that database tables and objects will be there
* we want to start from empty database



