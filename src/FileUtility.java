import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUtility {
  public List<String> textFileToList(String filePath) {
    List<String> lines = new ArrayList<String>();
    try {
      Scanner sc = new Scanner(new File(filePath));
      while (sc.hasNextLine())
        lines.add(sc.nextLine());
      sc.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return lines;
  }

  public String[] getNextDocText(Scanner sc) {
    String[] urlDocStringPair = new String[2];
    String url = new String();
    StringBuilder lines = new StringBuilder();

    String nextLine = sc.nextLine();
    // get article id
    while (sc.hasNextLine() && !nextLine.equals("<DOCHDR>"))
      nextLine = sc.nextLine();
    url = sc.nextLine();
    // skip the Trec Headers
    while (sc.hasNextLine() && !nextLine.equals("</DOCHDR>"))
      nextLine = sc.nextLine();
    // read doc contents
    while (sc.hasNextLine() && !nextLine.equals("</DOC>")) {
      lines.append(nextLine);
      nextLine = sc.nextLine();
    }

    urlDocStringPair[0] = url;
    urlDocStringPair[1] = lines.toString();
    return urlDocStringPair;
  }

  /**
   * Prints string to the given handle one line at a time
   * 
   * @param output - output handle
   * @param string - output string
   */
  public void println(PrintWriter output, String string) {
    output.println(string);
    output.flush();
  }


  /**
   * Prints string to the given handle one string at a time
   * 
   * @param output - output handle
   * @param string - output string
   */
  public void print(PrintWriter output, String string) {
    output.print(string);
    output.flush();
  }

  public void writeStringToFile(String outputString, String outputPath) {
    try {
      PrintWriter outputHandle = new PrintWriter(outputPath);
      outputHandle.print(outputString);
      outputHandle.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
