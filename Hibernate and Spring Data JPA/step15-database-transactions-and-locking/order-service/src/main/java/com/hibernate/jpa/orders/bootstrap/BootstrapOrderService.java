package com.hibernate.jpa.orders.bootstrap;


import com.hibernate.jpa.orders.domain.OrderHeader;
import com.hibernate.jpa.orders.repository.OrderHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BootstrapOrderService {
    private final OrderHeaderRepository orderHeaderRepository;

    @Autowired
    public BootstrapOrderService(OrderHeaderRepository orderHeaderRepository) {
        this.orderHeaderRepository = orderHeaderRepository;
    }

    @Transactional
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