## Mô tả dự án
Dự án này là một ứng dụng Android sử dụng Java và Firebase (Cloud Firestore) để quản lý dữ liệu. Ứng dụng bao gồm các tính năng:
- CRUD (Thêm, Sửa, Đọc, Xóa).
- Giao diện người dùng.
- Giao diện admin.
## Yêu cầu hệ thống
- Android Studio phiên bản mới nhất.
- JDK 8 hoặc cao hơn.
- Kết nối internet.
## Cách chạy dự án
### 1. Clone dự án
Sao chép mã nguồn dự án từ repository GitHub bằng lệnh:
git clone <https://github.com/nguyenthanhduc242004/GarageManagement>
Hoặc tải file zip về từ https://github.com/nguyenthanhduc242004/GarageManagement
### 2. Mở dự án trong Android Studio
- Bước 1: Mở Android Studio.
- Bước 2: Chọn File > Open và điều hướng đến thư mục chứa mã nguồn.
- Bước 3: Chọn thư mục và nhấn OK.
### 3. Cấu hình Firebase cho dự án
- Bước 1: Truy cập Tool trong AndroidStudio chọn Firebase
- Bước 2: Chọn Get started with Cloud Storage[JAVA]
- Bước 3: Add SDK của Firebase 
### 4. Đồng bộ hóa Gradle
Kiểm tra tệp build.gradle của module app và đảm bảo các phụ thuộc Firebase đã được thêm:
implementation 'com.google.firebase:firebase-firestore:24.8.1'
implementation 'com.google.firebase:firebase-auth:22.0.0'

Sau đó, đồng bộ hóa Gradle bằng cách nhấn Sync Now.
### 5. Chạy ứng dụng trên thiết bị hoặc trình giả lập
- Bước 1: Kết nối điện thoại với máy tính hoặc sử dụng trình giả lập.
- Bước 2: Nhấn Run > Run 'app' hoặc nhấn tổ hợp phím Shift + F10.
- Bước 3: Kiểm tra ứng dụng trên thiết bị.

