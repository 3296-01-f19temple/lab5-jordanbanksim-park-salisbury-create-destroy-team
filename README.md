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
operate in a round-robin schedule. As a result, in certain situations, we will run into a race condition.

The most likely case that a race condition will occur is if two threads interact with the same "account". 
