package com.master.faez.telbot.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@ToString
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    int id;
    @JsonProperty("name")
    @Column(unique = true, nullable = false)
    String name;
    public Book() {
    }

    @JsonCreator
    public Book(@JsonProperty("id") int id, @JsonProperty("name") String name) {
        this.name = name;
        this.id = id;
    }
    @OneToMany(mappedBy = "book")
    @JsonIgnore
    List<Resource> resources;

}
