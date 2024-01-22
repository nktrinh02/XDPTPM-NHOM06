package com.web.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "exchange_image")
@Getter
@Setter
public class ExchangeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String linkImage;

    @ManyToOne
    @JoinColumn(name = "exchange_id")
    @JsonBackReference
    private Exchange exchange;
}
