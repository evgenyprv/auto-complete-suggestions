package com.example.autocompletesuggestionsservice.worker;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class Helper {

    private static String NAME_REGEX;

    static {
        NAME_REGEX = "[A-Z]{1}[a-z]{%x}";
    }

    public Supplier<Double> updateDivider(AtomicReference<Double> divider){
        return () -> {
            divider.updateAndGet(v -> (v - 1.0));
            return 0.0;
        };
    }

    public Double round(Double value){
        return Math.round(value*100)/100.d;
    }

    public Function<String, String> filterName(){
        return name -> {
            NAME_REGEX = String.format(NAME_REGEX, name.length()-1);

            return !name.matches(NAME_REGEX) ? transformName().apply(name) : name;
        };
    }

    private Function<String, String> transformName(){
        return str -> {
            char[] strArr = str.toCharArray();
            strArr[0] = Character.toUpperCase(strArr[0]);

            for(int i = 1; i < strArr.length; i++){
                strArr[i] = Character.toLowerCase(strArr[i]);
            }

            return String.valueOf(strArr);
        };
    }

    public String nameChecker(String name){
        return Optional.ofNullable(name).map(String::trim).orElse(Strings.EMPTY);
    }

    public Double transformToDouble(String val){
        return Optional.ofNullable(val).map(Double::valueOf).orElse(0.0);
    }
}
