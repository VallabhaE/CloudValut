# ☁️ CloudVault

**CloudVault** is a Google Drive-inspired cloud file storage application built with **Spring Boot** and **Go**. It provides a foundation for securely managing users, folders, and files through a backend architecture designed for cloud storage functionality.

The project is divided into two backend services:

* **CloudVault** — Java/Spring Boot service responsible for folder and file management.
* **CloudVault Login** — Go service responsible for authentication and JWT-based user sessions.

---

## 🚀 Features

* 🔐 User authentication using JWT
* 📁 Folder creation and management
* 📄 File management
* 🗂️ Hierarchical folder structure
* 👤 User-specific data handling
* 🔑 Middleware-based authentication
* 🗄️ SQL database schema
* 🌐 REST API architecture
* ⚡ Separate authentication and file-management services
* 🧩 Modular backend structure

---

## 🏗️ Architecture

CloudVault follows a service-oriented backend architecture.

```text
                    ┌─────────────────────┐
                    │      Client         │
                    │ Web / Mobile / API  │
                    └──────────┬──────────┘
                               │
                 ┌─────────────┴─────────────┐
                 │                           │
                 ▼                           ▼
       ┌───────────────────┐       ┌───────────────────┐
       │ CloudVault Login  │       │     CloudVault    │
       │       Go          │       │    Spring Boot    │
       └─────────┬─────────┘       └─────────┬─────────┘
                 │                           │
                 │ JWT                       │
                 │                           │
                 └─────────────┬─────────────┘
                               ▼
                      ┌─────────────────┐
                      │    Database     │
                      │      SQL        │
                      └─────────────────┘
```

### Authentication Service

The `cloudvault-login` service is implemented in Go and handles:

* User authentication
* JWT generation
* Authentication handlers
* User models
* Database access

### File Management Service

The `cloudvault` service is implemented using Spring Boot and handles:

* Folders
* Files
* Folder hierarchy
* REST controllers
* Business logic
* Database repositories
* Authentication middleware

---

## 🛠️ Tech Stack

### Backend

| Technology      | Purpose                            |
| --------------- | ---------------------------------- |
| Java            | Main application language          |
| Spring Boot     | REST API and application framework |
| Spring Data JPA | Database persistence               |
| Maven           | Java dependency management         |
| Go              | Authentication service             |
| JWT             | Authentication and authorization   |
| SQL             | Database storage                   |

### Project Structure

```text
CloudValut/
│
├── README.md
│
├── cloudvault/
│   ├── pom.xml
│   ├── HELP.md
│   ├── mvnw
│   ├── mvnw.cmd
│   │
│   ├── schema/
│   │   └── cloudVault.sql
│   │
│   └── src/
│       ├── main/
│       │   ├── java/com/example/cloudvault/
│       │   │
│       │   └── resources/
│       │       └── application.properties
│       │
│       └── test/
│
└── cloudvault-login/
    ├── main.go
    ├── handlers.go
    ├── jwt.go
    ├── models.go
    ├── dao.go
    ├── go.mod
    ├── go.sum
    └── Makefile
```

---

## 📂 Spring Boot Structure

The main CloudVault application contains the following components:

```text
com.example.cloudvault
│
├── CloudvaultApplication.java
│
├── controller/
│   └── FolderController.java
│
├── dto/
│   ├── ApiResp.java
│   └── UserDt.java
│
├── folder/
│   ├── FileEntity.java
│   └── FolderEntity.java
│
├── middleware/
│   └── Middleware.java
│
├── repository/
│   ├── FileRepository.java
│   └── FolderRepository.java
│
└── service/
    └── FolderService.java
```

### Controller

`FolderController` exposes the REST endpoints used to interact with folders and related resources.

### Service

`FolderService` contains the business logic for folder operations.

### Repository

The repository layer provides database access through:

* `FolderRepository`
* `FileRepository`

### Entities

The application models cloud storage resources using:

* `FolderEntity`
* `FileEntity`

### Middleware

`Middleware` handles authentication-related processing before protected requests reach the application.

---

## 🔑 Authentication

CloudVault uses **JWT (JSON Web Tokens)** for authentication.

The general authentication flow is:

```text
User
 │
 │ Login credentials
 ▼
Go Authentication Service
 │
 │ Validate user
 ▼
JWT Token
 │
 │ Authorization header
 ▼
Spring Boot API
 │
 │ Validate JWT
 ▼
Protected Resource
```

Authenticated requests can use the JWT token to access user-specific CloudVault resources.

Example:

```http
Authorization: Bearer <JWT_TOKEN>
```

---

## 📁 Cloud Storage Model

CloudVault follows a folder/file model similar to traditional cloud storage systems.

