package edu.kit.ipd.are.archdoclink.nlp;

import java.util.List;
import java.util.Optional;

/**
 * Interface for NaturalLanguageProcessing. Classes implementing this interface can be used to process texts or
 * {@link Document}s.
 * 
 * @author Jan Keim
 *
 */
public interface NaturalLanguageProcessing extends DebugPrintable {
    /**
     * Initialise the NLP with the given {@link Document}
     * 
     * @param document
     *            Document that should be able to be processed
     */
    public void init(Document document);

    /**
     * Initialise the NLP for the given text with the given Id.
     * 
     * @param text
     *            text that should be able to be processed
     * @param documentId
     *            Id of the text/document
     */
    public void init(String text, String documentId);

    /**
     * @return List of NamedEntities that were found within the init. text
     */
    public List<? extends Word> getNamedEntityWords();

    /**
     * @return List of words that were found within the init. text.
     */
    public List<? extends Word> getWords();

    /**
     * @return List of NounPhrases that were found within the init. text.
     */
    public List<? extends Phrase> getNounPhrases();

    /**
     * @return List of Phrases that were found within the init. text.
     */
    public List<? extends Phrase> getPhrases();

    /**
     * @return List of Sentences that were found within the init. text.
     */
    public List<? extends Sentence> getSentences();

    /**
     * @return List of Coreferences that were found within the init. text.
     */
    public List<? extends Coreference> getCoreferences();

    /**
     * Returns the Phrase the given word is contained in. If the word is not contained in any phrase, an empty
     * {@link Optional} is returned.
     * 
     * @param word
     *            Word that should be looked for in Phrases
     * @return Optional containing the Phrase the given word is contained in. Empty Optional if word is not contained in
     *         any phrase.
     */
    public Optional<? extends Phrase> getPhraseOfWord(Word word);

    /**
     * @return the document the NLP is initialised with.
     */
    public Document getDocument();

    /**
     * Adds the given {@link Annotation} to the provided {@link TextElement}
     * 
     * @param textElement
     *            textElement that should be annotated
     * @param annotation
     *            the Annotation
     */
    public void annotate(TextElement textElement, Annotation annotation);
}
