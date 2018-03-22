import java.util.ArrayList;
import java.util.List;

public class IndexerConfig {

  private String outputFolderPath;
  private String inputDocsPath;
  private String parserOutputPath;
  private String unigramOutputPath;
  private String bigramOutputPath;
  private String trigramOutputPath;
  private String unigramWithTermPosOutputPath;
  private String docStatPath;

  private int docsCount;
  private List<String> exclusionSelectors;

  IndexerConfig() {
    inputDocsPath = "./input/BFSCrawledDocuments.txt";
    outputFolderPath = "./output/";
    parserOutputPath = outputFolderPath + "parserOutput/";
    unigramOutputPath = outputFolderPath + "unigramOutput/";
    bigramOutputPath = outputFolderPath + "bigramOutput/";
    trigramOutputPath = outputFolderPath + "trigramOutput/";
    unigramWithTermPosOutputPath = outputFolderPath + "unigramWithTermPosOutput/";
    docStatPath = outputFolderPath + "docStat/";
    docsCount = 1000;
    setExclusionSelectors(getExclusionList());
  }

  /* List of exclusion elements to be ignored by the parser */
  private List<String> getExclusionList() {
    List<String> exclusionCSSList = new ArrayList<String>();
    exclusionCSSList.add("[role=navigation]");
    exclusionCSSList.add("[class='external text']");
    exclusionCSSList.add("[class*='navigation']");
    exclusionCSSList.add(".internal");
    exclusionCSSList.add(".image");
    exclusionCSSList.add(".mw-wiki-logo");
    return exclusionCSSList;
  }

  public String getParserOutputPath() {
    return parserOutputPath;
  }

  public void setParserOutputPath(String parserOutputPath) {
    this.parserOutputPath = parserOutputPath;
  }

  public void setOutputFolderPath(String outputFolderPath) {
    this.outputFolderPath = outputFolderPath;
  }

  public void setInputDocsPath(String inputDocsPath) {
    this.inputDocsPath = inputDocsPath;
  }

  public void setDocsCount(int docsCount) {
    this.docsCount = docsCount;
  }

  public String getOutputFolderPath() {
    return outputFolderPath;
  }

  public String getInputDocsPath() {
    return inputDocsPath;
  }

  public int getDocsCount() {
    return docsCount;
  }

  public String getUnigramOutputPath() {
    return unigramOutputPath;
  }

  public void setUnigramOutputPath(String unigramOutputPath) {
    this.unigramOutputPath = unigramOutputPath;
  }

  public String getBigramOutputPath() {
    return bigramOutputPath;
  }

  public void setBigramOutputPath(String bigramOutputPath) {
    this.bigramOutputPath = bigramOutputPath;
  }

  public String getTrigramOutputPath() {
    return trigramOutputPath;
  }

  public void setTrigramOutputPath(String trigramOutputPath) {
    this.trigramOutputPath = trigramOutputPath;
  }

  public String getUnigramWithTermPosOutputPath() {
    return unigramWithTermPosOutputPath;
  }

  public void setUnigramWithTermPosOutputPath(String unigramWithTermPosOutputPath) {
    this.unigramWithTermPosOutputPath = unigramWithTermPosOutputPath;
  }

  public String getDocStatPath() {
    return docStatPath;
  }

  public void setDocStatPath(String docStatPath) {
    this.docStatPath = docStatPath;
  }

  public List<String> getExclusionSelectors() {
    return exclusionSelectors;
  }

  public void setExclusionSelectors(List<String> exclusionSelectors) {
    this.exclusionSelectors = exclusionSelectors;
  }

}
