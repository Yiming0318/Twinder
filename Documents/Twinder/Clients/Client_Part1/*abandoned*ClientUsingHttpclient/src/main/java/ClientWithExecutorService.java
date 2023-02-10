import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class ClientWithExecutorService {
  private static final Integer MIN_ID = 1;
  private static final Integer SWIPER_MAX_ID = 5000;
  private static final Integer SWIPEE_MAX_ID = 1000000;
  private static final Integer NUMTHREADS = 4;
  private static final Integer NUMREQUESTS = 8000;
  public static void main(String[] args) throws Exception {
    ArrayList <URI> uris = generateRandomURIS(NUMREQUESTS);
    Counter counter = new Counter();
    int numThreads = NUMTHREADS;
    int numRequestsPerThread = NUMREQUESTS / numThreads;
    List<Callable<Void>> tasks = new ArrayList<>();
    for (int i = 0; i < numThreads; i++) {
      int startIndex = i * numRequestsPerThread;
      int endIndex = (i + 1) * numRequestsPerThread - 1;
      tasks.add(() -> {
        try (CloseableHttpClient client = defineClient()) {
          for (int j = startIndex; j <= endIndex; j++) {
            HttpPost request = new HttpPost(uris.get(j));
            try (CloseableHttpResponse response = client.execute(request)) {
              int statusCode = response.getStatusLine().getStatusCode();
              if (statusCode != HttpStatus.SC_CREATED) {
                counter.failInc();
                System.err.println("Method failed: " + response.getStatusLine());
              } else {
//                System.out.println("Response status code: " + statusCode);
                String responseBody = EntityUtils.toString(response.getEntity());
//                System.out.println("Response body: " + responseBody);
                counter.successInc();
              }
            }
          }
        }
        return null;
      });
    }
    long start = System.currentTimeMillis();
    ExecutorService executor = Executors.newFixedThreadPool(numThreads);
    List<Future<Void>> results = executor.invokeAll(tasks);
    executor.shutdown();
    long end = System.currentTimeMillis();
    double seconds = (end - start) / 1000.0;
    System.out.println("Finished all threads, spent " + seconds + " seconds");
    System.out.println("The total throughput in requests per second: " + NUMREQUESTS / seconds);
    System.out.println("Finished all threads, success request " + counter.getSuccessfulCount());
    System.out.println("Finished all threads, failed request " + counter.getFailedCount());
    System.out.println("The total throughput in requests per second: " + NUMREQUESTS / seconds);
  }

  private static ArrayList <URI> generateRandomURIS(int numRequest) throws URISyntaxException {
    ArrayList<URI> listOfURI = new ArrayList<URI>();
    String swipe = Utilities.generateLeftOrRight();
    int swiper = Utilities.generateRandomInt(MIN_ID, SWIPER_MAX_ID);
    int swipee = Utilities.generateRandomInt(MIN_ID, SWIPEE_MAX_ID);
    String comment = Utilities.generateComment();
    for (int i = 0; i < numRequest; i++) {
      // Create a method instance.
      URI uri = new URIBuilder()
          .setScheme("http")
          .setHost("localhost:8080")
          .setPath("/ServePrototype_war_exploded/swipe/" + swipe)
          .setParameter("swiper", String.valueOf(swiper))
          .setParameter("swipee", String.valueOf(swipee))
          .setParameter("comment", comment)
          .build();
      listOfURI.add(uri);
    }
    return listOfURI;
  }


  private static CloseableHttpClient defineClient(){
    // Provide custom retry handler is necessary, retry 5 times before counting it as a failed request
    HttpRequestRetryHandler retryHandler = (exception, executionCount, context) -> {
      if (executionCount >= 5) {
        return false;
      }
      return exception != null;
    };
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(200);
    connectionManager.setDefaultMaxPerRoute(200);
    return HttpClientBuilder.create()
        .setRetryHandler(retryHandler)
        .setConnectionManager(connectionManager)
        .build();
  };
}
