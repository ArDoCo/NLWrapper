package edu.kit.ipd.are.archdoclink.nlp;

import java.util.List;

/**
 * Classes implementing this interface represent a Phrase. Examples for phrases include noun phrases and verb phrases.
 * 
 * @author Jan Keim
 *
 */
public interface Phrase extends TextElement {
    /**
     * @return the PhraseType
     */
    public PhraseType getType();

    /**
     * @return String containing the text without any leading stopwords
     */
    public String getTextWithoutLeadingStopwords();

    /**
     * @return a list of the words contained in the phrase
     */
    public List<? extends Word> getWords();

    /**
     * Checks whether a word is contained in the phrase.
     * 
     * @param word
     *            Word that should be checked for
     * @return true, if the given word is contained in the phrase, else false
     */
    public boolean containsWord(Word word);
}
