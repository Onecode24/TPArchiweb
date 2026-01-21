package com.episen.tparchiweb.service;

import com.episen.tparchiweb.dto.CreateOrderRequest;
import com.episen.tparchiweb.dto.OrderItemResponse;
import com.episen.tparchiweb.dto.OrderResponse;
import com.episen.tparchiweb.model.Order;
import com.episen.tparchiweb.model.OrderItem;
import com.episen.tparchiweb.model.Product;
import com.episen.tparchiweb.model.User;
import com.episen.tparchiweb.repository.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderService {
    
    @Inject
    private EntityManager entityManager;
    
    @Inject
    private OrderRepository orderRepository;
    
    @Inject
    private OrderItemService orderItemService;
    
    public OrderResponse createOrder(CreateOrderRequest request) {
        try {
            entityManager.getTransaction().begin();
            
            User user = entityManager.find(User.class, request.getUserId());
            if (user == null) {
                throw new IllegalArgumentException("User not found with id: " + request.getUserId());
            }
            System.out.println(" User get : " + user.getId() + request.getUserId());

            for (CreateOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
                Product product = entityManager.find(Product.class, itemRequest.getProductId());
                if (product == null) {
                    throw new IllegalArgumentException("Product not found with id: " + itemRequest.getProductId());
                }
                entityManager.find(Product.class, itemRequest.getProductId());
                
                if (product.getStock() < itemRequest.getQuantity()) {
                    throw new IllegalArgumentException(
                        "Stock insuffisant pour le produit '" + product.getName() + "'. Stock disponible: " + product.getStock()
                    );
                }
            }

            Order order = new Order();
            order.setUser(user);
            
            orderRepository.save(order);
            entityManager.flush();
            
            for (CreateOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
                orderItemService.createOrderItem(
                    order.getId(),
                    itemRequest.getProductId(),
                    itemRequest.getQuantity()
                );
                
                // Déduire le stock du produit
                Product product = entityManager.find(Product.class, itemRequest.getProductId());
                product.setStock(product.getStock() - itemRequest.getQuantity());
                entityManager.merge(product);
            }
            
            entityManager.getTransaction().commit();
            
            // Reload order with items
            Order createdOrder = orderRepository.findById(order.getId());
            return mapToOrderResponse(createdOrder);
            
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }
    
    public OrderResponse findById(Long id) {
        Order order = orderRepository.findById(id);
        if (order == null) {
            throw new IllegalArgumentException("Order not found with id: " + id);
        }
        return mapToOrderResponse(order);
    }
    
    public List<OrderResponse> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }
    
    public void deleteOrder(Long id) {
        try {
            entityManager.getTransaction().begin();
            
            Order order = orderRepository.findById(id);
            if (order == null) {
                throw new IllegalArgumentException("Order not found with id: " + id);
            }

            
            entityManager.remove(order);
            
            entityManager.getTransaction().commit();
            
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }
    
    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::mapToOrderItemResponse)
                .collect(Collectors.toList());
        
        return new OrderResponse(
            order.getId(),
            order.getUser().getId(),
            items,
            order.getCreatedAt(),
            order.getUpdatedAt()
        );
    }
    
    private OrderItemResponse mapToOrderItemResponse(OrderItem item) {
        return new OrderItemResponse(
            item.getId(),
            item.getProduct().getId(),
            item.getProduct().getName(),
            item.getQuantity(),
            item.getProduct().getPrice().doubleValue()
        );
    }
}
