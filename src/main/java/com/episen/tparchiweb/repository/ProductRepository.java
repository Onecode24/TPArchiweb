package com.episen.tparchiweb.repository;

import com.episen.tparchiweb.model.Product;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ProductRepository {

    public void save(EntityManager em, Product product) {
        em.getTransaction().begin();
        em.persist(product);
        em.getTransaction().commit();
    }

    public void update(EntityManager em, Product product) {
        em.merge(product);
    }

    public void delete(EntityManager em, Product product) {
        em.remove(product);
    }

    public Product findById(EntityManager em, Long id) {
        return em.find(Product.class, id);
    }

    public List<Product> findAll(EntityManager em) {
        return em.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();
    }

    public List<Product> findByCategory(EntityManager em, String category) {
        return em.createQuery("SELECT p FROM Product p WHERE p.category = :cat", Product.class)
                .setParameter("cat", category)
                .getResultList();
    }
}
