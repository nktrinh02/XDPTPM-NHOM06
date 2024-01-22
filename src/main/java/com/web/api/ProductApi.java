package com.web.api;

import com.web.dto.ProductDTO;
import com.web.entity.Product;
import com.web.entity.ProductImage;
import com.web.entity.User;
import com.web.exception.MessageException;
import com.web.repository.ProductImageRepository;
import com.web.repository.ProductRepository;
import com.web.utils.ProductStatus;
import com.web.utils.ProductType;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ProductApi {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private UserUtils userUtils;

    @GetMapping("/public/productByID")
    public Product findById(@RequestParam("id") Long id){
        return productRepository.findById(id).get();
    }

    @DeleteMapping("/admin/deleteProduct")
    public void deleteProduct(@RequestParam("id") Long id){
        Product p = productRepository.findById(id).get();
        if(p.getProductStatus().equals(ProductStatus.DANG_HIEN_THI)){
            throw new MessageException("Sản phẩm này chưa được bán, không thể xóa");
        }
        productRepository.deleteById(id);
    }

    @PostMapping("/user/addOrUpdateproduct")
    public Product save(@RequestBody ProductDTO productDto){
        Product product = productDto.getProduct();
        if(product.getId() == null){
            product.setCreatedDate(new Date(System.currentTimeMillis()));
            product.setCreatedTime(new Time(System.currentTimeMillis()));
            product.setProductStatus(ProductStatus.DANG_HIEN_THI);
            product.setLocked(false);
            product.setUser(userUtils.getUserWithAuthority());
        }
        else{
            Product p = productRepository.findById(product.getId()).get();
            if(p.getProductStatus().equals(ProductStatus.DA_BAN)){
                throw new MessageException("Sản phẩm đã bán, không thể sửa");
            }
            product.setCreatedDate(p.getCreatedDate());
            product.setCreatedTime(p.getCreatedTime());
            product.setProductStatus(p.getProductStatus());
            product.setUser(p.getUser());
            product.setLocked(p.getLocked());
        }
        Product result = productRepository.save(product);

        for(String link : productDto.getLinkImage()){
            ProductImage productImage = new ProductImage();
            productImage.setLinkImage(link);
            productImage.setProduct(result);
            productImageRepository.save(productImage);
        }
        return result;
    }

    @GetMapping("/public/allProduct")
    public Page<Product> getProductIndexPage(Pageable pageable){
        Page<Product> page = productRepository.findAll(pageable);
        return page;
    }

    @GetMapping("/public/product-by-param")
    public Page<Product> getProductByParam(@RequestParam(value = "search", required = false) String search, Pageable pageable){
        if(search == null){
            search = "";
        }
        Page<Product> page = productRepository.findByParam("%"+search+"%", pageable);
        return page;
    }

    @GetMapping("/public/product-by-category-id")
    public Page<Product> findByCategory(@RequestParam("id") Long categoryid, Pageable pageable){
        Page<Product> page = productRepository.findByCategory(categoryid, pageable);
        return page;
    }

    @GetMapping("/public/san-pham-lien-quan")
    public Page<Product> sanPhamLienQuan(@RequestParam("id") Long idProduct, Pageable pageable){
        Product product = productRepository.findById(idProduct).get();
        Page<Product> page = productRepository.findByCategoryAndProduct(product.getCategory().getId(), idProduct, pageable);
        return page;
    }

    @GetMapping("/user/myProduct")
    public List<Product> myProduct(@RequestParam("loai") ProductType productType){
        User user = userUtils.getUserWithAuthority();
        List<Product> list = productRepository.getProductByUser(user.getId(), productType);
        return list;
    }

    @GetMapping("/admin/allProduct-list")
    public List<Product> allProduct(@RequestParam(value = "id", required = false) Long idCategory){
        List<Product> list = null;
        if(idCategory == null){
            list = productRepository.findAllDesc();
        }
        else{
            list = productRepository.findByCategory(idCategory);
        }
        return list;
    }


    @PostMapping("/user/updateTrangThaiDaBan")
    public void updateTrangThaiDaBan(@RequestParam("id") Long id){
        Product product = productRepository.findById(id).get();
        if(product.getProductStatus().equals(ProductStatus.DANG_HIEN_THI)){
            product.setProductStatus(ProductStatus.DA_BAN);
        }
        else{
            product.setProductStatus(ProductStatus.DANG_HIEN_THI);
        }
        productRepository.save(product);
    }


    @DeleteMapping("/user/deleteProduct")
    public void deleteProductByUser(@RequestParam("id") Long id){
        productRepository.deleteById(id);
    }
}
