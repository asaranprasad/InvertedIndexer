import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

public class InvertedIndexer {
  private HashMap<String, Integer> docLength;
  private HashMap<String, TreeSet<IndexEntry>> invIndex;
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
    invIndex = new HashMap<String, TreeSet<IndexEntry>>();
    this.config = config;
    fUtility = new FileUtility();
    parsedDocumentsDir = new File(config.getParserOutputPath());
  }

  /* Store the number of terms in each document in 
   * a separate data structure 
   */
  public void generateDocStatistics() {
    try {
      PrintWriter statOutput = new PrintWriter(config.getDocStatPath() + "stat.txt");
      File[] directoryListing = parsedDocumentsDir.listFiles();
      if (directoryListing != null)
        for (File doc : directoryListing) {
          String fileContent = fUtility.textFileToString(doc);
          String fileName = doc.getName();
          int termCount = fileContent.split(" ").length;
          docLength.put(fileName, termCount);
          statOutput.println(fileName + " " + termCount);
        }
      statOutput.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void generateNgramInvIndex(int n, boolean storeTermPos) {
    File[] directoryListing = parsedDocumentsDir.listFiles();
    if (directoryListing != null)
      for (File doc : directoryListing) {
        String[] words = fUtility.textFileToString(doc).split(" ");
        String docID = doc.getName();
        int windowingLimit = words.length - (n - 1);
        for (int i = 0; i < windowingLimit; i++) {
          String term = new String();
          term = words[i];
          if (n > 1)
            term = term + " " + words[i + 1];
          if (n > 2)
            term = term + " " + words[i + 2];

          if (!term.isEmpty()) {
            TreeSet<IndexEntry> docSetSorted;
            // if the term occurs for the first time in the inverted index
            if (!invIndex.containsKey(term)) {
              docSetSorted = new TreeSet<IndexEntry>(new IndexEntryComp());
              IndexEntry entry = new IndexEntry(docID);
              entry.appendPos(i);
              docSetSorted.add(entry);
              invIndex.put(term, docSetSorted);
            } else {
              docSetSorted = invIndex.get(term);
              Iterator<IndexEntry> it = docSetSorted.iterator();
              IndexEntry current = null;
              while (it.hasNext()) {
                current = it.next();
                if (current.getDocID().equals(docID))
                  break;
              }
              // if the inverted index contains the term and the docID entry
              if (current != null)
                current.appendPos(i);
              // if the term occurs for the first time in the docID
              else {
                IndexEntry entry = new IndexEntry(docID);
                entry.appendPos(i);
                docSetSorted.add(entry);
//                invIndex.put(term, docSetSorted);
              }
            }
          }
        }
      }
    // print inverted index to file
    printNGramIndexToFile(n, storeTermPos);
  }

  private void printNGramIndexToFile(int n, boolean storeTermPos) {
    try {
      String outputIndexPath = new String();
      switch (n) {
        case 1:
          outputIndexPath = config.getUnigramOutputPath() + "unigramInvIndex.txt";
          if (storeTermPos)
            outputIndexPath =
                config.getUnigramWithTermPosOutputPath() + "unigramInvIndexWithPos.txt";
          break;
        case 2:
          outputIndexPath = config.getBigramOutputPath() + "bigramInvIndex.txt";
          break;
        case 3:
          outputIndexPath = config.getTrigramOutputPath() + "trigramInvIndex.txt";
          break;
      }
      PrintWriter ngramOutput = new PrintWriter(outputIndexPath);
      StringBuilder outLine;
      for (Map.Entry<String, TreeSet<IndexEntry>> entry : invIndex.entrySet()) {
        outLine = new StringBuilder();
        String term = entry.getKey();
        outLine.append(term + " ->");
        TreeSet<IndexEntry> docEntries = entry.getValue();
        for (IndexEntry docEntry : docEntries) {
          outLine.append(" (" + docEntry.getDocID());
          outLine.append(", " + docEntry.getTf());
          if (storeTermPos) {
            outLine.append(", [");
            for (int termPos : docEntry.getTermPos())
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
  }
}


class IndexEntryComp implements Comparator<IndexEntry> {
  @Override
  public int compare(IndexEntry o1, IndexEntry o2) {
    return o1.getDocID().compareTo(o2.getDocID());
  }
}
