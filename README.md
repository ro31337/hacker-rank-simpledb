# hacker-rank-simpledb

In  May-June 2016 I applied to Thumbtack through HackerRank. Challenge was to create simple Redis-like database with transactions support. My solution comes with java language. I use ItelliJ IDEA CE as my IDE, and Java 8.

You can find solution itself in `solution` folder. You can find test outputs and expectations in `tests` folder. There is also `minified` folder, it contains exactly the same code, but 1 file only. I was only allowed to submit 1 file.

Challenge was not free, and I should get paid $500 for this.

### Challenge description

Since you can't copy from HackerRank page (and I was too lazy to disable JavaScript), here are few screenshots. It's a full challenge description.

![image](https://cloud.githubusercontent.com/assets/1477672/15803197/bc504ba0-2a86-11e6-8bb0-20fe8debced2.png)

![image](https://cloud.githubusercontent.com/assets/1477672/15803200/d241c506-2a86-11e6-94fb-ceefdf3a6aaf.png)

![image](https://cloud.githubusercontent.com/assets/1477672/15803202/def52df6-2a86-11e6-9f81-7d15d86e371f.png)

![image](https://cloud.githubusercontent.com/assets/1477672/15803203/eb11e7fa-2a86-11e6-9072-3ab4e9620c03.png)

### Reply from Thumbtack

What is a problem is the fact that his GET command iterates through all open transactions to find the value, which violates the requirement of that command not being dependent on the number of active transactions.

### My answer to their reply

Thanks for reply and checking with engineer, ... I really like and appreciate this kind of technical feedback.

But in requirements it's mentioned that M << N << T.

Where N is the total number of variables stored in the database.
And M is number of variables updated in transaction.
and T is the number of transactions.

Most of transactions are empty ("the vast majority of transactions will only update a small number of variables" + M <<<< T). All the operations not used by the algorithm are O(0) and by that rule they are not considered on calculations. Performance of the database itself is O(1). Considering that M <<<< T, I think that conditions are met.

If M << N, overall performance of the database can be considered as O(1).

### Result

Never heard back from Thumbtack again.
