package com.bm.erp.organization;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganizationRespository extends JpaRepository<UUID, Organization> {
}
