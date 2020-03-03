package edu.kit.ipd.are.archdoclink.nlp.parse;

import java.util.List;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

import edu.kit.ipd.are.archdoclink.nlp.Annotation;
import edu.kit.ipd.are.archdoclink.nlp.Document;
import edu.kit.ipd.are.archdoclink.nlp.Phrase;
import edu.kit.ipd.are.archdoclink.nlp.PhraseType;
import edu.kit.ipd.are.archdoclink.nlp.Util;
import edu.kit.ipd.are.archdoclink.nlp.Word;
import edu.kit.ipd.parse.luna.graph.INode;

public class ParsePhrase implements Phrase {
    private MutableList<ParseWord> words;
    private PhraseType type;

    public ParsePhrase(List<INode> containedNodes, String type) {
        this.words = Lists.mutable.ofAll(containedNodes)
                                  .collect(ParseWord::new);
        this.type = PhraseType.getPhraseType(type);
    }

    public ParsePhrase(String type) {
        this.words = Lists.mutable.empty();
        this.type = PhraseType.getPhraseType(type);
    }

    /**
     * Appends a word to this phrase. Same as {@link #addWord(ParseWord)}.
     *
     * @param word
     *            Word that should be added
     */
    public void appendWord(ParseWord word) {
        words.add(word);
    }

    /**
     * Prepends a word to this phrase.
     *
     * @param word
     *            Word that should be added
     */
    public void prependWord(ParseWord word) {
        addWord(word, 0);
    }

    /**
     * Adds a word to the end of this phrase.
     *
     * @param word
     *            Word that should be added
     */
    public void addWord(ParseWord word) {
        appendWord(word);
    }

    /**
     * Adds a word at the given position to this phrase.
     *
     * @param word
     *            Word that should be added
     * @param position
     *            Position where the word should be inserted
     */
    public void addWord(ParseWord word, int position) {
        if (position < 0 || position >= words.size()) {
            throw new IllegalArgumentException("Invalid index!");
        }
        words.add(position, word);
    }

    @Override
    public PhraseType getType() {
        return this.type;
    }

    @Override
    public MutableList<ParseWord> getWords() {
        return words;
    }

    @Override
    public boolean containsWord(Word word) {
        if (!(word instanceof ParseWord)) {
            return false;
        }
        return words.contains(word);
    }

    @Override
    public String getText() {
        return Util.getTextFromWords(words);
    }

    @Override
    public String toString() {
        return getText();
    }

    @Override
    public String getTextWithoutLeadingStopwords() {
        if (words.isEmpty()) {
            return "";
        }
        MutableList<Word> wordsCpy = Lists.mutable.ofAll(words);
        int index = 0;
        while (index < words.size() && words.get(index)
                    .isStopword()) {
            wordsCpy = wordsCpy.drop(++index);
        }
        return Util.getTextFromWords(wordsCpy);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((words == null) ? 0 : words.hashCode());
        return result;
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
        if (!(obj instanceof ParsePhrase)) {
            return false;
        }
        ParsePhrase other = (ParsePhrase) obj;
        if (words == null) {
            if (other.words != null) {
                return false;
            }
        } else if (!words.equals(other.words)) {
            return false;
        }
        return true;
    }

    @Override
    public void annotate(Document document, Annotation annotation) {
        // first create the annotation for the document
        document.annotate(annotation);

        // then create reference if the document is a ParseDocument (that it should be)
        if (document instanceof ParseDocument) {
            ParseDocument parseDocument = (ParseDocument) document;
            INode phraseNode = parseDocument.createNodeForPhrase(this);
            parseDocument.createReference(phraseNode, annotation);
        }
    }
}
