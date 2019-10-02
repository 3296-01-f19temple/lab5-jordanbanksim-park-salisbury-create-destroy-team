# BankSim - Multithreading and Synchronization in Java



***NOTE:** Any UML diagrams, written responses to questions, and notes on teamwork and testing should be uploaded to the repository’s README.md.*

### Task 1:

- **1a.** Before running the given source code, spend some time inspecting each of the classes in the project. Provide a high-level description of the requirements of the BankSim project. Keep the description non-technical, explain the semantics/significance behind what is actually happening when you run the project.
- **1b.** Now provide a brief technical overview of the project, paying particular attention to how multithreading is currently supported. Under the current implementation, there is a major race condition that inhibits the successful execution of the Bank; what is it?
- **1c.** Draw a UML sequence diagram to support your technical comprehension (the race condition should be apparent in the diagram)
- **1d.** Upload these written tasks and your sequence diagram to the repository’s README.md

### Task 2:

- **2a.** Research these three common thread synchronization techniques in Java. Describe some similarities and differences between the three. Provide an argument for which technique seems most applicable to the current race condition problem:

  - Synchronized Methods
  - Synchronized Code Blocks
  - ReentrantLock
 - **2b.** Implement one of these synchronization solutions to resolve the race condition discovered in Task 1. **IMPORTANT:** The solution *must* allow the bank to transfer multiple funds between two unrelated accounts:
*(i.e.)* A transfer from Account [2] to Account [3] *AND* a transfer from Account [6] to Account [7] can occur at the same time.

### Task 3:

- **3a.** The race condition discovered in Task 1 should be resolved, but your BankSim will still more than likely hold another race condition. Using the later steps of this task as a hint, provide an explanation for this second race condition.
- **3b.** Refactor the Bank class such that the testing method occurs on a separate thread from the transfer thread.
- **3c.** Implement mutual exclusion between the testing thread and the transferring thread.
  - *(Hint):* When the testing thread is called it must send a signal to each transferring thread.

### Task 4:

- **4a.** A transfer cannot occur between two Accounts if the requested withdrawal by the *‘to’* Account exceeds the balance within the *‘from’* Account. Explain why the default implementation--while functional--is a poor solution in a multithreading environment.
- **4b.** Design and implement a wait/notify solution to defer any invalid transfers until the *‘from’* Account’s balance exceeds the requested amount. Be sure to document your solution.

### Task 5:

- **5a.** A deadlock condition may occur that blocks the successful termination of the BankSim after the simulation has reached the predefined number of transfers. What is causing this deadlock?
- **5b.** Implement a solution to this deadlock condition.
  - *(Hint):* Which thread ‘knows’ exactly when the simulation has completed?
