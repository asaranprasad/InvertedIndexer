# Inverted Indexer - Java Implementation

Task1: Generating the corpus
Task2: Implementing an inverted indexer and creating inverted indexes
Task3: Corpus statistics

## Setting up

The ./src/ folder consists of source code files
```
1. InvertedIndexerCaller.java  - Contains tests performs the tasks for the indexer.
2. InvertedIndexer.java        - Manages creation and maintenance of the inverted indexes and its associated datastructures.
3. IndexerConfig.java          - Configuration of the indexer such as io paths, document parser exclusion lists etc.
4. CorpusGenerator.java        - Consists of methods that parses the saved articles and creates the corpus for indexing. Employes regex and other configurable exclusion handling.
5. IndexEntry.java             - Datastructure representing an entry in the inverted index.
6. FileUtility.java            - Consists of File IO utility methods.
```

The ./output/ folder consists of the hand in files

Task 1 - Generated corpus
```parserOutput/``` - List of parsed documents from the downloaded article collection.


Task 2 - Output files from index generation
unigramOutput/unigramInvIndex.txt  - Task 2a - Inverted Index
             /unigramDocLength.txt - Task 2b - Number of terms in each document
bigramOutput/
trigramOutput/
unigramWithTermPosOutput/unigramInvIndexWithPos.txt - Task 2d - unigram index storing term positions.


Task 3 - Output files from corpus statistics



## External Libraries Referenced

The following external library might need to be referenced to the build path, or via using a Maven dependency.

1. JSoup

```
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.9.2</version>
</dependency>
```


The above library has been included in the ./externalLibrary/ folder

