package com.master.faez.telbot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(length = 65535,unique=true)
    String fileName;
    Double size;
    String fileId;
    @ManyToOne
    @JsonIgnore
    Resource resource;
}
