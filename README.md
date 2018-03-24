# Inverted Indexer - Java Implementation
```
Task1: Generating the corpus
Task2: Implementing an inverted indexer and creating inverted indexes
Task3: Corpus statistics - Stop list explanation included in the README
```

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
```
Task 1 - Generated corpus
parserOutput/ - List of parsed documents from the downloaded article collection.


Task 2 - Output files from index generation
unigramOutput/unigramInvIndex.txt  - Task 2a - Inverted Index - unigram.
unigramOutput/unigramDocLength.txt - Task 2b - Number of terms in each document.
bigramOutput/bigramInvIndex.txt    - Task 2a - Inverted Index - bigram.
bigramOutput/bigramDocLength.txt   - Task 2b - Number of terms in each document.
trigramOutput/trigramInvIndex.txt  - Task 2a - Inverted Index - trigram.
trigramOutput/trigramDocLength.txt - Task 2b - Number of terms in each document.
unigramWithTermPosOutput/unigramInvIndexWithPos.txt - Task 2d - unigram index storing term positions.


Task 3 - Output files from corpus statistics
1 unigramOutput/unigramTermFreq.txt - Task 3a - Term frequency table - unigram.
2 unigramOutput/unigramDocFreq.txt  - Task 3b - Document frequency table - unigram.
3 bigramOutput/bigramTermFreq.txt   - Task 3a - Term frequency table - bigram.
4 bigramOutput/bigramDocFreq.txt    - Task 3b - Document frequency table - bigram.
5 trigramOutput/trigramTermFreq.txt - Task 3a - Term frequency table - trigram.
6 trigramOutput/trigramDocFreq.txt  - Task 3b - Document frequency table - trigram.


Task 3 c - Stoplists and explanation
Stop List Explanation - given in the next section of this README file
1 unigramOutput/unigramStopList.txt       - Stop lists from manual analysis of term frequencies - unigram.
2 unigramOutput/unigramStopListTfIdf.txt  - Attempt to auto-generate stop list using tf-idf - unigram.
3 bigramOutput/bigramStopList.txt         - Stop lists from manual analysis of term frequencies - bigram.
4 bigramOutput/bigramStopListTfIdf.txt    - Attempt to auto-generate stop list using tf-idf - bigram.
5 trigramOutput/trigramStopList.txt       - Stop lists from manual analysis of term frequencies - trigram.
6 trigramOutput/trigramStopListTfIdf.txt  - Attempt to auto-generate stop list using tf-idf - trigram.
```

## Stop Lists Explanation - Task 3 c
```
Created stop lists using 2 approaches
Approach 1. Manual analysis of the term frequencies of the ngrams
Approach 2. Attempt to auto-generate stop lists using tf-idf weight computation and choosing top 500 terms. 

Inference: Approach 2 helped auto creating the stop list, but did not yield an efficient outcome. Few of the common words got ignored in the list. 
The tf-idf weighing takes into consideration the importance of a term in the document(through its frequency), but does not take into consideration the relevance of the term in the corpus.

Approach1 - Cutoff choices made:
1 Unigram Stop Lists
Keeping the top 33 entries form the term frequency table as cutoff.
Reason for choosing the cut-off:
Terms specific to the corpus like "sun", "water" began showing up post this cutoff.

2 Bigram Stop Lists
Keeping the top 31 entries form the term frequency table as cutoff.
Reason for choosing the cut-off:
Terms specific to the corpus like "the solar" began showing up post this cutoff.

3 Trigram Stop Lists
Keeping the top 8 entries form the term frequency table as cutoff.
Reason for choosing the cut-off:
Phrases specific to the corpus like "of the earth", "the solar system" began showing up post this cutoff.
```

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

