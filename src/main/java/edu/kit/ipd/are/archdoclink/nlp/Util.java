package edu.kit.ipd.are.archdoclink.nlp;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

import edu.kit.ipd.parse.luna.graph.IArc;
import edu.kit.ipd.parse.luna.graph.IArcType;
import edu.kit.ipd.parse.luna.graph.IGraph;
import edu.kit.ipd.parse.luna.graph.INode;
import edu.kit.ipd.parse.luna.graph.INodeType;
import edu.kit.ipd.parse.luna.graph.ParseGraph;

/**
 * Utility class that provides some utility functions.
 * 
 * @author Jan Keim
 *
 */
public final class Util {
    private static Logger logger = Logger.getLogger(Util.class);

    private static final String TOKEN_NODE_TYPE = "token";
    private static final String VALUE = "value";

    private static final String ARC_TYPE_NAME = "relation";
    private static final String ARC_TYPE_VALUE = "NEXT";

    private Util() {
    }

    /**
     * Returns the "main" INodes in order. This means that "token"-type INodes are collected and returned in a sorted
     * list. Sorting is based on the "next"-arc, starting with the first utterance node.
     * 
     * @param graph
     *            Graph representing an input
     * @return Sorted List of "token"-INodes
     */
    public static ImmutableList<INode> getINodesInOrder(IGraph graph) {
        if (!(graph instanceof ParseGraph)) {
            logger.error("Graph is no ParseGraph!");
            return Lists.immutable.empty();
        }
        ParseGraph parseGraph = (ParseGraph) graph;
        IArcType arcType = graph.getArcType(ARC_TYPE_NAME);

        MutableList<INode> orderedNodes = Lists.mutable.empty();
        INode node = parseGraph.getFirstUtteranceNode();
        if (node == null) {
            return orderedNodes.toImmutable();
        }

        while (orderedNodes.add(node)) {
            if (node.getNumberOfOutgoingArcs() < 1) {
                break;
            }
            for (IArc arc : node.getOutgoingArcsOfType(arcType)) {
                Object value = arc.getAttributeValue(VALUE);
                if (value.equals(ARC_TYPE_VALUE)) {
                    node = arc.getTargetNode();
                    break;
                }
            }
        }
        return orderedNodes.toImmutable();
    }

    /**
     * Returns whether a given node has type "token" within the given graph
     * 
     * @param graph
     *            the Graph
     * @param node
     *            the Node that should be checked
     * @return true if the node has type "token"
     */
    public static boolean nodeHasTokenTypeInIGraph(IGraph graph, INode node) {
        return getTokenINodeType(graph).equals(node.getType());
    }

    /**
     * Returns the {@link INodeType} for "token". If the given graph does not have such a type yet, the token is created
     * 
     * @param graph
     *            Graph to operate on
     * @return The {@link INodeType} for "token".
     */
    public static INodeType getTokenINodeType(IGraph graph) {
        return graph.hasNodeType(TOKEN_NODE_TYPE) ? graph.getNodeType(TOKEN_NODE_TYPE)
                : graph.createNodeType(TOKEN_NODE_TYPE);
    }

    /**
     * Returns the String value of the attribute "value" for a given node.
     * 
     * @param node
     *            the node
     * @return the String value of the attribute "value" for a given node.
     */
    public static String getINodeValue(INode node) {
        return String.valueOf(node.getAttributeValue(VALUE));
    }

    public static String getTextFromWords(Stream<? extends Word> words) {
    	return getTextFromWords(words.collect(Collectors.toList()));
    }
        
    public static String getTextFromWords(List<? extends Word> words) {
        if (words.isEmpty()) {
            return "";
        }
        StringBuilder textBuilder = new StringBuilder(words.get(0)
                                                           .getText());

        for (int i = 1; i < words.size(); i++) {
            Word word = words.get(i);
            String text = word.getText();
            if (!word.isPunctuation() && !text.startsWith("'")) {
                textBuilder.append(" ");
            }
            textBuilder.append(text);
        }

        return textBuilder.toString();
    }

    /**
     * Based on Levenshtein Distance, that then is normalized with the length of the longer String.
     * 
     * @param first
     *            first String
     * @param second
     *            second String
     * @return normalized Levenshtein Distance
     */
    public static double relativeDistance(String first, String second) {
        first = first.toLowerCase();
        second = second.toLowerCase();
        int[] costs = new int[second.length() + 1];
        for (int j = 0; j < costs.length; j++) {
            costs[j] = j;
        }

        for (int i = 1; i <= first.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= second.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        first.charAt(i - 1) == second.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }

        double distance = (double) costs[second.length()];
        double length = (double) Math.max(first.length(), second.length());
        double relativeDistance = 1 - (distance / length);
        return Double.isNaN(relativeDistance) ? 0 : relativeDistance;
    }
}
