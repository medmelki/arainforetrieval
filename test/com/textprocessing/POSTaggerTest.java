package com.textprocessing;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class POSTaggerTest {

    @Test
    public void testPOSTag() throws IOException, ClassNotFoundException {
        POSTagger tagger = new POSTagger();
        String result = tagger.POSTag("الإسهال الحاد والمزمن");
        assertEquals("الاسهال/DTNN الحاد/DTJJ و/CC المزمن/DTJJ ", result);
    }
}
