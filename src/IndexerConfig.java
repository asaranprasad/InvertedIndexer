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
  private String unigramStopListPath;
  private String bigramStopListPath;
  private String trigramStopListPath;
  private String unigramStopListPathTBRS;
  private String bigramStopListPathTBRS;
  private String trigramStopListPathTBRS;
  private String unigramDocStatPath;
  private String bigramDocStatPath;
  private String trigramDocStatPath;

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
    unigramStopListPath = unigramOutputPath + "unigramStopListTfIdf.txt";
    bigramStopListPath = bigramOutputPath + "bigramStopListTfIdf.txt";
    trigramStopListPath = trigramOutputPath + "trigramStopListTfIdf.txt";
    unigramStopListPathTBRS = unigramOutputPath + "unigramStopListUsingTbrs.txt";
    bigramStopListPathTBRS = bigramOutputPath + "bigramStopListUsingTbrs.txt";
    trigramStopListPathTBRS = trigramOutputPath + "trigramStopListUsingTbrs.txt";
    unigramDocStatPath = unigramOutputPath + "unigramDocLength.txt";
    bigramDocStatPath = bigramOutputPath + "bigramDocLength.txt";
    trigramDocStatPath = trigramOutputPath + "trigramDocLength.txt";
    docsCount = 1000;
    setExclusionSelectors(getExclusionList());
  }

  /* List of exclusion elements to be ignored by the parser */
  private List<String> getExclusionList() {
    List<String> exclusionCSSList = new ArrayList<String>();
    exclusionCSSList.add("[role=navigation]");
    exclusionCSSList.add("[class='external text']");
    exclusionCSSList.add("[class*='navigation']");
    exclusionCSSList.add(".reference");
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

  public String getUnigramStopListPathTBRS() {
    return unigramStopListPathTBRS;
  }

  public void setUnigramStopListPathTBRS(String unigramStopListPathTBRS) {
    this.unigramStopListPathTBRS = unigramStopListPathTBRS;
  }

  public String getBigramStopListPathTBRS() {
    return bigramStopListPathTBRS;
  }

  public void setBigramStopListPathTBRS(String bigramStopListPathTBRS) {
    this.bigramStopListPathTBRS = bigramStopListPathTBRS;
  }

  public String getTrigramStopListPathTBRS() {
    return trigramStopListPathTBRS;
  }

  public void setTrigramStopListPathTBRS(String trigramStopListPathTBRS) {
    this.trigramStopListPathTBRS = trigramStopListPathTBRS;
  }

  public String getUnigramStopListPath() {
    return unigramStopListPath;
  }

  public void setUnigramStopListPath(String unigramStopListPath) {
    this.unigramStopListPath = unigramStopListPath;
  }

  public String getBigramStopListPath() {
    return bigramStopListPath;
  }

  public void setBigramStopListPath(String bigramStopListPath) {
    this.bigramStopListPath = bigramStopListPath;
  }

  public String getTrigramStopListPath() {
    return trigramStopListPath;
  }

  public void setTrigramStopListPath(String trigramStopListPath) {
    this.trigramStopListPath = trigramStopListPath;
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

  public List<String> getExclusionSelectors() {
    return exclusionSelectors;
  }

  public void setExclusionSelectors(List<String> exclusionSelectors) {
    this.exclusionSelectors = exclusionSelectors;
  }

  public String getUnigramDocStatPath() {
    return unigramDocStatPath;
  }

  public void setUnigramDocStatPath(String unigramDocStatPath) {
    this.unigramDocStatPath = unigramDocStatPath;
  }

  public String getBigramDocStatPath() {
    return bigramDocStatPath;
  }

  public void setBigramDocStatPath(String bigramDocStatPath) {
    this.bigramDocStatPath = bigramDocStatPath;
  }

  public String getTrigramDocStatPath() {
    return trigramDocStatPath;
  }

  public void setTrigramDocStatPath(String trigramDocStatPath) {
    this.trigramDocStatPath = trigramDocStatPath;
  }

  public String getNGramIndexPath(int n, boolean storeTermPos) {
    switch (n) {
      case 1:
        if (storeTermPos)
          return getUnigramWithTermPosIndexPath();
        return getUnigramIndexPath();
      case 2:
        return getBigramIndexPath();
      case 3:
        return getTrigramIndexPath();
    }
    return null;
  }

  public String getNGramTermFreqPath(int n) {
    switch (n) {
      case 1:
        return getUnigramTermFreqPath();
      case 2:
        return getBigramTermFreqPath();
      case 3:
        return getTrigramTermFreqPath();
    }
    return null;
  }

  public String getNGramDocFreqPath(int n) {
    switch (n) {
      case 1:
        return getUnigramDocFreqPath();
      case 2:
        return getBigramDocFreqPath();
      case 3:
        return getTrigramDocFreqPath();
    }
    return null;
  }

  public String getNGramStopListPath(int n) {
    switch (n) {
      case 1:
        return getUnigramStopListPath();
      case 2:
        return getBigramStopListPath();
      case 3:
        return getTrigramStopListPath();
    }
    return null;
  }

  public String getTermFreqPath(int n) {
    switch (n) {
      case 1:
        return getUnigramTermFreqPath();
      case 2:
        return getBigramTermFreqPath();
      case 3:
        return getTrigramTermFreqPath();
    }
    return null;
  }

  public String getNGramStopListPathForTBRS(int n) {
    switch (n) {
      case 1:
        return getUnigramStopListPathTBRS();
      case 2:
        return getBigramStopListPathTBRS();
      case 3:
        return getTrigramStopListPathTBRS();
    }
    return null;
  }

  public String getDocStatPath(int n) {
    switch (n) {
      case 1:
        return getUnigramDocStatPath();
      case 2:
        return getBigramDocStatPath();
      case 3:
        return getTrigramDocStatPath();
    }
    return null;
  }
}
