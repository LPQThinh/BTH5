package com.example.SOA5.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.SOA5.entity.ProductReport;

public interface ProductReportRepository extends JpaRepository<ProductReport, Integer> {
    List<ProductReport> findByOrderReportId(Integer orderReportId);
}
