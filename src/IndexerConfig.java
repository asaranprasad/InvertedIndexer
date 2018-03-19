public class IndexerConfig {

  private String outputFolderPath;
  private String inputDocsPath;
  private int docsCount;

  IndexerConfig() {
    outputFolderPath = "./output/";
    inputDocsPath = "./input/BFSCrawledDocuments.txt";
    docsCount = 1000;
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

}
