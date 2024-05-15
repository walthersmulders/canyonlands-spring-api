# [WIP] Canyonlands Spring API

**This project is a work in progress and might not perform as expected.**

For more information about the project please visit the following website:

https://walthersmulders.com/personal-projects/canyonlands/overview

You will also find a preliminary `Prerequistes and Quick Start` guides accompanying the project
`Overview` documentation.

## Up and Running

The **fastest** way to get the project up and running is to make sure you have `Docker` up and 
running. In the root of the project you will find a `docker-compose.yml` file, this file 
contains the infrastructure needed to run the project locally. It includes `PostgreSQL` and 
`Keycloak`. 

Start the Docker infrastructure up with the following command:

```bash
docker compose up -d
```

Create a `.env` file in the root of the project and make use of these defaults:

```text
KEYCLOAK_URL=http://localhost:8024/realms/canyonlands
POSTGRES_DATABASE=canyonlands
POSTGRES_HOST=localhost
POSTGRES_PASSWORD=password
POSTGRES_PORT=5432
POSTGRES_USER=admin
LOGGING_LEVEL=INFO
```

Once you have the Docker containers up and running you need to start the project, this can be 
done by running the following command in your Terminal window:

```bash
./gradlew bootRun
```

Verify the application started up successfully by checking the `actuator` endpoint with the 
following cURL command: 

```bash
curl --location 'http://localhost:8080/actuator/health'
```

## Get Token
Perform a login on the `Keycloak` instance with the following cURL command:

```bash
curl --location 'http://localhost:8024/realms/canyonlands/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=canyonlands' \
--data-urlencode 'username=walthersmulders' \
--data-urlencode 'password=walthersmulders' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'client_secret=yWbjVxoipwXoqSmruM13IJ2GSvQHrM2K'
```

Copy the `access_token` value from the response for subsequent API requests.

## Register User
The first action to perform should be to register the user. We’ll make use of the `access_token` 
copied in the previous step to make an authenticated API call to the `/register` endpoint.

Execute the following cURL command to do so (substitute the copied `access_token` and replace 
the `eyJhbGciOiJSUzI1NiI..........` part):

```bash
curl --location 'http://localhost:8080/register' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiI..........'
```

Upon executing the above request you should receive a successful response with an HTTP status code of `HTTP 201 Created`.

This can be verified by taking a look at the application logs:

```bash
INFO 15687 --- [canyonlands-spring-api] [nio-8080-exec-1] c.w.service.user.RegistrationService     : User registered successfully
```

## Retrieving all users
In order to retrieve a list of all users in the system make an authenticated API call to the `/users` endpoint.

Execute the following cURL command to do so (substitute the copied `access_token` and replace
the `eyJhbGciOiJSUzI1NiI..........` part):

```bash
curl --location 'http://localhost:8080/users' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiI..........'
```

Upon executing the above request you should receive a successful response with an HTTP status 
code of `HTTP 200 OK` and a response body like the following:

```json
[
    {
        "userID": "1c529433-063f-40c2-8dba-4d5a8197fd5b",
        "username": "walthersmulders",
        "emailAddress": "info@walthersmulders.com",
        "firstName": "Walther",
        "lastName": "Smulders"
    }
]
```

Shutdown and clean the Docker environment with the following command:

```bash
docker compose down -v
```



