package com.example.SOA5.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.SOA5.dto.CreateOrderReportRequest;
import com.example.SOA5.dto.CreateProductReportRequest;
import com.example.SOA5.entity.OrderReport;
import com.example.SOA5.entity.ProductReport;
import com.example.SOA5.repository.OrderReportRepository;
import com.example.SOA5.repository.ProductReportRepository;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class ReportingService {

    private final OrderReportRepository orderReportRepository;
    private final ProductReportRepository productReportRepository;
    private final ExternalServiceClient externalServiceClient;

    public ReportingService(OrderReportRepository orderReportRepository,
                            ProductReportRepository productReportRepository,
                            ExternalServiceClient externalServiceClient) {
        this.orderReportRepository = orderReportRepository;
        this.productReportRepository = productReportRepository;
        this.externalServiceClient = externalServiceClient;
    }

    public List<OrderReport> getAllOrderReports() {
        return orderReportRepository.findAll();
    }

    public OrderReport getOrderReport(Integer id) {
        return orderReportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order report not found"));
    }

    public List<ProductReport> getAllProductReports() {
        return productReportRepository.findAll();
    }

    public ProductReport getProductReport(Integer id) {
        return productReportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product report not found"));
    }

    @Transactional
    public OrderReport createOrderReport(CreateOrderReportRequest request, String authorization) {
        if (orderReportRepository.existsByOrderId(request.getOrderId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This order already has a report");
        }

        JsonNode order = externalServiceClient.getOrder(request.getOrderId(), authorization);
        if (order == null || order.isMissingNode()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found in SOA4");
        }

        JsonNode items = order.path("items");
        if (!items.isArray()) {
            items = order.path("orderItems");
        }
        if (!items.isArray()) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Order response does not contain items array");
        }

        BigDecimal revenue = BigDecimal.ZERO;
        BigDecimal calculatedCost = BigDecimal.ZERO;

        for (JsonNode item : items) {
            BigDecimal itemRevenue = decimal(item, "totalPrice");
            if (itemRevenue == null) {
                itemRevenue = decimal(item, "totalAmount");
            }
            if (itemRevenue == null) {
                BigDecimal unitPrice = decimal(item, "unitPrice");
                int quantity = item.path("quantity").asInt(0);
                itemRevenue = (unitPrice == null ? BigDecimal.ZERO : unitPrice)
                        .multiply(BigDecimal.valueOf(quantity));
            }
            revenue = revenue.add(itemRevenue);

            int productId = item.path("productId").asInt(0);
            int quantity = item.path("quantity").asInt(0);
            BigDecimal unitCost = request.getUnitCosts() == null
                    ? null : request.getUnitCosts().get(productId);
            if (unitCost != null) {
                calculatedCost = calculatedCost.add(unitCost.multiply(BigDecimal.valueOf(quantity)));
            }
        }

        if (revenue.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal totalAmount = decimal(order, "totalAmount");
            if (totalAmount != null) revenue = totalAmount;
        }

        BigDecimal cost = request.getTotalCost() != null ? request.getTotalCost() : calculatedCost;
        OrderReport report = new OrderReport();
        report.setOrderId(request.getOrderId());
        report.setTotalRevenue(revenue);
        report.setTotalCost(cost);
        report.setTotalProfit(revenue.subtract(cost));
        return orderReportRepository.save(report);
    }

    @Transactional
    public ProductReport createProductReport(CreateProductReportRequest request, String authorization) {
        OrderReport orderReport = orderReportRepository.findById(request.getOrderReportId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order report not found"));

        JsonNode product = externalServiceClient.getProduct(request.getProductId(), authorization);
        if (product == null || product.isMissingNode()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found in SOA3");
        }

        JsonNode order = externalServiceClient.getOrder(orderReport.getOrderId(), authorization);
        JsonNode items = order.path("items");
        if (!items.isArray()) items = order.path("orderItems");

        int quantity = 0;
        BigDecimal revenue = BigDecimal.ZERO;
        if (items.isArray()) {
            for (JsonNode item : items) {
                if (item.path("productId").asInt(-1) == request.getProductId()) {
                    int itemQuantity = item.path("quantity").asInt(0);
                    quantity += itemQuantity;
                    BigDecimal itemTotal = decimal(item, "totalPrice");
                    if (itemTotal == null) {
                        BigDecimal unitPrice = decimal(item, "unitPrice");
                        itemTotal = (unitPrice == null ? BigDecimal.ZERO : unitPrice)
                                .multiply(BigDecimal.valueOf(itemQuantity));
                    }
                    revenue = revenue.add(itemTotal);
                }
            }
        }

        if (quantity == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product is not included in this order");
        }

        BigDecimal unitCost = request.getUnitCost() == null ? BigDecimal.ZERO : request.getUnitCost();
        BigDecimal cost = unitCost.multiply(BigDecimal.valueOf(quantity));

        ProductReport report = new ProductReport();
        report.setOrderReport(orderReport);
        report.setProductId(request.getProductId());
        report.setTotalSold(quantity);
        report.setRevenue(revenue);
        report.setCost(cost);
        report.setProfit(revenue.subtract(cost));
        return productReportRepository.save(report);
    }

    @Transactional
    public void deleteOrderReport(Integer id) {
        OrderReport report = getOrderReport(id);
        orderReportRepository.delete(report);
    }

    @Transactional
    public void deleteProductReport(Integer id) {
        ProductReport report = getProductReport(id);
        productReportRepository.delete(report);
    }

    private BigDecimal decimal(JsonNode node, String field) {
        JsonNode value = node.path(field);
        if (value.isMissingNode() || value.isNull() || value.isNumber() == false) return null;
        return value.decimalValue();
    }
}
