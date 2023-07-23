package com.hibernate.jpa.orders.domain;

import com.hibernate.jpa.orders.enums.OrderStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@AttributeOverrides({
        @AttributeOverride(
                name = "shippingAddress.address",
                column = @Column(name = "shipping_address")
        ),
        @AttributeOverride(
                name = "shippingAddress.city",
                column = @Column(name = "shipping_city")
        ),
        @AttributeOverride(
                name = "shippingAddress.state",
                column = @Column(name = "shipping_state")
        ),
        @AttributeOverride(
                name = "shippingAddress.zipCode",
                column = @Column(name = "shipping_zip_code")
        ),
        @AttributeOverride(
                name = "billToAddress.address",
                column = @Column(name = "bill_to_address")
        ),
        @AttributeOverride(
                name = "billToAddress.city",
                column = @Column(name = "bill_to_city")
        ),
        @AttributeOverride(
                name = "billToAddress.state",
                column = @Column(name = "bill_to_state")
        ),
        @AttributeOverride(
                name = "billToAddress.zipCode",
                column = @Column(name = "bill_to_zip_code")
        )
})
public class OrderHeader extends BaseEntity{
    // if we don't need to get the customer ...
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;
    @Embedded
    private Address shippingAddress;
    @Embedded
    private Address billToAddress;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // step 3: update the relation to OrderLine with a one to many
    // note: we will have: orderLine id null if we do not cascade
    // for delete:
    // Hibernate: delete from order_line where id=?
    // Hibernate: delete from order_header where id=?
    @OneToMany(
            mappedBy = "orderHeader",
            cascade = {
                    CascadeType.PERSIST, CascadeType.REMOVE
            },
            fetch = FetchType.EAGER )
    @Fetch(FetchMode.SUBSELECT)// should fix n+1 queries
    private Set<OrderLine> orderLines;

    // if we add the @OneToOne relationship to OrderHeader, we don't need to use orphan removal,
    // we need to use mappedBy instead
    @OneToOne(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
            // orphanRemoval = true // orderApproval value is not deleted if absent
            // mappedBy = "orderHeader"
    )
    @Fetch(FetchMode.SELECT)
    private OrderApproval orderApproval;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Address getBillToAddress() {
        return billToAddress;
    }

    public void setBillToAddress(Address billToAddress) {
        this.billToAddress = billToAddress;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Set<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(Set<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public OrderApproval getOrderApproval() {
        return orderApproval;
    }

    public void setOrderApproval(OrderApproval orderApproval) {
        this.orderApproval = orderApproval;
        orderApproval.setOrderHeader(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OrderHeader that = (OrderHeader) o;

        if (!Objects.equals(customer, that.customer)) return false;
        if (!Objects.equals(shippingAddress, that.shippingAddress))
            return false;
        if (!Objects.equals(billToAddress, that.billToAddress))
            return false;
        if (orderStatus != that.orderStatus) return false;
        return Objects.equals(orderLines, that.orderLines);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (shippingAddress != null ? shippingAddress.hashCode() : 0);
        result = 31 * result + (billToAddress != null ? billToAddress.hashCode() : 0);
        result = 31 * result + (orderStatus != null ? orderStatus.hashCode() : 0);
        result = 31 * result + (orderLines != null ? orderLines.hashCode() : 0);
        return result;
    }

    /**
     * Helper method
     * @param orderLine
     */
    public void addOrderLine(OrderLine orderLine) {
        if(orderLines == null)
            orderLines = new HashSet<>();

        orderLines.add(orderLine);
        orderLine.setOrderHeader(this);
    }
}

