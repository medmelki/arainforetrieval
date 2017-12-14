package com.awn.dao;

import com.awn.db.DBAccess;
import com.awn.util.BuckwalterToArabic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArabicWordSenseDao {

    private PreparedStatement ps;   //global statement
    private ResultSet rs;           // global result set
    private ArrayList<String> results;

    public static void main(String[] args) {
        DBAccess.connectToDB();
        final ArabicWordSenseDao senseDao = new ArabicWordSenseDao();
        final BuckwalterToArabic translator = new BuckwalterToArabic();
        List<String> roots = senseDao.getSynsetFromRoot("وصل");
        roots = roots.stream()
                .map(s -> s.replaceAll("_.+", ""))
                .map(translator::transliterate)
                .collect(Collectors.toList());
        System.out.println("...");
        DBAccess.closeConnection();
    }

    /**
     * Retrieves a list of synsets for an inflected search word without
     * diacritics
     *
     * @param word -
     *             (String) The inflected arabic search word
     * @return results - (ArrayList<String>) A List of synsets the word is
     * found in
     */
    public ArrayList<String> getSynsetNoDiacritics(String word) {
        results = new ArrayList<>();
        try {
            ps = DBAccess.conn
                    .prepareStatement("SELECT synsetid FROM word WHERE value LIKE ?");
            ps.setString(1, word);
            rs = ps.executeQuery();
            while (rs.next()) {
                results.add(rs.getString("synsetid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Retrieves a list of synsets for an inflected search word with diacritics
     *
     * @param word -
     *             (String) The inflected arabic search word
     * @return results - (ArrayList<String>) A List of synsets the word is
     * found in
     */
    public ArrayList<String> getSynsetDiacritics(String word) {
        results = new ArrayList<>();
        try {
            ps = DBAccess.conn
                    .prepareStatement("SELECT synsetid FROM word WHERE value = ?");
            ps.setString(1, word);
            rs = ps.executeQuery();
            while (rs.next()) {
                results.add(rs.getString("synsetid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Retrieves a list of synsets for a root search word with diacritics
     *
     * @param root -
     *             (String) The arabic search root word
     * @return results - (ArrayList<String>) A List of synsets the root is
     * found in
     */
    public ArrayList<String> getSynsetFromRoot(String root) {
        results = new ArrayList<>();
        try {
            ps = DBAccess.conn.prepareStatement("SELECT synsetid FROM form, word WHERE "
                    + "form.value = ? AND form.type = 'root' "
                    + "AND form.wordid = word.wordid");
            ps.setString(1, root);
            rs = ps.executeQuery();
            while (rs.next()) {
                results.add(rs.getString("synsetid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Retrieves a list of synsets for a root search word without diacritics
     *
     * @param root -
     *             (String) The root arabic search word
     * @return results - (ArrayList<String>) A List of synsets the word is
     * found in
     */
    public ArrayList<String> getSynsetRootNoDiacritics(String root,
                                                       String searchstring) {
        results = new ArrayList<>();
        try {
            ps = DBAccess.conn.prepareStatement("SELECT synsetid FROM form, word WHERE "
                    + "form.value = ? AND form.type = 'root' "
                    + "AND form.wordid = word.wordid "
                    + "AND word.value LIKE ?");
            ps.setString(1, root);
            ps.setString(2, searchstring);
            rs = ps.executeQuery();
            while (rs.next()) {
                results.add(rs.getString("synsetid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Retrieves a list of synsets for a root search word with diacritics
     *
     * @param root -
     *             (String) The root arabic search word
     * @return results - (ArrayList<String>) A List of synsets the word is
     * found in
     */
    public ArrayList<String> getSynsetRootDiacritics(String root, String word) {
        results = new ArrayList<>();
        try {
            ps = DBAccess.conn.prepareStatement("SELECT synsetid FROM form, word WHERE "
                    + "form.value = ? AND form.type = 'root' "
                    + "AND form.wordid = word.wordid " + "AND word.value = ?");
            ps.setString(1, root);
            ps.setString(2, word);
            rs = ps.executeQuery();
            while (rs.next()) {
                results.add(rs.getString("synsetid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Gets the translation equivalent for a synset in another specified language
     *
     * @param synsetId - (String) The synset id to be translated
     * @return translated String
     */
    public String getArabicTranslation(String synsetId) {
        String result = "";
        try {
            ps = DBAccess.conn
                    .prepareStatement("SELECT link1 FROM link WHERE link2= ? "
                            + "AND type='equivalent'");
            ps.setString(1, synsetId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString("link1");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return result;
    }

}
