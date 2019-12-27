## Text search test application
spec: https://drive.google.com/file/d/1ya_eAKQsYV6COvPiLSCEFs0gtJwn4-Yn/view

Requires Java8.

Allows to search for words in files. Reads files in given directory and receives words to look for.
Returns top10 files with match percentage:
```
$ java -jar SimpleSearch.jar Searcher /foo/bar
4 files have been read in directory .
search> to be or not to be
file1.txt:100%
file2.txt:90%
search>
search> cats
no matches found
search> :quit
```

## Installation and usage
1. Compile code and build jar
    ```
    $ javac src/ru/smartel/Searcher.java -cp src -d out
    $ cd out 
    $ jar cmvf ../MANIFEST.MF SimpleSearch.jar *
    ```
2. Run application with a single argument: path to directory. Path can be absolute or relational.

    You will see following lines:
    ```
    $ java -jar SimpleSearch.jar /foo/bar
    4 files have been read in directory .
    search>
    ```

3. Write 1 or more words you want to find. Press 'enter'
4. Program will print top10 files with match percentage:
    ```
    search> one two three four five
    file.txt: 100%    // every single word found
    t1.txt: 20%       // 1/5 of given words found
    search>
    ``` 
5. Write ```:quit``` to stop
## Tests 
Tests require dependencies:
```
 org.assertj:assertj-core:3.14.0
 org.junit.jupiter:junit-jupiter:5.5.2
 org.mockito:mockito-all:1.10.19
 org.mockito:mockito-junit-jupiter:3.2.4
```

 