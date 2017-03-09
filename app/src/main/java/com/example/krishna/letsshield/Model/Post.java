package com.example.krishna.letsshield.Model;

/**
 * Created by krishna on 7/3/17.
 */

        import com.google.firebase.database.Exclude;
        import com.google.firebase.database.IgnoreExtraProperties;

        import java.util.HashMap;
        import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Post {

    public String uid;
   // public String author;
    public String Number1;
    public String Number2;
    public String Number3;
    public String Number4;
    public String Number5;
  //  public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String Numbers[] ) {
        this.uid = uid;
       // this.author = author;
        this.Number1 = Numbers[0];
        this.Number2 = Numbers[1];
        this.Number3 = Numbers[2];
        this.Number4 = Numbers[3];
        this.Number5 = Numbers[4];
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
      //  result.put("author", author);
        result.put("Number1", Number1);
        result.put("Number2", Number2);
        result.put("Number3", Number3);
        result.put("Number4", Number4);
        result.put("Number5", Number5);
      //  result.put("starCount", starCount);
        //result.put("stars", stars);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]
