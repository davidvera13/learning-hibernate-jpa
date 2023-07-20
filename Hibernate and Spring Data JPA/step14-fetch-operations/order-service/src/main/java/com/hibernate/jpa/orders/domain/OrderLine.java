package com.hibernate.jpa.orders.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.util.Objects;


// step 2: create the entity and the relation to OrderHeader
@Entity
public class OrderLine extends BaseEntity {
    private Integer quantityOrdered;

    @ManyToOne
    private OrderHeader orderHeader;
    @ManyToOne
    private Product product;

    public Integer getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(Integer quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public OrderHeader getOrderHeader() {
        return orderHeader;
    }

    public void setOrderHeader(OrderHeader orderHeader) {
        this.orderHeader = orderHeader;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OrderLine orderLine = (OrderLine) o;

        if (!Objects.equals(quantityOrdered, orderLine.quantityOrdered))
            return false;
        if (!Objects.equals(orderHeader, orderLine.orderHeader))
            return false;
        return Objects.equals(product, orderLine.product);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (quantityOrdered != null ? quantityOrdered.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        return result;
    }

//    fix stack overflow
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        if (!super.equals(o)) return false;
//
//        OrderLine orderLine = (OrderLine) o;
//
//        if (!Objects.equals(quantityOrdered, orderLine.quantityOrdered))
//            return false;
//        return Objects.equals(orderHeader, orderLine.orderHeader);
//    }
//
//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (quantityOrdered != null ? quantityOrdered.hashCode() : 0);
//        result = 31 * result + (orderHeader != null ? orderHeader.hashCode() : 0);
//        return result;
//    }
}
