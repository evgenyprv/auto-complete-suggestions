package com.example.autocompletesuggestionsservice.controller;


import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.example.autocompletesuggestionsservice.entity.SuggestionEntities;
import com.example.autocompletesuggestionsservice.service.SuggestionsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api", produces = APPLICATION_JSON_UTF8_VALUE)
public class SuggestionsController {

    private final SuggestionsService suggestionsService;

    @GetMapping(path="/")
    public String initialApiPage(){
        return "Welcome to my simple api.";
    }

    @GetMapping(path ="/suggestions")
    public @ResponseBody SuggestionEntities findAllSuggestions(@RequestParam("q") String name,
                                                                   @RequestParam(value = "latitude", required = false) String latitude,
                                                                   @RequestParam(value = "longitude", required = false) String longitude){
        return suggestionsService.retrieveSuggestionsForAutoComplete(name, latitude, longitude);

    }

}
