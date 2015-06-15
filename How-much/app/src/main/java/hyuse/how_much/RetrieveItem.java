package hyuse.how_much;

/**
 * Created by hwang-gyojun on 2015. 5. 10..
 */
public class RetrieveItem {
    private String name;
    private String sub_id;
    /* Kyojun Hwang  code */
    public RetrieveItem(String name, String sub_id){
        this.name = name;
        this.sub_id = sub_id;
    }

    public String getName(){return this.name;}
    public String getSubID(){return this.sub_id;}
    /* Kyojun Hwang  code end */
}