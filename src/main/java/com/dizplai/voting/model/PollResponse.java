package com.dizplai.voting.model;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Contains a poll's detail - the object created by calling new poll endpoint.
 * Example:
 * {
 *     "id": 1,
 *     "question": "Who will win the Premier League?",
 *     "options": [
 *         "Arsenal",
 *         "Liverpool",
 *         "Manchester City"
 *     ]
 * }
 * @param id
 * @param question
 * @param options
 */
public record PollResponse(@NotNull long id, @NotNull String question, List<String> options) {
}
