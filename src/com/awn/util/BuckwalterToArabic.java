package com.awn.util;

import java.util.HashMap;

/**
 * Class for transliterating Buckwalter alphabet to the Arabic script alphabet using an extension of HashMap with
 * <K,V> where Key is the Buckwalter letter and the Value is the Arabic script letter.
 */
public class BuckwalterToArabic extends HashMap<String, String> {

    boolean usingDiacritics;

    public BuckwalterToArabic() {
        this.put(" ", " ");
        this.put("'", "\u0621");
        this.put("|", "\u0622");
        this.put(">", "\u0623");
        this.put("&", "\u0624");
        this.put("<", "\u0625");
        this.put("}", "\u0626");
        this.put("A", "\u0627");
        this.put("b", "\u0628");
        this.put("p", "\u0629");
        this.put("t", "\u062A");
        this.put("v", "\u062B");
        this.put("j", "\u062C");
        this.put("H", "\u062D");
        this.put("x", "\u062E");
        this.put("d", "\u062F");
        this.put("*", "\u0630");
        this.put("r", "\u0631");
        this.put("z", "\u0632");
        this.put("s", "\u0633");
        this.put("$", "\u0634");
        this.put("S", "\u0635");
        this.put("D", "\u0636");
        this.put("T", "\u0637");
        this.put("Z", "\u0638");
        this.put("E", "\u0639");
        this.put("g", "\u063A");
        this.put("_", "\u0640");
        this.put("f", "\u0641");
        this.put("q", "\u0642");
        this.put("k", "\u0643");
        this.put("l", "\u0644");
        this.put("m", "\u0645");
        this.put("n", "\u0646");
        this.put("h", "\u0647");
        this.put("w", "\u0648");
        this.put("Y", "\u0649");
        this.put("y", "\u064A");


        // punctuation
        this.put(".", "."); //JAMES
        this.put(",", ","); // JAMES


        this.put("`", "\u0670");
        //this.put("{","\u0652");
        setUsingDiacritics(true);
    }

    /**
     * Adds/removes buckwalter letters to the HashMap and their
     * respective Arabic script short vowel equivalents
     *
     * @param using - (Boolean) True if using diacritics; false if not
     */
    public void setUsingDiacritics(boolean using) {
        usingDiacritics = using;
        if (usingDiacritics) {
            this.put("a", "\u064E");
            this.put("u", "\u064F");
            this.put("i", "\u0650");
            this.put("~", "\u0651");
            this.put("o", "\u0652");
            this.put("F", "\u064B");
            this.put("N", "\u064C");
            this.put("K", "\u064D");
        } else {
            this.remove("a");
            this.remove("u");
            this.remove("i");
            this.remove("~");
            this.remove("o");
            this.remove("F");
            this.remove("N");
            this.remove("K");
        }
    }

    /**
     * Transliterates a Buckwalter string to Arabic string by referencing the HashMap.
     *
     * @param s - (String) The Buckwalter string to transliterate
     * @return result - (String) The transliterated Arabic script string
     */
    public String transliterate(String s) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            ret.append(this.get("" + s.charAt(i)));
        }
        return ret.toString();
    }
}
