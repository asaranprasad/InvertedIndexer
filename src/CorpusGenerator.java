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

  /**
   * Parses the downloaded document collection into queryable terms
   * 
   * @param doesCaseFolding - case folding enabled / disabled flag
   * @param handlesPunctuations - punctuation handling enabled / disabled flag
   */
  public void generateCorpus(boolean doesCaseFolding, boolean handlesPunctuations) {
    String outputFolderPath = config.getParserOutputPath();
    String inputDocsPath = config.getInputDocsPath();
    String[] titleDocStringPair = new String[2];

    try {
      Scanner sc = new Scanner(new File(inputDocsPath));
      for (int i = 0; i < config.getDocsCount(); i++) {
        titleDocStringPair = fUtility.getNextDocText(sc);
        String articleTitle = getArticleTitle(titleDocStringPair[0]);

        // generate page object for this document
        Document page = Jsoup.parse(titleDocStringPair[1]);
        String docText = getDocText(page, doesCaseFolding, handlesPunctuations);

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
   * Parses the downloaded document collection into separate raw html documents
   */
  public void dissolveCorpus() {
    String outputFolderPath = config.getParserOutputPath();
    String inputDocsPath = config.getInputDocsPath();
    String[] titleDocStringPair = new String[2];

    try {
      Scanner sc = new Scanner(new File(inputDocsPath));
      for (int i = 0; i < config.getDocsCount(); i++) {
        titleDocStringPair = fUtility.getNextDocText(sc);
        String articleTitle = getArticleTitle(titleDocStringPair[0]);

        // save parsed document to output
        String outputFileName = outputFolderPath + "raw/" + articleTitle + ".txt";
        fUtility.writeStringToFile(titleDocStringPair[1], outputFileName);
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

    // extracting title and body text
    String docText = page.title() + " " + page.body().text();

    // case folding
    if (doesCaseFolding)
      docText = docText.toLowerCase();

    // remove punctuation from text but preserve hyphens 
    // also retain punctuation within digits (, or .)
    if (handlesPunctuations) {
      // removes punctuations except - , and .
      docText = docText.replaceAll("[^\\p{L}0-9-–−\\., ]", "");

      // Retaining punctuation within digits (, or .) 
      // as well as hyphens using Negative look ahead
      String punctRegex = "(?!"; // Exclusion group
      punctRegex = punctRegex + "[0-9]+,[0-9]+"; // comma between numbers
      punctRegex = punctRegex + "|" + "[0-9]+\\.[0-9]+"; // decimal point between numbers
      punctRegex = punctRegex + "|" + "[0-9]*-[0-9]+"; // negation before numbers
      punctRegex = punctRegex + "|" + "[\\p{L}]*-[\\p{L}]+"; // hyphen between alphabets
      punctRegex = punctRegex + ")";
      punctRegex = punctRegex + "([^\\p{L}0-9- ]+)"; // all non-alphanumeric characters
      docText = docText.replaceAll(punctRegex, "");
      docText = docText.replaceAll("(?![ ]+[-][0-9]+)[ ]+[-]+", " ");

      // remove stand-alone hyphens. Various UTF-8 types
      docText = docText.replace(" − ", " ");
      docText = docText.replace(" - ", " ");
    }

    // replace multispaces with a single space
    docText = docText.replaceAll("[ ]+", " ");

    return docText;
  }

  /**
   * Returns title of the article, parsed from the given URL
   */
  private String getArticleTitle(String url) {
    return url.substring(url.lastIndexOf('/') + 1, url.length());
  }


}
