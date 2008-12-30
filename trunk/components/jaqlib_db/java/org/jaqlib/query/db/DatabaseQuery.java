package org.jaqlib.query.db;

import java.util.Collection;
import java.util.Map;

import org.jaqlib.DatabaseQBProperties;
import org.jaqlib.query.AbstractQuery;
import org.jaqlib.query.FromClause;
import org.jaqlib.util.Assert;
import org.jaqlib.util.reflect.MethodCallRecorder;

/**
 * @author Werner Fragner
 * 
 * @param <T> the result element class of the query.
 */
public class DatabaseQuery<T> extends AbstractQuery<T, DbSelectDataSource>
{

  private final DatabaseQBProperties properties;
  private DbSelectResult<T> resultDefinition;


  public DatabaseQuery(MethodCallRecorder methodCallRecorder,
      DatabaseQBProperties properties)
  {
    super(methodCallRecorder);
    this.properties = Assert.notNull(properties);
  }


  public FromClause<T, DbSelectDataSource> createFromClause(
      DbSelectResult<T> resultDefinition)
  {
    this.resultDefinition = Assert.notNull(resultDefinition);
    return new FromClause<T, DbSelectDataSource>(this);
  }


  private boolean getStrictColumnCheck()
  {
    return properties.getStrictColumnCheck();
  }


  @Override
  protected void addResults(Collection<T> result, boolean stopAtFirstMatch)
  {
    CollectionJaqLibOrMapper<T> orMapper = new CollectionJaqLibOrMapper<T>();
    configureOrMapper(orMapper);

    orMapper.addResults(result, stopAtFirstMatch);
  }


  @Override
  protected <KeyType> void addResults(Map<KeyType, T> resultMap)
  {
    MapJaqLibOrMapper<T> orMapper = new MapJaqLibOrMapper<T>();
    configureOrMapper(orMapper);

    orMapper.addResults(resultMap);
  }


  private void configureOrMapper(AbstractJaqLibOrMapper<T> orMapper)
  {
    orMapper.setDataSource(getDataSource());
    orMapper.setElementPredicate(tree);
    orMapper.setResultDefinition(resultDefinition);
    orMapper.setMethodInvocation(getCurrentInvocation());
    orMapper.setStrictColumnCheck(getStrictColumnCheck());
  }

}
