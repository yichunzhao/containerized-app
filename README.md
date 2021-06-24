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
