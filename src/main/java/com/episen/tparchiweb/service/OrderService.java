package com.episen.tparchiweb.service;

import com.episen.tparchiweb.model.Order;
import com.episen.tparchiweb.repository.OrderRepository;
import java.util.List;

public class OrderService {
    
    private OrderRepository repository = new OrderRepository();
    
    public List<Order> getAll() {
        return repository.findAll();
    }
    
    public Order getById(Long id) {
        return repository.findById(id);
    }
    
    public List<Order> getByUserId(Long userId) {
        return repository.findByUserId(userId);
    }
    
    public Order create(Order order) {
        return repository.save(order);
    }
    
    public Order update(Long id, Order order) {
        order.setId(id);
        return repository.save(order);
    }
    
    public void delete(Long id) {
        repository.delete(id);
    }
}
