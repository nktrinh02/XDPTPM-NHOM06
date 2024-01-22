package com.web.repository;

import com.web.entity.Cart;
import com.web.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange,Long> {

    @Query("select e from Exchange e where e.product.id = ?1")
    public List<Exchange> findByProduct(Long productId);
}
