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
3. The API will be accessible on : `http://localhost:8080`

### Running Python Scripts
Each script is **independent** and must be run separately, and the database must be deleted
before executing a new script. Make sure the backend is running before executing them.
```sh
  py script_requete_http/author.py
```
Replace `author.py` by another script name to perform operations on another API.

### Database management
The project uses a **SQLite database (`dataLibrary.db`)** located in `src/main/resources/`.
The database will have to be reset between each script execution.

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

#### Security Configuration
The `SecurityConfig.java` class defines the security rules for the application. It includes:

- **Public access :** Allows unauthenticated users to access book listings, author information, and other general data.
- **Restricted access** for authenticated users (`ADMIN` and  `READER` role) : Requires authentication for actions like posting reviews and managing favorites.
- **Admin-only access :** Restricts full CRUD operations (create, update, delete) to users with the ADMIN role.
