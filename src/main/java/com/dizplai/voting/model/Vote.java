package com.dizplai.voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * An entity to store a vote cast.
 * 1. Poll is a poll the vote is associated
 * 2. Option is one of the option available for the poll question
 * 3. Cast_on is the date and time the vote was cast - useful for audit.
 */
@Entity(name = "vote")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Vote {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "poll", nullable = false) private long poll;
    @Column(name = "option", nullable = false) private String option;
    @Column(name = "cast_on", columnDefinition = "TIMESTAMP") private LocalDateTime castOn;
}
