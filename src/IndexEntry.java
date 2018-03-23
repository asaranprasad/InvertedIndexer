import java.util.LinkedList;
import java.util.List;

/**
 * Represents an index entry in the inverted index
 * 
 * @author asara
 *
 */
public class IndexEntry {
  private String docID; // id of the document
  private List<Integer> termPos;// term position list
  private int tf;// term frequency

  public IndexEntry(String docID) {
    this.docID = docID;
    termPos = new LinkedList<Integer>();
    tf = 0;
  }

  public IndexEntry(String docID, List<Integer> termPos, int tf) {
    this.docID = docID;
    this.termPos = termPos;
    this.tf = tf;
  }

  public void appendPos(int pos) {
    termPos.add(pos);
    tf++;
  }

  public String getDocID() {
    return docID;
  }

  public void setDocID(String docID) {
    this.docID = docID;
  }

  public List<Integer> getTermPos() {
    return termPos;
  }

  public int getTf() {
    return tf;
  }
}
