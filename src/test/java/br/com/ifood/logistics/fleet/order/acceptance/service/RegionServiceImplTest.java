package br.com.ccrs.logistics.fleet.order.acceptance.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import br.com.ccrs.logistics.fleet.order.acceptance.exception.RegionNotFoundException;
import br.com.ccrs.logistics.fleet.order.acceptance.model.Region;
import br.com.ccrs.logistics.fleet.order.acceptance.repository.RegionRepository;

public class RegionServiceImplTest {

    private RegionService regionService;
    private RegionRepository regionRepository;
    private GisService gisService;

    @Before
    public void before() {
        regionRepository = mock(RegionRepository.class);
        gisService = mock(GisService.class);

        when(regionRepository.findAll()).thenReturn(getRegions());
        when(gisService.getUuidFromGis(new ArrayList<>(getRegionsMap().keySet()), 1.0, 2.0)).thenReturn(Optional.of(UUID.fromString("62aa53af-6716-411a-9521-855588227b12")));
        when(regionRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        regionService = new RegionServiceImpl(regionRepository, gisService);
    }

    @Test
    public void updateSaturatedByRegionTest() {
        final Region region = new Region();
        region.setRegionUuid("a1");

        when(regionRepository.findByRegionUuid("a1")).thenReturn(Optional.of(region));
        regionService.updateSaturatedByRegion("a1", true);
        verify(regionRepository, times(1)).save(any(Region.class));
    }

    @Test(expected = RegionNotFoundException.class)
    public void updateSaturatedByRegionNotFoundTest() {
        regionService.updateSaturatedByRegion("a1", true);
    }

    @Test
    public void findByLongitudeLatitudeTest(){
        Region region = regionService.findByLongitudeLatitude(1.0, 2.0);
        Assert.assertEquals(region.getKml(), "62aa53af-6716-411a-9521-855588227b12");
    }

    @Test
    public void findByLongitudeLatitudeNotFoundTest(){
        Region region = regionService.findByLongitudeLatitude(2.0, 2.0);
        Assert.assertNull(region);
    }

    @Test
    public void shouldNormalizeOfflinePaymentByRegion() {
        final String regionUuid = "xx-yy-zz";
        final Region region = new Region();
        region.setSaturatedOfflinePayment(true);
        when(regionRepository.findByRegionUuid(regionUuid)).thenReturn(Optional.of(region));

        regionService.normalizeOfflinePaymentByRegion(regionUuid);

        verify(regionRepository, Mockito.times(1)).findByRegionUuid(regionUuid);
        final ArgumentCaptor<Region> argumentCaptor = ArgumentCaptor.forClass(Region.class);
        verify(regionRepository, times(1)).save(argumentCaptor.capture());
        assertFalse(argumentCaptor.getValue().getSaturatedOfflinePayment());
    }

    @Test
    public void shouldSaturateOfflinePaymentByRegion() {
        final String regionUuid = "xx-yy-zz";
        final Region region = new Region();
        region.setSaturatedOfflinePayment(false);
        when(regionRepository.findByRegionUuid(regionUuid)).thenReturn(Optional.of(region));

        regionService.saturateOfflinePaymentByRegion(regionUuid);

        verify(regionRepository, Mockito.times(1)).findByRegionUuid(regionUuid);
        final ArgumentCaptor<Region> argumentCaptor = ArgumentCaptor.forClass(Region.class);
        verify(regionRepository, times(1)).save(argumentCaptor.capture());
        assertTrue(argumentCaptor.getValue().getSaturatedOfflinePayment());
    }

    @Test(expected = RegionNotFoundException.class)
    public void shouldThrowExceptionWhenRegionNotFoundAndNormalizeOfflinePaymentByRegion() {
        when(regionRepository.findByRegionUuid(anyString())).thenReturn(Optional.empty());
        regionService.normalizeOfflinePaymentByRegion("any-uuid");
        regionService.enableOfflinePaymentByRegion("any-uuid");
    }

    @Test(expected = RegionNotFoundException.class)
    public void shouldThrowExceptionWhenRegionNotFoundAndEnablingOfflinePayment() {
        when(regionRepository.findByRegionUuid(anyString())).thenReturn(Optional.empty());
        regionService.enableOfflinePaymentByRegion("any-uuid");
    }

    @Test
    public void shouldEnableOfflinePayment() {
        final String regionUuid = "xx-yy-zz";
        final Region region = new Region();
        region.setOfflinePaymentEnabled(false);
        when(regionRepository.findByRegionUuid(regionUuid)).thenReturn(Optional.of(region));

        regionService.enableOfflinePaymentByRegion(regionUuid);

        verify(regionRepository, Mockito.times(1)).findByRegionUuid(regionUuid);
        final ArgumentCaptor<Region> argumentCaptor = ArgumentCaptor.forClass(Region.class);
        verify(regionRepository, times(1)).save(argumentCaptor.capture());
        assertTrue(argumentCaptor.getValue().getOfflinePaymentEnabled());
    }

    @Test
    public void shouldDisableOfflinePayment() {
        final String regionUuid = "xx-yy-zz";
        final Region region = new Region();
        region.setOfflinePaymentEnabled(true);
        when(regionRepository.findByRegionUuid(regionUuid)).thenReturn(Optional.of(region));

        regionService.disableOfflinePaymentByRegion(regionUuid);

        verify(regionRepository, Mockito.times(1)).findByRegionUuid(regionUuid);
        final ArgumentCaptor<Region> argumentCaptor = ArgumentCaptor.forClass(Region.class);
        verify(regionRepository, times(1)).save(argumentCaptor.capture());
        assertFalse(argumentCaptor.getValue().getOfflinePaymentEnabled());
    }

    private Region getRegion(Long id, String uuId, String kml){
        Region region = new Region();
        region.setRegionUuid(uuId);
        region.setId(id);
        region.setKml(kml);
        return region;
    }

    private List<Region> getRegions(){
        List<Region> regions = new ArrayList<>();
        regions.add(getRegion(1L, "62aa53af-6716-411a-9521-855588227b12", "62aa53af-6716-411a-9521-855588227b12"));
        regions.add(getRegion(2L, "1e6e76d5-65c3-4c67-8cb8-22d682a826c0", "1e6e76d5-65c3-4c67-8cb8-22d682a826c0"));
        regions.add(getRegion(3L, "b1ee5c0e-7cf9-44b0-97d0-1e813f6066dd", "b1ee5c0e-7cf9-44b0-97d0-1e813f6066dd"));
        return regions;
    }

    private Map<UUID, Region> getRegionsMap(){
        return getRegions().stream()
                .collect(Collectors.toMap(Region::getKmlAsUuid,
                        Function.identity()));
    }
}
