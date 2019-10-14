package edu.temple.cis.c3238.banksim;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;

/**
 * @author Christopher Park
 * @author Scott Salisbury
 */
class TestThread extends Thread {

    private final Bank bank;
    public Lock ntransactsLock;
    public Condition fundsTransferred;

    public TestThread(Bank b, Lock ntransactsLock, Condition fundsTransferred) {
        this.bank = b;
        this.ntransactsLock = ntransactsLock;
        this.fundsTransferred = fundsTransferred;
    }

    @Override
    public void run() {
        while(true) {
            try{
                ntransactsLock.lock();
                fundsTransferred.await();
                bank.test();
            } catch(InterruptedException e) {
                System.err.println("tester thread interrupted while waiting to run test");
            } finally{
                ntransactsLock.unlock();
            }
        }
    }
}
