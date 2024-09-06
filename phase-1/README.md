# PHASE 1

# Prerequisites:
- Maven 3.9.9 
- JDK 21

## Run project
By the moment, to run the project it's highly recommended to configurate the App launch in the Intellij Idea settings:

![run_config_img](./public/img/run_config.png)


## Maven dependency installation

If for some reason you editor doesn't install the dependencies or they aren't in the project, you can try this:

`mvn wrapper:wrapper`

And then try to run the `App.java`

Try to make sure you have maven already installed. You can check this with:

`mvn --version`

# Users API Documentation

## GET /api/users

Retrieves a list of all users.

### Request

- Method: GET
- URL: `http://localhost:4000/api/users`

### Response

#### Successful Request (200 OK)

```json
[
  {
    "id": "user_id_1",
    "name": "User Name 1",
    "login": "user_login_1",
    "password": "hashed_password_1"
  },
  {
    "id": "user_id_2",
    "name": "User Name 2",
    "login": "user_login_2",
    "password": "hashed_password_2"
  }
]
```

## POST /api/users

Creates a new user.

### Request

- Method: POST
- URL: `http://localhost:4000/api/users`
- Content-Type: application/json

#### Request Body

```json
{
  "name": "New User",
  "login": "new_user_login",
  "password": "new_user_password"
}
```

### Response

#### Successful Creation (200 OK)

```json
{
  "id": "generated_user_id",
  "name": "New User",
  "login": "new_user_login",
  "password": "hashed_password"
}
```

## GET /api/users/{id}

Retrieves a specific user by ID.

### Request

- Method: GET
- URL: `http://localhost:4000/api/users/{id}`

### Response

#### Successful Request (200 OK)

```json
{
  "id": "requested_user_id",
  "name": "User Name",
  "login": "user_login",
  "password": "hashed_password"
}
```

#### User Not Found (404 Not Found)

No body returned for this status.

## GET /api/users/login/{login}

Retrieves a specific user by login.

### Request

- Method: GET
- URL: `http://localhost:4000/api/users/login/{login}`

### Response

#### Successful Request (200 OK)

```json
{
  "id": "user_id",
  "name": "User Name",
  "login": "requested_login",
  "password": "hashed_password"
}
```

#### User Not Found (404 Not Found)

No body returned for this status.

## PUT /api/users/{id}

Updates an existing user's information.

### Request

- Method: PUT
- URL: `http://localhost:4000/api/users/{id}`
- Content-Type: application/json

#### Request Body

```json
{
  "name": "Updated Name",
  "login": "updated_login",
  "password": "updated_password"
}
```

### Response

#### Successful Update (200 OK)

```json
{
  "id": "user_id",
  "name": "Updated Name",
  "login": "updated_login",
  "password": "updated_password"
}
```

#### User Not Found (404 Not Found)

No body returned for this status.

### Notes

- All fields in the request body are optional for PUT. Only the provided fields will be updated.
- The user's ID cannot be changed.
- Updates are immediately reflected in the system.
- For security reasons, consider not returning the password field in responses, even if it's hashed.
