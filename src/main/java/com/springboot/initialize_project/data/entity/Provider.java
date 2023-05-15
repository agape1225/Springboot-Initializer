package com.springboot.initialize_project.data.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "provider")
public class Provider extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "provider"
            , cascade = CascadeType.PERSIST
            , fetch = FetchType.EAGER
            , orphanRemoval = true)
    @ToString.Exclude
    private List<Product> productList = new ArrayList<>();
}