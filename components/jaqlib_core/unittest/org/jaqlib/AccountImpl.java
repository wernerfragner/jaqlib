package org.jaqlib;

/**
 * @author Werner Fragner
 */
public class AccountImpl implements Account
{

  private Long id = Id.getNext();

  private String lastName;
  private String firstName;

  private Double balance = 0.0;
  private CreditRating creditRating = CreditRating.POOR;

  private boolean changed = false;


  public Long getId()
  {
    return id;
  }


  public boolean hasChanged()
  {
    return changed;
  }


  private void setChanged()
  {
    changed = true;
  }


  public void setId(Long id)
  {
    setChanged();
    this.id = id;
  }


  public void setBalance(Double balance)
  {
    setChanged();
    this.balance = balance;
  }


  public Double getBalance()
  {
    return balance;
  }


  public String getLastName()
  {
    return lastName;
  }


  public void setLastName(String lastName)
  {
    setChanged();
    this.lastName = lastName;
  }


  public String getFirstName()
  {
    return firstName;
  }


  public void setFirstName(String firstName)
  {
    setChanged();
    this.firstName = firstName;
  }


  public CreditRating getCreditRating()
  {
    return creditRating;
  }


  public void setCreditRating(CreditRating creditRating)
  {
    setChanged();
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
