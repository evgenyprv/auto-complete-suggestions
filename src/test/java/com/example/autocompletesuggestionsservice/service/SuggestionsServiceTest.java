package com.example.autocompletesuggestionsservice.service;

import com.example.autocompletesuggestionsservice.entity.SuggestionEntities;
import com.example.autocompletesuggestionsservice.entity.SuggestionEntity;
import com.example.autocompletesuggestionsservice.repository.SuggestionsRepository;
import com.example.autocompletesuggestionsservice.worker.Helper;
import com.example.autocompletesuggestionsservice.worker.ScoreCalculator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class SuggestionsServiceTest {

    @Mock
    private SuggestionsRepository suggestionsRepository;
    @Mock
    private ScoreCalculator scoreCalculator;
    @Mock
    private Helper helper;

    @Test
    public void testRetrieveSuggestionsForAutoComplete(){
        SuggestionEntity suggestionEntity = new SuggestionEntity("John", "40.56", "76.44");
        SuggestionEntities suggestionEntities = new SuggestionEntities(Arrays.asList(suggestionEntity));

        when(helper.filterName()).thenReturn((f) -> {return "John";});
        when(suggestionsRepository.getAllSuggestions(any(String.class))).thenReturn(suggestionEntities);
        when(scoreCalculator.mapNameScore(suggestionEntity)).thenReturn((f) -> 1.0);
        when(scoreCalculator.mapLatitudeScore(suggestionEntity)).thenReturn((f) -> 1.0);
        when(scoreCalculator.mapLongitudeScore(suggestionEntity)).thenReturn((f) -> 1.0);
        when(helper.updateDivider(any(AtomicReference.class))).thenReturn(() -> 3.0);
        when(helper.round(any(Double.class))).thenReturn(0.97);

        SuggestionsService suggestionsService = new SuggestionsService(suggestionsRepository, scoreCalculator, helper);

        SuggestionEntities result =
                suggestionsService.retrieveSuggestionsForAutoComplete("John", "40.56", "76.44");

        assertThat(result).isNotNull();
        assertThat(result.getSuggestions()).isNotNull();
        assertThat(result.getSuggestions().size()).isEqualTo(1);
        assertThat(result.getSuggestions().get(0).getName()).isEqualTo(suggestionEntity.getName());
        assertThat(result.getSuggestions().get(0).getScore()).isEqualTo(0.97);
    }
}
