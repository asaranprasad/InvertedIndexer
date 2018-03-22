public class InvertedIndexerCaller {

  /* Tests */
  public static void main(String[] args) {
    try {
      InvertedIndexer invInd = new InvertedIndexer();
      IndexerConfig ic = new IndexerConfig();
      CorpusGenerator cg = new CorpusGenerator();

      //      // Task 1 - Parse Crawled Documents
      //      cg.generateCorpus(true, true);
      //
      //      // Task 2b - Store the number of terms in each document
      //      invInd.generateDocStatisticsFromCorpus();
      //
      //      // Task 2c - Generate three inverted indexes,
      //      //           one for each value of n = 1, 2, 3
      //      invInd = new InvertedIndexer();
      //      invInd.generateNgramInvIndexFromCorpus(1, false);
      //      invInd = new InvertedIndexer();
      //      invInd.generateNgramInvIndexFromCorpus(2, false);
      //      invInd = new InvertedIndexer();
      //      invInd.generateNgramInvIndexFromCorpus(3, false);
      //
      //      // Task 2d - Generate another unigram index, this time,
      //      //           storing term positions
      //      invInd = new InvertedIndexer();
      //      invInd.generateNgramInvIndexFromCorpus(1, true);
      //
      //      // Task 3 - unigram
      //      invInd = new InvertedIndexer();
      //      // Load unigram inverted index from file
      //      invInd.loadInvertedIndexFromFile(ic.getUnigramIndexPath());
      //            // Task 3 a - unigram
      //            invInd.generateNGramTermFreqTable(1);
      //            // Task 3 b - unigram
      //            invInd.generateNGramDocFreqTable(1);
      //      
      //            // Task 3 - bigram
      //            invInd = new InvertedIndexer();
      //            // Load bigram inverted index from file
      //            invInd.loadInvertedIndexFromFile(ic.getBigramIndexPath());
      //            // Task 3 a - bigram
      //            invInd.generateNGramTermFreqTable(2);
      //            // Task 3 b - bigram
      //            invInd.generateNGramDocFreqTable(2);
      //      
      //            // Task 3 - trigram
      //            invInd = new InvertedIndexer();
      //            // Load trigram inverted index from file
      invInd.loadInvertedIndexFromFile(ic.getTrigramIndexPath());
      //            // Task 3 a - trigram
      //            invInd.generateNGramTermFreqTable(3);
      //            // Task 3 b - trigram
      //            invInd.generateNGramDocFreqTable(3);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
