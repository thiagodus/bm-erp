package com.bm.erp.organization.repository;

import com.bm.erp.organization.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganizationRespository extends JpaRepository<UUID, Organization> {
}
