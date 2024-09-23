package org.dreamdevwork.shop.services;

import org.dreamdevwork.shop.dto.ImageDto;
import org.dreamdevwork.shop.models.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> files, Long idProduct);
    void updateImage(MultipartFile file, Long idImage);
}
