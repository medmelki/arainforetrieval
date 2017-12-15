package com.textprocessing.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class TextUtility {

    public static List<String> arabicStopWords = new ArrayList<>();

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

    public static List<String> removeStopWords(List<String> words) {
        words = words.stream().map(TextUtility::removeHamza).collect(Collectors.toList());
        for (String stopWord : arabicStopWords) {
            words.removeIf(token -> token.equals(stopWord));
        }
        return words;
    }

    public static List<String> tokenize(String text) {
        String[] tokens = text.split("\\s*(=>|,|\\s)\\s*");
        List<String> listTokens = new ArrayList<>();
        Collections.addAll(listTokens, tokens);
        return listTokens;
    }

    public static String removePunctuations(String currentText) {

        ArrayList<String> tokens = new ArrayList<>();
        StringBuilder modifiedWord = new StringBuilder("");

        ArrayList<String> modifiedText = removeExtraSpaces(currentText);
        for (String aModifiedText : modifiedText) {
            modifiedWord.setLength(0);
            for (int j = 0; j < aModifiedText.length(); j++) {
                if (!(Constants.punctuations.contains(aModifiedText.substring(j, j + 1))))
                    modifiedWord.append(aModifiedText.substring(j, j + 1));
            }
            tokens.add(modifiedWord.toString());
        }  // for each token in the text
        StringBuilder result = new StringBuilder();
        for (String t : tokens) {
            result.append(t).append(" ");
        }
        result = new StringBuilder(result.substring(0, result.length() - 1));
        return result.toString();
    }

    private static ArrayList<String> removeExtraSpaces(String currentText) {
        ArrayList<String> tt = new ArrayList<>();
        StringBuilder word = new StringBuilder();
        currentText = currentText + " ";
        for (int i = 0; i < currentText.length(); i++) {
            // if the character is not a space, add it to a word
            if ((!Character.isWhitespace(currentText.charAt(i)))) {
                word.append(currentText.charAt(i));
            } else {
                if (word.length() != 0) {
                    tt.add(word.toString());
                    word.setLength(0);
                }
            }
        }
        return tt;
    }

    private static String removeHamza(String word) {
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == Constants.ALIF_HAMZA_BELOW || chars[i] == Constants.ALIF_HAMZA_ABOVE)
                chars[i] = Constants.ALIF;
        }
        return new String(chars);
    }
}

