package br.com.ccrs.logistics.fleet.order.acceptance.service;

import static org.apache.commons.lang3.StringUtils.EMPTY;
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

import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import br.com.ccrs.logistics.fleet.order.acceptance.controller.dto.LocalityDTO;
import br.com.ccrs.logistics.fleet.order.acceptance.controller.dto.OrderDTO;
import br.com.ccrs.logistics.fleet.order.acceptance.model.Order;
import br.com.ccrs.logistics.fleet.order.acceptance.model.OrderReject;
import br.com.ccrs.logistics.fleet.order.acceptance.model.PaymentType;
import br.com.ccrs.logistics.fleet.order.acceptance.model.Region;
import br.com.ccrs.logistics.fleet.order.acceptance.repository.OrderRepository;
import br.com.ccrs.logistics.fleet.order.acceptance.service.conditions.NoRegionConditionImpl;
import br.com.ccrs.logistics.fleet.order.acceptance.service.conditions.OfflineConditionImpl;
import br.com.ccrs.logistics.fleet.order.acceptance.service.conditions.OriginAndDestinationConditionImpl;
import br.com.ccrs.logistics.fleet.order.acceptance.service.conditions.RejectCondition;
import br.com.ccrs.logistics.fleet.order.acceptance.service.conditions.SaturatedConditionImpl;
import br.com.ccrs.logistics.fleet.order.acceptance.service.conditions.SaturatedOfflineConditionImpl;

public class OrderServiceImplUnitTest {

    private OrderService orderServiceImpl;
    private RegionService regionServiceImpl;
    private OrderRejectService orderRejectServiceImpl;
    private OrderRepository orderRepository;

    @Before
    public void before() {
        regionServiceImpl = mock(RegionService.class);

        orderRepository = mock(OrderRepository.class);
        when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        final SNSService snsService = mock(SNSService.class);
        final TrackableSourceService trackableSourceService = mock(TrackableSourceService.class);
        final String queueName = "";

        orderRejectServiceImpl = mock(OrderRejectService.class);
        final RejectCondition saturatedConditionImpl = new SaturatedConditionImpl(orderRejectServiceImpl, regionServiceImpl);
        final RejectCondition originAndDestinationConditionImpl = new OriginAndDestinationConditionImpl(orderRejectServiceImpl, regionServiceImpl);
        final RejectCondition offlineConditionImpl = new OfflineConditionImpl(orderRejectServiceImpl, regionServiceImpl);
        final RejectCondition noRegionConditionImpl = new NoRegionConditionImpl(orderRejectServiceImpl, regionServiceImpl);
        final RejectCondition saturatedOfflineConditionImpl = new SaturatedOfflineConditionImpl(orderRejectServiceImpl, regionServiceImpl);

        List<RejectCondition> conditions = new LinkedList<>();
        conditions.add(noRegionConditionImpl);
        conditions.add(saturatedConditionImpl);
        conditions.add(originAndDestinationConditionImpl);
        conditions.add(offlineConditionImpl);
        conditions.add(saturatedOfflineConditionImpl);

        orderServiceImpl = new OrderServiceImpl(regionServiceImpl, orderRepository,
                snsService,
                trackableSourceService,
                queueName,
                conditions);
    }

    @Test
    public void acceptanceAllMatchTest() {
        when(regionServiceImpl.findByLongitudeLatitude(any(), any())).thenReturn(getRegion(1L,false, true, false));

        final OrderDTO orderDTO = getOrderDTO("OWN_DELIVERY");
        Assert.assertTrue(orderServiceImpl.acceptance(orderDTO));
    }

    @Test
    public void acceptanceSaturatedErrorMatchTest() {
        when(regionServiceImpl.findByLongitudeLatitude(any(), any())).thenReturn(getRegion(1L,true, true, false));

        final OrderDTO orderDTO = getOrderDTO("OWN_DELIVERY");
        Assert.assertFalse(orderServiceImpl.acceptance(orderDTO));
        verify(orderRejectServiceImpl, times(1)).createOrderRejection(orderDTO.getExternalId(), OrderReject.RejectReason.SATURATED_REGION,
                orderDTO.getTenantIdentifier());
    }

