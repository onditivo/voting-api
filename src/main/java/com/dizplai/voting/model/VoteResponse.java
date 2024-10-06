package com.dizplai.voting.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

/**
 * A payload for all the ballot associated with a poll. It is a list of all the votes cast.
 * Example:
 *      {
 *     "votes": [
 *         {
 *             "poll": 1,
 *             "option": "Manchester City",
 *             "castOn": "2024-10-06T12:24:41.154181"
 *         },
 *         {
 *             "poll": 1,
 *             "option": "Arsenal",
 *             "castOn": "2024-10-06T12:24:41.154181"
 *         },
 *         {
 *             "poll": 1,
 *             "option": "Liverpool",
 *             "castOn": "2024-10-06T12:24:41.154181"
 *         }
 *     ]
 * }
 * @param votes
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record VoteResponse(List<Vote> votes) {
}
