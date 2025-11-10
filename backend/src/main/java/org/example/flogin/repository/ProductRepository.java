package org.example.flogin.repository;

import org.example.flogin.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find all products that are not deleted
    List<Product> findByDeletedFalse();

    // Find product by id and not deleted
    Optional<Product> findByIdAndDeletedFalse(Long id);

    // Search products by name (case insensitive) and not deleted
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.deleted = false")
    List<Product> searchByName(String name);
}
