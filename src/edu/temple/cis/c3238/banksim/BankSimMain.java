package edu.temple.cis.c3238.banksim;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

/**
 * @author Cay Horstmann
 * @author Modified by Paul Wolfgang
 * @author Modified by Charles Wang
 */
public class BankSimMain {

    public static final int NACCOUNTS = 10;
    public static final int INITIAL_BALANCE = 10000;


    public static void main(String[] args) {
        Bank b = new Bank(NACCOUNTS, INITIAL_BALANCE);
        Thread[] threads = new Thread[NACCOUNTS]; 
        // Start a thread for each account
        Lock ntransactsLock = new ReentrantLock(true);
        Condition fundsTransferred = ntransactsLock.newCondition();

        Thread testerThread = new TestThread(b, ntransactsLock, fundsTransferred);
        testerThread.start();
        for (int i = 0; i < NACCOUNTS; i++) {
            threads[i] = new TransferThread(b, i, INITIAL_BALANCE, ntransactsLock, fundsTransferred);
            threads[i].start();
        }

          System.out.println("Bank transfer is in the process.");
    }
}


