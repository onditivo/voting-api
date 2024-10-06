package com.dizplai.voting.controller;

import com.dizplai.voting.model.*;
import com.dizplai.voting.service.VoteService;
import com.dizplai.voting.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/poll")
@RequiredArgsConstructor
@Slf4j
public class VoteController {
    private final VoteService voteService;

    /**
     * Create a new poll with the given detail. If the payload constraint test fails, an error is thrown
     * Example input:
     *    {
     *          "question": "Who will win the Premier League?",
     *          "options": [
     *              "Manchester City",
     *              "Arsenal",
     *              "Liverpool"
     *          ]
     *     }
     *
     * @param pollRequest - the input payload shown above.
     * @return - the identity of the created poll.
     */
    @PostMapping(produces = "application/json")
    public Long createPoll(final @RequestBody PollRequest pollRequest) {
        log.info("Creating poll: {}",pollRequest);

        if (pollRequest.options().size() < 2 || pollRequest.options().size() > 7) {
            log.error("Invalid input - A poll can have between 2 and 7 options. Actual {}", pollRequest.options().size());
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid input - A poll can have between 2 and 7 options");
        }
        Long id = voteService.createPoll(pollRequest);

        log.info("Created poll with id: {}", id);

        return id;
    }

    /**
     * Given a poll identity e.g. 1, return corresponding poll details e.g.
     * {
     *     "id": 1,
     *     "question": "Who will win the Premier League?",
     *     "options": [
     *         "Arsenal",
     *         "Liverpool",
     *         "Manchester City"
     *     ]
     * }
     *
     * @param pollId - the identity of the poll
     * @return poll details. See example above.
     */
    @GetMapping(value = "/{pollId}",produces = "application/json")
    public PollResponse getPollDetail(final @PathVariable String pollId) {
        log.info("Retrieving poll: {}",pollId);

        PollResponse response = voteService.getPoll(pollId);

        log.info("Retrieved poll: {}", response);

        return response;
    }

    /**
     * Save the vote cast for the associated poll identity.
     *
     * @param pollId - the identity of the associated poll e.g 1
     * @param voteRequest - the input payload e.g. {"voteCast": "Arsenal"}
     *
     * @return a list of votes distribution amongst the available options for the poll e.g.
     * [
     *     {
     *         "option": "Liverpool",
     *         "count": "25%",
     *         "poll": "1"
     *     },
     *     {
     *         "option": "Manchester City",
     *         "count": "25%",
     *         "poll": "1"
     *     },
     *     {
     *         "option": "Arsenal",
     *         "count": "50%",
     *         "poll": "1"
     *     }
     * ]
     */
    @PostMapping(value = "/{pollId}/vote",  consumes = "application/json", produces = "application/json")
    public List<OptionResponse> castVote(final @PathVariable String pollId, final @RequestBody VoteRequest voteRequest) {
        log.info("Casting vote {} for poll id: {}", voteRequest, pollId);
        return MessageUtil.toPollResponses(voteService.castVote(Long.parseLong(pollId), voteRequest.voteCast()), Optional.of(Long.parseLong(pollId)), true);
    }

    /**
     * List all the options associated with a given poll
     * @param pollId - the identity of the associated poll e.g 1
     * @return a list of votes distribution amongst the available options for the poll e.g.
     * [
     *     {
     *         "option": "Liverpool",
     *         "count": "25%",
     *         "poll": "1"
     *     },
     *     {
     *         "option": "Manchester City",
     *         "count": "25%",
     *         "poll": "1"
     *     },
     *     {
     *         "option": "Arsenal",
     *         "count": "50%",
     *         "poll": "1"
     *     }
     * ]
     */
    @GetMapping(value = "/{pollId}/options",produces = "application/json")
    public List<OptionResponse> getPollOptions(final @PathVariable String pollId) {
        log.info("Retrieving poll options for poll id: {}", pollId);
        return MessageUtil.toPollResponses(voteService.getVotes(pollId), Optional.of(Long.parseLong(pollId)), false);
    }

    /**
     *
     * @param pollId - the identity of the associated poll e.g 1
     * @return - a list of votes distribution amongst the available options for the poll e.g.
     * [
     *     {
     *         "option": "Liverpool",
     *         "count": "25%",
     *         "poll": "1"
     *     },
     *     {
     *         "option": "Manchester City",
     *         "count": "25%",
     *         "poll": "1"
     *     },
     *     {
     *         "option": "Arsenal",
     *         "count": "50%",
     *         "poll": "1"
     *     }
     * ]
     */
    @GetMapping(value = "/{pollId}/votes",produces = "application/json")
    public List<OptionResponse> getPollResult(final @PathVariable String pollId) {
        log.info("Retrieving vote distribution for poll id: {}", pollId);
        return MessageUtil.toPollResponses(voteService.getVotes(pollId), Optional.of(Long.parseLong(pollId)), true);
    }

    /**
     * View all votes for a given poll and the time the ballot was cast
     *
     * @param pollId - the identity of the associated poll e.g 1
     * @return - a list of all the ballot detail e.g.
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
     */
    @GetMapping(value = "/{pollId}/ballots",  produces = "application/json")
    public VoteResponse getVoteDetail(final @PathVariable Integer pollId) {
        log.info("Retrieving vote details for poll id: {}", pollId);
        return VoteResponse.builder()
                .votes(voteService.getVotes(Integer.toString(pollId)))
                .build();
    }
}