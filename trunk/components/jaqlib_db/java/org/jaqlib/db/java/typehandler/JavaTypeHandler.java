package org.jaqlib.db.java.typehandler;


/**
 * <p>
 * Converts a given database value to a Java type value and vice versa.
 * </p>
 * <b>Example:</b>
 * 
 * <pre>
 * public class CreditRatingTypeHandler extends AbstractJavaTypeHandler
 * {
 *   public Object convert(Object value)
 *   {
 *     if (value instanceof Integer)
 *     {
 *       // convert database value to Java enumeration type
 * 
 *       return CreditRating.rating((Integer) value);
 *     }
 *     else if (value instanceof CreditRating)
 *     {
 *       // convert Java enumeration type to database value
 * 
 *       CreditRating rating = (CreditRating) value;
 *       return rating.intValue();
 *     }
 *     else
 *     {
 *       // given value cannot be handled
 * 
 *       throw handleIllegalInputValue(value, CreditRating.class);
 *     }
 *   }
 * }
 * </pre>
 * 
 * @author Werner Fragner
 */
public interface JavaTypeHandler
{

  /**
   * @param value a null object that may be null.
   * @return the converted or untouched object (depending on the type handler
   *         implementation).
   * @throws IllegalArgumentException if the given value cannot be converted to
   *           the desired Java or database type.
   */
  Object convert(Object value);

}
