package com.episen.tparchiweb.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class EntityManagerProducer {
    
    private EntityManagerFactory entityManagerFactory;
    
    @Produces
    @ApplicationScoped
    public EntityManagerFactory createEntityManagerFactory() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("default");
        }
        return entityManagerFactory;
    }
    
    @Produces
    @RequestScoped
    public EntityManager createEntityManager(EntityManagerFactory factory) {
        return factory.createEntityManager();
    }
    
    public void closeEntityManager(@Disposes EntityManager entityManager) {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
    
    public void closeEntityManagerFactory(@Disposes EntityManagerFactory factory) {
        if (factory.isOpen()) {
            factory.close();
        }
    }
}
