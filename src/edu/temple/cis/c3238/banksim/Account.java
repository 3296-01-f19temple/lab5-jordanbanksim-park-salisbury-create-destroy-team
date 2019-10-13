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

    private final ReentrantLock balanceLock;

    public Account(Bank myBank, int id, int initialBalance) {
        this.myBank = myBank;
        this.id = id;
        balance = initialBalance;
        balanceLock = new ReentrantLock(true);
    }

    public int getBalance() {
        return balance;
    }

    public boolean withdraw(int amount) {
        boolean withdrawalSuccess= false;

        balanceLock.lock();
        if (amount <= balance) {
            int currentBalance = balance;
//            Thread.yield(); // Try to force collision
            int newBalance = currentBalance - amount;
            balance = newBalance;
            withdrawalSuccess = true;
        }
        balanceLock.unlock();

        return withdrawalSuccess;
    }

    public void deposit(int amount) {
        balanceLock.lock();
        int currentBalance = balance;
//        Thread.yield();   // Try to force collision
        int newBalance = currentBalance + amount;
        balance = newBalance;
        balanceLock.unlock();
    }
    
    @Override
    public String toString() {
        return String.format("Account[%d] balance %d", id, balance);
    }
}
