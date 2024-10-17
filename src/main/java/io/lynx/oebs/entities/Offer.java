package io.lynx.oebs.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID offerId;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    private String name;

    private String description;

    private int duration; // Duration in minutes

    private BigDecimal price;

    @ElementCollection
    private List<String> tags;

    private boolean isAvailable = true;
}

