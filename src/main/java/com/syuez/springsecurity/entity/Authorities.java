package com.syuez.springsecurity.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "t_authority ")
public class Authorities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String authority;
}
