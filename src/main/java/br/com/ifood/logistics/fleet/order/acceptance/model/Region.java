package br.com.ccrs.logistics.fleet.order.acceptance.model;

import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "REGION", //
       indexes = {@Index(name = "idx_region_region_uuid", columnList = "REGION_UUID", unique = true)})
@DynamicUpdate
public class Region implements Serializable {

    private static final long serialVersionUID = 7754163269959391861L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "REGION_UUID", nullable = false, length = 36)
    private String regionUuid;

    @Column(name = "NAME")
    private String name;

    @Column(name = "KML")
    private String kml;

    @Column(name = "SATURATED")
    private Boolean saturated = false;

    @Column(name = "SATURATED_OFFLINE_PAYMENT")
    private Boolean saturatedOfflinePayment = false;

    @Column(name = "OFFLINE_PAYMENT_ENABLED")
    private Boolean offlinePaymentEnabled = false;

    @Column(name = "ACCEPTANCE_TOGGLE")
    private Boolean acceptanceToggle = false;

    @Column(name = "CREATE_TOGGLE")
    private Boolean createToggle = false;

    @Version
    @NotNull
    @Column(name = "VERSION", nullable = false)
    private Long version;

    public UUID getKmlAsUuid() {
        return UUID.fromString(this.kml);
    }

}
