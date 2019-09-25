package com.example.autocompletesuggestionsservice.worker;

import com.example.autocompletesuggestionsservice.entity.SuggestionEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ScoreCalculatorTest {

    @InjectMocks
    private ScoreCalculator scoreCalculator;
    private SuggestionEntity suggestionEntity;

    @Mock
    private Helper helper;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(scoreCalculator);

        suggestionEntity = new SuggestionEntity("Montreal", "40.566", "75.0312" );
        suggestionEntity.setScore(0.8);
    }

    @Test
    public void testMapNameScore(){
        when(helper.nameChecker(any(String.class))).thenReturn("Montreal");

        Double result = scoreCalculator.mapNameScore(suggestionEntity).apply("Montreal");

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(1.0);
    }

    @Test
    public void testMapLatitudeScore(){
        when(helper.transformToDouble(any(String.class))).thenReturn(40.566);

        Double result = scoreCalculator.mapLatitudeScore(suggestionEntity).apply("40.566");

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(1.0);
    }

    @Test
    public void testMapLongitudeScore(){
        when(helper.transformToDouble(any(String.class))).thenReturn(75.0312);

        Double result = scoreCalculator.mapLongitudeScore(suggestionEntity).apply("75.0312");

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(1.0);
    }
}
