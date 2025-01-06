package com.master.faez.telbot.models;

import com.master.faez.telbot.constants.USER_ROLE;
import com.master.faez.telbot.state.USER_STATES;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
public class User {
    @Id
    Long id;
    String name;
    String lastName;
    String username;
    @Enumerated(EnumType.STRING)
    @Column(length = 512)
    USER_ROLE USER_ROLE;
}
