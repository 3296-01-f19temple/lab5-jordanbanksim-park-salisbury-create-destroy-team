# NOTES

### 2a. Thread Synchronization Techniques
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
