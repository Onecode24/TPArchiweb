package com.episen.tparchiweb.service;

import com.episen.tparchiweb.model.Order;
import com.episen.tparchiweb.model.OrderItem;
import com.episen.tparchiweb.model.Product;
import com.episen.tparchiweb.repository.OrderItemRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class OrderItemService {
    
    @Inject
    private EntityManager entityManager;
    
    @Inject
    private OrderItemRepository orderItemRepository;
    
    public OrderItem createOrderItem(Long orderId, Long productId, Integer quantity) {
        Order order = entityManager.find(Order.class, orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found with id: " + orderId);
        }
        
        Product product = entityManager.find(Product.class, productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with id: " + productId);
        }
        
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        
        orderItemRepository.save(orderItem);
        
        return orderItem;
    }
    
    public OrderItem findById(Long id) {
        return orderItemRepository.findById(id);
    }
}
