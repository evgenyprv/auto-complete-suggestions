package com.example.autocompletesuggestionsservice.service;

import com.example.autocompletesuggestionsservice.entity.SuggestionEntities;
import com.example.autocompletesuggestionsservice.entity.SuggestionEntity;
import com.example.autocompletesuggestionsservice.repository.SuggestionsRepository;
import com.example.autocompletesuggestionsservice.worker.Helper;
import com.example.autocompletesuggestionsservice.worker.ScoreCalculator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class SuggestionsService {

    private final SuggestionsRepository suggestionsRepository;
    private final ScoreCalculator scoreCalculator;
    private final Helper helper;

    public SuggestionEntities retrieveSuggestionsForAutoComplete(String name, String latitude, String longitude){
        return retrieveSuggestionsWithCalculatedScore(name, latitude, longitude);
    }

    private SuggestionEntities retrieveSuggestionsWithCalculatedScore(String name, String latitude, String longitude){
        String filteredName = helper.filterName().apply(name);
        SuggestionEntities suggestionsEntities = suggestionsRepository.getAllSuggestions(filteredName);

        for(SuggestionEntity entity: suggestionsEntities.getSuggestions()){
            Double score = retrieveOverallScore(name, latitude, longitude).apply(entity);
            entity.setScore(score);
        }

        suggestionsEntities.getSuggestions().sort(Collections.reverseOrder());

        return suggestionsEntities;
    }

    private @NonNull Function<SuggestionEntity, Double> retrieveOverallScore(String name,
                                                                             String latitude,
                                                                             String longitude){
        return entity -> {
            AtomicReference<Double> divider = new AtomicReference<>(3.0);
            Double nameScore =  scoreCalculator.mapNameScore(entity).apply(name);
            Double latScore = Optional.ofNullable(latitude).map(scoreCalculator.mapLatitudeScore(entity))
                    .orElseGet(helper.updateDivider(divider));
            Double longScore = Optional.ofNullable(longitude).map(scoreCalculator.mapLongitudeScore(entity))
                    .orElseGet(helper.updateDivider(divider));

            return helper.round((nameScore + latScore + longScore) / divider.get());
        };
    }

}
