package br.com.ccrs.logistics.fleet.order.acceptance.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.ccrs.logistics.fleet.order.acceptance.model.OrderReject;
import br.com.ccrs.logistics.fleet.order.acceptance.repository.OrderRejectRepository;

public class OrderRejectServiceImplTest {

    private OrderRejectService orderRejectServiceImpl;
    private OrderRejectRepository orderRejectRepository;
    private OrderReject orderReject;

    @Before
    public void before() {
        orderReject = new OrderReject();

        orderRejectRepository = mock(OrderRejectRepository.class);
        when(orderRejectRepository.save(any(OrderReject.class))).thenReturn(orderReject);
        orderRejectServiceImpl = new OrderRejectServiceImpl(orderRejectRepository);
    }

    @Test
    public void createOrderRejection(){
        Assert.assertEquals(orderRejectServiceImpl.createOrderRejection("", OrderReject.RejectReason.SATURATED_REGION,
                ""), orderReject);
    }

}
