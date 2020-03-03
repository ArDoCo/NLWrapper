package edu.kit.ipd.are.archdoclink.nlp.parse;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

import edu.kit.ipd.are.archdoclink.nlp.Annotation;
import edu.kit.ipd.are.archdoclink.nlp.Document;
import edu.kit.ipd.are.archdoclink.nlp.Word;
import edu.kit.ipd.parse.luna.graph.INode;
import edu.kit.ipd.parse.luna.graph.IType;

public class ParseWord implements Word {
    private static final String NER_ATTRIBUTE_NAME = "ner";

    private static final String NER_OTHER = "O";
    private static final String CHUNK_START = "B-";
    private static final String INSTRUCTION_NUMBER = "instructionNumber";
    private static final String SUCCESSORS = "successors";
    private static final String CHUNK_NAME = "chunkName";
    private static final String LEMMA = "lemma";
    private static final String CHUNK_IOB = "chunkIOB";
    private static final String POS = "pos";
    private static final String VALUE = "value";
    private static final String SENTENCE_NUMBER = "sentenceNumber";

    private static final ImmutableList<String> PUNCTUATION = Lists.immutable.of(".", ",", ":", "!", "?", ")");

    private INode node;

    public ParseWord(INode node) {
        this.node = node;
    }

    @Override
    public boolean isPunctuation() {
        String pos = getPos();
        // when there is a hyphen, it's POS is often ":", which leads to wrong result
        // therefore: explicit check for "-"
        return PUNCTUATION.contains(pos) && !getText().equals("-");
    }

    @Override
    public String getPos() {
        return String.valueOf(node.getAttributeValue(POS));
    }

    @Override
    public String getText() {
        return String.valueOf(node.getAttributeValue(VALUE));
    }

    /**
     * @return the chunkIOB
     */
    public String getChunkIOB() {
        return String.valueOf(node.getAttributeValue(CHUNK_IOB));
    }

    /**
     * @return the chunk name
     */
    public String getChunkName() {
        return String.valueOf(node.getAttributeValue(CHUNK_NAME));
    }

    /**
     * @return the successors value
     */
    public int getSuccessors() {
        return (int) node.getAttributeValue(SUCCESSORS);
    }

    /**
     * @return the instruction number
     */
    public int getInstructionNumber() {
        return (int) node.getAttributeValue(INSTRUCTION_NUMBER);
    }

    /**
     * @return the sentence number
     */
    public int getSentenceNumber() {
        return (int) node.getAttributeValue(SENTENCE_NUMBER);
    }

    /**
     * @return true if the word is at the start of a chunk
     */
    public boolean isChunkStart() {
        return getChunkIOB().startsWith(CHUNK_START);
    }

    @Override
    public String getLemma() {
        return String.valueOf(node.getAttributeValue(LEMMA));
    }

    @Override
    public boolean isNamedEntity() {
        Object nerAttribute = node.getAttributeValue(NER_ATTRIBUTE_NAME);
        return (nerAttribute != null && !nerAttribute.toString()
                                                     .equals(NER_OTHER));
    }

    // TODO: maybe there should be a check if the annotation should be on the word-node or be an own node referencing
    // the word-node
    @Override
    public void annotate(Document document, Annotation annotation) {
        IType type = node.getType();
        String typeAsString = annotation.getAnnotationObject()
                                        .getClass()
                                        .getTypeName();
        String attributeName = annotation.getValueName();
        if (!type.containsAttribute(attributeName, typeAsString)) {
            type.addAttributeToType(typeAsString, attributeName);
        }

        node.setAttributeValue(attributeName, annotation.getAnnotationObject());
    }

    /**
     * @return the underlying node of this word
     */
    INode getNode() {
        return node;
    }

    @Override
    public String toString() {
        return node.toString();
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
        result = prime * result + ((node == null) ? 0 : node.hashCode());
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
        if (!(obj instanceof ParseWord)) {
            return false;
        }
        ParseWord other = (ParseWord) obj;
        if (node == null) {
            if (other.node != null) {
                return false;
            }
        } else if (!node.equals(other.node)) {
            return false;
        }
        return true;
    }
}
