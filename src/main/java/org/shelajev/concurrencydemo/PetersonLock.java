package org.shelajev.concurrencydemo;

/*
 * The three criteria are mutual exclusion, progress, and bounded waiting.
 *
 * P0 and P1 can never be in the critical section at the same time
 *
 * if no process is executing in its critical section and some processes wish
 * to enter their critical sections, then only those processes that are not
 * executing in their remainder sections can participate in making the decision
 * as to which process will enter its critical section next.
 *
 * the number of times a process is bypassed by another process after it
 * has indicated its desire to enter the critical section is bounded by
 * a function of the number of processes in the system.
 */
public class PetersonLock {
  public volatile boolean flag0 = false;
  public volatile boolean flag1 = false;

  public volatile int turn;

}
