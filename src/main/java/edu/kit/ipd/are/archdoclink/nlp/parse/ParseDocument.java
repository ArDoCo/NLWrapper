package edu.kit.ipd.are.archdoclink.nlp.parse;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.list.mutable.FastList;

import edu.kit.ipd.are.archdoclink.nlp.Annotation;
import edu.kit.ipd.are.archdoclink.nlp.Document;
import edu.kit.ipd.are.archdoclink.nlp.PhraseType;
import edu.kit.ipd.are.archdoclink.nlp.Util;
import edu.kit.ipd.parse.luna.graph.IArc;
import edu.kit.ipd.parse.luna.graph.IArcType;
import edu.kit.ipd.parse.luna.graph.IGraph;
import edu.kit.ipd.parse.luna.graph.INode;
import edu.kit.ipd.parse.luna.graph.INodeType;

public class ParseDocument implements Document {
    private static final String STRING_TYPE = "String";
    private static final String TEXT_ATTRIBUTE = "text";
    private static final String INCLUDES = "includes";
    private static final String PHRASE_TYPE = "phrase";
    private static final String CONTEXT_RELATION_ARC_TYPE = "contextRelation";
    private final String documentId;
    private IGraph graph;
    private MutableList<ParseSentence> sentences = null;
    private final Calendar processingDate;

    int sentenceCount = -1;

    public ParseDocument(IGraph graph, String documentId) {
        this.graph = graph;
        this.documentId = documentId;
        this.processingDate = Calendar.getInstance();
    }

    private MutableList<INode> getTokens() {
        return FastList.newList(Util.getINodesInOrder(graph))
                       .select(node -> Util.nodeHasTokenTypeInIGraph(graph, node));
    }

    @Override
    public MutableList<ParseWord> getNamedEntityWords() {
        return getWords().select(ParseWord::isNamedEntity);
    }

    @Override
    public MutableList<ParseWord> getWords() {
        MutableList<INode> nodes = getTokens();
        return nodes.collect(ParseWord::new);
    }

    @Override
    public MutableList<ParsePhrase> getPhrases() {
        return getSentences().flatCollect(ParseSentence::getPhrases);
    }

    @Override
    public MutableList<ParsePhrase> getNounPhrases() {
        return getPhrasesOfType(PhraseType.NOUN_PHRASE);
    }
    

	@Override
	public MutableList<ParsePhrase> getPhrasesOfType(PhraseType type) {
        return getPhrases().select(phrase -> phrase.getType()
                .equals(type));
	}

    @Override
    public synchronized MutableList<ParseSentence> getSentences() {
        if (sentences == null) {
            createSentenceList();
        }
        return this.sentences;
    }

    private void createSentenceList() {
        sentences = Lists.mutable.empty();
        ParseSentence currSentence = null;

        for (INode token : getTokens()) {
            ParseWord word = new ParseWord(token);
            int sentenceNumber = word.getSentenceNumber();
            if (sentenceNumber > sentenceCount) {
                sentenceCount = sentenceNumber;
                currSentence = new ParseSentence(this);
                sentences.add(currSentence);
            }
            if (currSentence != null) {
                // should not be null, but to be sure
                currSentence.appendWord(word);
            }
        }
    }

    @Override
    public String getText() {
    	//TODO: use the Util class
        StringBuilder textBuilder = new StringBuilder();
        for (ParseSentence sentence : getSentences()) {
            textBuilder.append(sentence.getText());
            textBuilder.append(" ");
        }
        return textBuilder.toString()
                          .trim();
    }

    @Override
    public List<ParseCoreference> getCoreferences() {
        // TODO improve!
        List<ParseCoreference> corefList = Lists.mutable.empty();

        IArcType contextRelationType = graph.getArcType(CONTEXT_RELATION_ARC_TYPE);
        List<IArc> arcs = graph.getArcsOfType(contextRelationType);
        for (IArc arc : arcs) {
            String arcAttributeName = arc.getAttributeValue("name")
                                         .toString();
            if (arcAttributeName.equals("anaphoraReferent")) {
                ParseWord sourceWord = new ParseWord(arc.getSourceNode());
                ParseWord targetWord = new ParseWord(arc.getTargetNode());
                corefList.add(new ParseCoreference(sourceWord, targetWord));
            }
        }
        return corefList;
    }

    @Override
    public String getId() {
        return documentId;
    }

    @Override
    public Calendar getProcessingDate() {
        return processingDate;
    }

    @Override
    public String toDebugString() {
        return graph.showGraph();
    }

