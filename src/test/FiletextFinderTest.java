package test;

import javafx.util.Pair;
import ru.smartel.finder.FiletextFinderImpl;
import ru.smartel.finder.FulltextFinder;
import ru.smartel.repository.FilesWordsRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FiletextFinderTest {

    @ParameterizedTest
    @MethodSource("source1")
    void whenSearchInDefinedFiles_thenFindExpected(List<String> query,
                                                   Map<String, Integer> expected) {
        FilesWordsRepository repository = Mockito.mock(FilesWordsRepository.class);

        Map<String, Set<String>> availableFilesWords = new HashMap<>();
        availableFilesWords.put("file1", new HashSet<>(Arrays.asList("foo", "bar", "baz")));
        availableFilesWords.put("file2", new HashSet<>(Arrays.asList("foo", "foo", "foo")));
        availableFilesWords.put("file3", new HashSet<>(Arrays.asList("bar", "boo")));

        Mockito.when(repository.getFileNames()).thenReturn(availableFilesWords.keySet());
        for (Map.Entry<String, Set<String>> available : availableFilesWords.entrySet()) {
            String fileName = available.getKey();
            for (String word : available.getValue()) {
                Mockito.when(repository.fileContainsWord(fileName, word)).thenReturn(true);
            }
        }

        FulltextFinder finder = new FiletextFinderImpl(repository);
        Map<String, Integer> actual = finder.findWordsGetScores(query);
        assertThat(actual)
                .as("Should return expected match scores for each file")
                .containsExactlyInAnyOrderEntriesOf(expected);
    }

    private static Stream<Arguments> source1() {
        return Stream.of(
                Arguments.of(
                        Arrays.asList("foo", "bar"),
                        Stream.of(new Pair<>("file1", 100), new Pair<>("file2", 50), new Pair<>("file3", 50))
                                .collect(Collectors.toMap(Pair::getKey, Pair::getValue))),
                Arguments.of(
                        Arrays.asList("zoo", "bee"),
                        new HashMap<>()),
                Arguments.of(
                        Arrays.asList("boo", "foo"),
                        Stream.of(new Pair<>("file1", 50), new Pair<>("file2", 50), new Pair<>("file3", 50))
                                .collect(Collectors.toMap(Pair::getKey, Pair::getValue))),
                Arguments.of(
                        Arrays.asList("foo", "foo", "foo"),
                        Stream.of(new Pair<>("file1", 100), new Pair<>("file2", 100))
                                .collect(Collectors.toMap(Pair::getKey, Pair::getValue)))
        );
    }
}
