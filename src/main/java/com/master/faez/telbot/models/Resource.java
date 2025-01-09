package com.master.faez.telbot.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(unique = true, nullable = false)
    String name;

    @OneToMany(mappedBy = "resource", fetch = FetchType.EAGER)
    @JsonIgnore
    List<File> files;

    @JsonIgnore
    @ManyToOne
    Book book;
}
