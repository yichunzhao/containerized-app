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


