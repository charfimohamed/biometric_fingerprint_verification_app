# Biometric Fingerprint Verification App

This project implements a biometric fingerprint verification system with a backend and an Android frontend application. The app captures user fingerprint data, checks it against an existing database, and adds new entries if no match is found.


## Hardware Compatibility

This app is specifically designed and tested to function with the **MorphoTablet 2**:

[MorphoTablet.png] 

## Getting Started

Follow these steps to run the application:

**1. Backend Setup**

- Run the `docker-compose.yml` file to start the backend services.

**2. Database Initialization**

- **First-time setup only:**
  - Connect to the PostgreSQL database:
    ```bash
    psql -h localhost -U mohamed -p 5332
    ```
  - Enter password: `mohamed`
  - Create the customer database:
    ```sql
    CREATE DATABASE customer;
    ```

**3. IP Address Configuration**

- Update the following files with your system's IP address:
  - `biometric_fingerprint_verification_app/backend/src/main/java/com/st2i/generateData`: (For generating random customer data) 
  - `frontend/app/src/main/java/com/st2i/retrofit/RetrofitService.java`

**4. Backend Startup**

- Navigate to the backend directory and run the `main.java` file.

**5. Android App Execution**

- Open the Android project in your IDE (e.g., Android Studio) and run the app.
