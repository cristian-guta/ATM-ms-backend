package com.disertatie.client.controller;

import com.disertatie.client.dto.ResultDTO;
import com.disertatie.client.model.ImageModel;
import com.disertatie.client.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/image")
public class ImageController {

    private ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<HttpStatus> uploadImage(@RequestParam("imageFile") MultipartFile file, Principal principal) throws IOException {
        return imageService.upload(file, principal);
    }

    @GetMapping(path = {"/get"})
    public ImageModel getImage(Principal principal) {
        return imageService.getImage(principal);
    }

    @PostMapping(value = "/aws", consumes = "multipart/form-data")
    public ResultDTO uploadImageToAwsS3(@RequestParam("imageFile") MultipartFile file, Principal principal) throws IOException {
        return imageService.uploadImageToAwsS3(file, principal);
    }
}
