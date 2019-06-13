package br.com.ccrs.logistics.fleet.order.acceptance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import br.com.ccrs.logistics.fleet.order.acceptance.model.TrackableSource;

public interface TrackableSourceRepository extends JpaRepository<TrackableSource, Long> {
    Optional<TrackableSource> findByName(final String name);
}
