public class InvertedIndexerCaller {

  /* Tests */
  public static void main(String[] args) {
    try {
      InvertedIndexer invInd = new InvertedIndexer();
      CorpusGenerator cg = new CorpusGenerator();

      // Task 1 - Parse Crawled Documents
      //      cg.generateCorpus(true, true);

      // Task 2b - Store the number of terms in each document
      //      invInd.generateDocStatistics();

      // Task 2c - Generate three inverted indexes,
      //           one for each value of n = 1, 2, 3
      //      invInd.generateNgramInvIndex(1, false);
      //      invInd.generateNgramInvIndex(2, false);
      //      invInd.generateNgramInvIndex(3, false);

      // Task 2d - Generate another unigram index, this time,
      //           storing term positions
      invInd.generateNgramInvIndex(1, true);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
