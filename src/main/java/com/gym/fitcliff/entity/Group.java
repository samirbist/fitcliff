package com.gym.fitcliff.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Temporal(TemporalType.DATE)
    private Date date;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<GroupPayment> payments;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<Customer> customers;

}