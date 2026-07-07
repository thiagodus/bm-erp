package com.bm.erp.organization.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "organization")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    @Id
    private  UUID id;
    @Column(nullable = false)
    private  String legalName;
    @Column(nullable = false)
    private  String tradeName;
    @Column(nullable = false, unique = true)
    private  String cnpj;
    @Column(length = 255)
    private  String email;
    private  String phone;
    @CreatedDate
    private  Instant createdAt;
    @LastModifiedDate
    private  Instant updatedAt;
    private  String street;
    private  String city;
    private  String state;
    private  String zipCode;
    private  String country;
    private  Boolean active = true;
}
