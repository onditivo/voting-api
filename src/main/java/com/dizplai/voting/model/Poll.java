package com.dizplai.voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * An entity to represent a poll. A poll has an id and a poll question
 * The column value cannot be null.
 */
@Entity(name = "poll")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Poll {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "question", nullable = false) String question;
}
