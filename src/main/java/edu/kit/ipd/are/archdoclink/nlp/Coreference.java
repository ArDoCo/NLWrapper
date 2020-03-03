package edu.kit.ipd.are.archdoclink.nlp;

public interface Coreference extends TextElement {

    public Word getReferencedWord();

    public Word getWord();
}
