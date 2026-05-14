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

## from AWS and cmd's used in amazon linux 2023
# AWS EC2 Deployment Steps

## 1. Connect to EC2

```powershell
ssh -i "C:\Users\Nishath A\Downloads\taskflow-key.pem" ec2-user@<EC2_PUBLIC_IP>
2. Install Java & Nginx
sudo yum install java-17-amazon-corretto nginx -y

sudo systemctl start nginx
sudo systemctl enable nginx
3. Build Spring Boot Backend
cd U:\TaskFlow\taskflow-backend

mvn clean package -DskipTests

Generated JAR:

target/taskflow-0.0.1-SNAPSHOT.jar
4. Upload Backend JAR to EC2
scp -i "C:\Users\Nishath A\Downloads\taskflow-key.pem" "U:\TaskFlow\taskflow-backend\target\taskflow-0.0.1-SNAPSHOT.jar" ec2-user@<EC2_PUBLIC_IP>:~
5. Build React Frontend
cd U:\TaskFlow\taskflow-frontend

npm install

npm run build
6. Upload Frontend Build
scp -i "C:\Users\Nishath A\Downloads\taskflow-key.pem" -r "U:\TaskFlow\taskflow-frontend\dist\*" ec2-user@<EC2_PUBLIC_IP>:/tmp/dist/
7. Configure Nginx

Create config:

sudo nano /etc/nginx/conf.d/taskflow.conf

Nginx config:

server {
    listen 80;

    root /usr/share/nginx/html;
    index index.html;

    location / {
        try_files $uri /index.html;
    }

    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
8. Copy Frontend Files
sudo rm -rf /usr/share/nginx/html/*

sudo cp -r /tmp/dist/* /usr/share/nginx/html/

sudo systemctl reload nginx
9. Create systemd Service
sudo nano /etc/systemd/system/taskflow.service

Service config:

[Unit]
Description=TaskFlow Spring Boot Application
After=network.target

[Service]
User=ec2-user
WorkingDirectory=/home/ec2-user
ExecStart=/usr/bin/java -jar /home/ec2-user/taskflow-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
10. Enable & Start Backend Service
sudo systemctl daemon-reload

sudo systemctl start taskflow

sudo systemctl enable taskflow
11. Verify Service
sudo systemctl status taskflow
Live Application

http://<EC2_PUBLIC_IP>

## Future Improvements

- PostgreSQL migration
- Redis caching
- Docker deployment
- AWS RDS
- CI/CD pipeline
- Kafka notifications
- WebSocket real-time updates
