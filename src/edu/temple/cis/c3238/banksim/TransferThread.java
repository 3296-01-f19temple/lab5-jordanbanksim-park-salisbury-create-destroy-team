package edu.temple.cis.c3238.banksim;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

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

    private final Lock numActiveTransactsLock;
    private final Condition testingDone;
    private final Condition readyToTest;

    public TransferThread(Bank b, int from, int max) {
        bank = b;
        fromAccount = from;
        maxAmount = max;
        this.ntransactsLock = b.ntransactsLock;

        this.numActiveTransactsLock = b.numActiveTransactsLock;
        this.testingDone = b.testingDone;
        this.readyToTest = b.readyToTest;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            int toAccount = (int) (bank.size() * Math.random());
            int amount = (int) (maxAmount * Math.random());

            bank.waitForFundsAvailable(fromAccount, amount);

            try {
                numActiveTransactsLock.lock();
                while (bank.shouldTest()) {
                    readyToTest.signal();
                    testingDone.await();
                }

                bank.incrementNumActiveTransacts();
            } catch (InterruptedException e) {
                System.err.println("transfer thread interrupted while coordinating with tester thread before transaction");
            } finally {
                numActiveTransactsLock.unlock();
            }

            bank.transfer(fromAccount, toAccount, amount);

            ntransactsLock.lock();
            bank.incrementNtransacts();
            bank.checkIfShouldTest();
            ntransactsLock.unlock();

            try {
                numActiveTransactsLock.lock();
                bank.decrementNumActiveTransacts();
                while (bank.shouldTest()) {
                    readyToTest.signal();
                    testingDone.await();
                }
            } catch (InterruptedException e) {
                System.err.println("transfer thread interrupted while coordinating with tester thread after transaction");
            } finally {
                numActiveTransactsLock.unlock();
            }

        }


    }
}
