package edu.kit.ipd.are.archdoclink.nlp.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.kit.ipd.are.archdoclink.nlp.Document;
import edu.kit.ipd.are.archdoclink.nlp.Sentence;
import edu.kit.ipd.are.archdoclink.nlp.Word;

public class ParseProcessingTest {
    private static Logger logger = Logger.getLogger(ParseProcessingTest.class);
    private static ParseProcessing parseProcessing;
    private static final String TEXT1 = "The Assembly_UserManagement <UserManagement> connects the Assembly_Facade <Facade> to the Assembly_UserDBAdapter <UserDBAdapter>. This ensures a nice separation of concerns and reduces the user's risks.";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        logger.trace("Starting initialising and testing of ArchDocLink");
        parseProcessing = new ParseProcessing();
    }

    @Ignore
    @Test
    public void testDocumentText() {
        parseProcessing.init(TEXT1, "DOC_TEXT_TEST");

        Document doc = parseProcessing.getDocument();
        String docText = doc.getText();
        assertEquals(TEXT1, docText);
    }

    @Ignore
    @Test
    public void testDocumentTextWithHyphens() {
        String text = "The Assembly_UserManagement <UserManagement> connects - after all - the Assembly_Facade <Facade> to the Assembly_UserDBAdapter <UserDBAdapter>. The web-service needs to be connected to the data source.";
        parseProcessing.init(text, "DOC_TEXT_HYPENS_TEST");

        Document doc = parseProcessing.getDocument();
        String docText = doc.getText();
        assertEquals(text, docText);
    }

    @Ignore
    @Test
    public void testDocumentSentences() {
        parseProcessing.init(TEXT1, "DOC_SENTENCE_TEST");

        // expected amount of words
        Document doc = parseProcessing.getDocument();
        List<? extends Sentence> sentences = doc.getSentences();
        assertEquals(2, sentences.size());

        // all sentences are from the text and form the original text (ignoring spaces)
        String text = new String(TEXT1);
        for (Sentence sentence : sentences) {
            assertTrue(text.contains(sentence.getText()));
            text = text.replaceFirst(sentence.getText(), "")
                       .trim();
        }
        assertTrue(text.isEmpty());
    }

    @Ignore
    @Test
    public void testDocumentWords() {
        parseProcessing.init(TEXT1, "DOC_WORDS_TEST");

        // expected amount of words
        Document doc = parseProcessing.getDocument();
        List<? extends Word> words = doc.getWords();
        assertEquals(26, words.size());

        // all words are from the text and form the original text (ignoring spaces)
        String text = new String(TEXT1);
        for (Word word : words) {
            assertTrue(text.contains(word.getText()));
            text = text.replaceFirst(word.getText(), "")
                       .trim();
        }
        assertTrue(text.isEmpty());
    }

}
