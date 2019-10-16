package edu.temple.cis.c3238.banksim;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Cay Horstmann
 * @author Modified by Paul Wolfgang
 * @author Modified by Charles Wang
 */

public class Bank {

    public static final int NTEST = 10;
    private final Account[] accounts;

    private long ntransacts;//note- this only exactly measures the number of transactions completed so far when numActiveTransacts == 0
    public final Lock ntransactsLock;


    private long numActiveTransacts;
    public final Lock numActiveTransactsLock;
    public final Condition testingDone;
    public final Condition readyToTest;

    private boolean shouldTest;

    private final int initialBalance;
    private final int numAccounts;

    private boolean isOpen;

    public Bank(int numAccounts, int initialBalance) {
        this.initialBalance = initialBalance;
        this.numAccounts = numAccounts;
        accounts = new Account[numAccounts];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account(this, i, initialBalance);
        }
        ntransacts = 0;
        numActiveTransacts = 0;

        isOpen = true;

        shouldTest = false;

        ntransactsLock = new ReentrantLock(true);

        numActiveTransactsLock = new ReentrantLock(true);
        testingDone = numActiveTransactsLock.newCondition();
        readyToTest = numActiveTransactsLock.newCondition();
    }

    public void waitForFundsAvailable(int accountId, int amount) {
        accounts[accountId].waitForAvailableFunds(amount);
    }

    public void transfer(int from, int to, int amount) {
        if (accounts[from].withdraw(amount)) {
            accounts[to].deposit(amount);
        }
//        System.out.println("another transaction: ntransacts= " + ntransacts);
    }

    public void test() {
        int sum = 0;
        for (Account account : accounts) {
            System.out.printf("%s %s%n", 
                    Thread.currentThread().toString(), account.toString());
            sum += account.getBalance();
        }
        System.out.println(Thread.currentThread().toString() + 
                " Sum: " + sum);
        if (sum != numAccounts * initialBalance) {
            System.out.println(Thread.currentThread().toString() + 
                    " Money was gained or lost");
            System.exit(1);
        } else {
            System.out.println(Thread.currentThread().toString() + 
                    " The bank is in balance");
        }
    }

    public int size() {
        return accounts.length;
    }


    public boolean shouldTest() {
        return shouldTest;
    }

    public boolean canTest() {
//        System.out.println("should test: " + shouldTest + " and numActiveTransacts: " + numActiveTransacts);
        return shouldTest && numActiveTransacts == 0;
    }

    public void checkIfShouldTest() {
        if(ntransacts % NTEST == 0)
            shouldTest = true;
    }

    public void markTestDone() { shouldTest = false; }


    public long getNtransacts() { return ntransacts; }

    public void incrementNtransacts() {
        ntransactsLock.lock();
        ntransacts++;
        ntransactsLock.unlock();
    }

    public long getNumActiveTransacts() {
        return numActiveTransacts;
    }

    public void incrementNumActiveTransacts() {
        numActiveTransactsLock.lock();
        this.numActiveTransacts++;
        numActiveTransactsLock.unlock();
    }
    public void decrementNumActiveTransacts() {
        numActiveTransactsLock.lock();
        this.numActiveTransacts--;
        numActiveTransactsLock.unlock();
    }

    public boolean getIsOpen() { return isOpen; }

    public void close() {
        isOpen = false;
        shouldTest = true;
        for (Account account : accounts) {
            account.wake();
        }
        System.out.println("Bank is closing");
    }

}
