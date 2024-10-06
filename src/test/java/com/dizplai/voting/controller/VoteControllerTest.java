package com.dizplai.voting.controller;

import com.dizplai.voting.model.PollRequest;
import com.dizplai.voting.model.OptionResponse;
import com.dizplai.voting.model.Vote;
import com.dizplai.voting.service.VoteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoteController.class)
@AutoConfigureMockMvc
public class VoteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private VoteService voteService;

    private final List<Vote> options = new ArrayList<>();
    private final List<OptionResponse> optionResponse = new ArrayList<>();

    @BeforeEach()
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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

        optionResponse.add(OptionResponse.builder().poll("1").option("Manchester City").build() );
        optionResponse.add(OptionResponse.builder().poll("1").option("Liverpool").build() );
        optionResponse.add(OptionResponse.builder().poll("1").option("Arsenal").build() );
    }

    @Test
    public void invalid_input_create_poll_throws_exception() throws JsonProcessingException {
        PollRequest request = new PollRequest("Who will win the Premier League?", Arrays.asList(new String[]
                {"Manchester City"}));

        String jsonRequest = mapper.writeValueAsString(request);

        Exception exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform( post("/api/v1/poll")
                            .content(jsonRequest)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError());
        });

        HttpClientErrorException cause = (HttpClientErrorException) exception.getCause();

        assertEquals(HttpStatus.BAD_REQUEST, cause.getStatusCode());
        assertEquals("400 Invalid input - A poll can have between 2 and 7 options", cause.getMessage());
    }

    @Test
    public void valid_input_create_poll_returns_initialised_poll() throws Exception {
        PollRequest request = new PollRequest("Who will win the Premier League?", Arrays.asList(new String[]
                {"Manchester City", "Arsenal","Liverpool"}));

        String jsonRequest = mapper.writeValueAsString(request);

        String jsonResponse = mapper.writeValueAsString(optionResponse);

        when(voteService.createPoll(request)).thenReturn(1L);

        mockMvc.perform( post("/api/v1/poll")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("1"));
    }

    @Test
    public void valid_input_get_options_returns_options() throws Exception {
        String jsonResponse = mapper.writeValueAsString(optionResponse);

        when(voteService.getVotes("1")).thenReturn(options);

        mockMvc.perform( get("/api/v1/poll/1/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    public void valid_input_cast_vote_returns_votes_cast() throws Exception {
        optionResponse.clear();
        optionResponse.add(OptionResponse.builder().poll("1").option("Manchester City").count("0%").build() );
        optionResponse.add(OptionResponse.builder().poll("1").option("Liverpool").count("0%").build() );
        optionResponse.add(OptionResponse.builder().poll("1").option("Arsenal").count("0%").build() );

        String jsonResponse = mapper.writeValueAsString(optionResponse);

        when(voteService.castVote(1L,"{\"voteCast\": \"Arsenal\"}")).thenReturn(options);

        mockMvc.perform( post("/api/v1/poll/1/vote")
                        .content("{\"voteCast\": \"Arsenal\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    public void valid_input_poll_result_returns_options() throws Exception {
        String jsonResponse = mapper.writeValueAsString(optionResponse);

        when(voteService.getVotes("1")).thenReturn(options);

        mockMvc.perform( get("/api/v1/poll/1/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    public void valid_input_ballot_details_returns_options() throws Exception {
        when(voteService.getVotes("1")).thenReturn(options);

        mockMvc.perform( get("/api/v1/poll/1/ballots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
