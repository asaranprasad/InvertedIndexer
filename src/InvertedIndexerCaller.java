public class InvertedIndexerCaller {

  /* Tests */
  public static void main(String[] args) {
    try {
      InvertedIndexer invInd = new InvertedIndexer();
      CorpusGenerator cg = new CorpusGenerator();

      cg.generateCorpus(true, true);

      invInd.generateDocStatistics();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
