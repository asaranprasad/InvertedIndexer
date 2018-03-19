import java.util.ArrayList;
import java.util.List;

public class IndexerConfig {

  private String outputFolderPath;
  private String inputDocsPath;
  private int docsCount;
  private List<String> exclusionSelectors;

  IndexerConfig() {
    outputFolderPath = "./output/";
    inputDocsPath = "./input/BFSCrawledDocuments.txt";
    docsCount = 1000;
    setExclusionSelectors(getExclusionList());
  }

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

  public List<String> getExclusionSelectors() {
    return exclusionSelectors;
  }

  public void setExclusionSelectors(List<String> exclusionSelectors) {
    this.exclusionSelectors = exclusionSelectors;
  }

}
