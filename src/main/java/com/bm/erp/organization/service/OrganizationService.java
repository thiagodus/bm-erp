package com.bm.erp.organization.service;

import com.bm.erp.organization.exception.OrganizationNotFoundException;
import com.bm.erp.organization.mapper.OrganizationMapper;
import com.bm.erp.organization.entity.Organization;
import com.bm.erp.organization.dto.OrganizationRequest;
import com.bm.erp.organization.dto.OrganizationResponse;
import com.bm.erp.organization.repository.OrganizationRepository;
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

        organization.setActive(true);

        var savedOrganization = organizationRepository.save(organization);

        return  organizationMapper.toResponse(savedOrganization);


    }

    public OrganizationResponse getOrganization() {
        return organizationRepository.findFirstBy()
                .map(organizationMapper::toResponse)
                .orElseThrow(OrganizationNotFoundException::new);

    }
}
