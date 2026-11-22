package com.example.SOA5.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.SOA5.dto.CreateOrderReportRequest;
import com.example.SOA5.dto.CreateProductReportRequest;
import com.example.SOA5.entity.OrderReport;
import com.example.SOA5.entity.ProductReport;
import com.example.SOA5.service.ReportingService;

@RestController
@RequestMapping("/reports")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/orders")
    public List<OrderReport> getAllOrderReports() {
        return reportingService.getAllOrderReports();
    }

    @GetMapping("/orders/{id}")
    public OrderReport getOrderReport(@PathVariable Integer id) {
        return reportingService.getOrderReport(id);
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderReport> createOrderReport(
            @Valid @RequestBody CreateOrderReportRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reportingService.createOrderReport(request, authorization));
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrderReport(@PathVariable Integer id) {
        reportingService.deleteOrderReport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products")
    public List<ProductReport> getAllProductReports() {
        return reportingService.getAllProductReports();
    }

    @GetMapping("/products/{id}")
    public ProductReport getProductReport(@PathVariable Integer id) {
        return reportingService.getProductReport(id);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductReport> createProductReport(
            @Valid @RequestBody CreateProductReportRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reportingService.createProductReport(request, authorization));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProductReport(@PathVariable Integer id) {
        reportingService.deleteProductReport(id);
        return ResponseEntity.noContent().build();
    }
}
