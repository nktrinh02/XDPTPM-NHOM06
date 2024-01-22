package com.web.api;

import com.web.entity.Cart;
import com.web.entity.Category;
import com.web.entity.Product;
import com.web.entity.User;
import com.web.exception.MessageException;
import com.web.repository.CartRepository;
import com.web.service.UserService;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CartApi {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserUtils userUtils;

    @PostMapping("/user/add-cart")
    public void save(@RequestParam("id") Long productId){
        User user = userUtils.getUserWithAuthority();
        Optional<Cart> cart = cartRepository.findByUserAndProduct(user.getId(), productId);
        if(cart.isPresent()){
            throw new MessageException("Sản phẩm này đã được thêm vào giỏ hàng");
        }
        Cart c = new Cart();
        c.setUser(user);
        Product p = new Product();
        p.setId(productId);
        c.setProduct(p);
        cartRepository.save(c);
    }


    @GetMapping("/user/cart-by-user")
    public List<Cart> findByUser(){
        User user = userUtils.getUserWithAuthority();
        return cartRepository.findByUser(user.getId());
    }

    @DeleteMapping("/user/delete-cart")
    public void delete(@RequestParam("id") Long id){
        cartRepository.deleteById(id);
    }

}
