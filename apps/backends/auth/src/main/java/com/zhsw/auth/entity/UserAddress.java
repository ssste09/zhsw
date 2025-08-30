package com.zhsw.auth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_addresses")
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_address_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    @Column(name = "user_id")
    private User user;

    @Column(nullable = false, name = "is_default")
    private boolean isDefault = false;

    @Column(nullable = false, name = "street")
    private String street;

    @Column(nullable = false, name = "city")
    private String city;

    @Column(nullable = false, name = "postal_code")
    private String postalCode;

    @Column(nullable = false, name = "country")
    private String country;


}
