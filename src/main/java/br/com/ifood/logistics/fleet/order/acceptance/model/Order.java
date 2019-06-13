package br.com.ccrs.logistics.fleet.order.acceptance.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "ORDER_DELIVERY",
       indexes = {@Index(name = "idx_order_delivery_order_uuid", columnList = "ORDER_UUID", unique = true)},
       uniqueConstraints = {@UniqueConstraint(name = "unique_order_delivery_external_id_tenant_identifier",
                                              columnNames = {"EXTERNAL_ID", "TENANT_IDENTIFIER"})})
@DynamicUpdate
public class Order implements Serializable {

    private static final long serialVersionUID = -8925112225380148533L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "ORDER_UUID", nullable = false, length = 36)
    private String orderUuid;

    @Column(name = "EXTERNAL_ID")
    private String externalId;

    @Column(name = "TENANT_IDENTIFIER")
    private String tenantIdentifier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TRACKABLE_SOURCE_ID")
    private TrackableSource trackableSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGION_ID")
    private Region region;

    @Column(name = "PAYMENT_TYPE")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "ACCEPTED_DATE")
    private ZonedDateTime acceptedDate;

    @Column(name = "CREATED_DATE")
    private ZonedDateTime createdDate;

    @Column(name = "JSON")
    private String json;

    public enum OrderStatus {
        ACCEPTED, CREATED, GAVE_UP
    }

}
