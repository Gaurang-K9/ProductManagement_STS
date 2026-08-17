package com.demo.repo;

import com.demo.model.product.Product;
import com.demo.model.review.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductSearchRepositoryImpl implements ProductSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Product> searchProducts(String name, String category, BigDecimal minPrice, BigDecimal maxPrice, Short rating, Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Main Query
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> product = cq.from(Product.class);

        List<Predicate> predicates = buildPredicates(cb, product, name, category, minPrice, maxPrice);

        cq.where(predicates.toArray(Predicate[]::new));

        applyRatingFilter(cb,cq,product,rating);

        if (pageable.getSort().isSorted()) {

            List<Order> orders = pageable.getSort()
                    .stream()
                    .map(sortOrder -> sortOrder.isAscending() ? cb.asc(product.get(sortOrder.getProperty())): cb.desc(product.get(sortOrder.getProperty())))
                    .toList();

            cq.orderBy(orders);
        }

        TypedQuery<Product> query = entityManager.createQuery(cq);

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Product> products = query.getResultList();

        // Count Query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);

        List<Predicate> countPredicates = buildPredicates(cb, countRoot, name, category, minPrice, maxPrice);

        countQuery.where(countPredicates.toArray(Predicate[]::new));

        if (rating != null) {
           applyRatingFilter(cb, countQuery, countRoot, rating);

            countQuery.select(countRoot.get("productId"));

            long total = entityManager.createQuery(countQuery)
                    .getResultList()
                    .size();

            return new PageImpl<>(products, pageable, total);
        }

        countQuery.select(cb.count(countRoot));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(products, pageable, total);
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Product> root, String name, String category, BigDecimal minPrice, BigDecimal maxPrice) {
        List<Predicate> predicates = new ArrayList<>();

        if (category != null && !category.isBlank()) {
            predicates.add(cb.equal(root.get("category"), category));
        }
        if (name != null && !name.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("productName")),
                    "%" + name.toLowerCase() + "%"));
        }
        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        return predicates;
    }

    private <T> void applyRatingFilter(CriteriaBuilder cb, CriteriaQuery<T> query, Root<Product> root, Short rating){

        if(rating != null){
            Join<Product, Review> review = root.join("reviews", JoinType.LEFT);

            query.groupBy(root.get("productId"));

            query.having(cb.ge(cb.coalesce(cb.avg(review.get("rating")), 0.0),rating.doubleValue()));
        }
    }
}