package edu.temple.cis.c3238.banksim;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author Christopher Park
 * @author Scott Salisbury
 */
class TestThread extends Thread {

    private final Bank bank;

    private final Lock numActiveTransactsLock;
    private final Condition testingDone;
    private final Condition readyToTest;

    public TestThread(Bank b) {
        this.bank = b;

        this.numActiveTransactsLock = b.numActiveTransactsLock;
        this.testingDone = b.testingDone;
        this.readyToTest = b.readyToTest;
    }

    @Override
    public void run() {
        while(true) {
            try{
                numActiveTransactsLock.lock();
                while( !bank.canTest() ) {
//                    System.out.println("can't test yet");
                    readyToTest.await();
                }
                bank.test();
                bank.markTestDone();//note- this would have a race condition except that all transfer threads should be waiting for testingDone signals here (see Bank.canTest())
                testingDone.signalAll();
            } catch(InterruptedException e) {
                System.err.println("tester thread interrupted while waiting to run test");
            } finally{
                numActiveTransactsLock.unlock();
            }
        }
    }
}
