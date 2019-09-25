package com.example.autocompletesuggestionsservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SuggestionEntities {

    @JsonProperty
    private List<SuggestionEntity> suggestions;

    @Builder
    public SuggestionEntities(List<SuggestionEntity> suggestions){
        this.suggestions = suggestions == null ? new ArrayList<>(): new ArrayList<>(suggestions);
    }
}