```text
My Drive
│
├── Documents
│   ├── Resume.pdf
│   └── Notes.txt
│
├── Projects
│   └── CloudVault
│       ├── README.md
│       └── project.zip
│
└── Images
    ├── photo1.jpg
    └── photo2.png
```

Folders can contain files and other folders, allowing a hierarchical storage structure.

---

## 🗄️ Database

The SQL database schema is located at:

```text
cloudvault/schema/cloudVault.sql
```

The database stores information required for users, folders, files, and their relationships.

Before starting the application, configure the database connection in:

```text
cloudvault/src/main/resources/application.properties
```

Example configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cloudvault
spring.datasource.username=<username>
spring.datasource.password=<password>
```

Use the database configuration appropriate for your environment.

---

## ⚙️ Prerequisites

Make sure you have the following installed:

* **Java 17+**
* **Maven** or the included Maven Wrapper
* **Go 1.20+**
* **MySQL** or the database configured by the project
* **Git**

Check your installations:

```bash
java -version
go version
git --version
```

---

## 📥 Installation

Clone the repository:

```bash
git clone git@github.com:VallabhaE/CloudValut.git
cd CloudValut
```

### 1. Configure the Database

Create the required database and execute:

```text
cloudvault/schema/cloudVault.sql
```

Then update:

```text
cloudvault/src/main/resources/application.properties
```

with your database credentials.

---

## ▶️ Running CloudVault

### Start the Spring Boot Service

Move into the Spring Boot project:

```bash
cd cloudvault
```

Using Maven Wrapper:

```bash
./mvnw spring-boot:run
```

On Windows:

```cmd
mvnw.cmd spring-boot:run
```

Or using Maven:

```bash
mvn spring-boot:run
```

---

### Start the Go Authentication Service

Open another terminal:

```bash
cd cloudvault-login
```

Download dependencies:

```bash
go mod download
```

Start the service:

```bash
go run .
```

---

## 🧪 Running Tests

For the Spring Boot application:

```bash
cd cloudvault
./mvnw test
```

The project currently contains Spring Boot application tests under:

```text
src/test/java/com/example/cloudvault/
```

---

## 🔌 API

CloudVault exposes REST APIs for interacting with storage resources.

The main controller currently resides at:

```text
FolderController.java
```

The API is intended to support operations such as:

```text
Authentication
     │
     ├── Login
     └── JWT generation
     
Files
     │
     ├── Upload
     ├── Download
     └── Delete
     
Folders
     │
     ├── Create
     ├── List
     ├── Rename
     └── Delete
```

> API endpoints may change as the project develops. Refer to the controller classes for the current endpoint definitions.

---

## 🔒 Security

CloudVault uses JWT-based authentication to protect application resources.

For production deployment:

* Never commit JWT secrets to Git.
* Never commit database passwords.
* Use environment variables or a secrets manager.
* Validate and sanitize uploaded files.
* Implement file-size limits.
* Restrict file types where appropriate.
* Use HTTPS in production.
* Store uploaded files outside the application source directory.
* Add authorization checks to ensure users can only access their own resources.

---

## 🗺️ Future Improvements

CloudVault is designed to evolve into a complete Google Drive-like platform.

Potential improvements include:

* [ ] File upload and download
* [ ] File previews
* [ ] File sharing
* [ ] Public share links
* [ ] Folder sharing
* [ ] User permissions
* [ ] Trash / recycle bin
* [ ] File versioning
* [ ] File search
* [ ] Starred files
* [ ] Recent files
* [ ] Storage quotas
* [ ] Google Drive-style web UI
* [ ] Drag-and-drop uploads
* [ ] Cloud object storage integration
* [ ] Docker support
* [ ] CI/CD pipeline
* [ ] Production deployment
* [ ] Automated API documentation

---

## 🎯 Project Goal

The goal of CloudVault is to build a **Google Drive-inspired cloud storage platform** while learning and applying:

* REST API development
* Spring Boot
* Go backend development
* JWT authentication
* Database design
* File and folder management
* Microservice-style architecture
* Backend security
* Cloud storage concepts

---

## 🤝 Contributing

Contributions, suggestions, and improvements are welcome.

1. Fork the repository.
2. Create a feature branch.

```bash
git checkout -b feature/my-feature
```

3. Make your changes.
4. Commit your changes.

```bash
git commit -m "Add my feature"
```

5. Push the branch.

```bash
git push origin feature/my-feature
```

6. Open a Pull Request.

---

## 📜 License

This project is currently intended for learning and development purposes.

Add an appropriate open-source license before distributing the project publicly.

---

## 👨‍💻 Author

**VallabhaE**

GitHub repository:

[CloudValut on GitHub](https://github.com/VallabhaE/CloudValut?utm_source=chatgpt.com)

---

⭐ If you find the project useful, consider giving the repository a star!
