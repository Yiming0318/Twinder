import java.util.concurrent.CountDownLatch;

public class Client {
  private final static Integer NUMTHREADS = 150;
  private final static  Integer NUMREQUEST = 500000;
  private final static  Double MILLISECONDS = 1000.0;
  public static void main(String[] args) throws InterruptedException {
    Counter counter = new Counter();
    CountDownLatch completed = new CountDownLatch(NUMTHREADS);
    long start = System.currentTimeMillis();
    if (NUMREQUEST % NUMTHREADS == 0) {
      Threads.threadsWithEqualAmountRequest(NUMTHREADS, NUMREQUEST, counter, completed);
    } else {
      Threads.threadsWithDifferentAmountRequest(NUMTHREADS, NUMREQUEST, counter, completed);
    }
    long finish = System.currentTimeMillis();
    double seconds = (finish - start) / MILLISECONDS;
    System.out.println("Threads number should be equal to " + NUMTHREADS + " It is: " + counter.getThreadCount());
    System.out.println("Finished all threads, success request " + counter.getSuccessfulCount());
    System.out.println("Finished all threads, failed request " + counter.getFailedCount());
    System.out.println("Finished all threads, spent " + seconds + " seconds");
    System.out.println("The total throughput in requests per second: " + NUMREQUEST / seconds);
//    System.out.println("The median latency of each request in milliseconds: " + Utilities.median(counter.getLatencyList()));
//    System.out.println("Length: " + counter.getLatencyList().size());
  }

}
