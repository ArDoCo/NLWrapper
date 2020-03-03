package edu.kit.ipd.are.archdoclink.nlp;

/**
 * Classes implementing this interface show that they are annotatable to a document.
 * 
 * @author Jan Keim
 *
 */
public interface Annotatable {
    /**
     * Annotates the given annotation to the given document
     * 
     * @param document
     *            Document where the annotation should be added to
     * @param annotation
     *            Annotation that should be added
     */
    // TODO try to remove Document
    public void annotate(Document document, Annotation annotation);
}
