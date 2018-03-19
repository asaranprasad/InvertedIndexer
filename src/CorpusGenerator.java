import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CorpusGenerator {
  private IndexerConfig config;
  private FileUtility fUtility;
  private PrintWriter output;
  private PrintWriter docsDownload;

  /* Constructor assuming default indexer config */
  public CorpusGenerator() {
    this(new IndexerConfig());
  }

  /* Constructor accepting custom config for the indexer */
  public CorpusGenerator(IndexerConfig config) {
    fUtility = new FileUtility();
    this.config = config;
  }

  public void generateCorpus(boolean doesCaseFolding, boolean handlesPunctuations) {
    String outputFolderPath = config.getOutputFolderPath();
    String inputDocsPath = config.getInputDocsPath();
    String[] urlDocStringPair = new String[2];

    try {
      Scanner sc = new Scanner(new File(inputDocsPath));
      for (int i = 0; i < config.getDocsCount(); i++) {
        urlDocStringPair = fUtility.getNextDocText(sc);
        String articleTitle = getArticleTitle(urlDocStringPair[0]);

        // generate page object for this document
        Document page = Jsoup.parse(urlDocStringPair[1]);
        String docText = getDocText(page, doesCaseFolding, handlesPunctuations);

        // appending url to the doc text
        docText = urlDocStringPair[0] + docText;

        // save parsed document to output
        String outputFileName = outputFolderPath + articleTitle + ".txt";
        fUtility.writeStringToFile(docText, outputFileName);
      }
      sc.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
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

    String docText = page.body().text();

    if (doesCaseFolding)
      docText = docText.toLowerCase();

    // remove punctuation from text but preserve hyphens 
    // also retain punctuation within digits (, or .)
    if (handlesPunctuations) {
      // removes punctuations except - , and .
      docText = docText.replaceAll("[^a-zA-Z0-9-,. ]+", "");
      // punctuated decimal number and hyphenated characters regex
      // regex [[0-9]*(,|\.)([0-9]+)|[a-z]*-([a-z]+)]*
    }

    return docText;
  }

  private String getArticleTitle(String url) {
    return url.substring(url.lastIndexOf('/') + 1, url.length());
  }


}
