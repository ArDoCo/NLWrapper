package edu.kit.ipd.are.archdoclink.nlp;

/**
 * Classes implementing this interface provide methods to get debug Strings.
 * 
 * @author Jan Keim
 *
 */
interface DebugPrintable {
    /**
     * @return a string containing debug information
     */
    String toDebugString();

    /**
     * @return a string containing extensive debug information
     */
    String toExtensiveDebugString();
}
