# TaskFlow — Enterprise Task Management Platform

TaskFlow is a full stack enterprise task management platform built using Spring Boot and React. The application supports JWT authentication, role-based access control (RBAC), project management, task assignment, and Kanban-style workflow management.

## Live Demo
Frontend + Backend hosted on AWS EC2 using Nginx reverse proxy.

Live URL:
http://3.107.239.86

## Features

- JWT Authentication & Authorization
- Role-Based Access Control (Admin, Manager, Employee)
- Project & Task Management
- Kanban Task Board
- Dashboard Analytics
- RESTful APIs
- Responsive UI with React + Bootstrap
- AWS EC2 Deployment
- Nginx Reverse Proxy
- H2 Persistent Database

## Tech Stack

### Backend
- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA
- H2 Database
- Maven

### Frontend
- React 18
- Vite
- Axios
- Bootstrap 5
- React Router DOM

### Deployment
- AWS EC2
- Nginx
- Linux
- systemd

## Demo Credentials

### Admin
Email: admin@taskflow.com  
Password: admin123

### Manager
Email: manager@taskflow.com  
Password: manager123

### Employee
Email: employee@taskflow.com  
Password: emp123

## Architecture

Browser → Nginx → Spring Boot → H2 Database

## Screenshots

(Add screenshots here)

## Future Improvements

- PostgreSQL migration
- Redis caching
- Docker deployment
- AWS RDS
- CI/CD pipeline
- Kafka notifications
- WebSocket real-time updates
