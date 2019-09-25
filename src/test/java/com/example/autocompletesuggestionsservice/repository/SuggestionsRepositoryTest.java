package com.example.autocompletesuggestionsservice.repository;

import com.example.autocompletesuggestionsservice.entity.SuggestionEntities;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SuggestionsRepositoryTest {

    @Autowired
    private SuggestionsRepository suggestionsRepository;

    @Test
    public void testGetAllSuggestionsTestRealData(){
        SuggestionEntities result = suggestionsRepository.getAllSuggestions("Bran");

        assertThat(result).isNotNull();
        assertThat(result.getSuggestions().isEmpty()).isFalse();
    }

    @Test
    public void testGetAllSuggestionsWithNonExistingName(){
        SuggestionEntities result = suggestionsRepository.getAllSuggestions("AAAAA");

        assertThat(result).isNotNull();
        assertThat(result.getSuggestions().isEmpty()).isTrue();
    }

    @Test(expected = NullPointerException.class)
    public void testGetAllSuggestionsNull(){
        SuggestionEntities result = suggestionsRepository.getAllSuggestions(null);
    }
}
