package com.bm.erp.organization.service;

import com.bm.erp.organization.mapper.OrganizationMapper;
import com.bm.erp.organization.model.Organization;
import com.bm.erp.organization.model.dto.OrganizationRequest;
import com.bm.erp.organization.model.dto.OrganizationResponse;
import com.bm.erp.organization.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private OrganizationMapper mapper;

    @InjectMocks
    private OrganizationService service;

    @Test
    void shouldCreateOrganizationWhenNoneExists(){
        //Arrange
        OrganizationRequest organizationRequest = new OrganizationRequest(
                "Legal Name",
                "Trade Name",
                "000001",
                "test@test.com",
                "041988334",
                "Street",
                "City",
                "State",
                "80320010",
                "BR");

        Organization organization = new Organization();
        organization.setLegalName("Legal Name");
        organization.setTradeName("Trade Name");
        organization.setCnpj("000001");
        when(mapper.toEntity(organizationRequest)).thenReturn(organization);
        when(organizationRepository.findFirstBy()).thenReturn(Optional.empty());
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);

        OrganizationResponse organizationResponse = new OrganizationResponse("Legal Name",
                "Trade Name",
                "000001",
                "test@tes.com",
                "041988334",
                "Street",
                "City",
                "State",
                "80320010",
                "BR");
        when(mapper.toResponse(organization)).thenReturn(organizationResponse);

        ArgumentCaptor<Organization> captor = ArgumentCaptor.forClass(Organization.class);

        //Act
        OrganizationResponse response = service.save(organizationRequest);

        //Assert
        verify(organizationRepository).save(captor.capture());

        Organization saved = captor.getValue();

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getActive()).isTrue();
        assertThat(saved.getTradeName()).isEqualTo("Trade Name");
        assertThat(response).isSameAs(organizationResponse);
        verify(mapper).toResponse(organization);
        verify(mapper).toEntity(organizationRequest);
    }

}
