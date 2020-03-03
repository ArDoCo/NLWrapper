package edu.kit.ipd.are.archdoclink.nlp;

/**
 * This class represents an annotation. The {@link AnnotationBuilder} should preferably be used to build an annotation.
 * An annotation always needs a name and an annotationObject. Optionally, an annotation can also have a specific name
 * for the value and the arc, that should be used to connect the annotation
 * 
 * @author Jan Keim
 *
 */
public class Annotation {
    private final String name;
    private final Object annotationObject;
    private String valueName;
    private String arcName;

    public Annotation(String name, Object annotationObject) {
        super();
        this.name = name;
        this.valueName = name;
        this.annotationObject = annotationObject;
        this.arcName = name;
    }

    /**
     * Builder that can be used to create {@link Annotation}s
     * 
     * @author Jan Keim
     *
     */
    public static class AnnotationBuilder {
        private Annotation annotation;

        public AnnotationBuilder(String name, Object annotationObject) {
            annotation = new Annotation(name, annotationObject);
        }

        public static AnnotationBuilder create(String name, Object annotationObject) {
            return new AnnotationBuilder(name, annotationObject);
        }

        public AnnotationBuilder withValueName(String valueName) {
            annotation.valueName = valueName;
            return this;
        }

        public AnnotationBuilder withArcName(String arcName) {
            annotation.arcName = arcName;
            return this;
        }

        public Annotation build() {
            return annotation;
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the valueName
     */
    public String getValueName() {
        return valueName;
    }

    /**
     * @return the annotationObject
     */
    public Object getAnnotationObject() {
        return annotationObject;
    }

    /**
     * @return the valueName
     */
    public String getArcName() {
        return arcName;
    }

    /**
     * @param valueName
     *            the valueName to set
     */
    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    /**
     * @param arcName
     *            the arcName to set
     */
    public void setArcName(String arcName) {
        this.arcName = arcName;
    }

    @Override
    public String toString() {
        return "Annotation [name=" + name + ", valueName=" + valueName + ", annotationObject=" + annotationObject + "]";
    }

}