    @Test
    public void acceptanceOriginDestinationErrorMatchTest() {
        when(regionServiceImpl.findByLongitudeLatitude(1.0, 1.0)).thenReturn(getRegion(1L,false, true, false));
        when(regionServiceImpl.findByLongitudeLatitude(2.0, 2.0)).thenReturn(getRegion(2L,false, true, false));

        final OrderDTO orderDTO = getOrderDTO("HYBRID");
        Assert.assertFalse(orderServiceImpl.acceptance(orderDTO));
        verify(orderRejectServiceImpl, times(1)).createOrderRejection(orderDTO.getExternalId(), OrderReject.RejectReason.REGION_MISMATCH,
                orderDTO.getTenantIdentifier());
    }

    @Test
    public void acceptanceOfflineErrorMatchTest() {
        when(regionServiceImpl.findByLongitudeLatitude(any(), any())).thenReturn(getRegion(1L,false, false, false));

        final OrderDTO orderDTO = getOrderDTO("ON_DEMAND");
        Assert.assertFalse(orderServiceImpl.acceptance(orderDTO));
        verify(orderRejectServiceImpl, times(1)).createOrderRejection(orderDTO.getExternalId(), OrderReject.RejectReason.PAYMENT_OFFLINE,
                orderDTO.getTenantIdentifier());
    }

    @Test
    public void acceptanceOfflineSaturatedErrorMatchTest() {
        when(regionServiceImpl.findByLongitudeLatitude(any(), any())).thenReturn(getRegion(1L,false, true, true));

        final OrderDTO orderDTO = getOrderDTO("ON_DEMAND");
        Assert.assertFalse(orderServiceImpl.acceptance(orderDTO));
        verify(orderRejectServiceImpl, times(1)).createOrderRejection(orderDTO.getExternalId(), OrderReject.RejectReason.SATURATED_REGION_PAYMENT_OFFLINE,
                orderDTO.getTenantIdentifier());
    }

    @Test
    public void acceptanceNoRegionErrorMatchTest() {
        final OrderDTO orderDTO = getOrderDTO("ON_DEMAND");
        Assert.assertFalse(orderServiceImpl.acceptance(orderDTO));
        verify(orderRejectServiceImpl, times(1)).createOrderRejection(orderDTO.getExternalId(), OrderReject.RejectReason.NO_REGION,
                orderDTO.getTenantIdentifier());
    }

    @Test
    public void acceptanceNoRegionMarketPlaceMatchTest() {
        final OrderDTO orderDTO = getOrderDTO("MARKET_PLACE");
        Assert.assertTrue(orderServiceImpl.acceptance(orderDTO));
    }

    @Test
    public void shouldUpdateStatusWhenOrderCreatedEventReceived() {
        final Order order = new Order();
        order.setStatus(Order.OrderStatus.ACCEPTED);
        when(orderRepository.findOneByOrderUuid(anyString())).thenReturn(Optional.of(order));

        orderServiceImpl.updateOrderStatusToCreated("uuid-test", ZonedDateTime.now());

        final ArgumentCaptor<Order> argumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(1)).save(argumentCaptor.capture());
        Assert.assertEquals(Order.OrderStatus.CREATED, argumentCaptor.getValue().getStatus());
    }

    @Test
    public void shouldGiveUp() {
        final Order order = new Order();
        when(orderRepository.findOneByOrderUuid(anyString())).thenReturn(Optional.of(order));

        orderServiceImpl.giveUpOrder("uuid-test");

        final ArgumentCaptor<Order> argumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(1)).save(argumentCaptor.capture());
    }

    private OrderDTO getOrderDTO(final String orderType) {
        return new OrderDTO("trackable", "external", ZonedDateTime.now(), getOrigin(), getDestination(),
                PaymentType.CARD, 10L, 10L, orderType, Currency.getInstance("AUD"));
    }

    private LocalityDTO getOrigin() {
        return emptyLocalityDTO(1.0);
    }

    private LocalityDTO emptyLocalityDTO(final double v) {
        return new LocalityDTO(EMPTY,
                EMPTY,
                EMPTY,
                EMPTY,
                EMPTY,
                EMPTY,
                EMPTY,
                EMPTY,
                EMPTY,
                v,
                v,
                ZonedDateTime.now());
    }

    private LocalityDTO getDestination() {
        return emptyLocalityDTO(2.0);
    }

    private Region getRegion(final Long id, final boolean saturated, final boolean offlinePayment, final boolean saturatedOffline) {
        final Region region = new Region();
        region.setId(id);
        region.setSaturated(saturated);
        region.setOfflinePaymentEnabled(offlinePayment);
        region.setSaturatedOfflinePayment(saturatedOffline);
        return region;
    }

}
