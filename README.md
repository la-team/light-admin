# LightAdmin - Pluggable CRUD and data administration UI library for Java web applications developed in a "Rapid" manner

<img src="https://travis-ci.org/la-team/light-admin.png?branch=master"/>

The primary goal of the project is to speed up application development 
by bringing pluggable fully operational data management back-end for JPA based applications and to relieve your codebase for more important stuff.

[Light Admin](http://lightadmin.org) makes it possible to <b>focus on the stuff that matters</b> instead of spending time on auxiliary functionality.

## Features ##

* <b>DSL configurations</b>: Allows developers to easily configure their administration user interface
* <b>Displaying persistent entities</b>: Customizable Listing & Quick Views with paging & sorting capabilities
* <b>CRUD operations</b>: Complete entities manipulation support (including their associations)
* <b>Automatic Validation</b>: JSR-303 annotation-based validation rules support
* <b>Search</b>: Allows users to search entities by text fields, dates, numeric values & associations
* <b>Filtering Scopes</b>: Use scopes to filter data by predefined criteria
* <b>Pluggable Security</b>: Authentication based on [Spring Security](http://www.springsource.org/spring-security)
* <b>REST API</b>: Enriching your application with REST API based on [Spring Data REST](http://www.springsource.org/spring-data/rest)
* <b>Easy integration</b>: Servlet 2.5/3.0 web applications supported

## Integration examples ##

* [LightAdmin and Spring Boot](https://github.com/la-team/lightadmin-springboot)
* [LightAdmin and JHipster](https://github.com/la-team/lightadmin-jhipster)
* [LightAdmin running on Heroku](https://github.com/la-team/lightadmin-heroku)

## Documentation & Support ##

* Web site: [lightadmin.org](http://lightadmin.org)
* Documentation & Guides: [lightadmin.org/getting-started/](http://lightadmin.org/getting-started/)
* Wiki: [github.com/la-team/light-admin/wiki](http://github.com/la-team/light-admin/wiki)
* Live demo: [lightadmin.org/demo](http://lightadmin.org/demo)
* CI Server: [lightadmin.org/jenkins](http://lightadmin.org/jenkins)
* Use Google Groups for posting questions: [groups.google.com/group/lightadmin](http://groups.google.com/group/lightadmin)
* Use Stack Overflow for posting questions with <b>lightadmin</b> tag
* Contact LightAdmin Team directly on Twitter: <b>@lightadm_team</b>

## Bug Reports ##

* Bug Reports: [github.com/la-team/light-admin/issues](http://github.com/la-team/light-admin/issues)

## License ##

* <b>LightAdmin</b> is released under version 2.0 of the Apache License.

## Contribute ##

You're interested in contributing to LightAdmin? AWESOME. Here are the basic steps:

* Fork <b>LightAdmin</b> from here: http://github.com/la-team/light-admin
* Clone your fork
* Hack away
* If necessary, rebase your commits into logical chunks, without errors
* Verify your code by running the test suite, and adding additional tests if able
* Push the branch up to GitHub
* Send a pull request to the <b>la-team/light-admin</b> project

We'll do our best to get your changes in!

## Getting started ##

Declare maven dependency for using with Spring 4.0.X directly from Maven Central

```xml
<dependency>
  <groupId>org.lightadmin</groupId>
  <artifactId>lightadmin</artifactId>
  <version>1.2.0.RC1</version>
</dependency> 
```

or

```xml
<dependency>
  <groupId>org.lightadmin</groupId>
  <artifactId>lightadmin</artifactId>
  <version>1.2.0.BUILD-SNAPSHOT</version>
</dependency> 
```

For snapshots and LightAdmin compatible with Spring 3.2.X, please declare LA Nexus repositories:

```xml
<repositories>
  <repository>
    <id>lightadmin-nexus-releases</id>
    <url>http://lightadmin.org/nexus/content/repositories/releases</url>
    <releases>
      <enabled>true</enabled>
      <updatePolicy>always</updatePolicy>
    </releases>
  </repository>
  <repository>
    <id>lightadmin-nexus-snapshots</id>
    <url>http://lightadmin.org/nexus/content/repositories/snapshots</url>
    <snapshots>
      <enabled>true</enabled>
      <updatePolicy>always</updatePolicy>
    </snapshots>
  </repository>  
</repositories>
```

And dependency

```xml
<dependency>
  <groupId>org.lightadmin</groupId>
  <artifactId>lightadmin</artifactId>
  <version>1.0.0.M2</version>
</dependency> 
```



### Enable LightAdmin web-module in your <b>web.xml</b> if you have one: ###

```xml
<context-param>
  <param-name>light:administration:base-url</param-name>
  <param-value>/admin</param-value>
</context-param>

<context-param>
  <param-name>light:administration:security</param-name>
  <param-value>true</param-value>
</context-param>

<context-param>
  <param-name>light:administration:base-package</param-name>
  <param-value>[package with @Administration configurations, ex.: org.lightadmin.demo.config]</param-value>
</context-param>
```

### Or enable LightAdmin web-module in your <b>WebApplicationInitializer</b>: ###

```java
@Override
public void onStartup(ServletContext servletContext) throws ServletException {
  servletContext.setInitParameter(LIGHT_ADMINISTRATION_BASE_URL, "/admin");
  servletContext.setInitParameter(LIGHT_ADMINISTRATION_BACK_TO_SITE_URL, "http://lightadmin.org");
  servletContext.setInitParameter(LIGHT_ADMINISTRATION_BASE_PACKAGE, "org.lightadmin.administration");

  super.onStartup(servletContext);
}
```


### Include your JPA persistence provider of choice (Hibernate, EclipseLink, OpenJpa) and setup basic <b>Spring JPA</b> configuration. ###

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:jdbc="http://www.springframework.org/schema/jdbc"
       xmlns:jpa="http://www.springframework.org/schema/data/jpa"
       xsi:schemaLocation="http://www.springframework.org/schema/jdbc 
                           http://www.springframework.org/schema/jdbc/spring-jdbc.xsd
                           http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/data/jpa
                           http://www.springframework.org/schema/data/jpa/spring-jpa.xsd">
  
  <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
    <property name="dataSource" ref="dataSource" />
    <property name="jpaVendorAdapter">
      <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter" />
    </property>
  </bean>

  <bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
    <property name="entityManagerFactory" ref="entityManagerFactory" />
  </bean>

</beans>
```

### Create an entity: ###

```java
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  private String firstname;
  private String lastname;
       
  // Getters and setters
}
```
### Create an <b>@Administration configuration</b> in the package defined in <b>web.xml</b> previously: ###

```java
public class UserAdministration extends AdministrationConfiguration<User> {

  public EntityMetadataConfigurationUnit configuration( EntityMetadataConfigurationUnitBuilder configurationBuilder ) {
    return configurationBuilder.nameField( "firstname" ).build();
  }

  public ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
    return screenContextBuilder
      .screenName( "Users Administration" )
      .menuName( "Users" )
      .build();
  }

  public FieldSetConfigurationUnit listView( final FieldSetConfigurationUnitBuilder fragmentBuilder ) {
    return fragmentBuilder
      .field( "firstname" ).caption( "First Name" )
      .field( "lastname" ).caption( "Last Name" )
      .build();
  }

```

Voila! You have a brand new LightAdmin back-end configured.

## Check Out and Build from Source

1. Clone the repository from GitHub:

		$ git clone git://github.com/la-team/light-admin.git

2. Navigate into the cloned repository directory:

		$ cd light-admin

3. The project uses [Maven](http://maven.apache.org/) to build:

		$ mvn clean install

## Running from the Command Line

By default, the app will run in 'embedded' mode which does not require any external setup. The Tomcat 7 Maven plugin is configured for you in the POM file.

1. Navigate into demo application directory:

		$ cd lightadmin-sandbox

2. Launch Tomcat from the command line:

		$ mvn tomcat7:run

3. Access the deployed webapp at 

		http://localhost:8080/lightadmin-sandbox

## LightAdmin integration example

We prepared an example how easily you can integrate LightAdmin back-end to existing web application.

It's based on [Spring Travel](https://github.com/SpringSource/spring-webflow-samples/tree/master/booking-mvc) reference application.

1. Clone the repository from GitHub:

		$ git clone git://github.com/la-team/lightadmin-spring-travel.git

2. Navigate into the cloned repository directory:

		$ cd lightadmin-spring-travel

3. The project uses [Maven](http://maven.apache.org/) to build:

		$ mvn clean install
			
4. Launch Tomcat from the command line:

		$ mvn tomcat7:run

5. Access the deployed webapp at 

		http://localhost:8080/booking-mvc
		
## Screenshots

<b>Login to LightAdmin:</b>

![Login view](https://github.com/la-team/light-admin/raw/master/screenshots/login.png "login view")

<b>Dashboard:</b>

![Dashboard view](https://github.com/la-team/light-admin/raw/master/screenshots/dashboard.png "dashboard view")

<b>List of persistent entities configured:</b>

![List view](https://github.com/la-team/light-admin/raw/master/screenshots/list-view.png "list view")

<b>Search entities by criteria:</b>

![List view & Filtering](https://github.com/la-team/light-admin/raw/master/screenshots/search.png "list view & filtering")

<b>Quick view for particular entity:</b>

![Quick view](https://github.com/la-team/light-admin/raw/master/screenshots/quick-view.png "quick view")

<b>Editing entity:</b>

![Form view](https://github.com/la-team/light-admin/raw/master/screenshots/form-view-validation.png "form view")

<b>Show entity with all fields:</b>

![Show view](https://github.com/la-team/light-admin/raw/master/screenshots/show-view.png "show view")
