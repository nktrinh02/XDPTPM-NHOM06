package com.web.api;

import com.web.dto.ExchangeDto;
import com.web.dto.ProductDTO;
import com.web.entity.*;
import com.web.exception.MessageException;
import com.web.repository.ExchangeImageRepository;
import com.web.repository.ExchangeRepository;
import com.web.utils.ProductStatus;
import com.web.utils.ProductType;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ExchangeApi {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private ExchangeImageRepository exchangeImageRepository;

    @Autowired
    private UserUtils userUtils;

    @PostMapping("/user/send-exchange")
    public Exchange save(@RequestBody ExchangeDto dto){
        Exchange exchange = dto.getExchange();
        exchange.setUser(userUtils.getUserWithAuthority());
        exchange.setCreatedDate(new Date(System.currentTimeMillis()));
        exchange.setCreatedTime(new Time(System.currentTimeMillis()));
        Exchange result = exchangeRepository.save(exchange);

        for(String link : dto.getLinkImage()){
            ExchangeImage image = new ExchangeImage();
            image.setLinkImage(link);
            image.setExchange(result);
            exchangeImageRepository.save(image);
        }
        return exchange;
    }

    @GetMapping("/user/exchange-by-product")
    public List<Exchange> getExchangeByProduct(@RequestParam("id") Long productId){
        List<Exchange> list = exchangeRepository.findByProduct(productId);
        return list;
    }

    @GetMapping("/user/exchange-by-id")
    public Exchange findById(@RequestParam("id") Long id){
        return exchangeRepository.findById(id).get();
    }
}
