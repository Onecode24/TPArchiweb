package com.episen.tparchiweb.service;

import com.episen.tparchiweb.model.Product;
import com.episen.tparchiweb.repository.ProductRepository;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped // Le service vit tant que l'application tourne
@Transactional    // Remplace le comportement automatique du @Stateless
public class ProductService {

    @Inject // Changement ici : on passe de @PersistenceContext à @Inject
    private EntityManager em;

    @Inject // Injecte automatiquement le repository
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
            // Pas besoin de appeler update(), JPA détecte le changement à la fin de la transaction
        }
    }

    public boolean decreaseStock(Long productId, int quantity) {
        Product product = repo.findById(em, productId);
        if (product != null && product.getStock() >= quantity) {
            product.setStock(product.getStock() - quantity);
            return true;
        }
        return false; // Vous pourriez aussi jeter une exception ici
    }
}