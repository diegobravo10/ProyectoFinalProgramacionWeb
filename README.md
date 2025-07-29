# PROYECTO FINAL - CITAS MÉDICAS

Este repositorio contiene una aplicación web desarrollada con **React** como frontend y un backend desplegado en **WildFly** utilizando **Jakarta EE**.
La aplicación está orientada a la gestión de **citas médicas**, con un sistema multiusuario que diferencia entre los roles de **Administrador** (doctor o secretaria) y **Pacientes**.

---


### **Frontend (React)**

* Framework: **React**
* Librerías adicionales:

  * **react-icons**: Para el uso de iconos.

    ```bash
    npm install react-icons
    ```
  * **axios**: Para el consumo del backend desarrollado en Jakarta EE.

    ```bash
    npm install axios
    ```
* El frontend se encarga de:

  * Autenticación de usuarios utilizando **Firebase Authentication**.
  * Manejo de rutas y roles (administrador, paciente).
  * Consumo de la API REST desplegada en el servidor WildFly.
  * Interacción con la base de datos Firestore para algunos datos dinámicos.

### **Backend (WildFly + Jakarta EE)**

* Servidor de aplicaciones: **WildFly**
* Framework: **Jakarta EE**
* Principales características implementadas:

  * Endpoints RESTful para manejar:

    * Gestión de usuarios y roles.
    * Gestión de citas médicas (creación, modificación, cancelación).
  * Integración con Firebase para validar tokens de autenticación.
  * Generación de reportes (JasperReports) desde el servidor.
* Base de datos: Se utilizó **PostgreSQL** como motor relacional.

---

## **Servicios en la nube**

Se utilizaron varios servicios de **Firebase**:

* **Authentication**: Manejo de inicio de sesión con proveedores externos (Google).
* **Hosting**: Despliegue del frontend React.

---

## **Deploy**

* **Frontend**:
  Desplegado con **Firebase Hosting**:
  [Aplicación Web](https://citas-medicas-prointegrador.web.app/)
* **Backend**:
  Desplegado localmente en WildFly y expuesto con túneles seguros (ngrok) para permitir acceso remoto desde el frontend.

---
