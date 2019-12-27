package ru.smartel.finder;

import java.util.List;
import java.util.Map;

/**
 * Searches for the text in files
 */
public interface FulltextFinder {

    /**
     * Search for a words and get match scores for evey file
     * @param query words to look for (non empty list)
     * @return score percentage (from 0 to 100) for every file. Empty map if no matches
     */
    Map<String, Integer> findWordsGetScores(List<String> query);
}
