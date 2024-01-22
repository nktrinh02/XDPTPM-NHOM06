package com.web.repository;

import com.web.entity.Cart;
import com.web.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    @Query("select c from Cart c where c.user.id = ?1 and c.product.id = ?2")
    public Optional<Cart> findByUserAndProduct(Long userId, Long productId);

    @Query("select c from Cart c where c.user.id = ?1")
    public List<Cart> findByUser(Long userId);
}
