package br.com.ccrs.logistics.fleet.order.acceptance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import br.com.ccrs.logistics.fleet.order.acceptance.model.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByRegionUuid(String regionUuid);
}
