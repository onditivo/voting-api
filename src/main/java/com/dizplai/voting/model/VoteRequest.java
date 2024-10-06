package com.dizplai.voting.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * An input payload for casting a vote.
 * 1. voteCast is the value of the selected option from one of the available ones.
 * Example:
 *      {"voteCast": "Arsenal"}
 * @param voteCast
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record VoteRequest(@NotNull String voteCast) {
}
