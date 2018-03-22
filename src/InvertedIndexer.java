import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class InvertedIndexer {
  private HashMap<String, Integer> docLength;
  private HashMap<String, HashMap<String, IndexEntry>> invIndex;
  IndexerConfig config;
  private FileUtility fUtility;
  private File parsedDocumentsDir;

  /* Constructor assuming default indexer config */
  public InvertedIndexer() {
    this(new IndexerConfig());
  }

  /* Constructor accepting custom config for the indexer */
  public InvertedIndexer(IndexerConfig config) {
    docLength = new HashMap<String, Integer>();
    invIndex = new HashMap<String, HashMap<String, IndexEntry>>();
    this.config = config;
    fUtility = new FileUtility();
    parsedDocumentsDir = new File(config.getParserOutputPath());
  }

  public void loadInvertedIndexFromFile(String indexPath) {
    //    List<String> entries = fUtility.textFileToList(indexPath);
    try {
      Scanner sc = new Scanner(new File(filePath));
      while (sc.hasNextLine())
        lines.add(sc.nextLine());
      sc.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    for (String entry : entries) {
      entry = entry.trim();
      if (!entry.isEmpty()) {
        String[] termValue = entry.trim().split(" -> ");
        String[] docEntries = termValue[1].split(") (");
        HashMap<String, IndexEntry> invEntryVal = new HashMap<String, IndexEntry>();
        for (String docEntry : docEntries) {
          docEntry = docEntry.replace("(", "").replace(")", "");
          String[] components = docEntry.split(", ");
          String docID = components[0];
          int tf = Integer.parseInt(components[1]);

          // if term positions information present
          List<Integer> termPos = new LinkedList<Integer>();
          if (components.length > 2) {
            String[] pos = components[2].replace("[", "").replace(" ]", "").split(" ");
            for (String eachPos : pos)
              termPos.add(Integer.parseInt(eachPos));
          }
          IndexEntry ie = new IndexEntry(docID, termPos, tf);
          invEntryVal.put(docID, ie);
        }
        invIndex.put(termValue[0], invEntryVal);
      }
    }
  }

  /* Store the number of terms in each document in 
   * a separate data structure 
   */
  public void generateDocStatisticsFromCorpus() {
    try {
      PrintWriter statOutput = new PrintWriter(config.getDocStatPath());
      File[] directoryListing = parsedDocumentsDir.listFiles();
      if (directoryListing != null)
        for (File doc : directoryListing) {
          String fileContent = fUtility.textFileToString(doc);
          String fileName = trimFileExtn(doc.getName());
          int termCount = fileContent.split(" ").length;
          docLength.put(fileName, termCount);
          statOutput.println(fileName + " " + termCount);
        }
      statOutput.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void generateNgramInvIndexFromCorpus(int n, boolean storeTermPos) {
    File[] directoryListing = parsedDocumentsDir.listFiles();
    int fileCount = 0;
    if (directoryListing != null)
      for (File doc : directoryListing) {
        String[] words = fUtility.textFileToString(doc).split(" ");
        String docID = trimFileExtn(doc.getName());
        int windowingLimit = words.length - (n - 1);
        for (int i = 0; i < windowingLimit; i++) {
          String term = new String();
          term = words[i];
          if (n > 1)
            term = term + " " + words[i + 1];
          if (n > 2)
            term = term + " " + words[i + 2];

          if (!term.isEmpty()) {
            HashMap<String, IndexEntry> docSet;
            // if the term occurs for the first time in the inverted index
            if (!invIndex.containsKey(term)) {
              docSet = new HashMap<String, IndexEntry>();
              IndexEntry entry = new IndexEntry(docID);
              entry.appendPos(i);
              docSet.put(docID, entry);
              invIndex.put(term, docSet);
            } else {
              docSet = invIndex.get(term);
              IndexEntry entry = docSet.get(docID);
              // if the inverted index contains the term and the docID entry
              if (entry != null)
                entry.appendPos(i);
              // if the term occurs for the first time in the docID
              else {
                IndexEntry newEntry = new IndexEntry(docID);
                newEntry.appendPos(i);
                docSet.put(docID, newEntry);
              }
            }
          }
        }
        System.out.println("done: " + (++fileCount) + " | " + docID);
      }
    System.out.println("done: generating index for " + n + " gram");
    // print inverted index to file
    printNGramIndexToFile(n, storeTermPos);
  }

  private String trimFileExtn(String name) {
    return name.substring(0, name.lastIndexOf('.'));
  }

  private void printNGramIndexToFile(int n, boolean storeTermPos) {
    try {
      String outputIndexPath = new String();
      switch (n) {
        case 1:
          outputIndexPath = config.getUnigramIndexPath();
          if (storeTermPos)
            outputIndexPath =
                config.getUnigramWithTermPosIndexPath();
          break;
        case 2:
          outputIndexPath = config.getBigramIndexPath();
          break;
        case 3:
          outputIndexPath = config.getTrigramIndexPath();
          break;
      }
      PrintWriter ngramOutput = new PrintWriter(outputIndexPath);
      StringBuilder outLine;
      for (Map.Entry<String, HashMap<String, IndexEntry>> entry : invIndex.entrySet()) {
        outLine = new StringBuilder();
        String term = entry.getKey();
        outLine.append(term + " ->");
        HashMap<String, IndexEntry> docEntries = entry.getValue();
        for (Map.Entry<String, IndexEntry> docEntry : docEntries.entrySet()) {
          IndexEntry docEntryVal = docEntry.getValue();
          outLine.append(" (" + docEntryVal.getDocID());
          outLine.append(", " + docEntryVal.getTf());
          if (storeTermPos) {
            outLine.append(", [");
            for (int termPos : docEntryVal.getTermPos())
              outLine.append(termPos + " ");
            outLine.append("]");
          }
          outLine.append(")");
        }
        fUtility.println(ngramOutput, outLine.toString());
      }
      ngramOutput.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    System.out.println("done: printing index for " + n + " gram");
  }


  public void generateNGramTermFreqTable(int i) {

  }

  public void generateNGramDocFreqTable(int i) {

  }


}
