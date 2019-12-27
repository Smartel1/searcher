package test;

import ru.smartel.repository.FilesWordsRepository;
import ru.smartel.repository.FilesWordsRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FilesWordsRepositoryTest {

    @Test
    void returnsOnlyExpectedFilenames() {
        FilesWordsRepository repository = new FilesWordsRepositoryImpl();
        // file1
        repository.addFileWord("file1", "foo");
        repository.addFileWord("file1", "bar");
        // file2
        repository.addFileWord("file2", "foo");
        repository.addFileWord("file2", "baz");

        assertThat(repository.getFileNames())
                .as("Should return expected set of files")
                .containsExactlyInAnyOrder("file1", "file2");

        assertThat(repository.getFileNames())
                .as("Should not return nonexistent files")
                .doesNotContain("file3", "file4");
    }

    @ParameterizedTest
    @MethodSource("source1")
    void whenSearchInDefinedFiles_thenFindExpected(String file, String word, Boolean expectedResult) {
        FilesWordsRepository repository = new FilesWordsRepositoryImpl();
        // file1
        repository.addFileWord("file1", "foo");
        repository.addFileWord("file1", "bar");
        repository.addFileWord("file1", "bar"); //same word twice
        // file2
        repository.addFileWord("file2", "foo");
        repository.addFileWord("file2", "baz");

        assertThat(repository.fileContainsWord(file, word))
                .as("Should return expected result when is asked for word in file")
                .isEqualTo(expectedResult);
    }

    private static Stream<Arguments> source1() {
        return Stream.of(
                Arguments.of("file1", "foo", true),
                Arguments.of("file1", "bar", true),
                Arguments.of("file1", "baz", false),
                Arguments.of("file1", "bee", false),
                Arguments.of("file2", "foo", true),
                Arguments.of("file2", "baz", true),
                Arguments.of("file2", "fee", false)
        );
    }
}
