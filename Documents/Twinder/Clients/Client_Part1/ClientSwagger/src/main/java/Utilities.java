import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
public class Utilities {
  private static final String LEFT = "left";
  private static final String RIGHT = "right";
  private static final Integer MAX_CHAR_NUMBER = 256;
  private static final String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
  public static Integer generateRandomInt(Integer minValue, Integer maxValue){
//    Random rn = new Random();
    // minValue + rn.nextInt(maxValue - minValue + 1)
//    return minValue + rn.nextInt(maxValue - minValue + 1);
    return minValue + ThreadLocalRandom.current().nextInt(maxValue - minValue + 1);
  }
  public static String generateLeftOrRight(){
    int randomNumber = generateRandomInt(0,1);
    if (randomNumber <= 0.5) {
      return LEFT;
    } else {
      return RIGHT;
    }
  }

  public static String generateComment(){
    StringBuilder sb = new StringBuilder();
//    Random random = new Random();
    int commentLength = generateRandomInt(1,MAX_CHAR_NUMBER);
    for (int i = 0; i < commentLength; i++) {
//      sb.append(candidateChars.charAt(random.nextInt(candidateChars
//          .length())));
      sb.append(candidateChars.charAt(ThreadLocalRandom.current().nextInt(candidateChars
          .length())));
    }
    return sb.toString();
  }

  public static long median(ArrayList<Long> values) {
    Collections.sort(values);
    if (values.size() % 2 == 1)
      return values.get((values.size() + 1) / 2 - 1);
    else {
      long lower = values.get(values.size() / 2 - 1);
      long upper = values.get(values.size() / 2);

      return (lower + upper) / 2;
    }
  }


}
