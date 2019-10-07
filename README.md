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

### Sequence Diagram Of Base Version



### Race Condition Soltions For Base Version
- Synchronized Methods

Synchronized Methods are one tool used for race condition issues. If an object is visible
to more than one thread, all changes made to the object will be handled through the synchronized
method. By adding synchronization to an object, if multiple threads call the method at the same
time, only the first one will execute the method. All other methods must wait for that first
execution to complete before the next thread gets to execute the synchronized method.

- Synchronized Code Blocks

Synchronized Code Blocks also work to rectify race condition issues. Synchronized code
blocks works in a similar way to synchronized methods as the synchronized section is
only accessible and executable by one thread at a time. The primary different between
this and synchronized methods is that synchronized code blocks only locks off a small
predetermined section of a method. This is useful in cases where the method called
by the threads is immensely large, but the section in need of synchronization is small.

- ReentrantLock

Reentrant locks work in a similar manner to synchronized methods, however attempt to solve
a problem with sychronized methods: starvation. Synchronized methods do not innately have 
a queue system, so in programs with large quantities of threads, certain threads may be 
starved for resources. So rather than having a binary lock like synchronized methods do,
the reentrant lock uses an incrementing and decrementing count along with a fairness
paramenter to allocate resources. 

- Conclusion

Since the method being called by the threads is relatively small, I don't think using
synchronized code blocks is necessary as we can simply create a lock around the entire
method. That said, with an initially defined thread count of 10, starvation can potentially
become an issue, a notable downfall of synchronized methods. As a result, I believe that 
reentrant locks will be the best means of solving the race condition in the initial
BankSim build. 
