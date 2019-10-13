package edu.temple.cis.c3238.banksim;
/**
 * @author Christopher Park
 * @author Scott Salisbury
 */
class TestThread extends Thread {

    private final Bank bank;

    public TestThread() {
        bank = b;
    }

    @Override
    public void run() {
        while(true){
            fundsTransferred.await();
            ntransacts.Lock();
            bank.test();
            ntransact.Unlock();
        }
    }
}
