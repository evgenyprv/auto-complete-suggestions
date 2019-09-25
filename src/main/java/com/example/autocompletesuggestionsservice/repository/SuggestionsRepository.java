package com.example.autocompletesuggestionsservice.repository;

import com.example.autocompletesuggestionsservice.entity.SuggestionEntities;
import com.example.autocompletesuggestionsservice.entity.SuggestionEntity;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@Repository
public class SuggestionsRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(SuggestionsRepository.class);

    private static final String FILE_PATH = "static/cities_canada-usa.tsv";

    public SuggestionEntities getAllSuggestions(String name){
        Optional.ofNullable(name).orElseThrow(() -> new NullPointerException("Name cannot be null"));

        List<SuggestionEntity> suggestionEntityList = createSuggestionsEntityDb().stream()
                .filter(data -> data.getName().contains(name))
                .collect(Collectors.toList());

        return new SuggestionEntities(suggestionEntityList);
    }

    private List<SuggestionEntity> createSuggestionsEntityDb(){
        List<LinkedHashMap<String, String>> rawData = createTempDatabase();

        return transferRawDataIntoEntity().apply(rawData);
    }

    private List<LinkedHashMap<String, String>> createTempDatabase(){
        List<LinkedHashMap<String, String>> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource(FILE_PATH).getInputStream()))){
            List<String> list = br.lines().collect(Collectors.toList());

            String[] columns = createColumnHeaders().apply(list);

            List<String[]> rows = parseRowValues().apply(list);

            createMapOfValues(rows, columns, result);

        } catch (IOException e) {
            LOGGER.error("File Path error:" + e);
        }
        return result;
    }

    private Function<List<String>, String[]> createColumnHeaders(){
        return list -> Optional.ofNullable(list).map(mapColumnsList()).orElse(new String[0]);
    }

    private @NonNull Function<List<String>, String[]> mapColumnsList(){
        return lst -> {
            String[] columns = lst.get(0).split("\t");
            lst.remove(0);
            return columns;
        };
    }

    private Function<List<String>, List<String[]>> parseRowValues(){
        return list -> Optional.ofNullable(list).map(mapRowList()).orElse(new ArrayList<>());
    }

    private @NonNull Function<List<String>, List<String[]>> mapRowList(){
        return list -> list.stream().map(line -> line.split("\t"))
                .collect(Collectors.toList());
    }

    private void createMapOfValues(List<String[]> rows,
                                   String[] columns,
                                   List<LinkedHashMap<String, String>> result){
        for(String[] row: rows){
            LinkedHashMap<String, String> newRow = new LinkedHashMap<>();
            for(int i=0; i < row.length; i++){
                newRow.put(columns[i], row[i]);
            }
            result.add(newRow);
        }
    }

    private Function<List<LinkedHashMap<String, String>>, List<SuggestionEntity>> transferRawDataIntoEntity(){
        return rawData -> rawData.stream().map(createSuggestionsList()).collect(Collectors.toList());
    }

    private Function<LinkedHashMap<String, String>, SuggestionEntity> createSuggestionsList(){
        return rawList -> new SuggestionEntity(rawList.get("name"), rawList.get("lat"),
                rawList.get("long"));
    }
}
