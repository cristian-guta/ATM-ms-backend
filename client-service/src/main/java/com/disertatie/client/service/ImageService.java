package com.disertatie.client.service;

import com.disertatie.client.model.Client;
import com.disertatie.client.model.ImageModel;
import com.disertatie.client.repository.ClientRepository;
import com.disertatie.client.repository.ImageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageService {

    private ImageRepository imageRepository;
    private ClientRepository clientRepository;

    public ImageService(ImageRepository imageRepository, ClientRepository clientRepository) {
        this.imageRepository = imageRepository;
        this.clientRepository = clientRepository;
    }

    public ResponseEntity<HttpStatus> upload(MultipartFile file, Principal principal) throws IOException {
        Client client = clientRepository.findByUsername(principal.getName());
        Optional<ImageModel> img = imageRepository.findByName(client.getUsername() + ".png");

        if (img.isPresent()) {
            img.get().setName(file.getOriginalFilename());
            img.get().setType(file.getContentType());
            img.get().setPicByte(compressBytes(file.getBytes()));
            imageRepository.save(img.get());
        } else {
            ImageModel image = new ImageModel()
                    .setName(file.getOriginalFilename())
                    .setType(file.getContentType())
                    .setPicByte(file.getBytes());
            imageRepository.save(image);
        }

        client.setImageModelId(imageRepository.findByName(client.getUsername() + ".png").get().getId());
        clientRepository.save(client);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ImageModel getImage(Principal principal) {
        Client client = clientRepository.findByUsername(principal.getName());
        final Optional<ImageModel> retrievedImage = imageRepository.findById(client.getImageModelId());
        ImageModel img = new ImageModel();
        if (retrievedImage.isPresent()) {
            img.setName(retrievedImage.get().getName());
            img.setType(retrievedImage.get().getType());
            img.setPicByte(decompressBytes(retrievedImage.get().getPicByte()));
        }
        System.out.println(img.getName());
        return img;
    }

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
        return outputStream.toByteArray();
    }

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
        } catch (IOException | DataFormatException ioe) {
        }
        return outputStream.toByteArray();
    }
}
