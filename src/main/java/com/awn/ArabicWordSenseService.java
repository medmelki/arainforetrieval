package com.awn;

import com.awn.dao.ArabicWordSenseDao;

import java.util.ArrayList;

public class ArabicWordSenseService {


    private ArabicWordSenseDao arabicWordSenseDao;

    public ArabicWordSenseService() {
        this.arabicWordSenseDao = new ArabicWordSenseDao();
    }

    /**
     * Retrieve the senseIDs of a given word
     *
     * @param word            -
     *                        (String) The given word
     * @param root            -
     *                        (String) The given root
     * @param pos             -
     *                        (String) The desired part of speech
     * @param usingDiacritics -
     *                        (Boolean) Whether the search includes short vowels or not
     * @return results - (ArrayList<String>) A list of search results
     * containing synset IDs
     */

    public ArrayList<String> readWordSenses(String word, String root,
                                            String pos, boolean usingDiacritics) {

        ArrayList<String> result = new ArrayList<>();

        /* If diactritics aren't entered on the search word,
        we insert wildcard characters in order that the query
        will match words in the db, which all include diacritics */
        StringBuilder searchString = new StringBuilder();
        if (!usingDiacritics) {
            char[] wordChars = word.toCharArray();
            for (char wordChar : wordChars) {
                searchString.append(wordChar).append("%");
            }
        }
         /* If diacritics are not used in the search word, sql query
         must use LIKE in order to match the pattern with the
         wildcards in it
         This case used if a word but no root has been entered by the user */
        ArrayList<String> synsets = new ArrayList<>();
        if (!word.equals("") && root.equals("")) {
            if (!usingDiacritics) {
                synsets.addAll(arabicWordSenseDao
                        .getSynsetNoDiacritics(searchString.toString()));
            } else {
                synsets.addAll(arabicWordSenseDao.getSynsetDiacritics(word));
                System.out.println("word: " + word);
            }
        }
        // Different query used if no word but only a root has been entered
        else if (word.equals("") && !root.equals("")) {
            synsets.addAll(arabicWordSenseDao.getSynsetFromRoot(root));
        }
        // Final case is if both a word AND a root have been entered by the user
        else {
            if (!usingDiacritics) {
                synsets.addAll(arabicWordSenseDao.getSynsetRootNoDiacritics(root,
                        searchString.toString()));
            } else {
                synsets.addAll(arabicWordSenseDao
                        .getSynsetRootDiacritics(root, word));
            }
        }
        if (synsets.size() > 0) {
            for (String synset : synsets) {
                /* Only selectively add items according to pos if
                 a particular pos has been selected
                 Otherwise all retrieved synsets are added to the vector */
                if (pos.length() == 1) {
                    if (synset.indexOf("_" + pos) > 1) {
                        result.add(synset);
                    }
                } else {
                    result.add(synset);
                }
            }
        }
        return result;

    }
}
