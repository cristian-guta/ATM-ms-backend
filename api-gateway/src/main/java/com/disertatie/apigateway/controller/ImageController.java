package com.disertatie.apigateway.controller;

import com.disertatie.apigateway.model.Client;
import com.disertatie.apigateway.model.ImageModel;
import com.disertatie.apigateway.repository.ClientRepository;
import com.disertatie.apigateway.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/image")
public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ClientRepository clientRepository;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public BodyBuilder uploadImage(@RequestParam("imageFile") MultipartFile file, Principal principal) throws IOException {
        Client client = clientRepository.findByUsername(principal.getName());
        Optional<ImageModel> img = imageRepository.findByName(client.getUsername() + ".png");
        if (img.isPresent()) {
            imageRepository.delete(img.get());
        }
        img.get().setName(file.getOriginalFilename());
        img.get().setType(file.getContentType());
        img.get().setPicByte(compressBytes(file.getBytes()));

        imageRepository.save(img.get());
        client.setImageModelId(imageRepository.findByName(client.getUsername()+".png").get().getId());
        clientRepository.save(client);
        return ResponseEntity.status(HttpStatus.OK);
    }

    @GetMapping(path = {"/get"})
    public ImageModel getImage(Principal principal) throws IOException {
        Client client = clientRepository.findByUsername(principal.getName());
        final Optional<ImageModel> retrievedImage = imageRepository.findByName(client.getUsername() + ".png");
        ImageModel img = new ImageModel();
        img.setName(retrievedImage.get().getName());
        img.setType(retrievedImage.get().getType());
        img.setPicByte(decompressBytes(retrievedImage.get().getPicByte()));
        return img;
    }

    // compress the image bytes before storing it in the database
    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }
}
