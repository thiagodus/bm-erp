package com.bm.erp.organization.mapper;


import com.bm.erp.organization.model.Organization;
import com.bm.erp.organization.model.dto.OrganizationRequest;
import com.bm.erp.organization.model.dto.OrganizationResponse;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {

    public Organization toEntity(OrganizationRequest request) {
        Organization organization = new Organization();
        organization.setLegalName(request.legalName());
        organization.setEmail(request.email());
        organization.setPhone(request.phone());
        organization.setState(request.state());
        organization.setZipCode(request.zipCode());
        organization.setCnpj(request.cnpj());
        organization.setTradeName(request.tradeName());
        organization.setCountry(request.country());
        organization.setCity(request.city());
        organization.setStreet(request.street());
        return organization;
    }

    public OrganizationResponse toResponse(Organization organization) {
        return new OrganizationResponse(
                organization.getLegalName(),
                organization.getTradeName(),
                organization.getCnpj(),
                organization.getEmail(),
                organization.getPhone(),
                organization.getStreet(),
                organization.getCity(),
                organization.getState(),
                organization.getZipCode(),
                organization.getCountry()
        );



    }

}
