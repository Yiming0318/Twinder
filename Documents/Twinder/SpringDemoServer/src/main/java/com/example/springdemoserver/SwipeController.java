package com.example.springdemoserver;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import entities.ResponseMessage;
import entities.SwipeDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/swipe")
public class SwipeController {
  private static final String LEFT = "left";
  private static final String RIGHT = "right";
  private static final String MISSING_PARA = "Missing parameters!";
  private static final String INVALID_INPUT = "Invalid inputs";
  private static final String WRITE_SUCCESS = "Write successfully";
  private static final String USER_NOT_FOUND = "User not found";
  private static final Integer MIN_ID = 1;
  private static final Integer SWIPER_MAX_ID = 5000;
  private static final Integer SWIPEE_MAX_ID = 1000000;
  private static final Integer LEGAL_CHAR_NUMBER = 256;

  private boolean isValidLeftOrRight(String leftOrRight) {
    return leftOrRight.equals(LEFT) || leftOrRight.equals(RIGHT);
  }

  private boolean isValidRangeSwiper(String swiper) {
    try {
      int swiperID = Integer.parseInt(swiper);
      return (swiperID >= MIN_ID && swiperID <= SWIPER_MAX_ID);
    }catch(NumberFormatException e){
      return false;
    }
  }

  private boolean isValidRangeSwipee(String swipee) {
    try {
      int swipeeID = Integer.parseInt(swipee);
      return (swipeeID >= MIN_ID && swipeeID <= SWIPEE_MAX_ID);
    } catch(NumberFormatException e){
      return false;
    }
  }

  private boolean isValidCommentCharNumber(String comment) {
    int commentCharNumber = comment.length();
    return commentCharNumber <= LEGAL_CHAR_NUMBER;
  }

  @PostMapping(value = "/{leftOrRight}/", produces = "application/json")
  public ResponseEntity<Object> swipe(@PathVariable String leftOrRight, @RequestBody SwipeDetails swipeDetails) {
    try {
      Gson gson = new Gson();
      // get the body information
      String swiperID = swipeDetails.getSwiper();
      String swipeeID = swipeDetails.getSwipee();
      String comment = swipeDetails.getComment();

      if (!isValidLeftOrRight(leftOrRight)) {
        return new ResponseEntity<>(gson.toJson(new ResponseMessage(INVALID_INPUT)), HttpStatus.BAD_REQUEST);
      } else {
        if (swipeeID.isEmpty() || swiperID.isEmpty() || comment.isEmpty()) {
          return new ResponseEntity<>(gson.toJson(new ResponseMessage(MISSING_PARA)), HttpStatus.NOT_FOUND);
        } else {
          if (isValidRangeSwiper(swiperID) && isValidRangeSwipee(swipeeID) && isValidCommentCharNumber(
              comment)) {
            return new ResponseEntity<>(gson.toJson(new SwipeDetails(swiperID, swipeeID, comment)), HttpStatus.CREATED);
          } else if (!isValidCommentCharNumber(comment)) {
            return new ResponseEntity<>(gson.toJson(new ResponseMessage(INVALID_INPUT)), HttpStatus.BAD_REQUEST);
          } else {
            return new ResponseEntity<>(gson.toJson(new ResponseMessage(USER_NOT_FOUND)), HttpStatus.NOT_FOUND);
          }
        }
      }
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
