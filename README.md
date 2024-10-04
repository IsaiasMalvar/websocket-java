
---

# WebSocket Application

This is a WebSocket-based Java application built using Spring Boot, with a GUI developed using Swing. The application is containerized for deployment on Render. The project is configured to run as a JAR file, and the Docker configuration is specifically for deployment purposes on Render. 

## Table of Contents
- [Features](#features)
- [Technologies](#technologies)
- [Requirements](#requirements)
- [Setup](#setup)
- [Usage](#usage)
- [Deployment](#deployment)


## Features
- WebSocket-based real-time messaging.
- Backend developed using Spring Boot with built-in web server for local testing.
- GUI interface created with Java Swing.
- Deployed version available on Render for production use.

## Technologies
- **Java 22**: Primary language for backend and GUI.
- **Spring Boot**: Framework for building the WebSocket API.
  - Spring Boot Web
  - Spring Boot WebSocket
  - Spring Boot DevTools
  - Spring Boot Test
- **Swing**: For creating the GUI client.
- **Maven**: For building and managing dependencies.
- **Render**: Used to deploy the production version of the WebSocket server.

## Requirements
- Java 22 or higher.
- Maven 3.x.x or higher.
- An IDE like IntelliJ IDEA, Eclipse, or Visual Studio Code with Java support.

## Setup

### Clone the repository
```bash
git clone <repository-url>
cd websocket-app
```

### Maven Build
Build the application using Maven:
```bash
mvn clean install
```

### Running the application locally

The project is pre-configured to work on **localhost**. Simply run the class `WebsocketApplication` located in `src/main/java/com/example/websocket/MessageApplication` to start the Spring Boot application server:


This will start the WebSocket server locally at `ws://localhost:8080/ws`.

You can then test the WebSocket functionality using the Swing GUI client, which is also set up to connect to this local server.

### Running the Swing GUI
To run the GUI, run the `App` class located in `src/main/java/com/example/websocket/client/App`:



The GUI allows you to send and receive messages using WebSocket connections.

## Usage

### Localhost Testing
By default, the WebSocket server runs locally at `ws://localhost:8080/ws`. The Swing client is configured to connect to this local server for real-time messaging.

You can also use a WebSocket client (e.g., JavaScript `WebSocket` API or Postman) to connect to the local server for testing.

### Connecting to the Deployed Version
If you want to connect to the deployed version of the WebSocket server, which is hosted on Render, you need to change the WebSocket URL in the Swing client.

- Open the file located at `src/main/java/com/example/websocket/client/MyStompClient.java`.
- Change the line:
  ```java
  String url = "ws://localhost:8080/ws";
  ```
  to:
  ```java
  String url = "https//websocket-java.onrender.com/ws";
  ```

This will point the client to the WebSocket server hosted on Render at `https://websocket-java.onrender.com`.

## Deployment

The Docker configuration is specifically used by Render for deploying the application as a JAR file. For local testing and development, you can rely on the Spring Boot embedded server. No additional setup is needed for Docker on your local machine unless you are setting up the project for production deployment.

