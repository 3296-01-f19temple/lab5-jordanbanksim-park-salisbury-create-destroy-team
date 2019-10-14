package edu.temple.cis.c3238.banksim;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

/**
 * @author Christopher Park
 * @author Scott Salisbury
 */
class TestThread extends Thread {

    private final Bank bank;
    public static Lock ntransactsLock;
    public static Condition fundsTransferred;

    public TestThread(Bank b, Lock ntransactsLock, Condition fundsTransferred) {
        this.bank = b;
        this.ntransactsLock = ntransactsLock;
        this.fundsTransferred = fundsTransferred;
    }

    @Override
    public void run() {
        while(true){
            //fundsTransferred.await();
            //ntransactsLock.Lock();
            try{
                bank.test();
            } 
            finally{
                //ntransactsLock.Unlock();
            }
        }
    }
}
