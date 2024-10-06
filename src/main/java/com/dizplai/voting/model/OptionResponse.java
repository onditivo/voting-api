package com.dizplai.voting.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

/**
 * A response from a call to vote distribution to a poll. e.g.
 * [
 *     {
 *         "option": "Liverpool",
 *         "count": "23.08%",
 *         "poll": "1"
 *     },
 *     {
 *         "option": "Manchester City",
 *         "count": "76.92%",
 *         "poll": "1"
 *     },
 *     {
 *         "option": "Arsenal",
 *         "count": "0%",
 *         "poll": "1"
 *     }
 * ]
 * @param option
 * @param count
 * @param poll
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record OptionResponse(String option, String count, String poll) {
}
