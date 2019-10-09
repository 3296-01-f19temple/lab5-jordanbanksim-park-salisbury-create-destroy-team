package edu.temple.cis.c3238.banksim;
/**
 * @author Christopher Park
 */
class TestThread extends Thread {

    private final Bank bank;

    public TestThread() {
        bank = b;
    }

    @Override
    public void run() {
        if(bank.shouldTest()){
            bank.test();
        }
    }
}
