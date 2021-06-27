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
The JOIN FETCH expression is not a regular JOIN and it does not define a JOIN variable. Its only purpose is specifying related objects that should be fetched from the database with the query results on the same round trip. It tells that underlying 


**About OffSetDateTime and PostgreSQL**
When saving a offSetDateTime value into PostgreSQL database, the time offset will be transalated into the time that PostgreSQL is configured, which is normally is normally the time-zone of the host that is running the Postgres. If populating the following DateTime with TimeZone
````
insert into Client_orders(id, creation_date_time_with_zone, order_status, FK_Client_Id) values(2, '1999-01-08 14:05:06-08', 'SUSPENDING',1);

````
Then it will be mapped into the PostgreSQL database as  ``1999-01-08 23:05:06+01``, inline with the database time-zone (+1:00)

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

Using entity-based projections that project 

Interface-based projections:

Nullable Wrappers
Getters in projection interfaces can make use of nullable wrappers for improved null-safety. Currently supported wrapper types are:

java.util.Optional

com.google.common.base.Optional

scala.Option

io.vavr.control.Option

Class-based projections (DTOs)

Another way of defining projections is by using value type DTOs (Data Transfer Objects) that hold properties for the fields that are supposed to be retrieved. These DTO types can be used in exactly the same way projection interfaces are used, except that no proxying happens and no nested projections can be applied.

If the store optimizes the query execution by limiting the fields to be loaded, the fields to be loaded are determined from the parameter names of the constructor that is exposed.

so paratmeer name within a dto constructor decide what attributes are retreived from the aggregate root. 

Using Lombok @Value annotation can dramatically simplify the code for creating a DTO. 




Spring JPA Projection may optimize the SQL query according to queried parameters. The follow case demonstrates that the client is not eagerly loaded, for the target paramters are limited to the attributes belonging to the Order.     

````
public interface OrderRepository extends CrudRepository<Order, Integer> {

    OrderInfo findByBusinessId(UUID businessId);
}

Hibernate: select order0_.business_id as col_0_0_, order0_.creation_date_time_with_zone as col_1_0_, order0_.order_status as col_2_0_ from client_orders order0_ where order0_.business_id=?
````
In another case, if using an entity projection, then Hibernate generates an left outer join with Client, so a projection may improve the performance. 

````
Hibernate: select order0_.id as id1_1_0_, order0_.business_id as business2_1_0_, order0_.fk_client_id as fk_clien5_1_0_, order0_.creation_date_time_with_zone as creation3_1_0_, order0_.order_status as order_st4_1_0_, client1_.id as id1_0_1_, client1_.email as email2_0_1_, client1_.name as name3_0_1_ from client_orders order0_ left outer join client client1_ on order0_.fk_client_id=client1_.id where order0_.id=?
````

**About One-Many and Many-One default fetcg tpes**

@OneToMany fetch model default to lazy-loading; however, @ManyToOne default fetch model default to eager-loading
