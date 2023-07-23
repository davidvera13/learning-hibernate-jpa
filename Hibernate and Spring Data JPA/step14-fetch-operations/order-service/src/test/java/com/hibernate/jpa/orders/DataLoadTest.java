package com.hibernate.jpa.orders;

import com.hibernate.jpa.orders.domain.*;
import com.hibernate.jpa.orders.enums.ProductStatus;
import com.hibernate.jpa.orders.repository.CustomerRepository;
import com.hibernate.jpa.orders.repository.OrderHeaderRepository;
import com.hibernate.jpa.orders.repository.ProductRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@ActiveProfiles("dev")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DataLoadTest {
    final String PRODUCT_D1 = "Product 1";
    final String PRODUCT_D2 = "Product 2";
    final String PRODUCT_D3 = "Product 3";

    final String TEST_CUSTOMER = "TEST CUSTOMER";

    @Autowired
    OrderHeaderRepository orderHeaderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    // Hibernate N+1 problem occurs when you use FetchType. LAZY for your entity associations.
    // If you perform a query to select n-entities and if you try to call any access method of
    // your entity's lazy association, Hibernate will perform n-additional queries to load
    // lazily fetched objects.
    @Test
    void nPlusOneProblem() {
        Customer customer = customerRepository.findCustomerByCustomerNameIgnoreCase(TEST_CUSTOMER)
                .get();

        // n+1 query launched ...
        IntSummaryStatistics totalOrdered = orderHeaderRepository
                .findAllByCustomer(customer)
                .stream()
                .flatMap(orderHeader -> orderHeader.getOrderLines().stream())
                //.collect(Collectors.summarizingInt(ol -> ol.getQuantityOrdered()));
                .collect(Collectors.summarizingInt(OrderLine::getQuantityOrdered));

        System.out.println("Total ordered " + totalOrdered.getSum());
    }

    @Test
    void lazyVsEager() {
        // not find existing value for test
        OrderHeader orderHeader = orderHeaderRepository
                .getById(33L);
        System.out.println("order is " + orderHeader.getId());
        // if eager:
        // eager: outer join ...
        // select
        //      o1_0.id,
        //      o1_0.bill_to_address,
        //      o1_0.bill_to_city,
        //      o1_0.bill_to_state,
        //      o1_0.bill_to_zip_code,
        //      o1_0.created_date,
        //      c1_0.id,
        //      c1_0.address,
        //      c1_0.city,c1_0.state,
        //      c1_0.zip_code,
        //      c1_0.created_date,
        //      c1_0.customer_name,
        //      c1_0.email,
        //      c1_0.last_modified_date,
        //      c1_0.phone,
        //      o1_0.last_modified_date,
        //      o2_0.id,
        //      o2_0.approved_by,
        //      o2_0.created_date,
        //      o2_0.last_modified_date,
        //      o1_0.order_status,
        //      o1_0.shipping_address,
        //      o1_0.shipping_city,
        //      o1_0.shipping_state,
        //      o1_0.shipping_zip_code
        //  from order_header o1_0
        //      left join customer c1_0 on c1_0.id=o1_0.customer_id
        //      left join order_approval o2_0 on o1_0.id=o2_0.order_header_id
        //  where o1_0.id=?

        // if lazy, a second statement wil be triggered
        // select
        //      c1_0.id,c1_0.address,
        //      c1_0.city,c1_0.state,
        //      c1_0.zip_code,
        //      c1_0.created_date,
        //      c1_0.customer_name,
        //      c1_0.email,c1_0.last_modified_date,
        //      c1_0.phone
        // from customer c1_0
        // where c1_0.id=?

        System.out.println("Customer name is " + orderHeader.getCustomer().getCustomerName());

    }

    // @Disabled
    @Rollback(value = false)
    @Test
    void testDataLoader() {
        List<Product> products = loadProducts();
        Customer customer = loadCustomers();

        int ordersToCreate = 100;

        for (int i = 0; i < ordersToCreate; i++){
            System.out.println("Creating order #: " + i);
            saveOrder(customer, products);
        }

        orderHeaderRepository.flush();
    }

    private OrderHeader saveOrder(Customer customer, List<Product> products){
        Random random = new Random();

        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setCustomer(customer);

        products.forEach(product -> {
            OrderLine orderLine = new OrderLine();
            orderLine.setProduct(product);
            orderLine.setQuantityOrdered(random.nextInt(20));
            // orderHeader.getOrderLines().add(orderLine);
            orderHeader.addOrderLine(orderLine);
        });

        return orderHeaderRepository.save(orderHeader);
    }

    private Customer loadCustomers() {
        return getOrSaveCustomer(TEST_CUSTOMER);
    }

    private Customer getOrSaveCustomer(String customerName) {
        return customerRepository.findCustomerByCustomerNameIgnoreCase(customerName)
                .orElseGet(() -> {
                    Customer c1 = new Customer();
                    c1.setCustomerName(customerName);
                    c1.setEmail("test@example.com");
                    Address address = new Address();
                    address.setAddress("123 Main");
                    address.setCity("New Orleans");
                    address.setState("LA");
                    c1.setAddress(address);
                    return customerRepository.save(c1);
                });
    }
    private List<Product> loadProducts(){
        List<Product> products = new ArrayList<>();

        products.add(getOrSaveProduct(PRODUCT_D1));
        products.add(getOrSaveProduct(PRODUCT_D2));
        products.add(getOrSaveProduct(PRODUCT_D3));

        return products;
    }
    private Product getOrSaveProduct(String description) {
        return productRepository.findByDescription(description)
                .orElseGet(() -> {
                    Product p1 = new Product();
                    p1.setDescription(description);
                    p1.setProductStatus(ProductStatus.NEW);
                    return productRepository.save(p1);
                });
    }
}