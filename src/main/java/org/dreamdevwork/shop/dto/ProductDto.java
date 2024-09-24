package org.dreamdevwork.shop.dto;

import lombok.Data;
import org.dreamdevwork.shop.models.Category;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private Integer inventory;
    private String description;
    private Category category;
    private List<ImageDto> imageDto;
}
