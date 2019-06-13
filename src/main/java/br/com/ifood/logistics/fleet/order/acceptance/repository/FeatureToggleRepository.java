package br.com.ccrs.logistics.fleet.order.acceptance.repository;

import br.com.ccrs.logistics.fleet.order.acceptance.model.FeatureToggle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureToggleRepository extends JpaRepository<FeatureToggle, FeatureToggle.FeatureToggleName> {
}
