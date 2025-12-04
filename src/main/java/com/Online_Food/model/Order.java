package com.Online_Food.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @ManyToOne
    private User customer;

    @JsonIgnore
    @ManyToOne
    private Restaurant restaurant;

    private  Long totalAmount;

    private String oderStatus;
    private Date createAt;

    @JsonIgnore
    @ManyToOne
    private  Address deliveryAddress;
    @OneToMany
    private List<OrderItem> items;

//    private  Payment payment;

    private int totalItem;

    private  int totalPrice;


}
