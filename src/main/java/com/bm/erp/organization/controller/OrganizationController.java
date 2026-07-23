package com.bm.erp.organization.controller;

import com.bm.erp.organization.dto.OrganizationRequest;
import com.bm.erp.organization.dto.OrganizationResponse;
import com.bm.erp.organization.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<OrganizationResponse> get(){
        OrganizationResponse response = organizationService.getOrganization();
        return ResponseEntity.ok(response);
    }
}
