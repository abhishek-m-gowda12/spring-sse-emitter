package com.abhishek.springsseemitter.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "user", schema = "public")
public class UserEntity {
    @Id
    private Long id;

    @Column(name = "name")
    private String name;
}
