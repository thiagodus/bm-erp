package com.bm.erp.organization.controller;

import com.bm.erp.organization.model.Organization;
import com.bm.erp.organization.model.dto.OrganizationRequest;
import com.bm.erp.organization.model.dto.OrganizationResponse;
import com.bm.erp.organization.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PutMapping
    public ResponseEntity<OrganizationResponse> save(@Valid @RequestBody OrganizationRequest organizationRequest){
        OrganizationResponse response = organizationService.save(organizationRequest);
        return ResponseEntity.ok(response);
    }
}
