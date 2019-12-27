package ru.smartel.repository;

import java.util.Set;

/**
 * Repository for files and words from these files
 */
public interface FilesWordsRepository {
    /**
     * Add word to file. If word have been added before, do nothing
     * @param fileName file to add word to
     * @param word word
     */
    void addFileWord(String fileName, String word);

    /**
     * Get names of files
     * @return set of filenames
     */
    Set<String> getFileNames();

    /**
     * Search for a word in single file
     * @param word word to look for
     * @return true if file contains the word
     */
    boolean fileContainsWord(String fileName, String word);
}
