package edu.kit.ipd.are.archdoclink.nlp;

/**
 * Classes implementing this interface represent an Element that has an underlying text. For example a {@link Word} is
 * such an Element with the String of the word as text.
 * 
 * @author Jan Keim
 *
 */
public interface TextElement extends Annotatable {
    /**
     * @return the text of the element
     */
    public String getText();
}
