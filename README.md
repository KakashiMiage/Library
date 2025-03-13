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

- **src/main/webapp/**: Attempt for the frontend, for example with `authors.xhtml` for displaying authors.

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

To access the frontend for the authors, go to the following address : **http://localhost:8080/authors.xhtml**

Make sure to delete the database when switching from the Python test to the frontend test.

## API Endpoints
Each entity has its own entry point accessible at :

| **Entity**    | **Api endpoint** |
|---------------|-----------------:|
| **Authors**   |   `/api/authors` |
| **Books**     |     `/api/books` |
| **Editors**   |   `/api/editors` |
| **Genres**    |    `/api/genres` |
| **Reviews**   |   `/api/reviews` |
| **Types**     |     `/api/types` |
| **Users**     |     `/api/users` |

### Authors
- GET /api/authors → Get all authors
- GET /api/authors/{id} → Get author by ID
- POST /api/authors → Create a new author
- PUT /api/authors/{id} → Update an author
- DELETE /api/authors/{id} → Delete an author
- GET /api/authors/count → Get total count of authors
- GET /api/authors/search/fullname → Search author by first and last name
- GET /api/authors/search/lastname/{lastName} → Search authors by last name
- GET /api/authors/{id}/books → Get books by an author

### Books
- GET /api/books → Get all books
- GET /api/books/{id} → Get book by ID
- POST /api/books → Create a book
- PUT /api/books/{id} → Update a book
- DELETE /api/books/{id} → Delete a book
- GET /api/books/count → Get total count of books
- GET /api/books/search/title → Search books by title
- GET /api/books/search/author/{authorId} → Get books by author ID
- GET /api/books/search/genre/{genreId} → Get books by genre ID
- GET /api/books/search/editor/{editorId} → Get books by editor ID
- GET /api/books/search/type/{typeId} → Get books by type ID
- GET /api/books/search/keyword → Search books by keyword
- GET /api/books/reviews/not-empty → Get books with reviews
- GET /api/books/top-rated → Get top-rated books (min rating required)

### Editors
- GET /api/editors → Get all editors
- GET /api/editors/{id} → Get editor by ID
- POST /api/editors → Create an editor
- PUT /api/editors/{id} → Update an editor
- DELETE /api/editors/{id} → Delete an editor
- GET /api/editors/count → Get total count of editors
- GET /api/editors/search/name → Search editor by name
- GET /api/editors/search/siret → Search editor by SIRET number
- GET /api/editors/search/type/{typeId} → Get editors by type
- GET /api/editors/{id}/books → Get books by an editor
- GET /api/editors/search → Search editors by keyword

### Genres
- GET /api/genres → Get all genres
- GET /api/genres/{id} → Get genre by ID
- POST /api/genres → Create a genre
- PUT /api/genres/{id} → Update a genre
- DELETE /api/genres/{id} → Delete a genre
- GET /api/genres/count → Get total count of genres
- GET /api/genres/search → Search genre by name
- GET /api/genres/{id}/books → Get books by genre

### Reviews
- GET /api/reviews → Get all reviews
- GET /api/reviews/{id} → Get review by ID
- POST /api/reviews → Create a review
- PUT /api/reviews/{id} → Update a review
- DELETE /api/reviews/{id} → Delete a review
- GET /api/reviews/count → Get total count of reviews
- GET /api/reviews/book/{bookId} → Get reviews for a book
- GET /api/reviews/user/{userId} → Get reviews by a reader
- GET /api/reviews/book/{bookId}/average-rating → Get average rating of a book
- GET /api/reviews/top-rated → Get top-rated books (min rating required)

### Types
- GET /api/types → Get all types
- GET /api/types/{id} → Get type by ID
- POST /api/types → Create a type
- PUT /api/types/{id} → Update a type
- DELETE /api/types/{id} → Delete a type
- GET /api/types/count → Get total count of types
- GET /api/types/search/name → Search type by name
- GET /api/types/{id}/books → Get books by type
- GET /api/types/search/genre/{genreId} → Get types by genre

### Users
- GET /api/users → Get all users
- GET /api/users/{id} → Get user by ID
- POST /api/users → Create a user
- PUT /api/users/{id} → Update a user
- DELETE /api/users/{id} → Delete a user
- GET /api/users/count → Get total count of users
- GET /api/users/search/name → Search users by name
- GET /api/users/search/username → Search user by username
- GET /api/users/{id}/reviews → Get reviews written by a user
- GET /api/users/{id}/books-reviewed → Get books reviewed by a user
- PUT /api/users/{userId}/favorites/{bookId} → Add a book to user's favorites
- DELETE /api/users/{userId}/favorites/{bookId} → Remove a book from user's favorites
- GET /api/users/{userId}/favorites → Get a user's favorite books

## Authentication

The API includes authentication mechanisms to secure access to endpoints.
Users must be authenticated before accessing protected resources. The authentication system is structured like this :

- **AuthService.java:** Manages authentication logic, including user verification, password encryption, and token management.
- **SecurityConfig.java:** Configures authentication filters, security rules, and role-based access control.
- **AdminInitializer.java:** Initializes a default administrator and a reader user when the application starts.

### Default User Initialization
The `AdminInitializer.java` file for python test is responsible for creating a default administrator and a reader user if they do not exist in the database.

- **Admin credentials :**
  - Username : `admin`
  - Password : `admin`
  - Role : `ADMIN`
  
- **Reader credentials :**
  - Username : `jdupont`
  - Password : `password123`
  - Role : `READER`
 
credentials in `DataInitializer.java` for front test :

- **Admin credentials :**
  - Username : `admin`
  - Password : `admin123`
  - Role : `ADMIN`
  
- **Reader credentials :**
  - Username : `reader`
  - Password : `reader123`
  - Role : `READER`
  
If these users already exist in the database, no changes will be made.

### Security Configuration
The `SecurityConfig.java` class defines the security rules for the application. It includes:

- **Public access :** Reading access is public for the following resources: books, authors, genres, types, publishers, users and reviews. In addition, 
the creation of new users is also publicly authorized.
- **Restricted access** for authenticated users (`ADMIN` and  `READER` role) : Requires authentication for actions like posting reviews and managing favorites.
- **Admin-only access :** Restricts full CRUD operations (create, update, delete) to users with the ADMIN role.
