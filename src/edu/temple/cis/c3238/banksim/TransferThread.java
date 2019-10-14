package edu.temple.cis.c3238.banksim;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

/**
 * @author Cay Horstmann
 * @author Modified by Paul Wolfgang
 * @author Modified by Charles Wang
 */
class TransferThread extends Thread {

    private final Bank bank;
    private final int fromAccount;
    private final int maxAmount;
    private final Lock ntransactsLock;
    private final Condition fundsTransferred;

    public TransferThread(Bank b, int from, int max, Lock ntransactsLock, Condition fundsTransferred) {
        bank = b;
        fromAccount = from;
        maxAmount = max;
        this.ntransactsLock = ntransactsLock;
        this.fundsTransferred = fundsTransferred;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            int toAccount = (int) (bank.size() * Math.random());
            int amount = (int) (maxAmount * Math.random());
            bank.transfer(fromAccount, toAccount, amount);
            if(bank.shouldTest()){
                //fundsTransferred.signal();
            }
            
        }
    }
}
