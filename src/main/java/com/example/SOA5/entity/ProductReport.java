package com.example.SOA5.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_reports")
public class ProductReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_report_id", nullable = false)
    @JsonBackReference
    private OrderReport orderReport;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "total_sold", nullable = false)
    private Integer totalSold;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal revenue = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal cost = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal profit = BigDecimal.ZERO;

    public Integer getId() { return id; }
    public OrderReport getOrderReport() { return orderReport; }
    public Integer getProductId() { return productId; }
    public Integer getTotalSold() { return totalSold; }
    public BigDecimal getRevenue() { return revenue; }
    public BigDecimal getCost() { return cost; }
    public BigDecimal getProfit() { return profit; }

    public void setId(Integer id) { this.id = id; }
    public void setOrderReport(OrderReport orderReport) { this.orderReport = orderReport; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public void setTotalSold(Integer totalSold) { this.totalSold = totalSold; }
    public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
    public void setProfit(BigDecimal profit) { this.profit = profit; }
}
