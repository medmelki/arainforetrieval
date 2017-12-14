package com.textprocessing.util;

public class HadithIndexUtil {

    private static final int LAST_HADITH_SAHIH_INDEX = 5445;
    private static final int LAST_HADITH_FILED_INDEX = 92;

    public static int getIndexInSahih(int hadithFileIndex) {
        if (hadithFileIndex > LAST_HADITH_FILED_INDEX || hadithFileIndex < 0) // invalid index
            return -1;
        if (hadithFileIndex >= 10)
            return LAST_HADITH_SAHIH_INDEX + hadithFileIndex - LAST_HADITH_FILED_INDEX;
        else
            return LAST_HADITH_SAHIH_INDEX + hadithFileIndex - LAST_HADITH_FILED_INDEX + 1;
    }
}
