package edu.kit.ipd.are.archdoclink.nlp;

import java.util.List;

/**
 * Classes implementing this interface represent a sentence within a document. A sentence should have reference to the
 * previous and next sentence.
 *
 * @author Jan Keim
 *
 */
public interface Sentence extends TextElement, Identifiable {
    /**
     * @return the document the sentence is contained in
     */
	//TODO: Why should I need to retrieve a document from a sentence-Object, but from a word-object not?
    public Document getDocument();

    /**
     * @return the words contained in the sentence
     */
    public List<? extends Word> getWords();

    /**
     * @return list of phrases contained in the sentence
     */
    public List<? extends Phrase> getPhrases();

    /**
     * @return list of noun phrase contained in the sentence
     */
    public List<? extends Phrase> getNounPhrases();


    /**
     * @return list of phrase of the specific phrasetype contained in the sentence
     * @param type type of phrase
     */
    public List<? extends Phrase> getPhrasesOfType(PhraseType type);


    @Override
    public default void annotate(Document document, Annotation annotation) {
        document.annotate(annotation);
    }

    /**
     * Annotates the sentence to the document. Should be similar to {@link #annotate(Document, Annotation)}, but uses
     * {@link #getDocument()} for the document.
     *
     * @param annotation the annotation that should be annotated
     */
    public void annotate(Annotation annotation);
}
