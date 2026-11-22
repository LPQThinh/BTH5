# SOA5 - Reporting Service

## Thông tin
- Java 21
- Spring Boot 3.5.7
- MySQL database riêng: `reports_db`
- Port: `8084`
- JWT validation: SOA2 `http://localhost:8081/auth`
- Product service: SOA3 `http://localhost:8082`
- Order service: SOA4 `http://localhost:8083`

## Cài đặt
1. Tạo database bằng file `database/reports_db.sql` trong MySQL Workbench.
2. Sao chép `src/main/resources/application-example.properties` thành `application.properties`.
3. Điền username/password MySQL trong `application.properties`.
4. Import project Maven trong Eclipse và chạy `Soa5Application`.

## Thứ tự chạy service
SOA2 (8081) -> SOA3 (8082) -> SOA4 (8083) -> SOA5 (8084).

## API
Tất cả API `/reports/**` yêu cầu header `Authorization: Bearer <JWT>`.

- GET `/reports/orders`
- GET `/reports/orders/{id}`
- POST `/reports/orders`
- DELETE `/reports/orders/{id}`
- GET `/reports/products`
- GET `/reports/products/{id}`
- POST `/reports/products`
- DELETE `/reports/products/{id}`

### Tạo báo cáo đơn hàng
POST `/reports/orders`
```json
{
  "orderId": 2,
  "totalCost": 10000000
}
```
Hoặc truyền giá vốn theo từng sản phẩm để service tự cộng chi phí:
```json
{
  "orderId": 2,
  "unitCosts": {
    "3": 9000000
  }
}
```
Nếu không truyền `totalCost` hoặc `unitCosts`, chi phí mặc định là 0 vì SOA3 hiện chỉ cung cấp `id`, `name`, `price`, `quantity`, chưa có trường giá nhập. Lợi nhuận được tính bằng doanh thu trừ chi phí.

### Tạo báo cáo sản phẩm
POST `/reports/products`
```json
{
  "orderReportId": 1,
  "productId": 3,
  "unitCost": 9000000
}
```
Số lượng bán và doanh thu được lấy từ chi tiết đơn hàng ở SOA4; thông tin sản phẩm được kiểm tra qua SOA3.

## Lưu ý
- `orders_reports` và `product_reports` giữ các cột theo bảng phụ lục đề bài.
- Tồn kho hiện được quản lý bởi SOA3 (`GET /products/{id}`, trường `quantity`); không thêm cột ngoài bảng phụ lục vào bảng báo cáo.
- Khi tạo báo cáo đơn hàng, `totalCost` hoặc `unitCosts` là dữ liệu giá vốn do người gọi cung cấp, vì dữ liệu dịch vụ sản phẩm hiện không có giá nhập.
