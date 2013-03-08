# Light Admin #

[Light Admin](http://lightadmin.org) is an elegant solution for building administration style interfaces for Java web applications.

The primary goal of the project is to speed up application development 
by bringing pluggable easy-to-use data management back-end for JPA based applications.

## Features ##

This module deals with enhanced support for JPA based data access layers

* <b>DSL configurations</b>: Allows developers to easily configure their administration domain.
* <b>Displaying persistent entities</b>: Customizable Listing & Quick Views with paging & sorting capabilities.
* <b>CRUD operations</b>: Full support for entities manipulation together with their associations.
* <b>Automatic Validation</b>: JSR-303 annotation-based validation rules support.
* <b>Search</b>: Allows users to search entities by text fields, dates, numeric values & associations.
* <b>Filtering Scopes</b>: Use scopes to create sections of mutually exlusive entities for quick navigation.
* <b>Pluggable Security</b>: Authentication based on [Spring Security](http://www.springsource.org/spring-security)
* <b>REST API</b>: Enriching your application with REST API based on [Spring Data REST](http://www.springsource.org/spring-data/rest)
* <b>Easy integration</b>: Servlet 2.5 and 3.0 based web applications supported.

## Documentation & Support ##

* Documentation & Guides: [lightadmin.org/documentation](http://lightadmin.org/documentation)
* Wiki: [github.com/max-dev/light-admin/wiki](http://github.com/max-dev/light-admin/wiki)
* Live demo: [demo.lightadmin.org/admin](http://demo.lightadmin.org/admin)
* Web site: [lightadmin.org](http://lightadmin.org)
* For more detailed questions: [groups.google.com/group/lightadmin](http://groups.google.com/group/lightadmin)

## Bug Reports & Contribution:

* Bug Reports: [github.com/max-dev/light-admin/issues](http://github.com/max-dev/light-admin/issues)
* <b>Want to Contribute?</b>: Read the Guide ;)

## Getting started ##

Download the jar though Maven:

```xml
<dependency>
  <groupId>org.lightadmin</groupId>
  <artifactId>lightadmin</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency> 
```

Enable LightAdmin web-module in you web.xml:

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

Also include your JPA persistence provider of choice (Hibernate, EclipseLink, OpenJpa) and setup basic <b>Spring JPA</b> configuration.

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

Create an entity:

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
Create an <b>@Administration configuration</b> in the package defined in <b>web.xml</b> previously:

```java
@Administration( User.class )
public class UserAdministration {

  public static EntityMetadataConfigurationUnit configuration( EntityMetadataConfigurationUnitBuilder configurationBuilder ) {
    return configurationBuilder.nameField( "firstname" ).build();
  }

  public static ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
    return screenContextBuilder
      .screenName( "Users Administration" )
      .menuName( "Users" )
      .build();
  }

  public static FieldSetConfigurationUnit listView( final FieldSetConfigurationUnitBuilder fragmentBuilder ) {
    return fragmentBuilder
      .field( "firstname" ).caption( "First Name" )
      .field( "lastname" ).caption( "Last Name" )
      .build();
  }

```

Voila! You have a brand new Light Admin back-end configured.

## Check Out and Build from Source

1. Clone the repository from GitHub:

		$ git clone git://github.com/max-dev/light-admin.git

2. Navigate into the cloned repository directory:

		$ cd light-admin

3. The project uses [Maven](http://maven.apache.org/) to build:

		$ mvn clean install

## Running from the Command Line

Deploy the .war to any Servlet 2.5 or Servlet 3.0 container, such as Tomcat. By default, the app will run in 'embedded' mode which does not require any external setup. The Cargo Tomcat 7 Maven plugin is configured for you in the POM file.

1. Launch Tomcat from the command line:

		$ mvn cargo:run

2. Access the deployed webapp at 

		http://localhost:8080/light-admin


## Screenshots from [LightAdmin DEMO application](http://demo.lightadmin.org/admin)

<b>Login to LightAdmin:</b>

![Login view](https://github.com/max-dev/light-admin/raw/master/screenshots/login.png "login view")

<b>Dashboard:</b>

![Dashboard view](https://github.com/max-dev/light-admin/raw/master/screenshots/dashboard.png "dashboard view")

<b>List of persistent entities configured:</b>

![List view](https://github.com/max-dev/light-admin/raw/master/screenshots/list-view.png "list view")

<b>Search entities by criteria:</b>

![List view & Filtering](https://github.com/max-dev/light-admin/raw/master/screenshots/search.png "list view & filtering")

<b>Quick view for particular entity:</b>

![Quick view](https://github.com/max-dev/light-admin/raw/master/screenshots/quick-view.png "quick view")

<b>Editing entity:</b>

![Form view](https://github.com/max-dev/light-admin/raw/master/screenshots/form-view-validation.png "form view")

<b>Show entity with all fields:</b>

![Show view](https://github.com/max-dev/light-admin/raw/master/screenshots/show-view.png "show view")
