package com.textprocessing.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class TextUtility {

    private static List<String> arabicStopWords = new ArrayList<>();

    static {
        try {
            BufferedReader bf = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("assets/segmenter/stop-words-arabic.txt"), "UTF8"));

            String str;
            while ((str = bf.readLine()) != null) {
                arabicStopWords.add(str.trim());
            }
            bf.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static String removeStopWords(String s) {
        List<String> tokens = TextUtility.tokenize(s);
        for (String stopWord : arabicStopWords) {
            Iterator<String> iter = tokens.iterator();
            while (iter.hasNext()) {
                String token = iter.next();
                if (token.equals(stopWord))
                    iter.remove();
            }
        }
        StringBuilder result = new StringBuilder();
        for (String t : tokens)
            result.append(t).append(" ");
        return result.toString().trim();
    }

    public static List<String> tokenize(String s) {
        String[] tokens = s.split("\\s*(=>|,|\\s)\\s*");
        List<String> listTokens = new ArrayList<>();
        Collections.addAll(listTokens, tokens);
        return listTokens;
    }
}

