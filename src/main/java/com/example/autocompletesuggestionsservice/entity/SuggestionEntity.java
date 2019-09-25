package com.example.autocompletesuggestionsservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class SuggestionEntity implements Comparable<SuggestionEntity> {

    @JsonProperty
    private final String name;
    @JsonProperty
    private final String latitude;
    @JsonProperty
    private final String longitude;
    @JsonProperty
    private Double score;

    @Override
    public int compareTo(SuggestionEntity o) {
        return this.score.compareTo(o.getScore());
    }
}
