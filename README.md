# BankSim



### Purpose
When the project is run, it will create a single "bank" along with multiple "accounts". Each of these
accounts have some balance in them and a user can interact with his/her own account by despositing money, 
withdrawing money, or displaying the balance. The primary purpose of these interactions for the project
is to allow for transfering of funds between accounts by first withdrawing a sum from one account to verify
it has the required amount, then depositing that amount into another account.

### Existing Issues In Base Version
In this version of the BankSim, there will be 10 threads, each actively transfering money to and from each other.
Since there is no scheduling mechanism currently implemented, each of these equal-priority threads will
operate in a round-robin schedule. While this doesn't inherently cause a problem, the combination of this
with simultaneous interaction with global variables causes a race condition. 

One case where a race condition occurs is when two threads interact with the same "account". For the purpose
of this explanation, accountA will be used to reference this common account. When two threads call for transfers
involving the same accountA, although the other two accounts accountB and accountC will have the proper end totals,
accountA will only reflect the results of the second thread to finish. This is because at the start of both threads,
the same initial value of accountA is used for the transfer method, which does not reflect the changes caused by the
first thread to finish for accountA; in fact the result of the first thread to finish is simply overwritten.

Of course, this example only details issues with two threads that collide. This problem will compound as more threads
run simultaneously. 

### UML
