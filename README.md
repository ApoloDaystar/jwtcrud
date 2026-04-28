# 🔐 JWT CRUD API — Spring Boot 4

API REST desarrollada con **Spring Boot 4** que implementa autenticación y autorización mediante **JSON Web Tokens (JWT)**. Incluye registro de usuarios, login seguro y protección de endpoints.

---

## 🚀 Tecnologías utilizadas

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.5 |
| Spring Security | 7 |
| Spring Data JPA | — |
| SQL Server | 17 |
| jjwt | 0.11.5 |
| Lombok | — |
| Maven | 3.x |

---

## 📁 Estructura del proyecto

```
src/
└── main/
    └── java/com/example/jwtcrud/
        ├── config/
        │   ├── JwtUtil.java           # Genera y valida tokens JWT
        │   ├── JwtFilter.java         # Intercepta y autentica cada request
        │   └── SecurityConfig.java    # Configuración de Spring Security
        ├── controller/
        │   ├── AuthController.java    # Endpoints de register y login
        │   └── UserController.java    # Endpoints CRUD de usuarios
        ├── dto/
        │   ├── AuthRequest.java       # DTO para recibir credenciales
        │   └── AuthResponse.java      # DTO para devolver el token
        ├── model/
        │   └── User.java              # Entidad de usuario
        ├── repository/
        │   └── UserRepository.java    # Acceso a datos
        └── service/
            └── UserService.java       # Lógica de negocio
```

---

## ⚙️ Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/jwtcrud.git
cd jwtcrud
```

### 2. Configurar la base de datos

Crea una base de datos en SQL Server llamada `jwtcrud` y actualiza el archivo `application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=jwtcrud;encrypt=true;trustServerCertificate=true
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.jpa.hibernate.ddl-auto=update
server.port=8080
```

### 3. Correr el proyecto

```bash
mvn spring-boot:run
```

La API estará disponible en `http://localhost:8080`

---

## 🔌 Endpoints

### Autenticación (públicos — no requieren token)

> ⚠️ **¿Por qué estos endpoints no requieren token?**
> Porque es aquí donde el usuario **obtiene** el token. Si register y login también lo pidieran,
> nadie podría nunca autenticarse — sería un círculo imposible.
> El flujo correcto es:
> ```
> Sin token → /register → crea tu cuenta
> Sin token → /login    → recibe tu token
> Con token → /api/users/... → accede a los datos
> ```

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/api/auth/register` | Registra un nuevo usuario |
| `POST` | `/api/auth/login` | Inicia sesión y devuelve el token JWT |

### Usuarios (protegidos — requieren token)

> 🔐 Todos estos endpoints requieren enviar el token en el header:
> `Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...`
> El token lo obtienes al hacer login.

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/users/{username}` | Busca un usuario por username |
| `GET` | `/api/users/test-connection` | Lista todos los usuarios |
| `POST` | `/api/users` | Crea un usuario |
| `PUT` | `/api/users/{id}` | Actualiza un usuario |
| `DELETE` | `/api/users/{id}` | Elimina un usuario |

---

## 🧪 Pruebas con Postman

### 1. Registro de usuario

```
POST http://localhost:8080/api/auth/register
Content-Type: application/json
```

```json
{
    "username": "arlemis",
    "password": "011104"
}
```

**Respuesta:**
```
201 Created
Usuario registrado exitosamente
```

![Register](images/registrado.png)

---

### 2. Login

```
POST http://localhost:8080/api/auth/login
Content-Type: application/json
```

```json
{
    "username": "arlemis",
    "password": "011104"
}
```

**Respuesta:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

![Login](images/logeado.png)

---

### 3. Acceder a un endpoint protegido

```
GET http://localhost:8080/api/users/test-connection
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## 🔒 ¿Cómo funciona la autenticación JWT?

```
1. El usuario se registra → la contraseña se guarda encriptada con BCrypt
2. El usuario hace login → el servidor valida las credenciales y genera un token JWT
3. El cliente guarda el token y lo envía en cada request en el header Authorization
4. El JwtFilter intercepta cada request, valida el token y autentica al usuario
5. Si el token es inválido o no existe → 401 No autorizado
```

---

## 🛡️ Seguridad

- Las contraseñas se encriptan con **BCryptPasswordEncoder** antes de guardarse
- Los tokens JWT se firman con **HMAC256** usando una clave secreta de 50+ caracteres
- Los tokens tienen una **expiración de 24 horas**
- La API es completamente **stateless** — no usa sesiones
- El endpoint `/api/auth/**` es público, todo lo demás requiere token válido

---

## 👨‍💻 Autor

Desarrollado con 💙 usando Spring Boot 4 y Spring Security 7.
