# 🎵 Welcome to SoundWave

**SoundWave** is a secure and scalable REST API application designed for managing music albums, songs, and user accounts. With advanced search, filtering, and security features, SoundWave ensures an efficient and enjoyable experience for both users and administrators.

---

## 🎯 Key Features

### **Album Management**
- List albums with pagination (**USER** or **ADMIN**).
- Search albums by title with pagination and sorting (**USER** or **ADMIN**).
- Search albums by artist (**USER** or **ADMIN**).
- Filter albums by year with pagination and sorting (**USER** or **ADMIN**).
- Add a new album (**ADMIN only**).
- Update an existing album (**ADMIN only**).
- Delete an album (**ADMIN only**).

**Endpoints:**
- `/api/user/albums/**`
- `/api/admin/albums/**`

### **Song Management**
- List songs with pagination (**USER** or **ADMIN**).
- Search songs by title with pagination and sorting (**USER** or **ADMIN**).
- List songs from a specific album with pagination and sorting (**USER** or **ADMIN**).
- Add a new song (**ADMIN only**).
- Update an existing song (**ADMIN only**).
- Delete a song (**ADMIN only**).

**Endpoints:**
- `/api/user/songs/**`
- `/api/admin/songs/**`

### **User Management**
- Authenticate: `POST /api/auth/login`.
- Register a new user: `POST /api/auth/register`.
- List users (**ADMIN only**): `GET /api/admin/users`.
- Manage roles for a user (**ADMIN only**): `PUT /api/admin/users/{id}/roles`.

---

## 🔒 Security

- **Spring Security with JWT Authentication**:
    - Stateless authentication using **JSON Web Tokens (JWT)**.
    - Passwords are encrypted using `BCryptPasswordEncoder` or a stronger encoder.

- **Role-Based Access Control (RBAC)**:
    - `/api/admin/**` endpoints require the `ADMIN` role.
    - `/api/user/**` endpoints require the `USER` role.

---

## 🌐 API Endpoints Overview

### **Albums**
- `GET /api/user/albums`: List albums (USER/ADMIN).
- `GET /api/user/albums/search`: Search albums by title or artist (USER/ADMIN).
- `POST /api/admin/albums`: Add an album (ADMIN).
- `PUT /api/admin/albums/{id}`: Update an album (ADMIN).
- `DELETE /api/admin/albums/{id}`: Delete an album (ADMIN).

### **Songs**
- `GET /api/user/songs`: List songs (USER/ADMIN).
- `GET /api/user/songs/search`: Search songs by title (USER/ADMIN).
- `GET /api/user/songs/album/{albumId}`: List songs by album (USER/ADMIN).
- `POST /api/admin/songs`: Add a song (ADMIN).
- `PUT /api/admin/songs/{id}`: Update a song (ADMIN).
- `DELETE /api/admin/songs/{id}`: Delete a song (ADMIN).

### **Users**
- `POST /api/auth/login`: User login.
- `POST /api/auth/register`: User registration.
- `GET /api/admin/users`: List users (ADMIN).
- `PUT /api/admin/users/{id}/roles`: Assign roles to a user (ADMIN).

---

## 📄 Prerequisites

- **Java 21**
- **Maven** for dependency management.
- **Spring Boot** for API development.
- **PostgreSQL** or another relational database.
- **Postman** for API testing.
- **Swagger UI** for API documentation.

---

## ⚙️ Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/soundwave.git
   cd soundwave
   ```
2. Update the `application.yml` file in the `resources` directory with your database connection details.
3. Build and run the application using Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

---

## 🌐 Swagger UI

- **Swagger UI** is available for exploring and testing API endpoints.
- After running the application, navigate to `http://localhost:8085/swagger-ui/index.html` in your browser.

---

## 🐋 Deployment with Docker

1. **Dockerize the Application**:
    - Add the `Dockerfile` and `docker-compose.yml` files to the project.
    - Build the application using Docker:
      ```bash
      docker compose up -d --build
      ```
2. **Access the Application**:
    - The application will be available at `http://localhost:8085`.


---

## 🎉 Get Started with SoundWave Today!

For any questions, feedback, or suggestions, feel free to reach out to us. 📧
