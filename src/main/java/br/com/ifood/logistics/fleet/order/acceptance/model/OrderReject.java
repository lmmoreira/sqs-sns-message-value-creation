package br.com.ccrs.logistics.fleet.order.acceptance.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "ORDER_REJECT")
public class OrderReject implements Serializable {

    private static final long serialVersionUID = 929221763035190940L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EXTERNAL_ID")
    private String externalId;

    @Column(name = "TENANT_IDENTIFIER")
    private String tenantIdentifier;

    @Column(name = "REASON")
    @Enumerated(EnumType.STRING)
    private RejectReason reason;

    @Column(name = "REJECTED_DATE")
    private LocalDateTime rejectedDate = LocalDateTime.now();

    public enum RejectReason {
        SATURATED_REGION, SATURATED_REGION_PAYMENT_OFFLINE, PAYMENT_OFFLINE, PAYMENT_CASH, REGION_MISMATCH, NO_REGION;
    }
}
