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
    List<String[]> urlTxtPairs = new ArrayList<String[]>();
    HashSet<String> currentSet = new HashSet<String>();

    // sets the domain name as the BaseUri
    page.setBaseUri(config.getBaseUri());

    // Pre-Processing - Exclude non-content portion of the page
    for (String exclusionSelector : config.getCrawlExclusionSelectors())
      page.select(exclusionSelector).remove();
    Elements hyperLinks = page.select(config.getMainContentSelector());

    // Exclude administrative and previously visited URLs
    Iterator<Element> iter = hyperLinks.iterator();
    while (iter.hasNext()) {
      Element anchor = (Element) iter.next();
      String href = anchor.absUrl("href");
      String plainHref = anchor.attr("href");

      // remove administrative URLs and other irrelevant redirections
      boolean removeCondition = !href.contains(config.getBaseUri());
      removeCondition = removeCondition || !plainHref.startsWith(config.getArticleType());
      removeCondition = removeCondition || href.contains("#");
      removeCondition = removeCondition || plainHref.contains(":");

      // exclude pages already visited
      if (removeCondition)
        iter.remove();

      // form a URL-Text pair
      else if (!currentSet.contains(href)) {
        String[] urlTxtPair = new String[2];
        urlTxtPair[0] = href;
        urlTxtPair[1] = anchor.text().trim();
        urlTxtPairs.add(urlTxtPair);
        currentSet.add(href);
      }
    }
    return urlTxtPairs;
  }

  private String getArticleTitle(String url) {
    return url.substring(url.lastIndexOf('/') + 1, url.length());
  }


}
