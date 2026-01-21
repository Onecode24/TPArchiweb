package com.episen.tparchiweb.repository;

import com.episen.tparchiweb.config.EntityManagerProducer;
import com.episen.tparchiweb.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class UserRepository {
    private EntityManager em;

    public UserRepository() {
        EntityManagerProducer emp = new EntityManagerProducer();
        this.em = emp.createEntityManager();
    }

    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public List<User> search(String query) {
        String searchPattern = "%" + query.toLowerCase() + "%";
        return em.createQuery("SELECT u FROM User u WHERE LOWER(u.username) LIKE :query OR LOWER(u.email) LIKE :query", User.class)
                .setParameter("query", searchPattern)
                .getResultList();
    }

    public User findByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User findByEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    @Transactional
    public User save(User user) {
        if (user.getId() == null) {
            em.persist(user);
            return user;
        } else {
            return em.merge(user);
        }
    }
}
