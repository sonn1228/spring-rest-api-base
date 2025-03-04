package com.sonnguyen.base.service;
import com.sonnguyen.base.model.Product;
import com.sonnguyen.base.repository.ProductRepository;
import com.sonnguyen.base.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> searchProducts(String category, Double minPrice) {
        Specification<Product> spec = Specification.where(ProductSpecification.hasCategory(category))
                .and(ProductSpecification.hasPriceGreaterThan(minPrice));

        return productRepository.findAll(spec);
    }
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    public List<Product> saveProducts(List<Product> products) {
        return productRepository.saveAll(products);
    }

}
