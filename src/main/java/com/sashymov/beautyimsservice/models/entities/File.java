package com.sashymov.beautyimsservice.models.entities;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "download_uri")
    private String fileDownloadUri;
    @Column(name = "file_type")
    private String fileType;
    @Column(name = "file_size")
    private long size;
}