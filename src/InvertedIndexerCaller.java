public class InvertedIndexerCaller {

  /* Tests */
  public static void main(String[] args) {
    try {
      CorpusGenerator cg = new CorpusGenerator();
      cg.generateCorpus(true, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
