package io.lynx.oebs.dtos;

import io.lynx.oebs.entities.Offer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateOfferRequest {

    private String name;
    private String description;
    private int duration;
    private BigDecimal price;
    private List<String> tags;

    public Offer toOffer() {
        return Offer.builder()
                .name(name)
                .description(description)
                .duration(duration)
                .price(price)
                .tags(tags)
                .build();
    }
}
