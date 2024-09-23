package org.dreamdevwork.shop.controllers;

import lombok.AllArgsConstructor;
import org.dreamdevwork.shop.dto.ImageDto;
import org.dreamdevwork.shop.exceptions.ResourceNotFoundException;
import org.dreamdevwork.shop.models.Image;
import org.dreamdevwork.shop.response.ApiResponse;
import org.dreamdevwork.shop.services.IImageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@AllArgsConstructor
@RequestMapping("${api.prefix}images")
public class ImageController {
    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long idProduit){
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files, idProduit);
            return ResponseEntity.ok(new ApiResponse("Uploaded success", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Upload failed", e.getMessage()));
        }
    }

    @GetMapping("/image/download/{idImage}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long idImage ) throws SQLException {
        Image image = imageService.getImageById(idImage);
        ByteArrayResource resource = new ByteArrayResource(
                image.getImage().getBytes(1, (int) image.getImage().length())
        );
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename-\""+image.getFileName()+"\"")
                .body(resource);
    }


    @PutMapping("/image/{idImage}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long idImage, @RequestBody MultipartFile file){
        try {
            Image image = imageService.getImageById(idImage);
            if(image != null){
                imageService.updateImage(file, idImage);
                return ResponseEntity.ok(new ApiResponse("Update success", null));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed", INTERNAL_SERVER_ERROR));
    }
    @DeleteMapping("/image/{idImage}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long idImage){
        try {
            Image image = imageService.getImageById(idImage);
            if(image != null){
                imageService.deleteImageById(idImage);
                return ResponseEntity.ok(new ApiResponse("Delete success", null));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete failed", INTERNAL_SERVER_ERROR));
    }
}
