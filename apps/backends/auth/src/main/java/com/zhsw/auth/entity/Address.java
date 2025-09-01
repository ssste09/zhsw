package com.zhsw.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank(message = "Address is required")
    @Column(nullable = false, name = "address")
    private String address;

    @NotBlank(message = "Postal code is required")
    @Column(nullable = false, name = "postal_code")
    private String postalCode;

    @NotBlank(message = "City is required")
    @Column(nullable = false, name = "city")
    private String city;

    @NotBlank(message = "Country is required")
    @Column(nullable = false, name = "country")
    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
