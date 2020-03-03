package edu.kit.ipd.are.archdoclink.nlp.parse;

import java.util.Objects;

import edu.kit.ipd.are.archdoclink.nlp.Annotation;
import edu.kit.ipd.are.archdoclink.nlp.Coreference;
import edu.kit.ipd.are.archdoclink.nlp.Document;
import edu.kit.ipd.are.archdoclink.nlp.Word;

public class ParseCoreference implements Coreference {
    private Word word;
    private Word referencedWord;

    public ParseCoreference(Word word, Word referencedWord) {
        this.word = word;
        this.referencedWord = referencedWord;
    }

    @Override
    public String getText() {
        return word.getText();
    }

    @Override
    public Word getReferencedWord() {
        return referencedWord;
    }

    @Override
    public Word getWord() {
        return word;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(referencedWord, word);
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
        if (!(obj instanceof ParseCoreference)) {
            return false;
        }
        ParseCoreference other = (ParseCoreference) obj;
        return Objects.equals(referencedWord, other.referencedWord) && Objects.equals(word, other.word);
    }

    @Override
    public void annotate(Document document, Annotation annotation) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not yet supported");
    }

}
