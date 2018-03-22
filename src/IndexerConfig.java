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
  private String unigramWithTermPosIndexPath;
  private String unigramIndexPath;
  private String bigramIndexPath;
  private String trigramIndexPath;
  private String unigramTermFreqPath;
  private String bigramTermFreqPath;
  private String trigramTermFreqPath;
  private String unigramDocFreqPath;
  private String bigramDocFreqPath;
  private String trigramDocFreqPath;
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
    unigramWithTermPosOutputPath =
        outputFolderPath + "unigramWithTermPosOutput/";
    unigramWithTermPosIndexPath =
        unigramWithTermPosOutputPath + "unigramInvIndexWithPos.txt";
    unigramIndexPath = unigramOutputPath + "unigramInvIndex.txt";
    bigramIndexPath = bigramOutputPath + "bigramInvIndex.txt";
    trigramIndexPath = trigramOutputPath + "trigramInvIndex.txt";
    unigramTermFreqPath = unigramOutputPath + "unigramTermFreq.txt";
    bigramTermFreqPath = bigramOutputPath + "bigramTermFreq.txt";
    trigramTermFreqPath = trigramOutputPath + "trigramTermFreq.txt";
    unigramDocFreqPath = unigramOutputPath + "unigramDocFreq.txt";
    bigramDocFreqPath = bigramOutputPath + "bigramDocFreq.txt";
    trigramDocFreqPath = trigramOutputPath + "trigramDocFreq.txt";
    docStatPath = outputFolderPath + "docStat/stat.txt";
    docsCount = 1000;
    setExclusionSelectors(getExclusionList());
  }

  /* List of exclusion elements to be ignored by the parser */
  private List<String> getExclusionList() {
    List<String> exclusionCSSList = new ArrayList<String>();
    exclusionCSSList.add("[role=navigation]");
    exclusionCSSList.add("[class='external text']");
    exclusionCSSList.add("[class*='navigation']");
    exclusionCSSList.add("div#jump-to-nav");
    exclusionCSSList.add(".internal");
    exclusionCSSList.add(".image");
    exclusionCSSList.add(".mw-wiki-logo");
    exclusionCSSList.add("div.noprint");
    // all tables ignored as per piazza post 
    // https://piazza.com/class/jc6u5jg9h02ad?cid=143
    exclusionCSSList.add("table");
    exclusionCSSList.add("div#toc");
    // excluding see also, references and external links sections
    // as per piazza post https://piazza.com/class/jc6u5jg9h02ad?cid=146
    exclusionCSSList.add(".references");
    exclusionCSSList.add(".citation.book");
    exclusionCSSList.add("a[rel = 'nofollow']");
    exclusionCSSList.add(".external.text");
    exclusionCSSList.add("span#See_also");
    exclusionCSSList.add("span#Notes");
    exclusionCSSList.add("span#References");
    exclusionCSSList.add("span#External_links");
    exclusionCSSList.add("span#Further_reading");
    exclusionCSSList.add("span.mw-editsection");
    exclusionCSSList.add("div.reflist.columns.references-column-width ~ ul");
    exclusionCSSList.add(".printfooter");
    exclusionCSSList.add("div.catlinks");
    exclusionCSSList.add("div#footer");
    exclusionCSSList.add("div.hatnote.navigation-not-searchable");
    return exclusionCSSList;
  }

  public String getUnigramWithTermPosIndexPath() {
    return unigramWithTermPosIndexPath;
  }

  public void setUnigramWithTermPosIndexPath(String unigramWithTermPosIndexPath) {
    this.unigramWithTermPosIndexPath = unigramWithTermPosIndexPath;
  }

  public String getUnigramIndexPath() {
    return unigramIndexPath;
  }

  public void setUnigramIndexPath(String unigramIndexPath) {
    this.unigramIndexPath = unigramIndexPath;
  }

  public String getBigramIndexPath() {
    return bigramIndexPath;
  }

  public void setBigramIndexPath(String bigramIndexPath) {
    this.bigramIndexPath = bigramIndexPath;
  }

  public String getTrigramIndexPath() {
    return trigramIndexPath;
  }

  public void setTrigramIndexPath(String trigramIndexPath) {
    this.trigramIndexPath = trigramIndexPath;
  }

  public String getUnigramTermFreqPath() {
    return unigramTermFreqPath;
  }

  public void setUnigramTermFreqPath(String unigramTermFreqPath) {
    this.unigramTermFreqPath = unigramTermFreqPath;
  }

  public String getBigramTermFreqPath() {
    return bigramTermFreqPath;
  }

  public void setBigramTermFreqPath(String bigramTermFreqPath) {
    this.bigramTermFreqPath = bigramTermFreqPath;
  }

  public String getTrigramTermFreqPath() {
    return trigramTermFreqPath;
  }

  public void setTrigramTermFreqPath(String trigramTermFreqPath) {
    this.trigramTermFreqPath = trigramTermFreqPath;
  }

  public String getUnigramDocFreqPath() {
    return unigramDocFreqPath;
  }

  public void setUnigramDocFreqPath(String unigramDocFreqPath) {
    this.unigramDocFreqPath = unigramDocFreqPath;
  }

  public String getBigramDocFreqPath() {
    return bigramDocFreqPath;
  }

  public void setBigramDocFreqPath(String bigramDocFreqPath) {
    this.bigramDocFreqPath = bigramDocFreqPath;
  }

  public String getTrigramDocFreqPath() {
    return trigramDocFreqPath;
  }

  public void setTrigramDocFreqPath(String trigramDocFreqPath) {
    this.trigramDocFreqPath = trigramDocFreqPath;
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

  public void setUnigramOutputPath(String unigramOutputPath) {
    this.unigramOutputPath = unigramOutputPath;
  }

  public void setBigramOutputPath(String bigramOutputPath) {
    this.bigramOutputPath = bigramOutputPath;
  }

  public void setTrigramOutputPath(String trigramOutputPath) {
    this.trigramOutputPath = trigramOutputPath;
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
