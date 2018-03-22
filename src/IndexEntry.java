import java.util.LinkedList;
import java.util.List;

public class IndexEntry {
  private String docID;
  private List<Integer> termPos;
  private int tf;

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
