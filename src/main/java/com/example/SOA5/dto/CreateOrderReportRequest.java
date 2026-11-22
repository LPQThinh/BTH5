package com.example.SOA5.dto;

import java.math.BigDecimal;
import java.util.Map;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateOrderReportRequest {
    @NotNull
    @Positive
    private Integer orderId;

    private BigDecimal totalCost;

    private Map<Integer, BigDecimal> unitCosts;

    public Integer getOrderId() { return orderId; }
    public BigDecimal getTotalCost() { return totalCost; }
    public Map<Integer, BigDecimal> getUnitCosts() { return unitCosts; }

    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    public void setUnitCosts(Map<Integer, BigDecimal> unitCosts) { this.unitCosts = unitCosts; }
}
