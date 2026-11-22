package com.example.SOA5.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateProductReportRequest {
    @NotNull
    @Positive
    private Integer orderReportId;

    @NotNull
    @Positive
    private Integer productId;

    private BigDecimal unitCost;

    public Integer getOrderReportId() { return orderReportId; }
    public Integer getProductId() { return productId; }
    public BigDecimal getUnitCost() { return unitCost; }

    public void setOrderReportId(Integer orderReportId) { this.orderReportId = orderReportId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }
}
