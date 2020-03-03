package edu.kit.ipd.are.archdoclink.nlp.parse;

import java.util.List;
import java.util.Objects;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

import edu.kit.ipd.are.archdoclink.nlp.Annotation;
import edu.kit.ipd.are.archdoclink.nlp.Document;
import edu.kit.ipd.are.archdoclink.nlp.Phrase;
import edu.kit.ipd.are.archdoclink.nlp.PhraseType;
import edu.kit.ipd.are.archdoclink.nlp.Sentence;
import edu.kit.ipd.are.archdoclink.nlp.Util;
import edu.kit.ipd.are.archdoclink.nlp.Word;

public class ParseSentence implements Sentence {
    private static final String ID_SEPARATOR = ".";
    private final String id;

    private MutableList<ParseWord> words = Lists.mutable.empty();
    private MutableList<ParsePhrase> phrases = null;
    private ParseDocument document;
    private Sentence nextSentence = null;
    private Sentence previouSentence = null;

    ParseSentence(ParseDocument document) {
        this.document = document;
        this.id = String.valueOf(document.sentenceCount);
    }

    /**
     * Appends a word to the sentence. See also {@link #addWord(ParseWord)}.
     * 
     * @param word
     *            the word that should be appended
     */
    public void appendWord(ParseWord word) {
        words.add(word);
    }

    /**
     * Prepends a word to the sentence.
     * 
     * @param word
     *            the word that should be prepended
     */
    public void prependWord(ParseWord word) {
        addWord(word, 0);
    }

    /**
     * Adds a word to the end of the sentence.
     * 
     * @param word
     *            the word that should be added
     */
    public void addWord(ParseWord word) {
        appendWord(word);
    }

    /**
     * Adds a word to the given position within the sentence.
     * 
     * @param word
     *            the word that should be added
     * @param position
     *            position where the word should be added
     */

    public void addWord(ParseWord word, int position) {
        if (position < 0 || position >= words.size()) {
            throw new IllegalArgumentException("Invalid index!");
        }
        words.add(position, word);
    }

    /**
     * Checks whether the sentence contains a given word.
     * 
     * @param word
     *            Word that should be checked for
     * @return true if the sentence contains the word
     */
    public boolean contains(Word word) {
        return words.contains(word);
    }

    /**
     * Checks whether the sentence contains a given phrase.
     * 
     * @param phrase
     *            phrase that should be checked for
     * @return true if the sentence contains the phrase
     */
    public boolean containsPhrase(Phrase phrase) {
        return getPhrases().contains(phrase);
    }

    @Override
    public List<ParsePhrase> getNounPhrases() {
        return getPhrasesOfType(PhraseType.NOUN_PHRASE);
    }
    

	@Override
	public List<ParsePhrase> getPhrasesOfType(PhraseType type) {
        return getPhrases().select(phrase -> phrase.getType()
                .equals(type));
	}

    /**
     * @return returns the phrases of this sentence
     */
    public synchronized MutableList<ParsePhrase> getPhrases() {
        if (this.phrases == null) {
            createPhraseList();
        }
        return this.phrases;
    }

    // TODO: Problem: sometimes the sentences are discovered wrongly, which collides with successors (as they are mostly
    // correct)
    private MutableList<ParsePhrase> createPhraseList() {
        phrases = Lists.mutable.empty();
        int i = 0;
        while (i < words.size()) {
            ParseWord word = words.get(i++);
            if (word.isChunkStart()) {
                ParsePhrase phrase = new ParsePhrase(word.getChunkName());
                phrase.appendWord(word);
                int successors = word.getSuccessors();
                while (successors > 0 && i < words.size()) {
                    ParseWord successor = words.get(i++);
                    phrase.appendWord(successor);
                    successors--;
                }
                phrases.add(phrase);
            }
        }
        return phrases;
    }

    @Override
    public String getText() {
        return Util.getTextFromWords(words);
    }

    @Override
    public MutableList<ParseWord> getWords() {
        return this.words;
    }

    @Override
    public String getId() {
        return createSentenceId();
    }

    private String createSentenceId() {
        return document.getId() + ID_SEPARATOR + id;
    }

    @Override
    public Document getDocument() {
        return this.document;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(document, id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ParseSentence)) {
            return false;
        }
        ParseSentence other = (ParseSentence) obj;
        return Objects.equals(document, other.document) && Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "ParseSentence [id=" + id + ", text = \"" + getText() + "\"]";
    }

    @Override
    public void annotate(Annotation annotation) {
        // first create the annotation for the document
        document.annotate(annotation);

        // then create references if the document is a ParseDocument (what it should be)
        if (document instanceof ParseDocument) {
            ParseDocument parseDocument = (ParseDocument) document;
            parseDocument.createReference(words, annotation);
        }
    }


}
