package com.dizplai.voting.model;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * An input payload for creating a poll.
 * 1. Question - is a poll question e.g. 'Who will win the Premier League?'.
 *    A non-null constraint is applied to the input
 * 2. Options - a list of options available for the poll question e.g. 'Manchester City, Liverpool, Arsenal'
 *    A length condition is applied to the list of options =< length <=7
 *
 *    Example:
 *    {
 *          "question": "Who will win the Premier League?",
 *          "options": [
 *              "Manchester City",
 *              "Arsenal",
 *              "Liverpool"
 *          ]
 *     }
 * @param question
 * @param options
 */
public record PollRequest(@NotNull String question, List<String> options) {
}
