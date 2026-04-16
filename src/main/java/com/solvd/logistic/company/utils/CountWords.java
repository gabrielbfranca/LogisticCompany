package com.solvd.logistic.company.utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;

import java.util.stream.Collectors;



public final class CountWords {

    public static Map<String, Integer> countSpecialCharacters(String filepath, List<String> words) throws Exception {
        String data = FileUtils.readFileToString(new File(filepath), "UTF-8");

        return words.stream()
                .collect(Collectors.toMap(
                        word -> word,
                        word -> StringUtils.countMatches(data, word)
                ));


    }

    public static void toFile(String filepath, Map<String, Integer> word_occ) throws Exception {
        StringBuilder sb = new StringBuilder();
        word_occ.forEach((word, count) ->
                sb.append(word).append(": ")
                        .append(count).append('\n')
        );
        String output = word_occ.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(System.lineSeparator()));

        FileUtils.writeStringToFile(new File(filepath), output, "UTF-8");

    }
}
