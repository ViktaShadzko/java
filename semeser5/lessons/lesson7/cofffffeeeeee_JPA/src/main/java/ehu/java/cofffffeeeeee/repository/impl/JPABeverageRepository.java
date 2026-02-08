package ehu.java.cofffffeeeeee.repository.impl;

import ehu.java.cofffffeeeeee.entity.Beverage;
import ehu.java.cofffffeeeeee.repository.BeverageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Profile("jpa")
@Transactional
public class JPABeverageRepository implements BeverageRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Beverage> getAllBeverages() {
        TypedQuery<Beverage> query = entityManager.createQuery(
                "SELECT b FROM Beverage b", Beverage.class);
        return query.getResultList();
    }

    @Override
    public boolean addBeverage(Beverage beverage) {
        try {
            if (beverage.getId() == null) {
                entityManager.persist(beverage);
            } else {
                entityManager.merge(beverage);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Beverage deleteBeverage(long id) {
        Beverage beverage = getBeverageById(id);
        if (beverage != null) {
            entityManager.remove(beverage);
        }
        return beverage;
    }

    @Override
    public Beverage getBeverageById(long id) {
        return entityManager.find(Beverage.class, id);
    }

    @Override
    public long getBeverageCount() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(b) FROM Beverage b", Long.class);
        return query.getSingleResult();
    }
}

