Purpose: 

Running WebApp in a container and connecting to PostgreSQL database running in another container.

running a PostgreSQL container as a dependent Datasource.
deploying a web-application in a container, and linking to the PostgreSQL database
running a PgAdmin container to manage the database: flask_wtf.csrf.CSRFError: 400 Bad Request: The CSRF tokens do not match.

put all three containers in one network. 

````
docker network create mywebapp-network: creating a network

docker version : API version > 1.2 to run a docker connect command

docker network connect mywebapp-network myPostgres

docker network inspect mywebapp-network: inspecting which cantainer is connecting to this network

````

**On Delete No Action** 

Order keeps the FK that references to Client; as Cascade = default, i.e. {}, then SQL shows ON DELETE NO ACTION. 

![image](https://user-images.githubusercontent.com/17804600/123317968-f88af400-d52e-11eb-8187-18838c87e2c9.png)

**About @DataJPATest**

By default, it looks for an embedded database, but such a behaviour can be disabled and linking to a real database. 

````
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
````

**Flyway**
Flyway supports SQL and Java callbacks. To use SQL-based callbacks, place the callback scripts in the classpath:db/migration folder. To use Java-based callbacks, create one or more beans that implement Callback. Any such beans are automatically registered with Flyway. They can be ordered by using @Order or by implementing Ordered. Beans that implement the deprecated FlywayCallback interface can also be detected, however they cannot be used alongside Callback beans.

By default, Flyway autowires the (@Primary) DataSource in your context and uses that for migrations. If you like to use a different DataSource, you can create one and mark its @Bean as @FlywayDataSource. If you do so and want two data sources, remember to create another one and mark it as @Primary. Alternatively, you can use Flyway’s native DataSource by setting spring.flyway.[url,user,password] in external properties. Setting either spring.flyway.url or spring.flyway.user is sufficient to cause Flyway to use its own DataSource. If any of the three properties has not be set, the value of its equivalent spring.datasource property will be used.

There is a Flyway sample so that you can see how to set things up.

You can also use Flyway to provide data for specific scenarios. For example, you can place test-specific migrations in src/test/resources and they are run only when your application starts for testing. Also, you can use profile-specific configuration to customize spring.flyway.locations so that certain migrations run only when a particular profile is active. For example, in application-dev.properties, you might specify the following setting:

spring.flyway.locations=classpath:/db/migration,classpath:/dev/db/migration
With that setup, migrations in dev/db/migration run only when the dev profile is active.

**Spirng JPA database Init**

the ddl-auto attribute controls how hibernate run ddl to create db schema. 
spring.jpa.hibernate.ddl-auto = {none, create, create-drop, validate, update} 
for embedded database, spring select create-drop by default, and therefore creating schema automatically; for a real database, spring select none. 

**Implicit Inner Join with Single-Valued Association Navigation**

We don't use the JOIN key word. Whenever we navigate a single-valued association, JPA automatically creates an implicit join. 
````
"SELECT o.client FROM Client-orders o"
````
Order-Client is a many to one relationship. We didn't specify an inner join from the above JPQL query, but the JPA automatically creates an inner join between table Client-Orders and Client.

I didn't see the benifit of using implict join; maybe it is good enough to stick with the explicit join. 

**Explicit Inner Join with Single-Valued Association**

We use the JOIN keyword in our JPQL query
````
    @Query("SELECT c FROM Client c INNER JOIN c.orders o WHERE o.businessId = :businessId")
````    

**Joins in the WHERE Clause**

We can list two entities in the FROM clause and then specify the join condition in the WHERE clause. We use this method to join two entities when databae FK aren't in place. 
````
  "SELECT d FROM Employee e, Department d WHERE e.department = d";
````

**Cartesian Product**

We can list two entities in the FROM clause without specifying join condition, meaning that we get a full proudct of two tables. Both tables are paired together in a combination format. maybe this kind of queries won't perform well. 
````
  "SELECT d FROM Employee e, Department d"
````

**Join Fetch**
Join fetch is used to fetch lazy-loaded derivatives eagerly for the current query. 

````
 "SELECT d FROM Department d JOIN FETCH d.employees",
 ````
 
 Although this query looks very similar to other queries, there is one difference, and that is that the Employees are eagerly loaded. That means that once we call getResultList in the test above, the Department entities will have their employees field loaded, thus saving us another trip to the database.

But be aware of the memory trade-off. We may be more efficient because we only performed one query, but we also loaded all Departments and their employees into memory at once.

We can also perform the outer fetch join in a similar way to outer joins, where we collect records from the left entity that don't match the join condition. And additionally, it eagerly loads the specified association:

````
"SELECT d FROM Department d LEFT JOIN FETCH d.employees"
````
The JOIN FETCH expression is not a regular JOIN and it does not define a JOIN variable. Its only purpose is specifying related objects that should be fetched from the database with the query results on the same round trip. 


**About OffSetDateTime and PostgreSQL**
When saving a offSetDateTime value into PostgreSQL database, the time offset will be projected into the time zone that PostgreSQL host is running on. Populating the following DateTime with TimeZone into the database, 
````
insert into Client_orders(id, creation_date_time_with_zone, order_status, FK_Client_Id) values(2, '1999-01-08 14:05:06-08', 'SUSPENDING',1);

````
Then the date-time is automatically projected into  ``1999-01-08 23:05:06+01``, inline with the database time-zone (+1:00)


**About JPA translating OffSetDateTimet into Database**

The columnDefinition tells the Hibernate that please translate Java 8 OffsetDateTime into a cell in the db a timeStamp with a time-zone. Literally, "TIMESTAMP" indicates the time stamp formate, i.e. including date and time; "WITH TIME ZONE" tells that date-time followed by a time-zone delimited by '+' or '-'.

````
    @Column(name = "creationDateTimeWithZone", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;
````

if writting columeDifination in another way:
````
    @Column(name = "creationDateTimeWithZone", columnDefinition = "TIMES WITH TIME ZONE")
    private OffsetDateTime createdAt;
````
in the database, supprise, I found only time followed by time-zone.

**Parsing OffsetDateTime in Java**

Date and Time are separated by 'T', and time-zone starts with '+' or '-'; otherwise it leads to parsing error. 

````
        OffsetDateTime expectedTimeStamp = OffsetDateTime.parse("1999-01-08T14:05:06+01");
````

**About Spring-JPA Projections**

Using an entity-based projections retreives an aggregate root(repository managed) and its dependencies; in some cases, it overkills because of requiring only a portiion of the entire dataset. Spring data allows modeling dedicated return types, to more selectively retrieve partial views of the managed aggregates.

Interface-based projections

to limit the result of the queries to only the name attributes is by declaring an interface that exposes accessor methods(getters) for the properties to be read. 

Interface-based projection supports nested projections; it allows selectivley retrieve its aggregates. 

Interface-based project maybe closed projection(one getter maps one name attributes) or open projection (re-orgnise name attributes to a getter by Spel)


Class-based projections (DTOs)

Another way of defining projections is by using value type DTOs (Data Transfer Objects) that hold properties for the fields that are supposed to be retrieved. These DTO types can be used in exactly the same way projection interfaces are used, except that no proxying happens and no nested projections can be applied.

If optimizing the query execution by limiting the fields to be loaded, the fields to be loaded are determined from the parameter names of the constructor that is exposed. so paratmeer name within a DTO constructor decide what attributes are retreived from the aggregate root. 

Class-based project doesn't support the nested projection. Using Lombok @Value annotation can dramatically simplify the code for creating a DTO. 


Spring JPA Projection may optimize the SQL query according to queried data model. 

````
public interface OrderRepository extends CrudRepository<Order, Integer> {

    <T> T findByBusinessId(UUID businessId, Class<T> type);
}
````

if projecting to OrderInfo interface, Hibernate generates a simplified sql query.

````
Hibernate: 
select order0_.business_id as col_0_0_, order0_.creation_date_time_with_zone as col_1_0_, order0_.order_status as col_2_0_ from client_orders order0_ where order0_.business_id=?
````

In another case, if projecting to Order entity, then Hibernate generates an left outer join with Client(eager loading for to-one relationship).

````
Hibernate: 
select order0_.id as id1_1_0_, orderitems1_.id as id1_2_1_, order0_.business_id as business2_1_0_, order0_.fk_client_id as fk_clien5_1_0_, order0_.creation_date_time_with_zone as creation3_1_0_, order0_.order_status as order_st4_1_0_, orderitems1_.amount as amount2_2_1_, orderitems1_.fk_order_id as fk_order4_2_1_, orderitems1_.product_name as product_3_2_1_, orderitems1_.fk_order_id as fk_order4_2_0__, orderitems1_.id as id1_2_0__ from client_orders order0_ inner join order_items orderitems1_ on order0_.id=orderitems1_.fk_order_id where order0_.business_id=?
````

**About One-Many and Many-One default fetch-type**

Jpa @OneToMany fetch model defaults to lazy-loading; @ManyToOne fetch model defaults to eager-loading.  

**About @DynamicInsert annotation**

for inserting, @DynamicInsert signals that should this entity use dynamic sql generation where only non-null columns get referenced in the prepared sql statement.

````
    @Column(updatable = false, nullable = false)
    @ColumnDefault("gen_random_uuid()")
    @Type(type = "uuid-char")
    private UUID businessId;
````

The field bussinessId is constrained to a default uuid, which is auto-generated by a database function, meanwhile this field doesn't allow a null-value.

as saving the instance from java side, it causes a Data integraty exception: 

> org.springframework.dao.DataIntegrityViolationException: not-null property references a null or transient value : com.ynz.demo.containerizedapp.domain.Order.businessId; nested > exception is org.hibernate.PropertyValueException: not-null property references a null or transient value : com.ynz.demo.containerizedapp.domain.Order.businessId

When the entity instance is in transient state, the businessId is null; this viloates the contrains, i.e. nullable = false.   


**Using DTO along with Projection**

Using dtos along with creation operation, meanwhile using interface projections along with query operations. 

**Using springdoc-openapi-ui docment API**

springdoc-openapi-ui
http://localhost:8080/swagger-ui.html

**Using Actuator**

http://localhost:8080/actuator

**How does Java deserialization work?**

> When deserializing a byte stream back to an object it does not use the constructor. It creates an empty object and uses reflection to write the data to the fields. Just like 
> with serialization, private and final fields are also included.

therefore, the the pojo for reconscruting deserialization result, it should have a non-argument constructor.

**About java bean validation**

JSR 380 is a specification of the Java API for bean validation, part of Jakarta EE and JavaSE. This ensures that the properties of a bean meet specific criteria, using annotations such as @NotNull, @Min, and @Max. This version requires Java 8 or higher, and takes advantage of new features added in Java 8, such as type annotations and support for new types like Optional and LocalDate.

* expressing constraints on object models via annotations
* allowing custom constraints in an extensible way
* providing API to validate objects and object graphs
* providing API to validate paramters and return values of methods and constructors
* reporting the set of violations
* runs on JSE and Jakarta EE 8

All of the annotations used in the example are standard JSR annotations:

@NotNull validates that the annotated property value is not null.
@AssertTrue validates that the annotated property value is true.
@Size validates that the annotated property value has a size between the attributes min and max; can be applied to String, Collection, Map, and array properties.
@Min validates that the annotated property has a value no smaller than the value attribute.
@Max validates that the annotated property has a value no larger than the value attribute.
@Email validates that the annotated property is a valid email address.
Some annotations accept additional attributes, but the message attribute is common to all of them. This is the message that will usually be rendered when the value of the respective property fails validation.

And some additional annotations that can be found in the JSR:

* @NotEmpty validates that the property is not null or empty; can be applied to String, Collection, Map or Array values.
* @NotBlank can be applied only to text values and validates that the property is not null or whitespace.
* @Positive and @PositiveOrZero apply to numeric values and validate that they are strictly positive, or positive including 0.
* @Negative and @NegativeOrZero apply to numeric values and validate that they are strictly negative, or negative including 0.
* @Past and @PastOrPresent validate that a date value is in the past or the past including the present; can be applied to date types including those added in Java 8.
* @Future and @FutureOrPresent validate that a date value is in the future, or in the future including the present.

The validation annotations can also be applied to elements of a collection:

List<@NotBlank String> preferences;


Also, the specification supports the new Optional type in Java 8:

private LocalDate dateOfBirth;

public Optional<@Past LocalDate> getDateOfBirth() {
    return Optional.of(dateOfBirth);
}


Starting with Boot 2.3, we also need to explicitly add the spring-boot-starter-validation dependency:

<dependency> 
    <groupId>org.springframework.boot</groupId> 
    <artifactId>spring-boot-starter-validation</artifactId> 
</dependency>

In general, we use @Valid to validate RequestBody, meanwhile using @Validated to validate method parameters; In addition, using @Validated as validating partial constraints.

@Valid 

Marks a property(cacade validation), method parameter(Object type) or method return type for validation cascading.
Constraints defined on the object and its properties are be validated when the property, method parameter or method return type is validated.

Using @Valid Validating RequestBody

if validation fails, it triggers _a MethodArgumentNotValidException_ – This exception is thrown when an argument annotated with @Valid failed validation.
Spring will translate it into bad request automatically; this exception is defined in Spring framework, and it extends from BindException. 

The magic happends in the abstract class ResponseEntityExceptionHandler, where the global exception Advice is extended from. Spring by default handles the MethodArgumentNotValidException here, but return a ResponseEntity without a Body. So API user won't see any userful message. We may override this method from the custom code.  

@Validated

Variant of JSR-303's Valid, supporting the specification of validation groups. Designed for convenient use with Spring's JSR-303 support but not JSR-303 specific.
Can be used e.g. with _Spring MVC handler methods arguments_. Supported through SmartValidator's validation hint concept, with validation group classes acting as hint objects.
Can also be used with method level validation, indicating that a specific class is supposed to be validated at the method level (acting as a pointcut for the corresponding validation interceptor), but also optionally specifying the validation groups for method-level validation in the annotated class. 

In constrast of validating RequestBody, validating single method parameter, i.e. request parameters and path variables, we have to add Spring’s @Validated annotation to the controller at the class level to tell Spring to evaluate the constraint annotations on method parameters, and note that it will throw _ConstraintViolationException_.  

Creating Cross-Parameter Constraints

In some cases, we might need to validate multiple values at once, e.g., two numeric amounts being one bigger than the other.
Cross-parameter constraints can be considered as the method validation equivalent to class-level constraints.

**Mapping Primary Keys**

The TABLE strategy uses a database table to generate unique primary key values. This requires pessimistic locking and isn’t the most efficient approach.

The IDENTITY strategy forces Hibernate to execute the SQL INSERT statement immediately. Due to this, Hibernate can’t use any of its performance optimization strategies that require a delayed execution of the statement. One example of that is JDBC batching. But it can also affect simple things, like updating an attribute before the entity gets persisted. When Hibernate has to execute the INSERT statement immediately, it has to perform an additional UPDATE statement to persist the changed value instead of using that value in the INSERT statement.

More about Sequences

The best generation strategy you can use with a PostgreSQL database is the SEQUENCE strategy. It uses a simple database sequence and is highly optimized by PostgreSQL. And Hibernate uses an optimized algorithm by default to avoid unnecessary SELECT statements.

**Handling API Errors**

Http Status Codes: 
the server must notify the client if the request was successfully handled or not.

HTTP accomplishes this with five categories of status codes:

* 100-level (Informational) – server acknowledges a request
* 200-level (Success) – server completed the request as expected
* 300-level (Redirection) – client needs to perform further actions to complete the request
* 400-level (Client error) – client sent an invalid request
* 500-level (Server error) – server failed to fulfill a valid request due to an error with server
Based on the response code, a client can surmise the result of a particular request.

The simplest way we handle errors is to respond with an appropriate status code.

In an effort to standardize REST API error handling, the IETF devised RFC 7807, which creates a generalized error-handling schema.

This schema is composed of five parts:

1. type – a URI identifier that categorizes the error
1. title – a brief, human-readable message about the error
1. status – the HTTP response code (optional)
1. detail – a human-readable explanation of the error
1. instance – a URI that identifies the specific occurrence of the error

````
 {
    "type": "/errors/incorrect-user-pass",
    "title": "Incorrect username or password.",
    "status": 401,
    "detail": "Authentication failed due to incorrect username or password.",
    "instance": "/login/log/abc123"
 }
````

By using URIs, clients can follow these paths to find more information about the error

the best practices of REST API error handling:

* Providing specific status codes
* Including additional information in response bodies
* Handling exceptions in a uniform manner

While the details of error handling will vary by application, these general principles apply to nearly all REST APIs and should be adhered to when possible.

**Testcontainers**

Testcontainers is a Java library that supports JUnit tests, providing lightweight, throwaway instances of common databases, Selenium web browsers, or anything else that can run in a Docker container.

While TestContainers is tightly couples with the Junit4 rule API, this moudle provides an API that is based on the JUnit Jupiter extension model. 

* Data access layer integration tests: use a containerized instance of a MySQL, PostgreSQL or Oracle database to test your data access layer code for complete compatibility, but without requiring complex setup on developers' machines and safe in the knowledge that your tests will always start with a known DB state. Any other database type that can be containerized can also be used.
* Application integration tests: for running your application in a short-lived test mode with dependencies, such as databases, message queues or web servers.
* UI/Acceptance tests: use containerized web browsers, compatible with Selenium, for conducting automated UI tests. Each test can get a fresh instance of the browser, with no browser state, plugin variations or automated browser upgrades to worry about. And you get a video recording of each test session, or just each session where tests failed.

Jupiter integration is provided by means of the @Testcontainers annotation.

You might want to use Testcontainers' database support:

Instead of H2 database for DAO unit tests that doesn't emulate all database features. Testcontainers is not as performant as H2, but it runs a real DB inside of a container.
Instead of a database running on the local machine or in a VM for DAO unit tests or end-to-end integration tests that need a database to be present. In this context, the benefit of Testcontainers is that the database always starts in a known state, without any contamination between test runs or on developers' local machines.

     <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>postgresql</artifactId>
            <version>1.15.3</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>1.15.3</version>
            <scope>test</scope>
        </dependency>

The first one is for the PostgreSQL container and the second one is for JUnit 5 support.

