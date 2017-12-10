
package com.textprocessing;

import edu.stanford.nlp.international.arabic.process.ArabicSegmenter;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class POSTagger {

    private static final String MODEL_FILE = "data/arabic-accurate.tagger";
    private static final String SEGMENTER_FILE = "data/arabic-segmenter-atbtrain.ser.gz";

    public String POSTag(String text) throws IOException, ClassNotFoundException {
        MaxentTagger tagger = new MaxentTagger(MODEL_FILE);

        Properties props = new Properties();
        props.load(new FileInputStream("data/arabic-accurate.tagger.props"));

        ArabicSegmenter seg = new ArabicSegmenter(props);
        seg.loadSegmenter(SEGMENTER_FILE);

        String[] tokens = text.split("\\s");
        StringBuilder segTokens = new StringBuilder();

        for (String token : tokens) {
            List<HasWord> segmentedText = seg.segment(token);
            System.out.println(segmentedText); // return List<HasWord>
            StringBuilder txtAfterSeg = new StringBuilder();
            for (HasWord s : segmentedText) {
                txtAfterSeg.append(s.word()).append(" ");
            }
            segTokens.append(txtAfterSeg).append(" ");
        }
        return tagger.tagString(segTokens.toString().trim());
    }

}
