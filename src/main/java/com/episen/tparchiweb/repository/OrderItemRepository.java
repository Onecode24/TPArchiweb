package com.episen.tparchiweb.repository;

import com.episen.tparchiweb.model.OrderItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class OrderItemRepository {
    
    @Inject
    private EntityManager entityManager;
    
    public void save(OrderItem orderItem) {
        entityManager.persist(orderItem);
    }
    
    public OrderItem findById(Long id) {
        return entityManager.find(OrderItem.class, id);
    }
}
