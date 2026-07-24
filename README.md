# MicroShield

Hệ thống Microservice phục vụ niên luận **Tìm hiểu và trình bày các nguy cơ bảo mật của hệ thống website Microservice**.

## Thành phần

| Thành phần | Cổng |
|---|---:|
| Frontend | 3000 |
| API Gateway | 8085 |
| Order Service | 8080 |
| Inventory Service | 8081 |
| User Service | 8082 |
| Eureka | 8761 |
| RabbitMQ AMQP | 5672 |
| RabbitMQ Management | 15672 |
| User MySQL | 3307 |
| Order MySQL | 3308 |
| Inventory MySQL | 3309 |

## Chạy toàn bộ hệ thống

Yêu cầu: Docker Desktop đang chạy.

```powershell
cd F:\Projects\MicroShield
docker compose up --build
```

Lần build đầu sẽ lâu hơn do Maven và Docker tải dependency/image.

Kiểm tra container:

```powershell
docker compose ps
docker compose logs -f api-gateway
```

Dừng hệ thống:

```powershell
docker compose down
```

Xóa cả dữ liệu MySQL và RabbitMQ để chạy lại từ đầu:

```powershell
docker compose down -v
```

## Địa chỉ

- GUI: http://localhost:3000
- API Gateway: http://localhost:8085
- Eureka Dashboard: http://localhost:8761
- RabbitMQ Management: http://localhost:15672

RabbitMQ:

```text
microshield / microshield123
```

## Tài khoản được tạo tự động

```text
userA / 123456 / USER
userB / 123456 / USER
admin / admin123 / ADMIN
```

## Luồng RabbitMQ

1. Client tạo đơn qua `POST /api/orders`.
2. Order Service kiểm tra tồn kho qua Eureka + LoadBalancer.
3. Order Service lưu đơn hàng.
4. Order Service phát sự kiện JSON `order.created` lên exchange `microshield.order.exchange`.
5. Inventory Service nhận sự kiện từ queue `microshield.inventory.order-created`.
6. Inventory Service trừ số lượng tồn kho.

## Demo BOLA

Mặc định Docker Compose đặt:

```text
DEMO_BOLA_ENABLED=true
```

Khi User A gọi `GET /api/orders/{id}` với ID đơn thuộc User B, hệ thống cố ý trả dữ liệu để minh họa BOLA.

Chuyển sang trạng thái đã khắc phục:

```yaml
DEMO_BOLA_ENABLED: "false"
```

Sau đó:

```powershell
docker compose up -d --build order-service
```

## Demo Rate Limiting

API Gateway giới hạn mặc định 10 lần gọi `POST /api/auth/login` trong 60 giây cho mỗi địa chỉ IP. Khi vượt giới hạn, Gateway trả HTTP 429.

## Gọi API nhanh

Đăng nhập:

```powershell
$login = Invoke-RestMethod `
  -Method Post `
  -Uri http://localhost:8085/api/auth/login `
  -ContentType "application/json" `
  -Body '{"username":"userA","password":"123456"}'

$token = $login.token
```

Xem đơn của User A:

```powershell
Invoke-RestMethod `
  -Uri http://localhost:8085/api/orders `
  -Headers @{ Authorization = "Bearer $token" }
```

Tạo đơn và kích hoạt RabbitMQ:

```powershell
Invoke-RestMethod `
  -Method Post `
  -Uri http://localhost:8085/api/orders `
  -Headers @{ Authorization = "Bearer $token" } `
  -ContentType "application/json" `
  -Body '{"productName":"Keyboard","quantity":2,"totalPrice":100}'
```

## Các lỗi đã sửa

- Đồng bộ toàn bộ service về Spring Boot 3.2.5 và Spring Cloud 2023.0.0.
- Sửa Spring Security mặc định chặn `/api/auth/login`.
- Dùng JWT secret cố định qua cấu hình để User Service và Gateway dùng chung.
- Gateway xác thực JWT và loại bỏ header danh tính giả mạo từ client.
- Thêm route `/api/admin/**`.
- Chuyển frontend mặc định sang Gateway cổng 8085.
- Thêm `GET /api/orders/{id}` để demo BOLA.
- Tách danh sách đơn hàng theo người dùng đăng nhập.
- Thêm RabbitMQ producer/consumer để cập nhật tồn kho.
- Chuyển cấu hình database/Eureka/RabbitMQ sang biến môi trường.
- Thêm Dockerfile cho từng service và Docker Compose cho toàn hệ thống.
- Không trả password hash của người dùng trong JSON.

## Lưu ý về cổng service nội bộ

Các cổng `8080`, `8081` và `8082` được publish ra máy host để thuận tiện kiểm tra trong phòng lab. Trong môi trường triển khai thực tế, nên bỏ các dòng `ports` của ba business service và chỉ công khai API Gateway `8085`.
