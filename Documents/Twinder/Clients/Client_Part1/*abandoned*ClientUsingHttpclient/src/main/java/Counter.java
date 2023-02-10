import java.util.ArrayList;

public class Counter {
  private int successfulCount = 0;
  private int failedCount = 0;
  private int threadCount = 0;
  private ArrayList<Long> latencyList = new ArrayList<>();
  public ArrayList<Long> getLatencyList() {
    return latencyList;
  }

  public void setLatencyList(ArrayList<Long> latencyList) {
    this.latencyList = latencyList;
  }

  synchronized public void latencyAdd(long sec) {
    this.latencyList.add(sec);
  }
  synchronized public void threadInc() {
    this.threadCount++;
  }

  synchronized public void successInc() {
    this.successfulCount++;
  }
  synchronized public void failInc() {
    this.failedCount++;
  }
  public int getThreadCount() {
    return this.threadCount;
  }

  public int getSuccessfulCount() {
    return this.successfulCount;
  }

  public int getFailedCount() {
    return this.failedCount;
  }

}
