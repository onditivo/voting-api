package com.dizplai.voting.service;

import com.dizplai.voting.model.Poll;
import com.dizplai.voting.model.PollRequest;
import com.dizplai.voting.model.Vote;
import com.dizplai.voting.repository.PollRepository;
import com.dizplai.voting.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VoteServiceTest {
    @MockBean
    private VoteRepository voteRepository;
    @MockBean
    private PollRepository pollRepository;

    @Mock
    private Poll mockPoll;
    @Mock
    private PollRequest mockPollRequest;

    @Autowired
    private VoteService voteService;

    private final List<Vote> options = new ArrayList<>();

    @BeforeEach()
    public void setup() {
        options.add(Vote.builder()
                .poll(1)
                .option("Manchester City")
                .id(1L)
                .castOn(LocalDateTime.of(2024, 9, 25, 10, 30, 45))
                .build());
        options.add(Vote.builder()
                .poll(1)
                .option("Liverpool")
                .id(1L)
                .castOn(LocalDateTime.of(2024, 9, 25, 10, 30, 45))
                .build());
        options.add(Vote.builder()
                .poll(1)
                .option("Arsenal")
                .id(1L)
                .castOn(LocalDateTime.of(2024, 9, 25, 10, 30, 45))
                .build());
    }

    @Test
    public void savePollDetailToDatabase() {
        Poll newPoll = Poll.builder().id(1L).question("Who will win the Premier League?").build();

        when(mockPollRequest.question()).thenReturn("Who will win the Premier League?");
        when(mockPoll.getQuestion()).thenReturn("Who will win the Premier League?");
        when(mockPoll.getId()).thenReturn(1L);
        when(pollRepository.saveAndFlush(any(Poll.class))).thenReturn(newPoll);
        when(voteRepository.saveAllAndFlush(options)).thenReturn(options);

        Long pollId = voteService.createPoll(mockPollRequest);

        assertEquals(1L, pollId);
    }

    private static PollRequest getRequest() {
        return new PollRequest("Who will win the Premier League?", Arrays.asList(new String[]
                {"Manchester City"}));
    }
}
