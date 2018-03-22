import java.io.File;
import java.util.HashMap;

public class InvertedIndexer {
  private HashMap<String, Integer> docLength;
  IndexerConfig config;
  private FileUtility fUtility;

  /* Constructor assuming default indexer config */
  public InvertedIndexer() {
    this(new IndexerConfig());
  }

  /* Constructor accepting custom config for the indexer */
  public InvertedIndexer(IndexerConfig config) {
    docLength = new HashMap<String, Integer>();
    this.config = config;
    fUtility = new FileUtility();
  }

  /* Store the number of terms in each document in 
   * a separate data structure 
   */
  public void generateDocStatistics() {
    File dir = new File(config.getParserOutputPath());
    File[] directoryListing = dir.listFiles();
    if (directoryListing != null) {
      for (File doc : directoryListing) {

      }
    }
  }

}
