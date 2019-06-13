package br.com.ccrs.logistics.fleet.order.acceptance.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.deliverypf.gis.sdk.PolygonSdkService;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import br.com.ccrs.logistics.fleet.order.acceptance.AbstractIntTest;
import br.com.ccrs.logistics.fleet.order.acceptance.model.Region;
import br.com.ccrs.logistics.fleet.order.acceptance.repository.RegionRepository;

public class RegionServiceImplCacheTest extends AbstractIntTest {

    @Autowired
    RegionService regionServiceImpl;

    @MockBean
    private PolygonSdkService polygonSdkService;

    @MockBean
    private RegionRepository regionRepository;

    @Before
    public void before() {
        given(polygonSdkService.contains(new ArrayList<>(getRegionsMap().keySet()), 1.0, 1.0))
                .willReturn(Optional.of(UUID.fromString("62aa53af-6716-411a-9521-855588227b12")));
        given(polygonSdkService.contains(new ArrayList<>(getRegionsMap().keySet()), 2.0, 2.0))
                .willReturn(Optional.of(UUID.fromString("1e6e76d5-65c3-4c67-8cb8-22d682a826c0")));
        given(regionRepository.findAll()).willReturn(getRegions());
    }

    @Test
    public void findByLongitudeLatitudeCacheCalledOnceTest() {
        regionServiceImpl.findByLongitudeLatitude(1.0, 1.0);
        regionServiceImpl.findByLongitudeLatitude(1.0, 1.0);
        regionServiceImpl.findByLongitudeLatitude(1.0, 1.0);
        verify(polygonSdkService, times(1)).contains(new ArrayList<>(getRegionsMap().keySet()), 1.0, 1.0);
    }

    private Region getRegion(Long id, String uuId, String kml) {
        Region region = new Region();
        region.setRegionUuid(uuId);
        region.setId(id);
        region.setKml(kml);
        return region;
    }

    private List<Region> getRegions() {
        List<Region> regions = new ArrayList<>();
        regions.add(getRegion(1L, "62aa53af-6716-411a-9521-855588227b12", "62aa53af-6716-411a-9521-855588227b12"));
        regions.add(getRegion(2L, "1e6e76d5-65c3-4c67-8cb8-22d682a826c0", "1e6e76d5-65c3-4c67-8cb8-22d682a826c0"));
        regions.add(getRegion(3L, "b1ee5c0e-7cf9-44b0-97d0-1e813f6066dd", "b1ee5c0e-7cf9-44b0-97d0-1e813f6066dd"));
        return regions;
    }

    private Map<UUID, Region> getRegionsMap() {
        return getRegions().stream().collect(Collectors.toMap(Region::getKmlAsUuid, Function.identity()));
    }

}
