package com.example.autocompletesuggestionsservice.controller;

import com.example.autocompletesuggestionsservice.entity.SuggestionEntities;
import com.example.autocompletesuggestionsservice.entity.SuggestionEntity;
import com.example.autocompletesuggestionsservice.service.SuggestionsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class SuggestionsControllerTest {

    @Mock
    private SuggestionsService suggestionsService;

    @Test
    public void testFindAllSuggestions(){
        SuggestionEntity suggestionEntity1 = new SuggestionEntity("J", "42.123", "78.34");
        SuggestionEntity suggestionEntity2 = new SuggestionEntity("D", "35.3123","80.312");
        SuggestionEntities suggestionEntities = new SuggestionEntities(Arrays.asList(suggestionEntity1, suggestionEntity2));

        when(suggestionsService.retrieveSuggestionsForAutoComplete("J", "42.123", "78.34"))
                .thenReturn(suggestionEntities);

        SuggestionsController suggestionsController = new SuggestionsController(suggestionsService);

        SuggestionEntities result = suggestionsController.findAllSuggestions("J", "42.123", "78.34");

        assertThat(result).isNotNull();
        assertThat(result.getSuggestions().isEmpty()).isFalse();
        assertThat(result.getSuggestions().size()).isEqualTo(2);
    }
}
