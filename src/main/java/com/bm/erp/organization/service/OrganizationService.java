package com.bm.erp.organization.service;

import com.bm.erp.organization.mapper.OrganizationMapper;
import com.bm.erp.organization.model.Organization;
import com.bm.erp.organization.model.dto.OrganizationRequest;
import com.bm.erp.organization.model.dto.OrganizationResponse;
import com.bm.erp.organization.repository.OrganizationRepository;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    public OrganizationService(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
    }

    public OrganizationResponse save(OrganizationRequest organizationRequest) {

        Optional<Organization> existingOrganization = organizationRepository.findFirstBy();

        if (existingOrganization.isPresent()){
            throw new UnsupportedOperationException("No update yet");
        }

        Organization organization = organizationMapper.toEntity(organizationRequest);

        organization.setId(UUID.randomUUID());
        organization.setActive(true);
        organization.setCreatedAt(Instant.now());

        var savedOrganization = organizationRepository.save(organization);

        return  organizationMapper.toResponse(savedOrganization);


    }
}
