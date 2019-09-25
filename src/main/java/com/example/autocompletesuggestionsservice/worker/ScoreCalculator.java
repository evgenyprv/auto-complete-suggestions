package com.example.autocompletesuggestionsservice.worker;

import com.example.autocompletesuggestionsservice.entity.SuggestionEntity;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.function.Predicate;

@Component
@AllArgsConstructor
public class ScoreCalculator {

    private final Helper helper;

    public @NonNull Function<String, Double> mapNameScore(SuggestionEntity entity){
        return name -> nameScoreAlgorithm(helper.nameChecker(entity.getName()), helper.nameChecker(name));
    }

    public @NonNull Function<String, Double> mapLatitudeScore(SuggestionEntity entity){
        return latitude -> coordinateScoreAlgorithm(helper.transformToDouble(entity.getLatitude()),
                helper.transformToDouble(latitude));
    }

    public  @NonNull Function<String, Double> mapLongitudeScore(SuggestionEntity entity){
        return longitude -> coordinateScoreAlgorithm(helper.transformToDouble(entity.getLongitude()),
                helper.transformToDouble(longitude));
    }

    private Double nameScoreAlgorithm(String entityName, String name){
        double initialScore = 1.0;

        String[] splitName = entityName.split(" ");

        for(int i = 0; i < splitName.length; i++){
            if(splitName[i].contains(name)){
                /***
                 * Checks which place of the word it is, and if it is not first takes score off.
                 * Each place is word 0.1 of the score.
                 */
                initialScore = initialScore - (i * 0.1);

                /***
                 * Check if the name provided by the user is bigger/smaller than name found in
                 * the DB. If it is, than each extra letter is worth 0.01 of score.
                 */

                initialScore = splitName[i].length() != name.length() ?
                        initialScore - ((splitName[i].length() - name.length()) * 0.01): initialScore;
                break;
            }
        }

        return initialScore;
    }

    private Double coordinateScoreAlgorithm(Double entityVal, Double val){
        Double initialScore = 1.0;

        initialScore = !entityVal.equals(val) ?
                initialScorePercentageCalculation(initialScore, val).apply(entityVal) : initialScore;

        initialScore = checkIfValuesHaveOppositeSign(val).test(entityVal) ? initialScore - 0.5 : initialScore;

        return initialScore;
    }

    private @NonNull Function<Double, Double> initialScorePercentageCalculation(Double initialScore, Double value){
        return entityVal -> {
            double percentageCal = Math.abs(entityVal / value);
            return initialScore - (percentageCal < 1 ? percentageCal: percentageCal - 1.0);
        };
    }

    private @NonNull Predicate<Double> checkIfValuesHaveOppositeSign(Double val){
        return entityVal -> (val < 0 && entityVal > 0) || (val > 0 && entityVal < 0);
    }

}
