package org.jaqlib.tool;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

public class WikitextWriter
{

  private FileWriter writer;


  public WikitextWriter(String fileName)
  {
    try
    {
      writer = new FileWriter(fileName);
    }
    catch (IOException e)
    {
      throw toRuntimeException(e);
    }
  }


  public void writeLine(String text)
  {
    text = prepareText(text);
    writeLineToFile(text);
  }


  private void writeLineToFile(String text)
  {
    try
    {
      writer.write(text + "\n");
    }
    catch (IOException e)
    {
      throw toRuntimeException(e);
    }
  }


  private void close()
  {
    try
    {
      writer.close();
    }
    catch (IOException e)
    {
      throw toRuntimeException(e);
    }
  }


  private RuntimeException toRuntimeException(IOException e)
  {
    RuntimeException re = new RuntimeException(e);
    re.setStackTrace(e.getStackTrace());
    return re;
  }


  private String prepareText(String text)
  {
    text = replaceAll(text, " \\* ", "");
    text = replaceAll(text, "<i>", "''");
    text = replaceAll(text, "</i>", "''");
    text = replaceAll(text, "\\{@link #?(.*)\\}", "''$1''");
    text = replaceAll(text, "<A HREF=\"(.*)\">(<CODE>)?(.*)(</CODE>)?</A>",
        "[$1 $3]");
    text = replaceAll(text, "<a href=\"(.*)\">(<code>)?(.*)(</code>)?</a>",
        "[$1 $3]");
    return text;
  }


  private String replaceAll(String text, String regEx, String replacement)
  {
    Pattern p = Pattern.compile(regEx);
    return p.matcher(text).replaceAll(replacement);
  }


  private static void transform(String queryBuilder)
      throws FileNotFoundException, IOException
  {
    WikitextWriter writer = new WikitextWriter("output/" + queryBuilder
        + ".txt");

    FileReader reader = new FileReader("input/" + queryBuilder + ".javadoc");
    BufferedReader bReader = new BufferedReader(reader);

    while (bReader.ready())
    {
      String text = bReader.readLine();
      writer.writeLine(text);
    }

    reader.close();
    writer.close();
  }


  private static final String[] QUERYBUILDERS = new String[] {
      "IterableQueryBuilder", "DatabaseQueryBuilder", "XmlQueryBuilder" };


  /**
   * TODO after executing this tool:
   * <ul>
   * <li>remove @code parts.</li>
   * <li>Convert <a href=""></a> in [url name].</li>
   * </ul>
   */
  public static void main(String[] args) throws IOException
  {
    for (String queryBuilder : QUERYBUILDERS)
    {
      transform(queryBuilder);
    }
  }

}
