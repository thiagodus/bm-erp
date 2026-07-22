package com.bm.erp.organization.service;

import com.bm.erp.organization.exception.OrganizationNotFoundException;
import com.bm.erp.organization.mapper.OrganizationMapper;
import com.bm.erp.organization.entity.Organization;
import com.bm.erp.organization.dto.OrganizationRequest;
import com.bm.erp.organization.dto.OrganizationResponse;
import com.bm.erp.organization.repository.OrganizationRepository;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

        Organization organization = getOrganization();
        when(mapper.toEntity(organizationRequest)).thenReturn(organization);
        when(organizationRepository.findFirstBy()).thenReturn(Optional.empty());
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);

        OrganizationResponse organizationResponse = getOrganizationResponse();
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

    private static @NonNull OrganizationResponse getOrganizationResponse() {
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
        return organizationResponse;
    }

    private static @NonNull Organization getOrganization() {
        Organization organization = new Organization();
        organization.setLegalName("Legal Name");
        organization.setTradeName("Trade Name");
        organization.setCnpj("000001");
        return organization;
    }

    @Test
    void shouldReturnOrganizationWhenItExists() {
        //Arrange
        Organization organization = getOrganization();
        when(organizationRepository.findFirstBy()).thenReturn(Optional.of(organization));
        OrganizationResponse organizationResponse = getOrganizationResponse();
        when(mapper.toResponse(organization)).thenReturn(organizationResponse);
        //Act
        OrganizationResponse response = service.getOrganization();

        //Assert
        assertThat(response.legalName()).isEqualTo("Legal Name");
    }

    @Test
    void shouldThrowExceptionWhenOrganizationDoesNotExist(){
        //Arrange
        when(organizationRepository.findFirstBy()).thenReturn(Optional.empty());

        //ACt + Assert
        assertThatThrownBy(() -> service.getOrganization())
            .isInstanceOf(OrganizationNotFoundException.class);
    }

}
