# Library

## Project presentation
This project is a representation of a **book library** that uses RESTful API
to manage books, authors, editors, genres, types, reviews and users. It consists of a backend written in
**Java** and using the **Spring Boot** framework.
In addition, **Python scripts** are included to make HTTP requests to the API.

## Project structure

The project is organized into several folders:

- **script_requete_http/**: Here you'll find Python scripts for interacting with the API via
  HTTP requests. Each script is linked to an entity class, such as authors (`author.py`),
  books (`book.py`), publishers (`editor.py`), genres (`genre.py`), reviews (`review.py`),
  types (`type.py`) and users (`user.py`).

- **src/main/java/com/bibliomanager/library/**: This is the main Java backend folder.
    - **controller/** : Contains the REST controllers that manage API requests for each entity.
    - **model/**: Defines the entities representing the data stored in the database.
    - **repository/**: Contains interfaces for accessing data in the database.
    - **service/**: Provides the logic for processing controller requests.
    - **view/**: Defines specific views if required.
    - **LibraryApplication.java**: Used to launch the Spring Boot application.

- **src/main/resources/**: Contains configuration files and the SQLite database (`dataLibrary.db`).

- **src/main/webapp/**: Folder for the frontend. It includes files such as `authors.xhtml` for displaying
  display authors and `WEB-INF/` for web configurations.

- **pom.xml**: This is the file linked to Maven, used to manage dependencies.

- **.git/** : Linked to the project's Git repository.

## Installation and configuration
### Requirements
- **Java 17 or higher** for Spring Boot
- **Maven** for dependency management
- **Python 3.12** for the scripts

### Launch the backend (Spring Boot)
1. Go to the project directory :
   ```sh
   cd Library
   ```
2. Build and run the app :
   ```sh
   mvn spring-boot:run
   ```
3. The API can be tested using the following python scripts. The scripts are located 
in the `script_request_http` folder.

### Running Python Scripts
Each script is **independent**. Use the `AdminInitializer.java` file in the **config** folder
and uncomment it if the code is commented.
The `DataInitializer.java` file in the **config** folder must be commented.
Make sure the backend is running before executing them.

Python scripts check whether entries already exist in the database, to avoid exceptions linked to 
uniqueness constraints (IDs, unique keys, etc.). If need to check that the creation is working correctly, 
it's possible to remove the database by deleting the `dataLibrary.db` file in the **resources** folder, 
then restart the scripts and the server.

Here are the commands for executing the different scripts :

- **Author**
```sh
  py script_requete_http/author.py
```
- **Book**
```sh
  py script_requete_http/book.py
```
- **Editor**
```sh
  py script_requete_http/editor.py
```
- **Genre**
```sh
  py script_requete_http/genre.py
```
- **Review**
```sh
  py script_requete_http/review.py
```
- **Type**
```sh
  py script_requete_http/type.py
```
- **User**
```sh
  py script_requete_http/user.py
```

### Database management
The project uses a **SQLite database (`dataLibrary.db`)** located in `src/main/resources/`.
If the database does not exist, it is created when the application is launched.

### Launch the frontend
Use the `DataInitializer.java` file in the **config** folder, uncomment it if the code is commented 
and to create database entries.
The `AdminInitializer.java` file in the **config** folder must be commented.
Make sure the backend is running just before.

To access the frontend, go to the following address : **http://localhost:8080/authors.xhtml**

## API Endpoints
Each entity has its own entry point accessible at :

- **Authors :** `/api/authors`
- **Books :** `/api/books`
- **Editors :** `/api/editors`
- **Genres :** `/api/genres`
- **Reviews :** `/api/reviews`
- **Types :** `/api/types`
- **Users :** `/api/users`

## Authentication

The API includes authentication mechanisms to secure access to endpoints.
Users must be authenticated before accessing protected resources. The authentication system is structured like this :

- **AuthService.java:** Manages authentication logic, including user verification, password encryption, and token management.
- **SecurityConfig.java:** Configures authentication filters, security rules, and role-based access control.
- **AdminInitializer.java:** Initializes a default administrator and a reader user when the application starts.

### Default User Initialization
The `AdminInitializer.java` file is responsible for creating a default administrator and a reader user if they do not exist in the database.

- **Admin credentials :**
  - Username : `admin`
  - Password : `admin`
  - Role : `ADMIN`
  
- **Reader credentials :**
  - Username : `jdupont`
  - Password : `password123`
  - Role : `READER`
  
If these users already exist in the database, no changes will be made.

### Security Configuration
The `SecurityConfig.java` class defines the security rules for the application. It includes:

- **Public access :** Reading access is public for the following resources: books, authors, genres, types, publishers, users and reviews. In addition, 
the creation of new users is also publicly authorized.
- **Restricted access** for authenticated users (`ADMIN` and  `READER` role) : Requires authentication for actions like posting reviews and managing favorites.
- **Admin-only access :** Restricts full CRUD operations (create, update, delete) to users with the ADMIN role.
