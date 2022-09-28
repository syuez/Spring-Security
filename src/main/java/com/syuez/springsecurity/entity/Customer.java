package com.syuez.springsecurity.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "t_customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
}
