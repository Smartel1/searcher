package ru.smartel.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FilesWordsRepositoryImpl implements FilesWordsRepository {

    /**
     * Key = file name
     * value = set of words from file
     */
    private Map<String, Set<String>> filesWords = new HashMap<>();

    /**
     * Cache for words that we've already looked for
     * Key = file name
     * Value = Word-Result map
     */
    private Map<String, Map<String, Boolean>> cache = new HashMap<>();

    @Override
    public void addFileWord(String fileName, String word) {
        //add word to file (if it doesn't exist yet)
        Set<String> fileWords = filesWords.computeIfAbsent(fileName, unused -> new HashSet<>());
        fileWords.add(word);
    }

    @Override
    public Set<String> getFileNames() {
        return filesWords.keySet();
    }

    @Override
    public boolean fileContainsWord(String fileName, String word) {
        if (!filesWords.containsKey(fileName)) throw new IllegalArgumentException("No such file");
        Map<String, Boolean> fileCache = cache.computeIfAbsent(fileName, unused -> new HashMap<>());
        return fileCache.computeIfAbsent(word, unused -> filesWords.get(fileName).contains(word));
    }
}
