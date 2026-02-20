package com.zhsw.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Street number is required")
    @Column(nullable = false, name = "street_number")
    private Long streetNumber;

    @NotBlank(message = "Postal code is required")
    @Column(nullable = false, name = "postal_code")
    private String postalCode;

    @NotBlank(message = "City is required")
    @Column(nullable = false, name = "city")
    private String city;

    private String country = "Switzerland";

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
