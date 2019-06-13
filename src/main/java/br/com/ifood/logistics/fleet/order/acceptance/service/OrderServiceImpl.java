package br.com.ccrs.logistics.fleet.order.acceptance.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ccrs.logistics.fleet.order.acceptance.controller.dto.OrderDTO;
import br.com.ccrs.logistics.fleet.order.acceptance.exception.OrderNotFoundException;
import br.com.ccrs.logistics.fleet.order.acceptance.model.Order;
import br.com.ccrs.logistics.fleet.order.acceptance.repository.OrderRepository;
import br.com.ccrs.logistics.fleet.order.acceptance.service.conditions.ConditionDTO;
import br.com.ccrs.logistics.fleet.order.acceptance.service.conditions.RejectCondition;
import br.com.ccrs.logistics.fleet.order.acceptance.util.TransactionUtils;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = getLogger(OrderServiceImpl.class);
    private static final String TENANT_IDENTIFIER_KEY = "tenant-identifier";
    private static final String UUID_KEY = "order-uuid";
    private static final String API_KEY = "api-key";

    private final RegionService regionServiceImpl;
    private final List<RejectCondition> conditions;
    private final OrderRepository orderRepository;
    private final TrackableSourceService trackableSourceService;
    private final SNSService snsService;
    private final String orderCreateTopicName;

    @Autowired
    public OrderServiceImpl(final RegionService regionServiceImpl, final OrderRepository orderRepository,
            final SNSService snsService, final TrackableSourceService trackableSourceService,
            @Value("${sns.order.create.queue.name:}") final String orderCreateTopicName,
            final List<RejectCondition> conditions) {

        this.regionServiceImpl = regionServiceImpl;
        this.trackableSourceService = trackableSourceService;
        this.snsService = snsService;
        this.orderRepository = orderRepository;
        this.orderCreateTopicName = orderCreateTopicName;
        this.conditions = conditions;
    }

    @Override
    public boolean acceptance(final OrderDTO orderDTO) {
        final ConditionDTO conditionDTO = new ConditionDTO();
        conditionDTO.setOrderDTO(orderDTO);
        conditionDTO.setOrigin(regionServiceImpl.findByLongitudeLatitude(orderDTO.getOrigin().getLongitude(),
            orderDTO.getOrigin().getLatitude()));
        return conditions.stream().allMatch(rejectCondition -> rejectCondition.acceptance(conditionDTO));
    }

    @Override
    @Transactional
    public String createOrder(final String countryCode, final String rawOrder, final String apiKey,
                              final OrderDTO orderDTO) {
        final String externalId = orderDTO.getExternalId();
        final String tenantIdentifier = orderDTO.getTenantIdentifier();
        final Optional<Order> orderOptional =
            orderRepository.findOneByExternalIdAndTenantIdentifier(externalId, tenantIdentifier);
        final Order order = orderOptional.orElseGet(() -> createNewOrder(orderDTO, rawOrder));

        logger.debug("Order created as ACCEPTED {}", order.getOrderUuid());

        TransactionUtils.afterCommit(() -> {
            final var messageAttributes = Map.of(UUID_KEY, order.getOrderUuid(),
                    TENANT_IDENTIFIER_KEY, countryCode.toLowerCase(),
                    API_KEY, apiKey);
            snsService.publish(orderCreateTopicName, rawOrder, messageAttributes);
            logger.debug("Order {} sent to {}", order.getOrderUuid(), orderCreateTopicName);
        });

        return order.getOrderUuid();
    }

    private Order createNewOrder(final OrderDTO orderDTO, final String rawOrder) {
        final String uuid = UUID.randomUUID().toString();
        final Order order = new Order();
        order.setOrderUuid(uuid);
        order.setAcceptedDate(ZonedDateTime.now(ZoneId.systemDefault()));
        order.setExternalId(orderDTO.getExternalId());
        order.setJson(rawOrder);
        order.setPaymentType(orderDTO.getPaymentType());
        order.setRegion(regionServiceImpl.findByLongitudeLatitude(orderDTO.getOrigin().getLongitude(),
            orderDTO.getOrigin().getLatitude()));
        order.setStatus(Order.OrderStatus.ACCEPTED);
        order.setTenantIdentifier(orderDTO.getTenantIdentifier());
        order.setTrackableSource(trackableSourceService.findByName(orderDTO.getTrackableSourceName()));
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void updateOrderStatusToCreated(final String orderUuid, final ZonedDateTime createdDate) {
        final Optional<Order> orderOptional = orderRepository.findOneByOrderUuid(orderUuid);
        if (orderOptional.isPresent()) {
            final Order order = orderOptional.get();
            order.setStatus(Order.OrderStatus.CREATED);
            order.setCreatedDate(createdDate);
            orderRepository.save(order);
        } else {
            logger.info("Cannot find an Order to update status to create, orderUuid: {}", orderUuid);
        }
    }

    @Override
    @Transactional
    public void giveUpOrder(final String orderUUid) {
        final Optional<Order> orderOptional = orderRepository.findOneByOrderUuid(orderUUid);

        if (orderOptional.isPresent()) {
            final Order order = orderOptional.get();
            order.setStatus(Order.OrderStatus.GAVE_UP);
            orderRepository.save(order);
            logger.debug("Give-Up order {}", orderUUid);
        } else {
            logger.info("Cannot find an Order to update status to give-up, orderUuid: {}", orderUUid);
        }
    }

}
