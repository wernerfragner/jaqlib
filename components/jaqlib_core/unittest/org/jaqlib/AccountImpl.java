package org.jaqlib;

/**
 * @author Werner Fragner
 */
public class AccountImpl implements Account
{

  private final Long id = Id.getNext();
  private Integer balance = 0;

  private boolean match = false;
  private Object object = null;


  public AccountImpl()
  {
  }


  public AccountImpl(boolean match)
  {
    this.match = match;
  }


  public AccountImpl(Integer balance)
  {
    this.balance = balance;
  }


  public AccountImpl(Object object)
  {
    this(false);
    this.object = object;
  }


  public Long getId()
  {
    return id;
  }


  public boolean isMatch()
  {
    return match;
  }


  public Object getObject()
  {
    return object;
  }


  public Integer getBalance()
  {
    return balance;
  }


  public int compareTo(Account o)
  {
    if (balance == null)
    {
      return (o.getBalance() == null ? 0 : -1);
    }
    return balance.compareTo(o.getBalance());
  }


  @Override
  public String toString()
  {
    return getClass().getName() + ": id=" + id + "; balance=" + balance
        + "; match=" + match + "; object=" + object;
  }


}
