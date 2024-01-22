package com.web.repository;

import com.web.entity.Product;
import com.web.entity.User;
import com.web.utils.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query(value = "select p from Product p where p.locked <> true order by p.id desc ")
    public List<Product> findAllDesc();

    @Query(value = "select p from Product p where p.category.id = ?1 and p.locked <> true order by p.id desc ")
    public List<Product> findByCategory(Long categoryId);

    @Query(value = "select p from Product p where p.user.id = ?1 and p.locked <> true and p.productType = ?2 order by p.id desc ")
    public List<Product> getProductByUser(Long userId, ProductType productType);

    @Query(value = "select p from Product p where p.category.id = ?1 and p.locked <> true order by p.id desc ")
    public Page<Product> findByCategory(Long idCategory, Pageable pageable);

    @Query(value = "select p from Product p where p.category.id = ?1 and p.id <> ?2 and p.locked <> true order by p.id desc ")
    public Page<Product> findByCategoryAndProduct(Long idCategory,Long productId, Pageable pageable);

    @Query("select p from Product p where (p.name like ?1 or p.category.name like ?1) and p.locked <> true")
    public Page<Product> findByParam(String search, Pageable pageable);

    @Query("select p from Product p where p.locked <> true")
    public Page<Product> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Product p set p.locked = true where p.user.id = ?1")
    int lockProduct(Long userId);

    @Modifying
    @Transactional
    @Query("update Product p set p.locked = false where p.user.id = ?1")
    int unLockProduct(Long userId);

}
