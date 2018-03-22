import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CorpusGenerator {
  private IndexerConfig config;
  private FileUtility fUtility;

  /* Constructor assuming default parser config */
  public CorpusGenerator() {
    this(new IndexerConfig());
  }

  /* Constructor accepting custom config for the parser */
  public CorpusGenerator(IndexerConfig config) {
    fUtility = new FileUtility();
    this.config = config;
  }

  public void generateCorpus(boolean doesCaseFolding, boolean handlesPunctuations) {
    String outputFolderPath = config.getParserOutputPath();
    String inputDocsPath = config.getInputDocsPath();
    String[] titleDocStringPair = new String[2];

    try {
      Scanner sc = new Scanner(new File(inputDocsPath));
      int i = 0;
      for (; i < config.getDocsCount(); i++) {
        titleDocStringPair = fUtility.getNextDocText(sc);
        String articleTitle = getArticleTitle(titleDocStringPair[0]);

        // generate page object for this document
        Document page = Jsoup.parse(titleDocStringPair[1]);
        String docText = getDocText(page, doesCaseFolding, handlesPunctuations);

        // save parsed document to output
        String outputFileName = outputFolderPath + articleTitle + ".txt";
        if (fileExists(outputFileName)) {
          System.out
              .println("File Already Exists, overwriting it: " + articleTitle + ".txt");
        }
        fUtility.writeStringToFile(docText, outputFileName);
      }
      sc.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private boolean fileExists(String outputFileName) {
    File f = new File(outputFileName);
    return f.exists() && !f.isDirectory();
  }

  /**
   * Extract plain textual content of the article. Ignore/remove ALL markup notation (HTML tags),
   * URLs, references to images, tables, formulas, and navigational components.
   * 
   * @param page - handle of the page to be parsed.
   * @param doesCaseFolding - flag indicating if case folding is needed.
   * @param handlesPunctuations - flag for punctuations handling.
   * @return - plain textual content of the page.
   */
  private String getDocText(Document page, boolean doesCaseFolding,
      boolean handlesPunctuations) {
    // Pre-Processing - Exclude non-content portion of the page
    for (String exclusionSelector : config.getExclusionSelectors())
      page.select(exclusionSelector).remove();

    // extracting title and body text
    String docText = page.title() + " " + page.body().text();

    // case folding
    if (doesCaseFolding)
      docText = docText.toLowerCase();

    // remove punctuation from text but preserve hyphens 
    // also retain punctuation within digits (, or .)
    if (handlesPunctuations) {
      // removes punctuations except - , and .
      docText = docText.replaceAll("[^a-zA-Z0-9-\\., ]", " ");

      // Retaining punctuation within digits (, or .) 
      // as well as hyphens using Negative look ahead
      docText =
          docText.replaceAll(
              "(?![0-9]*,[0-9]+|[0-9]*\\.[0-9]+|[a-zA-Z0-9]*-[a-zA-Z0-9]+)([^a-zA-Z0-9- ]+)",
              " ");
    }

    // replace multispaces with a single space
    docText = docText.replaceAll("[ ]+", " ");

    return docText;
  }

  private String getArticleTitle(String url) {
    return url.substring(url.lastIndexOf('/') + 1, url.length());
  }


}
