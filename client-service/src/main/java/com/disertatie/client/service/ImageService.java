package com.disertatie.client.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.disertatie.client.dto.ResultDTO;
import com.disertatie.client.model.Client;
import com.disertatie.client.model.ClientEmotion;
import com.disertatie.client.model.ImageModel;
import com.disertatie.client.repository.ClientEmotionRepository;
import com.disertatie.client.repository.ClientRepository;
import com.disertatie.client.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.Principal;
import java.util.Base64;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@Slf4j
public class ImageService {

    private ImageRepository imageRepository;
    private ClientRepository clientRepository;
    private ClientEmotionRepository clientEmotionRepository;

    public ImageService(ImageRepository imageRepository, ClientRepository clientRepository, ClientEmotionRepository clientEmotionRepository) {
        this.imageRepository = imageRepository;
        this.clientRepository = clientRepository;
        this.clientEmotionRepository = clientEmotionRepository;
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

    public ResponseEntity<HttpStatus> upload(MultipartFile file, Principal principal) throws IOException {
        Client client = clientRepository.findByUsername(principal.getName());
        Optional<ImageModel> img = imageRepository.findByName(client.getUsername() + ".png");
        if (img.isPresent()) {
            img.get().setName(file.getOriginalFilename());
            img.get().setType(file.getContentType());
            img.get().setPicByte(compressBytes(file.getBytes()));
            img.get().setImgUrl(encodeImage(decompressBytes(img.get().getPicByte())));
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

    public String encodeImage(byte[] picByte) {
        return Base64.getUrlEncoder().encodeToString(picByte);
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
        return img;
    }

    public ResultDTO uploadImageToAwsS3(MultipartFile multipartFile, Principal principal) throws IOException {
        Client client = clientRepository.findByUsername(principal.getName());
        OutputStream fileOutputStream = new FileOutputStream(client.getUsername() + ".png");

        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
        String fileName = client.getUsername() + ".png";
        String bucket = "atmfmibucket";
//        String key = "AKIA4VWJIBQY7TAGBE7I";
//        InputStream stream = new ByteArrayInputStream(multipartFile.getBytes());
        Regions clientRegion = Regions.EU_CENTRAL_1;
        File fileToUpload = new File(fileName);

        System.out.format("Uploading %s to S3 bucket %s...\n", fileToUpload, bucket);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(clientRegion).build();
        try {
            s3.putObject(bucket, fileName, fileToUpload);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        } catch (SdkClientException e) {
            e.printStackTrace();
        }

        String objUrl = getObjectUrl(bucket, fileName);
        runPythonScript(objUrl, principal);

        return new ResultDTO().setStatus(false).setMessage("Image uploaded to AWS!");
    }

    public String getObjectUrl(String bucket, String name) {
        AmazonS3Client s3Client = (AmazonS3Client) AmazonS3ClientBuilder.defaultClient();
        return s3Client.getResourceUrl(bucket, name);
    }

    public void runPythonScript(String objUrl, Principal principal) throws IOException {
        Client client = clientRepository.findByUsername(principal.getName());
        String ScriptPath = "/Users/cristianguta/Desktop/ATM-BACKEND/client-service/src/main/resources/python/face_analysis.py";
        String pythonPath = "/Users/cristianguta/Desktop/ATM-BACKEND/review-service/venv/bin/python3.9";

        ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, ScriptPath, objUrl);
        processBuilder.redirectErrorStream(true);
        Process p = processBuilder.start();

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String s = stdInput.readLine();
        ClientEmotion clientEmotion = new ClientEmotion();
        clientEmotion.setClient(client);
        if (s == null) {
            s = "N/A";
        }
        clientEmotion.setEmotion(s);
        clientEmotionRepository.save(clientEmotion);
    }
}