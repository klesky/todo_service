# Service Descriptions

#### 1. Get all to-do items which are not done and with option to retrieve all

```
GET /todo
```

| Param       | Required    | Type          | Description |
| ----------- | ----------- | ------------- | ----------- |     
| getAll      | No          | QueryParam    | If provided as true, then endpoint will return all to-do items |

#### 2. Create a to-do item

```
POST /todo
```

| Param       | Required    | Type          | Description |
| ----------- | ----------- | ------------- | ----------- |     
| todo      | Yes          | RequestBody    | The payload for to-do item |

Sample Payload:

```
{
  "description": "Buy Food",
  "dueDate": "2021-09-08T06:43:55Z"
}
```

* dueDate value must be later than now

#### 3. Get a specific to-do item

```
GET /todo/{id}
```

| Param       | Required    | Type          | Description |
| ----------- | ----------- | ------------- | ----------- |     
| id      | Yes          | Path param    | The id of to-do item |

#### 4. Update a to-do item's description

```
PUT /todo/{id}/description
```

| Param       | Required    | Type          | Description |
| ----------- | ----------- | ------------- | ----------- |     
| description      | Yes          | RequestBody    | The new description |

#### 5. Update a to-do item as done

```
PUT /todo/{id}/status/done  
```

| Param       | Required    | Type          | Description |
| ----------- | ----------- | ------------- | ----------- |     
| id      | Yes          | Path param    | The id of to-do item |

#### 6. Update a to-do item as not done

```
PUT /todo/{id}/status/not-done
```

| Param       | Required    | Type          | Description |
| ----------- | ----------- | ------------- | ----------- |     
| id      | Yes          | Path param    | The id of to-do item |

# Tech Stack Used

* Spring Boot
* Spring Data JPA (Hibernate)
* H2 database
* Swagger

# How To

## Build the service

Build the project

```
mvn clean install
```

Build the container

```
docker build -t ooisy/todo-service .
```

## Run automatic test

You may use trigger maven test via

```
mvn clean test
```

## Run the service locally

### Via container

After building the container from the step above successfully. Run the container via

```
docker run -d -p 8080:8080 --name todo-service  -t ooisy/todo-service
```

If successful, you should be able to access Swagger UI via http://localhost:8080/swagger-ui.html

To stop container

```
docker stop todo-service
```

To remove container

```
docker rm todo-service
```

### Via cmd

Ensure you have the build the project, then run the below cmd

```
mvn spring-boot:run
```
