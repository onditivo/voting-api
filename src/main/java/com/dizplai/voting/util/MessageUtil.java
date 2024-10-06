package com.dizplai.voting.util;

import com.dizplai.voting.model.OptionResponse;
import com.dizplai.voting.model.Vote;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class MessageUtil {
    /**
     * A utility to format the response messages to suit the client requirements
     * @param votes - contains the vote detail. We can derive options list from vote list
     * @param pollId - the poll a vote is associated
     * @param includeVoteShare - flag to indicate the formatting of the text on Vote view and Vote result view
     * @return - a list of options e.g.
     *
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
     */
    public static List<OptionResponse> toPollResponses(final List<Vote> votes, final Optional<Long> pollId, final boolean includeVoteShare) {
        List<OptionResponse> optionResponse = new ArrayList<>();
        String pId = votes.isEmpty() ? null : Long.toString(votes.getFirst().getPoll());

        // count the votes for each option and put to a map
        Map<String, Long> voteMap = votes.stream()
                .collect(Collectors.groupingBy(
                        Vote::getOption,
                        Collectors.counting()
                ));

        // find the total votes cast so far
        float sum = (voteMap.values().stream().mapToLong(Long::intValue).sum() - voteMap.size());

        // compose the vote distribution as a percentage of the total votes cast
        voteMap.forEach((key, value) -> optionResponse.add(
                OptionResponse.builder()
                        .option(key)
                        .poll(pId)
                        .count(includeVoteShare ? new DecimalFormat("#.##")
                                .format(sum == 0 ? 0.0f : (value.intValue()-1)/sum * 100.0f) + "%" :
                                null)
                        .build())
        );

        return optionResponse;
    }
}
