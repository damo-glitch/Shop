package org.dreamdevwork.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.dreamdevwork.shop.dto.ImageDto;
import org.dreamdevwork.shop.exceptions.ResourceNotFoundException;
import org.dreamdevwork.shop.models.Image;
import org.dreamdevwork.shop.models.Product;
import org.dreamdevwork.shop.repositories.ImageRepository;
import org.dreamdevwork.shop.services.IImageService;
import org.dreamdevwork.shop.services.IProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;

    private final IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no imag found with this id "+ id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id)
                .ifPresentOrElse(imageRepository ::delete,
                        () -> {
                    throw new ResourceNotFoundException("no imag found with this id "+ id);
                });
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long idProduct) {
        Product product = productService.getProductById(idProduct);
        List<ImageDto> saveImageDtos = new ArrayList<>();
        for (MultipartFile file : files){
            try{
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buidDownload = "api/v1/images/image/dowload/";
                String downloadUrl = buidDownload + image.getId();
                image.setDownloadUrl(downloadUrl);
                Image saveImage = imageRepository.save(image);

                saveImage.setDownloadUrl(buidDownload + saveImage.getId());
                imageRepository.save(saveImage);


                ImageDto imageDto = new ImageDto();
                imageDto.setFileName(saveImage.getFileName());
                imageDto.setFileType(saveImage.getFileType());
                imageDto.setDowloadUrl(saveImage.getDownloadUrl());
                saveImageDtos.add(imageDto);
            }catch (IOException | SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return saveImageDtos;
    }


    @Override
    public void updateImage(MultipartFile file, Long idImage) {
        Image image = getImageById(idImage);
        try{
            image.setFileName(file.getOriginalFilename());
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        }catch (IOException | SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
