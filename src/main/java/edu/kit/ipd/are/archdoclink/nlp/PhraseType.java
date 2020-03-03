package edu.kit.ipd.are.archdoclink.nlp;

public enum PhraseType {
	// TODO: Missing Phrase Type. See ChunkIOBENUM
    NOUN_PHRASE("NP"), PREPROSITIONAL_PHRASE("PP"), VERB_PHRASE("VP"), ADVERB_PHRASE("ADVP"), ADJECTIVE_PHRASE(
            "ADJP"), CONJUNCTION_PHRASE("CONJP"), SUBORDINATING_CONJUNCTION("SBAR"), UNDEFINED("UNDEF");

    private String type;

    private PhraseType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static PhraseType getPhraseType(String type) {
        for (PhraseType pType : PhraseType.values()) {
            if (pType.type.equals(type)) {
                return pType;
            }
        }
        return UNDEFINED;
    }

}
