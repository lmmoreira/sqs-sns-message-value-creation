package br.com.ccrs.logistics.fleet.order.acceptance.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "TRACKABLE_SOURCE")
public class TrackableSource implements Serializable {

    private static final long serialVersionUID = 8857888484263223007L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;
}
