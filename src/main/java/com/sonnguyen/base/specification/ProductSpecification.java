package com.sonnguyen.base.specification;

import com.sonnguyen.base.model.Product;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

public class ProductSpecification {
    public static Specification<Product> hasCategory(String category) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                category == null ? null : criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<Product> hasPriceGreaterThan(Double price) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                price == null ? null : criteriaBuilder.greaterThan(root.get("price"), price);
    }
}
