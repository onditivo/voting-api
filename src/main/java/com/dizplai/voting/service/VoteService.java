package com.dizplai.voting.service;

import com.dizplai.voting.error.PollNotFoundException;
import com.dizplai.voting.model.*;
import com.dizplai.voting.repository.PollRepository;
import com.dizplai.voting.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A service to interact with:
 *      1. API layer
 *      2. Data store layer
 * All the business logic and data transformation are carried out in the layer.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {
    private final VoteRepository voteRepository;
    private final PollRepository pollRepository;

    /**
     * Given a poll identifier, return all the votes associated with the poll
     * For detail about Vote, see the description on the Vote class
     *
     * @param poll - poll identifier - e.g 1
     * @return - a list of votes
     */
    public List<Vote> getVotes(String poll) {
        log.info("Retrieving votes for poll: {}",poll);
        return voteRepository.findVotesByPoll(Long.parseLong(poll));
    }

    /**
     * Given a poll identifier and a vote cast, save the vote and return vote distribution taking into account
     * the new vote cast. For detail about Vote, see the description on the Vote class
     *
     * @param pollId - poll identifier e.g. 1
     * @param voteCast - vote to cast e.g Liverpool
     * @return - a list of vote distribution after the vote cast
     */
    public List<Vote> castVote(Long pollId, String voteCast) {
        log.info("Casting vote for poll: {} and option {}", pollId, voteCast);

        Vote vote = Vote.builder()
                .poll(pollId)
                .option(voteCast)
                .castOn(LocalDateTime.now())
                .build();

        Vote savedVote = voteRepository.saveAndFlush(vote);

        log.info("Saved vote with id: {} and option {}" , savedVote.getId(), pollId);

        return voteRepository.findVotesByPoll(pollId);
    }

    /**
     * Create a poll with a given payload. See the description about PollRequest class.
     * 1. Save the poll and use the identity of the created poll to associate the poll with options.
     * 2. Save the options
     *
     * @param pollRequest - poll detail to create.
     * @return id - the identity of the poll created.
     */
    public Long createPoll(PollRequest pollRequest) {
        Poll poll = Poll.builder().question(pollRequest.question()).build();
        Poll newPoll = pollRepository.saveAndFlush(poll);

        List<Vote> options = new ArrayList<>();
        pollRequest.options().forEach(opt -> {
            Vote vote = Vote.builder()
                    .poll(newPoll.getId())
                    .option(opt)
                    .castOn(LocalDateTime.now())
                    .build();

            options.add(vote);
        });

        List<Vote> votes = voteRepository.saveAllAndFlush(options);

        log.info("Created poll with poll id: {} and option list {}" , newPoll.getId(), votes);

        return newPoll.getId();
    }

    /**
     * Given a poll id, return all the detail about the poll. See the description in PollResponse class for detail.
     * If no poll is found, an exception is thrown to indicate there is no such poll in the data store.
     *
     * @param pollId - the identity of the poll
     * @return - poll details
     */
    public PollResponse getPoll(String pollId) {
        val options = voteRepository.findOptionsByPoll(Long.parseLong(pollId));
        Optional<Poll> poll = pollRepository.findById(Long.parseLong(pollId));

        if (poll.isEmpty() && options.isEmpty()) {
            log.error("Invalid values for poll with poll id: {} and option list {}" , pollId, options);
            throw new PollNotFoundException(pollId);
        }

        log.info("Retrieving poll with poll id: {} and option list {}" , pollId, options);

        return new PollResponse(Long.parseLong(pollId), poll.get().getQuestion(), options);
    }
}
