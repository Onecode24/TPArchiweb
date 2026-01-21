package com.episen.tparchiweb.repository;

import com.episen.tparchiweb.model.Order;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class OrderRepository {
    
    @Inject
    private EntityManager entityManager;
    
    public void save(Order order) {
        entityManager.persist(order);
    }
    
    public Order findById(Long id) {
        return entityManager.find(Order.class, id);
    }
    
    public List<Order> findAll() {
        return entityManager.createQuery("SELECT o FROM Order o", Order.class)
                .getResultList();
    }
}
