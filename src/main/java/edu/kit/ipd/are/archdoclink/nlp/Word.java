package edu.kit.ipd.are.archdoclink.nlp;

import java.util.Set;

import edu.stanford.nlp.coref.data.WordLists;

/**
 * Classes implementing this interface represent a word (within a text) Words should have a part-of-speech and a lemma.
 * 
 * @author Jan Keim
 *
 */
public interface Word extends TextElement {
    /**
     * @return the POS of the word
     */
    public String getPos();

    /**
     * @return true if the word is a stopword
     */
    public default boolean isStopword() {
        return isStopword(getText());
    }

    /**
     * @return true if the word is a named entity
     */
    public boolean isNamedEntity();

    /**
     * @return the lemma of the word
     */
    public String getLemma();

    /**
     * @return true if the "word" is actually a punctuation symbol
     */
    public boolean isPunctuation();

    /**
     * Checks whether a given word is a stopword
     * 
     * @param word
     *            word that should be checked
     * @return true if the given word is a stopword
     */
    public static boolean isStopword(String word) {
        String cleanWord = word.toLowerCase()
                               .trim();
        Set<String> stopWords = WordLists.stopWordsEn;
        return stopWords.contains(cleanWord);
    }
}
