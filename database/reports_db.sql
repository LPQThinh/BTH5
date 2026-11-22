CREATE DATABASE IF NOT EXISTS reports_db
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE reports_db;

CREATE TABLE IF NOT EXISTS orders_reports (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    total_revenue DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_cost DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_profit DECIMAL(10,2) NOT NULL DEFAULT 0.00
);

CREATE TABLE IF NOT EXISTS product_reports (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_report_id INT NOT NULL,
    product_id INT NOT NULL,
    total_sold INT NOT NULL DEFAULT 0,
    revenue DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    cost DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    profit DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_product_report_order
        FOREIGN KEY (order_report_id) REFERENCES orders_reports(id)
        ON DELETE CASCADE
);
