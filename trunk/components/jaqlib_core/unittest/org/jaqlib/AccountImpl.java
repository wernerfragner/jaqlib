package org.jaqlib;

/**
 * @author Werner Fragner
 */
public class AccountImpl implements Account
{

  private Long id = Id.getNext();

  private String lastName;
  private String firstName;

  private Integer balance = 0;
  private int creditRating = 0;


  public Long getId()
  {
    return id;
  }


  public void setId(Long id)
  {
    this.id = id;
  }


  public void setBalance(Integer balance)
  {
    this.balance = balance;
  }


  public Integer getBalance()
  {
    return balance;
  }


  public String getLastName()
  {
    return lastName;
  }


  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }


  public String getFirstName()
  {
    return firstName;
  }


  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }


  public int getCreditRating()
  {
    return creditRating;
  }


  public void setCreditRating(int creditRating)
  {
    this.creditRating = creditRating;
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
    return getClass().getName() + ": id=" + id + "; balance=" + balance;
  }


}
