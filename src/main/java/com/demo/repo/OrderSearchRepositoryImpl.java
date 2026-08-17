    package com.demo.repo;

import com.demo.model.order.Order;
import com.demo.model.order.OrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderSearchRepositoryImpl implements OrderSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Order> searchOrders(String pincode, BigDecimal minTotal, BigDecimal maxTotal, LocalDateTime from, LocalDateTime to, OrderStatus status, Long userId, String username, Pageable pageable) {

        CriteriaBuilder cb =  entityManager.getCriteriaBuilder();

        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> order = query.from(Order.class);

        List<Predicate> predicates = buildPredicates(cb, order, pincode, minTotal, maxTotal, from, to, status, userId, username);

        query.where(predicates.toArray(Predicate[]::new));

        if (pageable.getSort().isSorted()) {

            List<jakarta.persistence.criteria.Order> orders = pageable.getSort()
                    .stream()
                    .map(sortOrder -> sortOrder.isAscending() ? cb.asc(order.get(sortOrder.getProperty())): cb.desc(order.get(sortOrder.getProperty())))
                    .toList();

            query.orderBy(orders);
        }

        TypedQuery<Order> typedQuery = entityManager.createQuery(query);

        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Order> orders = typedQuery.getResultList();

        // Count Query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Order> countRoot = countQuery.from(Order.class);

        List<Predicate> countPredicates = buildPredicates(cb, countRoot, pincode, minTotal, maxTotal, from, to, status, userId, username);

        countQuery.where(countPredicates.toArray(Predicate[]::new));

        countQuery.select(cb.count(countRoot));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(orders, pageable, total);
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Order> orderRoot, String pincode, BigDecimal minTotal, BigDecimal maxTotal, LocalDateTime from, LocalDateTime to, OrderStatus status, Long userId, String username){
        List<Predicate> predicates = new ArrayList<>();

        if(pincode != null){
            predicates.add(cb.equal(orderRoot.get("pincode"), pincode));
        }

        if(minTotal != null){
            predicates.add(cb.greaterThanOrEqualTo(orderRoot.get("total"), minTotal));
        }

        if(maxTotal != null){
            predicates.add(cb.lessThanOrEqualTo(orderRoot.get("total"), maxTotal));
        }

        if(from != null){
            predicates.add(cb.greaterThan(orderRoot.get("orderTime"), from));
        }

        if(to != null){
            predicates.add(cb.lessThan(orderRoot.get("orderTime"), to));
        }

        if (userId != null) {
            predicates.add(
                    cb.equal(orderRoot.get("user").get("userId"), userId)
            );
        }

        if (username != null && !username.isBlank()) {
            predicates.add(
                    cb.equal(orderRoot.get("user").get("username"), username)
            );
        }

        return predicates;
    }
}
