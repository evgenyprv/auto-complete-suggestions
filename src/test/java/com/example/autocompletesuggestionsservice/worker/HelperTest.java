package com.example.autocompletesuggestionsservice.worker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelperTest {

    private Helper helper;

    @Before
    public void setUp(){
        helper = new Helper();
    }

    @Test
    public void testUpdateDivider(){
        AtomicReference<Double> divider = new AtomicReference<>(1.0);

        Double result = helper.updateDivider(divider).get();

        assertThat(divider.get()).isNotNull();
        assertThat(divider.get()).isEqualTo(0.0);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(0.0);
    }

    @Test
    public void testRound(){
        Double result = helper.round(1.788);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(1.79);
    }

    @Test
    public void testFileNameWithFirstLowerCaseLetter(){
        String result = helper.filterName().apply("john");

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("John");
    }

    @Test
    public void testFileNameWithRandomUpperCaseLetters(){
        String result = helper.filterName().apply("joHN");

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("John");
    }

    @Test
    public void testNameCheckerNull(){
        String result = helper.nameChecker(null);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("");
    }

    @Test
    public void testNameCheckerNotNull(){
        String result = helper.nameChecker("aaaa");

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("aaaa");
    }

    @Test
    public void testTransformToDoubleNull(){
        Double result = helper.transformToDouble(null);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(0.0);
    }

    @Test
    public void testTransformToDoubleInteger(){
        Double result = helper.transformToDouble("1");

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(1.0);
    }

    @Test
    public void testTransformToDoubleFloat(){
        Double result = helper.transformToDouble("1.032");

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(1.032);
    }
}
