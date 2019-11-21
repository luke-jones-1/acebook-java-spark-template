### Acebook

[![Build Status](https://travis-ci.org/luke-jones-1/acebook-java-spark-template.svg?branch=master)](https://travis-ci.org/luke-jones-1/acebook-java-spark-template)
[![codecov](https://codecov.io/gh/luke-jones-1/acebook-java-spark-template/branch/master/graph/badge.svg)](https://codecov.io/gh/luke-jones-1/acebook-java-spark-template)

#### Running the application

 - Make sure postgres is running
 - `createdb acebook`
 - `brew install maven`
 - `mvn install`
 - Open project in intellij 
 - Click the run button
 - Hit http://localhost:4567/posts
 
 #### Running the unit tests
  - Make sure postgres is running
  - `createdb acebook-test`
  - Open project in intellij 
  - Go to tests and hit run button
 
 #### Skills checklist
   
  - [ ] I can create a new endpoint and render a static template view
  - [ ] I can create a form which posts data to a new endpoint
  - [ ] I can persist data from an endpoint into the database
  - [ ] I can update the data model and database schema to include a new field
  
  
 #### Recommended resources
 
 - [Spark docs](http://sparkjava.com/)
 - [Velocity template docs](https://velocity.apache.org/engine/1.7/user-guide.html)
 - [Velocity template blog post](https://www.learnhowtoprogram.com/java-old-reference-only/web-applications-in-java/velocity-templates-in-spark)
 - [Velocity template + spark project example](https://github.com/epicodus-lessons/java-hello-friend-with-spark)
 - [Flyway migrations (db schema management) docs](https://flywaydb.org/documentation/migrations) 

#### Posting user stories (Leatherhead)
- As a user I would like to see the latest posts
  listed on the acebook
- As a user I would like to create a new post
  and have it appear with the latest posts
- As a user I would like to be able to like a post
  and see the number of likes next to the post
- As a user I only want people to be able to
  like a post once to avoid spam
- As a user I would like to be able to make
  and receive comments on posts
- As a user I would like to see
  the date of a post

heyfrens
