package org.jaqlib;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jaqlib.core.Task;
import org.jaqlib.core.WhereCondition;

public class IterableQBExamples
{

  private List<Account> accounts;


  public void methodCallRecorder()
  {
    // get accounts that should be queried

    List<Account> accounts = getAccounts();

    // get recorder object
    Account account = Jaqlib.List.getRecorder(Account.class);

    // select all accounts with a balance greater than 500.0
    List<Account> result = Jaqlib.List.selectFrom(accounts)
        .whereCall(account.getBalance()).isGreaterThan(500.0).asList();
  }


  public void customWhereCondition()
  {
    // create condition for negative balances
    WhereCondition<Account> deptCondition = new WhereCondition<Account>()
    {

      public boolean evaluate(Account account)
      {
        return (account.getBalance() < 0);
      }

    };

    // create condition for accounts with poor credit rating
    WhereCondition<Account> ratingCondition = new WhereCondition<Account>()
    {

      public boolean evaluate(Account account)
      {
        return (account.getCreditRating() == CreditRating.POOR);
      }
    };

    // execute query with these conditions
    List<Account> highRiskAccounts = Jaqlib.List.selectFrom(accounts)
        .where(deptCondition).and(ratingCondition).asList();
  }


  public void filterNullElements()
  {
    List<Account> notNullAccounts = Jaqlib.List.selectFrom(accounts).where()
        .element().isNotNull().asList();
  }


  public void filterComparable()
  {
    // Account implements the Comparable interface; the balance field is used
    // for comparing two accounts
    AccountImpl criteria = new AccountImpl();
    criteria.setBalance(5000.0);

    List<Account> result = Jaqlib.List.selectFrom(accounts).where().element()
        .isSmallerThan(criteria).asList();
  }


  public void mapResult()
  {
    // use the ID field of Account as key for the map
    Account recorder = Jaqlib.List.getRecorder(Account.class);
    Map<Long, Account> results = Jaqlib.List.selectFrom(accounts).asMap(
        recorder.getId());
  }


  public void setResult()
  {
    Set<Account> notNullAccounts = Jaqlib.List.selectFrom(accounts)
        .whereElement().isNotNull().asSet();
  }


  public void listResult()
  {
    List<Account> notNullAccounts = Jaqlib.List.selectFrom(accounts)
        .whereElement().isNotNull().asList();
  }


  public void uniqueResult()
  {
    Account recorder = Jaqlib.List.getRecorder(Account.class);
    Account result = Jaqlib.List.selectFrom(accounts)
        .whereCall(recorder.getId()).isEqual((long) 5).asUniqueResult();
  }


  public void firstResult()
  {
    Account recorder = Jaqlib.List.getRecorder(Account.class);
    Account result = Jaqlib.List.selectFrom(accounts)
        .whereCall(recorder.getBalance()).isGreaterThan(500.0).asFirstResult();
  }


  public void lastResult()
  {
    Account recorder = Jaqlib.List.getRecorder(Account.class);
    Account result = Jaqlib.List.selectFrom(accounts)
        .whereCall(recorder.getBalance()).isGreaterThan(500.0).asLastResult();
  }


  public void executeTask()
  {
    // create task that should be executed for each element
    Task<Account> task = new Task<Account>()
    {

      public void execute(Account account)
      {
        account.sendInfoEmail();
      }

    };
    Jaqlib.List.selectFrom(accounts).execute(task);
  }


  public void executeTaskWithResult()
  {
    Task<Account> task = null;

    // create condition for negative balances
    WhereCondition<Account> deptCond = new WhereCondition<Account>()
    {

      public boolean evaluate(Account account)
      {
        return (account.getBalance() < 0);
      }

    };

    // execute task only on elements that match the given condition
    Jaqlib.List.selectFrom(accounts).where(deptCond).execute(task);

    // or ...
    List<Account> result = Jaqlib.List.selectFrom(accounts).where(deptCond)
        .executeWithResult(task).asList();
  }


  private List<Account> getAccounts()
  {
    // TODO Auto-generated method stub
    return null;
  }

}
