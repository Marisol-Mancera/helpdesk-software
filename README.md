# Helpdesk Software API

## 📌 Descripción
Aplicación **REST API** para la gestión de solicitudes de soporte en un sistema de Helpdesk.  
Permite crear, consultar, actualizar, marcar como atendidas y eliminar solicitudes de soporte técnico.  

El proyecto sigue arquitectura **Spring Boot con capas (Controller, Service, Repository, Entity, DTO, Mapper, Exception)**  
y buenas prácticas profesionales (tests unitarios, integración con Postman ).

---

## 🚀 Características principales
- Crear nuevas solicitudes (`POST /api/v1/requests`).
- Consultar todas las solicitudes o por id (`GET /api/v1/requests`, `GET /api/v1/requests/{id}`).
- Actualizar solicitudes (`PUT /api/v1/requests/{id}`).
- Marcar solicitud como atendida (`PATCH /api/v1/requests/{id}/attend?technicianName=Alice`).
- Eliminar solicitudes ya atendidas (`DELETE /api/v1/requests/{id}`).

---

## ⚙️ Tecnologías utilizadas
- **Java 21**
- **Spring Boot 3**
- **Maven**
- **Spring Data JPA**
- **H2 Database (perfil dev/test)**
- **MySQL (producción)**
- **JUnit 5 y Mockito (tests)**  
- **Hamcrest** para asserts legibles
- **Postman** (colecciones de prueba)

---

## 🗂️ Estructura del proyecto
```
src/main/java/dev/marisol/helpdesk_software/
 ├── controllers   → Controladores REST
 ├── dtos          → Objetos de transferencia (Request/Response)
 ├── entities      → Entidades JPA
 ├── enums         → Enumerados (RequestStatus)
 ├── exceptions    → Excepciones personalizadas
 ├── mappers       → Conversión Entity ↔ DTO
 ├── repository    → Interfaces JPA
 └── service       → Lógica de negocio (interfaces e implementaciones)

src/test/java/dev/marisol/helpdesk_software/
 ├── controllers   → Tests unitarios de controladores
 ├── entities      → Tests unitarios de entidades
 ├── mappers       → Tests de mapeo
 ├── repository    → Tests de repositorio (H2)
 └── service       → Tests de servicio (Mockito)
```

---

## 📝 Endpoints principales

### **Crear solicitud**
- **POST** `/api/v1/requests`
- **Body JSON ejemplo**:
```json
{
  "applicantName": "María",
  "topicId": 1,
  "description": "No enciende el PC"
}
```

### **Obtener todas las solicitudes**
- **GET** `/api/v1/requests`

### **Obtener solicitud por id**
- **GET** `/api/v1/requests/{id}`

### **Actualizar solicitud**
- **PUT** `/api/v1/requests/{id}`

### **Marcar como atendida**
- **PATCH** `/api/v1/requests/{id}/attend?technicianName=Alice`

### **Eliminar solicitud (solo si está atendida)**
- **DELETE** `/api/v1/requests/{id}`

---

## 🧪 Testing

- **Cobertura mínima exigida:** 70%  
- Se incluyen **tests unitarios**:
  - `RequestEntityTest`, `TopicEntityTest`
  - `RequestRepositoryTest`
  - `RequestServiceImplTest`
  - `RequestControllerTest`
- **Frameworks:** JUnit 5, Mockito, Hamcrest.

👉 _Espacio para añadir captura de cobertura:_  
![Coverage Report](./assets/coverage.png)

---

## 🗃️ Diagramas

👉 _Espacio para añadir:_
- **Diagrama ER (Chen)**
- **Diagrama Relacional (Patas de gallo)**
- **Diagrama de clases (Mermaid o UML)**

---

## 📬 Postman

- Colección de endpoints creada y documentada en Postman.  
- Exportar la colección en formato `.json` y adjuntar al proyecto.

👉 _Espacio para añadir captura de Postman:_  
![Postman Screenshot](./assets/postman.png)

---


## ▶️ Ejecución del proyecto

### **1. Clonar repositorio**
```bash
git clone <url-del-repo>
cd helpdesk_software
```

### **2. Ejecutar en perfil dev (H2)**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### **3. Ejecutar tests**
```bash
mvn clean test
```

### **4. Empaquetar**
```bash
mvn clean package
```

### **5. Acceder a H2 Console**
```
http://localhost:8080/h2-console
```

---

## 📌 Autora
👩🏽‍💻 **Marisol Mancera**  
Proyecto académico **Factoria F5** – Backend con **Spring Boot**.

