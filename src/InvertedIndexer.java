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
import java.util.Set;

public class InvertedIndexer {
  private HashMap<String, Integer> docLength;
  private HashMap<String, HashMap<String, IndexEntry>> invIndex;
  private HashMap<String, Integer> termFreq;
  private HashMap<String, List<String>> docFreq;
  private List<String> stopList;
  private int collectionLength;
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
    stopList = new LinkedList<String>();
    collectionLength = 0;
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

  public void printDocStatToFile(int n) {
    try {
      System.out.println("Start: Printing Doc Stat");
      PrintWriter statOutput = new PrintWriter(config.getDocStatPath(n));
      for (Map.Entry<String, Integer> entry : docLength.entrySet())
        statOutput.println(entry.getKey() + " " + entry.getValue());
      statOutput.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    System.out.println("Done: Printing Doc Stat");
  }

  public void generateNgramInvIndexFromCorpus(int n, boolean storeTermPos) {
    invIndex = new HashMap<String, HashMap<String, IndexEntry>>();
    File[] directoryListing = parsedDocumentsDir.listFiles();
    int fileCount = 0;
    collectionLength = 0;
    if (directoryListing != null)
      for (File doc : directoryListing) {
        String[] words = fUtility.textFileToString(doc).split(" ");
        String docID = trimFileExtn(doc.getName());
        int windowingLimit = words.length - (n - 1);
        collectionLength += windowingLimit;
        // Task 2b - Store the number of terms in each document in a separate data structure
        docLength.put(docID, windowingLimit);
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
    // print doc stats to file
    printDocStatToFile(n);
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


  /* Based on the concepts discussed in the paper - 
   * "Automatically Building a Stopword List for an Information Retrieval System"
   * Uses Term Based Random Sampling Approach
   * http://terrierteam.dcs.gla.ac.uk/publications/rtlo_DIRpaper.pdf */
  //  public void generateStopListUsingTBRS(int n) {
  //    int X = 200, Y = 1000, L = 400;
  //
  //    List<Map.Entry<String, Double>> stopWordsAndWeightsList =
  //        new LinkedList<Map.Entry<String, Double>>();
  //    List<String> termFreqs = new ArrayList<String>(termFreq.keySet());
  //    for (int a = 0; a < Y; a++) {
  //      // Randomly choose a term in the lexicon file
  //      Random rand = new Random();
  //      String wRand = termFreqs.get(rand.nextInt(termFreqs.size()));
  //
  //      // Retrieve all the documents in the corpus that contains wRand
  //      Set<String> docIDs = invIndex.get(wRand).keySet();
  //
  //      // Use the refined Kullback-Leibler divergence measure to assign a weight
  //      // to every term in the retrieved documents. The assigned weight will give
  //      // us some indication of how important the term is.
  //      HashMap<String, Double> weight = new HashMap<String, Double>();
  //      int lx = 0; // sum of length of sampled document set
  //
  //      HashMap<String, Integer> sampledDocTermFreq = new HashMap<String, Integer>();
  //      for (String docID : docIDs) {
  //        File doc = new File(config.getParserOutputPath() + docID + ".txt");
  //        String[] words = fUtility.textFileToString(doc).split(" ");
  //        int termCount = words.length;
  //        int windowingLimit = termCount - (n - 1);
  //        lx += windowingLimit;
  //        for (int i = 0; i < windowingLimit; i++) {
  //          String term = new String();
  //          term = words[i];
  //          if (n > 1)
  //            term = term + " " + words[i + 1];
  //          if (n > 2)
  //            term = term + " " + words[i + 2];
  //          if (!term.isEmpty()) {
  //            // if the term occurs for the first time
  //            if (!sampledDocTermFreq.containsKey(term))
  //              sampledDocTermFreq.put(term, 1);
  //            else {
  //              int prevFreq = sampledDocTermFreq.get(term);
  //              sampledDocTermFreq.put(term, prevFreq + 1);
  //            }
  //          }
  //        }
  //      }
  //
  //      double maxWeight = -1;
  //      for (Map.Entry<String, Integer> entry : sampledDocTermFreq.entrySet()) {
  //        String term = entry.getKey();
  //        int tfx = entry.getValue(); // freq of the term in sampled document set
  //        int F = termFreq.get(term);// freq of the term in the whole collection
  //        double Px = ((double) tfx / (double) lx);
  //        double Pc = ((double) F / (double) collectionLength);
  //        double weightT = Px * (Math.log(Px / Pc) / Math.log(2));
  //        weight.put(term, weightT);
  //        if (maxWeight < weightT)
  //          maxWeight = weightT;
  //      }
  //
  //      // Divide each term’s weight by the maximum weight of all terms. As
  //      // a result, all the weights are controlled within [0,1]. In other words,
  //      // Normalize each weighted term by the maximum weight. 
  //      for (Map.Entry<String, Double> entry : weight.entrySet()) {
  //        String term = entry.getKey();
  //        double weightT = entry.getValue();
  //        weight.put(term, weightT / maxWeight);
  //      }
  //
  //      // Rank the weighted terms by their associated weight in ascending order.
  //      // Since the less informative a term is, the less useful a term is and hence,
  //      // the more likely it is a stopword.
  //      List<Map.Entry<String, Double>> leastImportantTerms =
  //          new LinkedList<Map.Entry<String, Double>>(weight.entrySet());
  //      Collections.sort(leastImportantTerms, new MapComparatorByValues());
  //
  //      // Extract the top X top-ranked (i.e. least weighted)
  //      if (leastImportantTerms.size() > X)
  //        leastImportantTerms.subList(X, leastImportantTerms.size()).clear();
  //
  //      // Add to final list
  //      stopWordsAndWeightsList.addAll(leastImportantTerms);
  //      System.out.println(a);
  //    }
  //
  //    // Combine terms in stopList
  //    Collections.sort(stopWordsAndWeightsList, new MapComparatorByKeys());
  //    Iterator<Entry<String, Double>> it = stopWordsAndWeightsList.iterator();
  //    Entry<String, Double> prev = it.next();
  //    double sum = prev.getValue();
  //    int count = 1;
  //    while (it.hasNext()) {
  //      Entry<String, Double> curr = it.next();
  //      if (curr.getKey().equals(prev.getKey())) {
  //        sum += curr.getValue();
  //        count++;
  //        it.remove();
  //      } else {
  //        prev.setValue(sum / (double) count);
  //        prev = curr;
  //        sum = curr.getValue();
  //        count = 1;
  //      }
  //    }
  //
  //    Collections.sort(stopWordsAndWeightsList, new MapComparatorByValues());
  //    // Extract the top L top-ranked (i.e. least weighted)
  //    if (stopWordsAndWeightsList.size() > L)
  //      stopWordsAndWeightsList.subList(L, stopWordsAndWeightsList.size()).clear();
  //
  //    // Print stopwords to output
  //    try {
  //      PrintWriter stopWordsOut = new PrintWriter(config.getNGramStopListPathForTBRS(n));
  //      for (Map.Entry<String, Double> stopWordAndWeight : stopWordsAndWeightsList) {
  //        stopList.add(stopWordAndWeight.getKey());
  //        fUtility.println(stopWordsOut, stopWordAndWeight.getKey());
  //      }
  //      stopWordsOut.close();
  //    } catch (FileNotFoundException e) {
  //      e.printStackTrace();
  //    }
  //  }



  public void generateStopListUsingTfidf(int n, int limit) {
    HashMap<String, Double> weights = new HashMap<String, Double>();
    Set<String> termSet = termFreq.keySet();
    double maxWeight = Double.MIN_VALUE;
    for (String t : termSet) {
      HashMap<String, IndexEntry> docEntries = invIndex.get(t);
      double sumTfIdf = 0;

      // Total number of documents
      int totNumDocs = docLength.size();
      // Number of documents with term t in it
      int numDocsWithT = docFreq.get(t).size();
      // IDF(t) = log_e(Total number of documents / Number of documents with term t in it).
      double IDF_t = Math.log((double) totNumDocs / (double) numDocsWithT);
      for (Map.Entry<String, IndexEntry> docEntry : docEntries.entrySet()) {
        // Number of times term t appears in a document
        int numT = docEntry.getValue().getTf();
        // Total number of terms in the document
        int totNumTerms = docLength.get(docEntry.getKey());

        // TF(t) = (Number of times term t appears in a document) / (Total number of terms in the document).
        double TF_t = ((double) numT / (double) totNumTerms);
        sumTfIdf += TF_t;
      }
      double avgTfIdf = (sumTfIdf / (double) docEntries.size()) * IDF_t;
      weights.put(t, avgTfIdf);
      if (avgTfIdf > maxWeight)
        maxWeight = avgTfIdf;
    }

    // Normalize weights using maxWeight
    for (Map.Entry<String, Double> termWeight : weights.entrySet())
      weights.put(termWeight.getKey(), termWeight.getValue() / maxWeight);

    // sort terms by increasing order of tf-idf weights
    List<Map.Entry<String, Double>> termWeights =
        new LinkedList<Map.Entry<String, Double>>(weights.entrySet());
    Collections.sort(termWeights, new MapComparatorByValues());

    // Print stop words to file
    try {
      PrintWriter stopWordsOut = new PrintWriter(config.getNGramStopListPath(n));
      int i = 1;
      for (Map.Entry<String, Double> stopWordAndWeight : termWeights) {
        stopList.add(stopWordAndWeight.getKey());
        fUtility.println(stopWordsOut,
            stopWordAndWeight.getKey() + " " + stopWordAndWeight.getValue());
        i++;
        if (i > limit)
          break;
      }
      stopWordsOut.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}


class MapComparatorByValues implements Comparator<Map.Entry<String, Double>> {
  @Override
  public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
    return (o1.getValue()).compareTo(o2.getValue());
  }
}


class MapComparatorByKeys implements Comparator<Map.Entry<String, Double>> {
  @Override
  public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
    return (o1.getKey()).compareTo(o2.getKey());
  }
}
