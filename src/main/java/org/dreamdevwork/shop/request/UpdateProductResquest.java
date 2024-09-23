package org.dreamdevwork.shop.request;

import lombok.Data;
import org.dreamdevwork.shop.models.Category;

import java.math.BigDecimal;

@Data
public class UpdateProductResquest {
    private String name;
    private String brand;
    private BigDecimal price;
    private Integer inventory;
    private String description;
    private Category category;
}
