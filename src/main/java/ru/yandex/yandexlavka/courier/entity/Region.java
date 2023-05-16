package ru.yandex.yandexlavka.courier.entity;

import jakarta.validation.constraints.Positive;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * @author Oleg Khilko
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "regions")
public class Region {

    public Region(Integer regionId) {
        this.regionId = regionId;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Positive
    @Column(name = "region_id")
    private Integer regionId;

    @Builder.Default
    @ToString.Exclude
    @ManyToMany(mappedBy = "regions", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Courier> regions = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        var region = (Region) o;

        if (!Objects.equals(id, region.id)) return false;
        if (!Objects.equals(regionId, region.regionId)) return false;

        return Objects.equals(regions, region.regions);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
