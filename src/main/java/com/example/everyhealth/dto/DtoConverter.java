package com.example.everyhealth.dto;

import com.example.everyhealth.domain.RepWeight;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DtoConverter {

    public static List<RepWeightDto> convertRepWeights(List<RepWeight> repWeights) {
        return repWeights.stream()
                .map(rw -> new RepWeightDto(rw))
                .collect(Collectors.toList());
    }

    public static <T, R> List<R> convertList(List<T> sourceList, Function<T, R> converter) {
        return sourceList.stream()
                .map(converter)
                .collect(Collectors.toList());
    }
}
