# MicroShield Frontend

GUI HTML/CSS/JavaScript thuần cho niên luận bảo mật Microservice.

## Chạy bằng Docker Compose

Tại thư mục gốc dự án:

```powershell
docker compose up --build
```

Mở:

- Frontend: http://localhost:3000
- API Gateway: http://localhost:8085
- Eureka: http://localhost:8761
- RabbitMQ Management: http://localhost:15672

RabbitMQ:

```text
username: microshield
password: microshield123
```

## Chạy riêng frontend

```powershell
cd frontend
py -m http.server 5500
```

Mở `http://localhost:5500`.

## Endpoint mặc định

```text
Gateway: http://localhost:8085
POST /api/auth/login
GET  /api/orders
GET  /api/orders/{id}
GET  /api/inventory
GET  /api/admin/dashboard
```

Tài khoản mẫu:

```text
userA / 123456
userB / 123456
admin / admin123
```

Chỉ sử dụng Security Lab trên hệ thống MicroShield cục bộ hoặc hệ thống được cho phép kiểm thử.
