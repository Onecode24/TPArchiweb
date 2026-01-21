package com.episen.tparchiweb.service;

import com.episen.tparchiweb.model.Product;
import com.episen.tparchiweb.repository.ProductRepository;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ProductService {

    @Inject
    private EntityManager em;

    @Inject
    private ProductRepository repo;

    public void addProduct(Product product) {
        repo.save(em, product);
    }

    public void updateProduct(Product product) {
        repo.update(em, product);
    }

    public void deleteProduct(Long id) {
        Product product = repo.findById(em, id);
        if (product != null) {
            repo.delete(em, product);
        }
    }

    public Product getProductById(Long id) {
        return repo.findById(em, id);
    }

    public List<Product> getAllProducts() {
        return repo.findAll(em);
    }

    public List<Product> getProductsByCategory(String category) {
        return repo.findByCategory(em, category);
    }

    public void increaseStock(Long productId, int quantity) {
        Product product = repo.findById(em, productId);
        if (product != null) {
            product.setStock(product.getStock() + quantity);

        }
    }

    public boolean decreaseStock(Long productId, int quantity) {
        Product product = repo.findById(em, productId);
        if (product != null && product.getStock() >= quantity) {
            product.setStock(product.getStock() - quantity);
            return true;
        }
        return false;
    }
}