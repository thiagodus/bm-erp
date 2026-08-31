package com.bm.erp.boleto.repository;

import com.bm.erp.boleto.entity.Boleto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BoletoRepository extends JpaRepository<Boleto, UUID> {

    Optional<Boleto> findByExternalId(String externalId);

    Optional<Boleto> findByOrderId(UUID id);
}
