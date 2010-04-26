package org.jaqlib.xml;

import org.jaqlib.util.FileResource;
import org.jaqlib.util.Resource;

/**
 * Represents the 'FROM' part of the XML query.
 * 
 * @author Werner Fragner
 * 
 * @param <T> the result type of the query.
 */
public class XmlFromClause<T>
{

  private final XmlQuery<T> query;


  /**
   * Default constructor.
   * 
   * @param query
   */
  public XmlFromClause(XmlQuery<T> query)
  {
    this.query = query;
  }


  /**
   * Uses the given <tt>xmlPath</tt> as the source for the query. The returned
   * {@link XmlWhereClause} can be used to constrain the query. XML attributes
   * are used to map XML data to java bean fields. <br>
   * For example,
   * 
   * <pre>
   * {@code 
   * <account lastName="huber" firstName="sepp" />
   * }
   * 
   * public class Account { 
   *   private String lastName; 
   *   private String firstName; 
   * }
   * 
   * </pre>
   * 
   * @param xmlPath the source for the query.
   * @return a {@link XmlWhereClause} to constrain the query.
   */
  public XmlWhereClause<T> from(String xmlPath)
  {
    return from(new FileResource(xmlPath));
  }


  /**
   * Uses the given <tt>xmlPath</tt> as the source for the query. The returned
   * {@link XmlWhereClause} can be used to constrain the query. XML attributes
   * are used to map XML data to java bean fields. <br>
   * For example,
   * 
   * <pre>
   * {@code 
   * <account lastName="huber" firstName="sepp" />
   * }
   * 
   * public class Account { 
   *   private String lastName; 
   *   private String firstName; 
   * }
   * 
   * </pre>
   * 
   * @param xmlPath the source for the query.
   * @return a {@link XmlWhereClause} to constrain the query.
   */
  public XmlWhereClause<T> from(Resource xmlPath)
  {
    return fromAttributes(xmlPath);
  }


  /**
   * Uses the given <tt>xmlPath</tt> as the source for the query. The returned
   * {@link XmlWhereClause} can be used to constrain the query. XML attributes
   * are used to map XML data to java bean fields. <br>
   * For example,
   * 
   * <pre>
   * {@code 
   * <account lastName="huber" firstName="sepp" />
   * }
   * 
   * public class Account { 
   *   private String lastName; 
   *   private String firstName; 
   * }
   * 
   * </pre>
   * 
   * @param xmlPath the source for the query.
   * @return a {@link XmlWhereClause} to constrain the query.
   */
  public XmlWhereClause<T> from(XmlSelectDataSource ds)
  {
    return query.createWhereClause(ds);
  }


  /**
   * Uses the given <tt>xmlPath</tt> as the source for the query. The returned
   * {@link XmlWhereClause} can be used to constrain the query. XML attributes
   * are used to map XML data to java bean fields. <br>
   * For example,
   * 
   * <pre>
   * {@code 
   * <account lastName="huber" firstName="sepp" />
   * }
   * 
   * public class Account { 
   *   private String lastName; 
   *   private String firstName; 
   * }
   * 
   * </pre>
   * 
   * @param xmlPath the source for the query.
   * @return a {@link XmlWhereClause} to constrain the query.
   */
  public XmlWhereClause<T> fromAttributes(String xmlPath)
  {
    return fromAttributes(new FileResource(xmlPath));
  }


  /**
   * Uses the given <tt>xmlPath</tt> as the source for the query. The returned
   * {@link XmlWhereClause} can be used to constrain the query. XML attributes
   * are used to map XML data to java bean fields. <br>
   * For example,
   * 
   * <pre>
   * {@code 
   * <account lastName="huber" firstName="sepp" />
   * }
   * 
   * public class Account { 
   *   private String lastName; 
   *   private String firstName; 
   * }
   * 
   * </pre>
   * 
   * @param xmlPath the source for the query.
   * @return a {@link XmlWhereClause} to constrain the query.
   */
  public XmlWhereClause<T> fromAttributes(Resource xmlPath)
  {
    return from(new XmlSelectDataSource(xmlPath, true));
  }


  /**
   * Uses the given <tt>xmlPath</tt> as the source for the query. The returned
   * {@link XmlWhereClause} can be used to constrain the query. XML attributes
   * are used to map XML data to java bean fields. <br>
   * For example,
   * 
   * <pre>
   * {@code 
   * <account lastName="huber" firstName="sepp" />
   * }
   * 
   * public class Account { 
   *   private String lastName; 
   *   private String firstName; 
   * }
   * 
   * </pre>
   * 
   * @param xmlPath the source for the query.
   * @return a {@link XmlWhereClause} to constrain the query.
   */
  public XmlWhereClause<T> fromAttributes(XmlSelectDataSource dataSource)
  {
    dataSource.setUseAttributes(true);
    return from(dataSource);
  }


  /**
   * Uses the given <tt>xmlPath</tt> as the source for the query. The returned
   * {@link XmlWhereClause} can be used to constrain the query. XML elements are
   * used to map XML data to java bean fields. <br>
   * For example,
   * 
   * <pre>
   * {@code 
   * <account>
   *   <lastName>huber</lastName>
   *   <firstName>sepp</firstName>
   * </account>
   * }
   * 
   * public class Account { 
   *   private String lastName; 
   *   private String firstName; 
   * }
   * 
   * </pre>
   * 
   * @param xmlPath the source for the query.
   * @return a {@link XmlWhereClause} to constrain the query.
   */
  public XmlWhereClause<T> fromElements(String xmlPath)
  {
    return fromElements(new FileResource(xmlPath));
  }


  /**
   * Uses the given <tt>xmlPath</tt> as the source for the query. The returned
   * {@link XmlWhereClause} can be used to constrain the query. XML elements are
   * used to map XML data to java bean fields. <br>
   * For example,
   * 
   * <pre>
   * {@code 
   * <account>
   *   <lastName>huber</lastName>
   *   <firstName>sepp</firstName>
   * </account>
   * }
   * 
   * public class Account { 
   *   private String lastName; 
   *   private String firstName; 
   * }
   * 
   * </pre>
   * 
   * @param xmlPath the source for the query.
   * @return a {@link XmlWhereClause} to constrain the query.
   */
  public XmlWhereClause<T> fromElements(Resource xmlPath)
  {
    return from(new XmlSelectDataSource(xmlPath, false));
  }


  /**
   * Uses the given <tt>xmlPath</tt> as the source for the query. The returned
   * {@link XmlWhereClause} can be used to constrain the query. XML elements are
   * used to map XML data to java bean fields. <br>
   * For example,
   * 
   * <pre>
   * {@code 
   * <account>
   *   <lastName>huber</lastName>
   *   <firstName>sepp</firstName>
   * </account>
   * }
   * 
   * public class Account { 
   *   private String lastName; 
   *   private String firstName; 
   * }
   * 
   * </pre>
   * 
   * @param xmlPath the source for the query.
   * @return a {@link XmlWhereClause} to constrain the query.
   */
  public XmlWhereClause<T> fromElements(XmlSelectDataSource dataSource)
  {
    dataSource.setUseAttributes(false);
    return from(dataSource);
  }

}
