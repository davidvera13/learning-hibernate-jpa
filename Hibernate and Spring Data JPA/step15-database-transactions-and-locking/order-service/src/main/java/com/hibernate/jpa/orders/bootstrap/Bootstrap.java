package com.hibernate.jpa.orders.bootstrap;

import com.hibernate.jpa.orders.domain.Customer;
import com.hibernate.jpa.orders.domain.OrderHeader;
import com.hibernate.jpa.orders.domain.Product;
import com.hibernate.jpa.orders.enums.ProductStatus;
import com.hibernate.jpa.orders.repository.CustomerRepository;
import com.hibernate.jpa.orders.repository.OrderHeaderRepository;
import com.hibernate.jpa.orders.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private final OrderHeaderRepository orderHeaderRepository;
    private final BootstrapOrderService bootstrapOrderService;
    private final CustomerRepository customerRepository;
    private final ProductService productService;


    @Autowired
    public Bootstrap(OrderHeaderRepository orderHeaderRepository, BootstrapOrderService bootstrapOrderService, CustomerRepository customerRepository, ProductService productService) {
        this.orderHeaderRepository = orderHeaderRepository;
        this.bootstrapOrderService = bootstrapOrderService;
        this.customerRepository = customerRepository;
        this.productService = productService;
    }

    // pessimistic locking
    private void updateProduct(){
        Product product = new Product();
        product.setDescription("My Product");
        product.setProductStatus(ProductStatus.NEW);

        Product savedProduct = productService.saveProduct(product);

        Product savedProduct2 = productService
                .updateQuantityOnHandStock(
                        savedProduct.getId(),
                        25);

        System.out.println("Updated Qty: " + savedProduct2.getQuantityOnHand());
    }

    // lazy initialize error
    // Caused by: org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: com.hibernate.jpa.orders.domain.Product.categories: could not initialize proxy - no Session
    //	at org.hibernate.collection.spi.AbstractPersistentCollection.throwLazyInitializationException(AbstractPersistentCollection.java:635) ~[hibernate-core-6.2.5.Final.jar:6.2.5.Final]
    //	at org.hibernate.collection.spi.AbstractPersistentCollection.withTemporarySessionIfNeeded(AbstractPersistentCollection.java:218) ~[hibernate-core-6.2.5.Final.jar:6.2.5.Final]
    //	at org.hibernate.collection.spi.AbstractPersistentCollection.initialize(AbstractPersistentCollection.java:615) ~[hibernate-core-6.2.5.Final.jar:6.2.5.Final]
    //	at org.hibernate.collection.spi.AbstractPersistentCollection.read(AbstractPersistentCollection.java:136) ~[hibernate-core-6.2.5.Final.jar:6.2.5.Final]
    //	at org.hibernate.collection.spi.PersistentSet.iterator(PersistentSet.java:169) ~[hibernate-core-6.2.5.Final.jar:6.2.5.Final]
    // SOLUTION: make transactional and use a transaction context in the whole method
//    @Transactional
//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println("I was called!");
//        // Internal method call
//        readOrderData();
//    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("I was called!");
        // internal call
        // Caused by: java.sql.SQLException: Cannot execute statement in a READ ONLY transaction.
        updateProduct();
        // external method call
        bootstrapOrderService.readOrderData();

        //  tests for adding
        Customer customer = new Customer();
        customer.setCustomerName("Testing Version");
        Customer savedCustomer = customerRepository.save(customer);
        System.out.println("Version is: " + savedCustomer.getVersion());

        // optimistic locking usage...
        // version is updated each time
        savedCustomer.setCustomerName("Testing Version 2");
        savedCustomer = customerRepository.save(savedCustomer);
        System.out.println("Version is: " + savedCustomer.getVersion());

        savedCustomer.setCustomerName("Testing Version 3");
        savedCustomer = customerRepository.save(savedCustomer);
        System.out.println("Version is: " + savedCustomer.getVersion());


        customerRepository.deleteById(savedCustomer.getId());
    }



    // working in the transaction context of the caller method
    // we can't annotate this method with transactional and remove it from run method :
    // In proxy mode (which is the default), only external method calls coming in through
    // the proxy are intercepted. This means that self-invocation (in effect, a method within
    // the target object calling another method of the target object) does not lead to an actual
    // transaction at runtime even if the invoked method is marked with @Transactional.
    // Also, the proxy must be fully initialized to provide the expected behavior, so you
    // should not rely on this feature in your initialization code for example, in a
    // @PostConstruct method.

    public void readOrderData() {
        OrderHeader orderHeader = orderHeaderRepository.findById(232L).get();

        orderHeader.getOrderLines().forEach(ol -> {
            System.out.println(ol.getProduct().getDescription());

            ol.getProduct().getCategories().forEach(cat -> {
                System.out.println(cat.getDescription());
            });
        });
    }
}