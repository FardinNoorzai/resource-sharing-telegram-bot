package com.master.faez.telbot.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Builder
@ToString
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    int id;
    @JsonProperty("name")
    String name;

    @JsonCreator
    public Book(@JsonProperty("id") int id, @JsonProperty("name") String name) {
        this.name = name;
        this.id = id;
    }

}