    @Override
    public String toExtensiveDebugString() {
        StringBuilder builder = new StringBuilder("\n");
        builder.append(graph.showGraph());
        builder.append("\n");
        builder.append(Util.getINodesInOrder(graph)
                           .makeString("\n"));
        builder.append("\n");
        return builder.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(documentId);
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
        if (!(obj instanceof ParseDocument)) {
            return false;
        }
        ParseDocument other = (ParseDocument) obj;
        return Objects.equals(documentId, other.documentId);
    }

    @Override
    public String toString() {
        return "ParseDocument [documentId=" + documentId + "]";
    }

    @Override
    public void annotate(Annotation annotation) {
        INodeType type;
        if (graph.hasNodeType(annotation.getName())) {
            type = graph.getNodeType(annotation.getName());
        } else {
            type = graph.createNodeType(annotation.getName());
        }
        INode node = graph.createNode(type);

        String typeAsString = annotation.getAnnotationObject()
                                        .getClass()
                                        .getSimpleName();
        String attributeName = annotation.getValueName();
        if (!type.containsAttribute(attributeName, typeAsString)) {
            type.addAttributeToType(typeAsString, attributeName);
        }

        node.setAttributeValue(attributeName, annotation.getAnnotationObject());
    }

    private Optional<INode> getAnnotationNode(Annotation annotation) {
        INodeType type = graph.getNodeType(annotation.getName());
        if (type == null) {
            return Optional.empty();
        }
        String attributeName = annotation.getValueName();
        for (INode node : graph.getNodesOfType(type)) {
            Object value = node.getAttributeValue(attributeName);
            if (value.equals(annotation.getAnnotationObject())) {
                return Optional.of(node);
            }
        }
        return Optional.empty();
    }

    /**
     * Creates a reference from the given annotation to the given words. Searches the graph for the {@link INode}
     * corresponding to the annotation or throws an {@link IllegalArgumentException} when no node is found.
     * 
     * @param words
     *            Words that should be referenced
     * @param annotation
     *            Annotation that references the word
     */
    public void createReference(List<ParseWord> words, Annotation annotation) {
        INode annotationNode = getAnnotationNode(annotation).orElseThrow(IllegalArgumentException::new);

        IArcType arcType = createArcType(annotation.getArcName());

        for (ParseWord word : words) {
            graph.createArc(annotationNode, word.getNode(), arcType);
        }
    }

    /**
     * Creates a reference from the given node to the given annotation. The annotation needs to have an arcName set.
     * 
     * @param node
     *            Node where the reference begins
     * @param annotation
     *            annotation where the reference should head to
     */
    public void createReference(INode node, Annotation annotation) {
        INode annotationNode = getAnnotationNode(annotation).orElseThrow(IllegalArgumentException::new);

        IArcType arcType = createArcType(annotation.getArcName());

        graph.createArc(node, annotationNode, arcType);

    }

    private IArcType createArcType(String arcName) {
        if (arcName == null) {
            throw new IllegalArgumentException("Invalid arcName (is null)");
        }
        IArcType arcType = graph.getArcType(arcName);
        if (arcType == null) {
            arcType = graph.createArcType(arcName);
        }
        return arcType;
    }

    /**
     * Creates a node for a phrase.
     * 
     * @param phrase
     *            Phrase for which a node should be created
     * @return the created node
     */
    public INode createNodeForPhrase(ParsePhrase phrase) {
        INode node = getOrCreatePhraseNode(phrase);

        node.setAttributeValue(TEXT_ATTRIBUTE, phrase.getText());

        IArcType arcType = createArcType(INCLUDES);

        for (ParseWord word : phrase.getWords()) {
            graph.createArc(node, word.getNode(), arcType);
        }

        return node;
    }

    private INode getOrCreatePhraseNode(ParsePhrase phrase) {
        INodeType phraseType = graph.getNodeType(PHRASE_TYPE);
        if (phraseType == null) {
            phraseType = graph.createNodeType(PHRASE_TYPE);
        }
        if (!phraseType.containsAttribute(TEXT_ATTRIBUTE, STRING_TYPE)) {
            phraseType.addAttributeToType(STRING_TYPE, TEXT_ATTRIBUTE);
        }

        INode node = null;
        for (INode phraseNode : graph.getNodesOfType(phraseType)) {
            String value = phraseNode.getAttributeValue(TEXT_ATTRIBUTE)
                                     .toString();
            if (phrase.getText()
                      .equals(value)) {
                node = phraseNode;
                break;
            }
        }
        if (node == null) {
            node = graph.createNode(phraseType);
        }
        return node;
    }

}
