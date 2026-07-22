package com.bm.erp.organization.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "organization")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    @Id
    @GeneratedValue
    private  UUID id;
    @Column(nullable = false)
    private  String legalName;
    @Column(nullable = false)
    private  String tradeName;
    @Column(nullable = false, unique = true, length = 14)
    private  String cnpj;
    @Column(length = 255)
    private  String email;
    @Column(length = 20)
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
    @Column(nullable = false)
    private  Boolean active = true;
}
