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
        while(bank.getIsOpen()) {
            try{
                numActiveTransactsLock.lock();
                while( !bank.canTest() ) {
                    System.out.println("can't test yet, numActiveTransacts = " + bank.getNumActiveTransacts());
                    //if not for the time limit, it would be possible to have a deadlock
                    // if the tester thread got to here (but hadn't called await() yet) then was preempted
                    // by all of the transfer threads, one of which ran the Bank.close() method to completion
                    // and then all of them finished
                    final long maxWaitPeriod = 90* 1000000000L;//waits at most 1.5 minutes before waking up to check the wait conditions again
                    readyToTest.awaitNanos(maxWaitPeriod);
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
