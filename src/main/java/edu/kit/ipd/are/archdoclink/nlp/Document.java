package edu.kit.ipd.are.archdoclink.nlp;

import java.util.Calendar;
import java.util.List;

/**
 * This class represents a document with all its sentences, phrases, words, etc.
 *
 * @author Jan Keim
 *
 */
public interface Document extends TextElement, Identifiable, DebugPrintable {

    /**
     * @return the processing date of the document.
     */
    public Calendar getProcessingDate();

    /**
     * @return List of all phrases of the document. The phrases are ordered by appearance.
     */
    public List<? extends Phrase> getPhrases();

    /**
     * @return List of all Noun phrases of the document. The phrases are ordered by appearance.
     */
    public List<? extends Phrase> getNounPhrases();


    /**
     * @return list of phrase of the specific phrasetype contained in the sentence
     * @param type type of phrase
     */
    public List<? extends Phrase> getPhrasesOfType(PhraseType type);

    /**
     * @return List of all sentences within the document. The sentences are ordered by appearance.
     */
    public List<? extends Sentence> getSentences();

    /**
     * @return List of all words within the document. The words are ordered by appearance.
     */
    public List<? extends Word> getWords();

    public List<? extends Coreference> getCoreferences();

    public List<? extends Word> getNamedEntityWords();

    @Override
    public default void annotate(Document document, Annotation annotation) {
        document.annotate(annotation);
    }

    /**
     * Annotates the given annotation to this document.
     *
     * @param annotation
     *            {@link Annotation} that should be annotated to this document.
     */
    public void annotate(Annotation annotation);
}
