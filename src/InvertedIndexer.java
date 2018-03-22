import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class InvertedIndexer {
  private HashMap<String, Integer> docLength;
  private HashMap<String, HashMap<String, IndexEntry>> invIndex;
  private HashMap<String, Integer> termFreq;
  private HashMap<String, List<String>> docFreq;
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
    termFreq = new HashMap<String, Integer>();
    docFreq = new HashMap<String, List<String>>();
    this.config = config;
    fUtility = new FileUtility();
    parsedDocumentsDir = new File(config.getParserOutputPath());
  }

  public void loadInvertedIndexFromFile(String indexPath) {
    try {
      invIndex = new HashMap<String, HashMap<String, IndexEntry>>();
      Scanner sc = new Scanner(new File(indexPath));
      int lineNum = 0;
      while (sc.hasNextLine()) {
        String entry = sc.nextLine().trim();
        if (!entry.isEmpty()) {
          String[] termValue = entry.trim().split(" -> ");
          String[] docEntries = termValue[1].split("\\) \\(");
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
          System.out.println((++lineNum) + " | " + termValue[0]);
        }
      }
      sc.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /* Store the number of terms in each document in 
   * a separate data structure 
   */
  public void generateDocStatisticsFromCorpus() {
    try {
      System.out.println("Start: Generate Doc Stat");
      docLength = new HashMap<String, Integer>();
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
    System.out.println("Done: Generate Doc Stat");
  }

  public void generateNgramInvIndexFromCorpus(int n, boolean storeTermPos) {
    invIndex = new HashMap<String, HashMap<String, IndexEntry>>();
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

  public void printNGramIndexToFile(int n, boolean storeTermPos) {
    try {
      String outputIndexPath = config.getNGramIndexPath(n, storeTermPos);
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


  public void generateNGramTermFreqTable(int n) {
    try {
      System.out.println("Start: Generate Term Freq Table for " + n + " gram");

      termFreq = new HashMap<String, Integer>();
      // fill term frequency table
      for (Map.Entry<String, HashMap<String, IndexEntry>> entry : invIndex.entrySet()) {
        String term = entry.getKey();
        int tf = 0;
        HashMap<String, IndexEntry> docEntries = entry.getValue();
        for (Map.Entry<String, IndexEntry> docEntry : docEntries.entrySet()) {
          IndexEntry docEntryVal = docEntry.getValue();
          tf += docEntryVal.getTf();
        }
        termFreq.put(term, tf);
      }
      termFreq = sortMapByValues(termFreq);

      // print term frequency table to file
      PrintWriter termFreqOut = new PrintWriter(config.getNGramTermFreqPath(n));
      for (Map.Entry<String, Integer> entry : termFreq.entrySet())
        fUtility.println(termFreqOut, entry.getKey() + " " + entry.getValue());
      termFreqOut.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    System.out.println("Done: Generate Term Freq Table for " + n + " gram");

  }

  public void generateNGramDocFreqTable(int n) {
    try {
      System.out.println("Start: Generate Doc Freq Table for " + n + " gram");
      docFreq = new HashMap<String, List<String>>();
      // fill term frequency table
      for (Map.Entry<String, HashMap<String, IndexEntry>> entry : invIndex.entrySet()) {
        String term = entry.getKey();
        HashMap<String, IndexEntry> docEntries = entry.getValue();
        List<String> docIDs = new LinkedList<String>();
        for (Map.Entry<String, IndexEntry> docEntry : docEntries.entrySet())
          docIDs.add(docEntry.getKey());
        docFreq.put(term, docIDs);
      }
      docFreq = sortMapByKeysDesc(docFreq);

      // print doc frequency table to file
      PrintWriter docFreqOut = new PrintWriter(config.getNGramDocFreqPath(n));
      for (Map.Entry<String, List<String>> entry : docFreq.entrySet()) {
        StringBuilder outLine = new StringBuilder();

        // term
        outLine.append(entry.getKey());

        // docID lists
        outLine.append(" [");
        for (String docID : entry.getValue())
          outLine.append(docID + " ");
        outLine.append("] ");

        // document frequency
        outLine.append(entry.getValue().size());

        fUtility.println(docFreqOut, outLine.toString());
      }
      docFreqOut.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    System.out.println("Finish: Generate Doc Freq Table for " + n + " gram");
  }

  private HashMap<String, List<String>> sortMapByKeysDesc(
      HashMap<String, List<String>> map) {
    List<Map.Entry<String, List<String>>> list =
        new LinkedList<Map.Entry<String, List<String>>>(map.entrySet());
    Collections.sort(list, new Comparator<Map.Entry<String, List<String>>>() {
      public int compare(Map.Entry<String, List<String>> o1,
          Map.Entry<String, List<String>> o2) {
        return (o1.getKey()).compareTo(o2.getKey());
      }
    });
    HashMap<String, List<String>> sortedHashMap =
        new LinkedHashMap<String, List<String>>();
    for (Map.Entry<String, List<String>> entry : list)
      sortedHashMap.put(entry.getKey(), entry.getValue());
    return sortedHashMap;
  }

  private static HashMap<String, Integer> sortMapByValues(HashMap<String, Integer> map) {
    List<Map.Entry<String, Integer>> list =
        new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
    Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
      public int compare(Map.Entry<String, Integer> o1,
          Map.Entry<String, Integer> o2) {
        return (o2.getValue()).compareTo(o1.getValue());
      }
    });
    HashMap<String, Integer> sortedHashMap = new LinkedHashMap<String, Integer>();
    for (Map.Entry<String, Integer> entry : list)
      sortedHashMap.put(entry.getKey(), entry.getValue());
    return sortedHashMap;
  }
}
