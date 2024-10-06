package com.dizplai.voting.repository;

import com.dizplai.voting.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A proxy for a class to interact with data store for Poll object - see description about Poll abject
 */
public interface PollRepository extends JpaRepository<Poll, Long> {
}
