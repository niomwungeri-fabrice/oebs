package io.lynx.oebs.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "businesses",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "owner_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID businessId;
    private String name;
    private String description;
    private String address;
    private String phoneNumber;
    private String email;
    private boolean isVerified = false;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Account owner;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "businesses_tags",
            joinColumns = @JoinColumn(name = "business_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Offer> offers;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "business_operating_hours",
            joinColumns = @JoinColumn(name = "business_id"),
            inverseJoinColumns = @JoinColumn(name = "operating_hours_id")
    )
    private List<OperatingHours> hoursOfOperation;
}
