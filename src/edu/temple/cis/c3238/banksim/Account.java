package edu.temple.cis.c3238.banksim;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

/**
 * @author Cay Horstmann
 * @author Modified by Paul Wolfgang
 * @author Modified by Charles Wang
 */
public class Account {

    private volatile int balance;
    private final int id;
    private final Bank myBank;

    private final Lock balanceLock;
    private final Condition sufficientBalance;

    public Account(Bank myBank, int id, int initialBalance) {
        this.myBank = myBank;
        this.id = id;
        balance = initialBalance;
        balanceLock = new ReentrantLock(true);
        sufficientBalance = balanceLock.newCondition();
    }

    public int getBalance() {
        return balance;
    }

    public boolean withdraw(int amount) {
        boolean withdrawalSuccess= false;

        balanceLock.lock();
        //if (amount <= balance) {
            int currentBalance = balance;
            Thread.yield(); // Try to force collision
            int newBalance = currentBalance - amount;
            balance = newBalance;
            withdrawalSuccess = true;
        //}
        balanceLock.unlock();

        return withdrawalSuccess;
    }

    public void deposit(int amount) {
        balanceLock.lock();
        int currentBalance = balance;
        Thread.yield();   // Try to force collision
        int newBalance = currentBalance + amount;
        balance = newBalance;
        //System.out.println("Deposit made to " + this.toString());
        sufficientBalance.signalAll();
        balanceLock.unlock();
    }

    public void waitForAvailableFunds(int amount){
        try{
            balanceLock.lock();
            while ( myBank.getIsOpen() && amount > balance) {
                System.out.println("Account: " + this.toString() + " waiting on funds");
                //if not for the time limit, it would be theoretically possible to have a deadlock
                // if all threads except one got to here (but hadn't called await() yet) then were preempted
                // by the one thread, which ran the Bank.close() method to completion
                final long maxWaitPeriod = 90* 1000000000L;//waits at most 1.5 minutes before waking up to check the wait conditions again
                sufficientBalance.awaitNanos(maxWaitPeriod);
            }
        } catch(InterruptedException ex) {
            System.err.println("transfer thread resumed without available funds");
        } finally{
            if (myBank.getIsOpen())
                System.out.println("Account: " + this.toString() + " has funds");
            else
                System.out.println("Account: " + this.toString() + " was waiting for funds when the bank closed");

            balanceLock.unlock();
        }
    }

    public void wake() {
        balanceLock.lock();
        sufficientBalance.signalAll();
        balanceLock.unlock();
    }

    @Override
    public String toString() {
        return String.format("Account[%d] balance %d", id, balance);
    }
}
