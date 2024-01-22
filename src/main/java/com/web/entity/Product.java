package com.web.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.web.utils.ProductStatus;
import com.web.utils.ProductType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private String image;

    private Double price;

    private Date createdDate;

    private Time createdTime;

    private ProductStatus productStatus;

    private ProductType productType;

    private String description;

    private String phone;

    private String linkFace;

    private String address;

    private Boolean locked;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<ProductImage> productImages = new ArrayList<>();
}
