-- Bảng Carriers
CREATE TABLE Carriers (
    cid VARCHAR(20) NOT NULL PRIMARY KEY,
    name VARCHAR(100)
);

-- Bảng Months
CREATE TABLE Months (
    mid INT NOT NULL PRIMARY KEY,
    month VARCHAR(20)
);

-- Bảng Weekdays
CREATE TABLE Weekdays (
    did INT NOT NULL PRIMARY KEY,
    day_of_week VARCHAR(100)
);

-- Bảng Flights
CREATE TABLE Flights (
    fid INT NOT NULL PRIMARY KEY,
    year INT,
    month_id INT,
    day_of_month INT,
    day_of_week_id INT,
    carrier_id VARCHAR(20),
    flight_num INT,
    origin_city VARCHAR(100),
    origin_state VARCHAR(100),
    dest_city VARCHAR(100),
    dest_state VARCHAR(100),
    departure_delay INT,
    taxi_out INT,
    arrival_delay INT,
    canceled INT,
    actual_time INT,
    distance INT,
    FOREIGN KEY (month_id) REFERENCES Months(mid),
    FOREIGN KEY (day_of_week_id) REFERENCES Weekdays(did),
    FOREIGN KEY (carrier_id) REFERENCES Carriers(cid)
);

-- Bảng customer
CREATE TABLE Customers (
    customer_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(15)
);

--Bảng booking
CREATE TABLE Bookings (
    booking_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    flight_id INT,
    booking_date DATE,
    seat_class VARCHAR(20),
    seat_number VARCHAR(10),
    price DECIMAL(10, 2),
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id),
    FOREIGN KEY (flight_id) REFERENCES Flights(fid)
);


-- Thêm dữ liệu mẫu vào bảng Customers
INSERT INTO Customers (first_name, last_name, email, phone) VALUES
('Nguyen', 'An', 'nguyen.an@example.com', '0912345678'),
('Tran', 'Binh', 'tran.binh@example.com', '0912345679'),
('Le', 'Cuong', 'le.cuong@example.com', '0912345680'),
('Pham', 'Duc', 'pham.duc@example.com', '0912345681'),
('Hoang', 'E', 'hoang.e@example.com', '0912345682'),
('Vu', 'F', 'vu.f@example.com', '0912345683'),
('Do', 'G', 'do.g@example.com', '0912345684'),
('Ngo', 'Hanh', 'ngo.hanh@example.com', '0912345685'),
('Bui', 'Khoa', 'bui.khoa@example.com', '0912345686'),
('Dang', 'Linh', 'dang.linh@example.com', '0912345687');





