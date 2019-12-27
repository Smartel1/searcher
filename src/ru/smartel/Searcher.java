package ru.smartel;

import ru.smartel.finder.FiletextFinderImpl;
import ru.smartel.repository.FilesWordsRepositoryImpl;
import ru.smartel.finder.FulltextFinder;
import ru.smartel.repository.FilesWordsRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class Searcher {
    private static final String EXIT_KEY = ":quit";

    private static FilesWordsRepository wordsRepository = new FilesWordsRepositoryImpl();
    private static FulltextFinder finder = new FiletextFinderImpl(wordsRepository);

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                throw new IllegalArgumentException("No directory given to index.");
            }

            final Path indexableDirectory = Paths.get(args[0]);
            if (!indexableDirectory.toFile().exists()) {
                throw new IllegalArgumentException("Specified directory doesn't exist.");
            }

            memorizeFileContents(wordsRepository, indexableDirectory);

            try (Scanner keyboard = new Scanner(System.in)) {
                while (true) {
                    System.out.print("search> ");
                    final String line = keyboard.nextLine();
                    if (EXIT_KEY.equals(line)) return;

                    List<String> words = Arrays.asList(line.split(" "));
                    searchAndPrintBestMatches(words);
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    /**
     * Read files of directory and fill repository
     * @param repository files and words repository
     * @param directory directory path
     */
    private static void memorizeFileContents(FilesWordsRepository repository, Path directory) {
        try (Stream<Path> pathsToFiles = Files.walk(directory, 1)) {
            long filesFound = pathsToFiles.map(Path::toFile)
                    .filter(File::isFile)
                    .peek(file -> {
                        try {
                            Scanner sc = new Scanner(file);
                            // send every word to repository
                            while (sc.hasNext()) {
                                String word = sc.next();
                                repository.addFileWord(file.getName(), word);
                            }
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException("File not found: " + file.getName()); //should never happen
                        }
                    })
                    .count();
            System.out.println(filesFound + " files have been read in directory " + directory);
        } catch (IOException e) {
            throw new RuntimeException("Files read error. " + e.getMessage());
        }
    }

    /**
     * Search for given words in every file and print score board
     * @param words words to look for
     */
    private static void searchAndPrintBestMatches(List<String> words) {
        Map<String, Integer> searchResult = finder.findWordsGetScores(words);
        if (searchResult.isEmpty()) {
            System.out.println("no matches found");
        }
        for (Map.Entry<String, Integer> entry : searchResult.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + "%");
        }
    }
}