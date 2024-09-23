package org.dreamdevwork.shop.dto;

import lombok.Data;

@Data
public class ImageDto {
    private Long id;
    private String fileName;
    private String fileType;
    private String dowloadUrl;
}
