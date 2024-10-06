package com.dizplai.voting.repository;

import com.dizplai.voting.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * A proxy for a class to interact with data store for Vote object - see description about Vote abject
 */

public interface VoteRepository extends JpaRepository<Vote, Long>  {
    List<Vote> findVotesByPoll(long poll);

    @Query("select distinct v.option from vote v where v.poll = ?1")
    List<String> findOptionsByPoll(long poll);
}
