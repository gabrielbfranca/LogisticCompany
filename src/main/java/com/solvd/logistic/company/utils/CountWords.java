package com.solvd.logistic.company.utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public final class CountWords {

    public static int countSpecialCharacters(String filepath) throws Exception {
        String data = FileUtils.readFileToString(new File(filepath), "UTF-8");

        ArrayList<String> specialCharacters = new ArrayList<>(Arrays.asList(
                ".", ",", ":", ";", "?", "!", "'", "\"", "-", "_",
                "+", "=", "<", ">", "%", "*", "/", "^", "~",
                "(", ")", "[", "]", "{", "}",
                "@", "#", "&", "$", "\\", "|", "`"
        ));

        int occurrences = 0;
        for( String str : specialCharacters) {
            occurrences += StringUtils.countMatches(data, str);
        }

        return occurrences;

    }
}
