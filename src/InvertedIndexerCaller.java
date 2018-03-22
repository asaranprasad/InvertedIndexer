public class InvertedIndexerCaller {

  /* Tests */
  public static void main(String[] args) {
    try {
      InvertedIndexer invInd = new InvertedIndexer();
      IndexerConfig ic = new IndexerConfig();
      CorpusGenerator cg = new CorpusGenerator();

      //       Task 1 - Parse Crawled Documents
      cg.generateCorpus(true, true);

      // Task 2b - Store the number of terms in each document
      invInd.generateDocStatisticsFromCorpus();

      // Task 2c - Generate inverted index for unigrams
      invInd = new InvertedIndexer();
      invInd.generateNgramInvIndexFromCorpus(1, false);
      // Task 3 a - unigram
      invInd.generateNGramTermFreqTable(1);
      // Task 3 b - unigram
      invInd.generateNGramDocFreqTable(1);


      // Task 2c - Generate inverted index for bigrams
      invInd = new InvertedIndexer();
      invInd.generateNgramInvIndexFromCorpus(2, false);
      // Task 3 a - bigram
      invInd.generateNGramTermFreqTable(2);
      // Task 3 b - bigram
      invInd.generateNGramDocFreqTable(2);


      // Task 2c - Generate inverted index for trigrams
      invInd = new InvertedIndexer();
      invInd.generateNgramInvIndexFromCorpus(3, false);
      // Task 3 a - trigram
      invInd.generateNGramTermFreqTable(3);
      // Task 3 b - trigram
      invInd.generateNGramDocFreqTable(3);


      // Task 2d - Generate another unigram index, this time,
      //           storing term positions
      invInd = new InvertedIndexer();
      invInd.generateNgramInvIndexFromCorpus(1, true);


      //      // Test Loader with term positions
      //      invInd = new InvertedIndexer();
      //      // Load trigram inverted index from file
      //      invInd.loadInvertedIndexFromFile(ic.getUnigramWithTermPosIndexPath());
      //      invInd.printNGramIndexToFile(1, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
