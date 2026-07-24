# MicroShield Frontend

GUI HTML/CSS/JavaScript thuần cho niên luận bảo mật Microservice.

## Cài vào dự án

Giải nén rồi đổi tên thư mục thành `frontend` và đặt tại:

```text
F:\Projects\MicroShield\frontend
```

## Chạy nhanh

```powershell
cd F:\Projects\MicroShield\frontend
py -m http.server 5500
```

Mở `http://localhost:5500`.

## Endpoint mặc định

```text
Gateway: http://localhost:8080
POST /api/auth/login
GET  /api/orders
GET  /api/orders/{id}
GET  /api/inventory
GET  /api/admin/dashboard
```

Nếu Controller/Gateway dùng đường dẫn khác, mở mục **Cấu hình API** trên GUI để sửa mà không cần sửa mã nguồn.

Phản hồi đăng nhập nên có một trong các trường: `token`, `accessToken`, `access_token`, `jwt` hoặc `data.token`.

## CORS tại API Gateway

Thêm vào `api-gateway/src/main/resources/application.yaml` nếu chưa có:

```yaml
spring:
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allowedOrigins:
              - "http://localhost:5500"
              - "http://localhost:3000"
            allowedMethods: [GET, POST, PUT, DELETE, OPTIONS]
            allowedHeaders: "*"
            allowCredentials: true
```

Khởi động lại API Gateway sau khi sửa.

## Chạy bằng Docker

```powershell
docker build -t microshield-frontend .
docker run --rm -p 3000:80 microshield-frontend
```

Mở `http://localhost:3000`.

## Luồng demo

1. Đăng nhập User A và nhận JWT.
2. Xem đơn hợp lệ.
3. Vào Security Lab, nhập ID đơn của User B để kiểm tra BOLA.
4. Giải mã JWT và quan sát role.
5. Dán token thử nghiệm do môi trường lab tạo rồi gọi API quản trị.
6. Gửi 10–20 lần đăng nhập sai để kiểm tra HTTP 429.

Chỉ sử dụng Security Lab trên hệ thống MicroShield cục bộ hoặc hệ thống được cho phép kiểm thử.
