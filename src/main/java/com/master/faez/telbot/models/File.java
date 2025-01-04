package com.master.faez.telbot.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String fileType;
    @Column(length = 65535)
    String fileName;
    Double size;
    String fileId;
    @ManyToOne
    Resource resource;
}
