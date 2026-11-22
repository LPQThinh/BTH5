package com.example.SOA5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.SOA5.entity.OrderReport;

public interface OrderReportRepository extends JpaRepository<OrderReport, Integer> {
    boolean existsByOrderId(Integer orderId);
}
