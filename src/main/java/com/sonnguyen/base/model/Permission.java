package com.sonnguyen.base.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String description;

}
