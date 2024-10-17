package io.lynx.oebs.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID tagId;

    @Column(unique = true)
    private String name; // e.g., "hair", "beauty", "salon"

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private List<Business> businesses;

    public Tag(String name) {
        this.name = name;
    }
}