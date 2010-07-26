package org.jaqlib;

public class TransactionImpl implements Transaction
{

  private String id;
  private double amount;


  public String getId()
  {
    return id;
  }


  public void setId(String id)
  {
    this.id = id;
  }


  public double getAmount()
  {
    return amount;
  }


  public void setAmount(double amount)
  {
    this.amount = amount;
  }


  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(amount);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }


  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TransactionImpl other = (TransactionImpl) obj;
    if (Double.doubleToLongBits(amount) != Double
        .doubleToLongBits(other.amount))
      return false;
    if (id == null)
    {
      if (other.id != null)
        return false;
    }
    else if (!id.equals(other.id))
      return false;
    return true;
  }


}
