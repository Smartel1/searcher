package ru.smartel.finder;

import ru.smartel.repository.FilesWordsRepository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FiletextFinderImpl implements FulltextFinder {
    public static final int TOP_SIZE = 10;

    private FilesWordsRepository repository;

    public FiletextFinderImpl(FilesWordsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Map<String, Integer> findWordsGetScores(List<String> query) {
        Set<String> fileNames = repository.getFileNames();
        Map<String, Integer> result = new HashMap<>();

        //Check every file for words. If even a single word found - put to result
        for (String fileName : fileNames) {
           int score = findWordsGetScore(fileName, query);
           result.put(fileName, score);
        }

        return getBestMatches(result);
    }

    /**
     * Search for words in a single file and get match score (0-100)
     * @param fileName file name
     * @param query words to search (not empty list)
     * @return match score (0 - none found, 100 - every word found)
     */
    private int findWordsGetScore(String fileName, List<String> query) {
        int queryWordsCount = query.size();
        int wordsFound = 0;
        for (String word : query) {
            if (repository.fileContainsWord(fileName, word)) {
                wordsFound++;
            }
        }
        return Math.round(wordsFound * 100F / queryWordsCount);
    }

    private Map<String, Integer> getBestMatches(Map<String, Integer> scores) {
        return scores.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != 0)
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(TOP_SIZE)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
