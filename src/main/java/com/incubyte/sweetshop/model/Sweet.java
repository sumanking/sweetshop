package com.incubyte.sweetshop.model;

import java.util.Base64;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Entity
@Table(name = "sweets")
public class Sweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private double price;
    private int quantity;

    // Stored in DB
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    // Used ONLY for form binding (NOT stored)
    @Transient
    private MultipartFile imageFile;

    @Transient
    public String getImageBase64() {
        if (image == null) return null;
        return Base64.getEncoder().encodeToString(image);
    }
}
