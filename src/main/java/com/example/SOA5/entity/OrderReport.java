package com.example.SOA5.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders_reports")
public class OrderReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "total_revenue", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(name = "total_cost", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalCost = BigDecimal.ZERO;

    @Column(name = "total_profit", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalProfit = BigDecimal.ZERO;

    @OneToMany(mappedBy = "orderReport", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductReport> productReports = new ArrayList<>();

    public Integer getId() { return id; }
    public Integer getOrderId() { return orderId; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public BigDecimal getTotalCost() { return totalCost; }
    public BigDecimal getTotalProfit() { return totalProfit; }
    public List<ProductReport> getProductReports() { return productReports; }

    public void setId(Integer id) { this.id = id; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    public void setTotalProfit(BigDecimal totalProfit) { this.totalProfit = totalProfit; }
    public void setProductReports(List<ProductReport> productReports) { this.productReports = productReports; }
}
