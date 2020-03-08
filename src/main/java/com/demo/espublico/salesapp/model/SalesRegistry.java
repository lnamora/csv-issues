package com.demo.espublico.salesapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "sales_registry")
public class SalesRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID",unique=true, nullable = false)
    private Long id;

    @Column(name = "ORDER_ID")
    private Long orderId;

    @Column(name = "REGION")
    private String region;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "ITEM_TYPE")
    private String itemType;

    @Column(name = "SALES_CHANNEL")
    private Boolean salesChannel;

    @Column(name = "ORDER_PRIORITY")
    private String orderPriority;

    @Column(name = "ORDER_DATE")
    private LocalDate orderDate;

    @Column(name = "SHIP_DATE")
    private LocalDate shipDate;

    @Column(name = "UNITS_SOLD")
    private Long unitsSold;

    @Column(name = "UNITS_PRICE")
    private BigDecimal unitPrice;

    @Column(name = "UNITS_COST")
    private BigDecimal unitCost;

    @Column(name = "TOTAL_REVENUE")
    private BigDecimal totalRevenue;

    @Column(name = "TOTAL_COST")
    private BigDecimal totalCost;

    @Column(name = "TOTAL_PROFIT")
    private BigDecimal totalProfit;

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setSalesChannel(Boolean salesChannel) {
        this.salesChannel = salesChannel;
    }

    public void setOrderPriority(String orderPriority) {
        this.orderPriority = orderPriority;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setShipDate(LocalDate shipDate) {
        this.shipDate = shipDate;
    }

    public void setUnitsSold(Long unitsSold) {
        this.unitsSold = unitsSold;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public String getItemType() {
        return itemType;
    }

    public Boolean getSalesChannel() {
        return salesChannel;
    }

    public String getOrderPriority() {
        return orderPriority;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public LocalDate getShipDate() {
        return shipDate;
    }

    public Long getUnitsSold() {
        return unitsSold;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    @Override
    public String toString() {
        return   orderId +
                "," + region +
                "," + country +
                "," + itemType +
                "," + salesChannel +
                "," + orderPriority +
                "," + orderDate +
                "," + shipDate +
                "," + unitsSold +
                "," + unitPrice +
                "," + unitCost +
                "," + totalRevenue +
                "," + totalCost +
                "," + totalProfit ;
    }
}
