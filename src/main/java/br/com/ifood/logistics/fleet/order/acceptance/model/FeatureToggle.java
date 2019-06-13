package br.com.ccrs.logistics.fleet.order.acceptance.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
@ToString
@Entity
@Table(name = "FEATURE_TOGGLE")
public class FeatureToggle {

    @Id
    @Enumerated(EnumType.STRING)
    private FeatureToggleName name;

    @NotNull
    @Column(name = "TOGGLED", nullable = false)
    private Boolean toggled;


    public enum FeatureToggleName {
        ORDER_CREATE_MASTER_SWITCH
    }
}
