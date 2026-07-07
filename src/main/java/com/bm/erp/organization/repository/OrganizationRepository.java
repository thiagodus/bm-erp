package com.bm.erp.organization.repository;

import com.bm.erp.organization.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    Optional<Organization> findFirstBy();
}
