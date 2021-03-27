package com.disertatie.apigateway.controller;

import com.disertatie.apigateway.model.ImageModel;
import com.disertatie.apigateway.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/check")
public class TestController {

    @Autowired
    ImageRepository imageRepository;

    @PostMapping(value = "/upload", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ImageModel uploadImage(@RequestParam("myFile") MultipartFile file) throws IOException {
        ImageModel img = new ImageModel(file.getOriginalFilename(), file.getContentType(), file.getBytes());
        final ImageModel savedImage = imageRepository.save(img);
        System.out.println("Image saved");
        return savedImage;
    }
}
