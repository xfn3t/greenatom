package com.caselab.greenatom.dto;

import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.*;

import java.time.ZonedDateTime;

@Data
@Entity
@Builder
@ToString
@Table(name = "t_file", schema = "private")
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    @Column(name = "file_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "creation_date", nullable = false, columnDefinition = "TIMESTAMPTZ DEFAULT timezone('Europe/Moscow', now())")
    private ZonedDateTime creationDate;

    @Column(name = "description")
    private String description;

    @Column(name = "file", nullable = false)
    @Description("file in base64")
    private String file;

    @PrePersist
    protected void onCreate() {
        this.creationDate = ZonedDateTime.now();
    }
}
