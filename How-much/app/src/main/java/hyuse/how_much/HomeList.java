package hyuse.how_much;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by VVl-lYSl3eaR on 2015-05-11.
 */
public class HomeList {
    private class HomeData {
        private String name;
        private String grade;
        private String data1;

        HomeData(String a,String b, String c){
            name = a;
            grade = b;
            data1 = c;
        }

        public String getName() {
            return name;
        }

        public String getGrade() {
            return grade;
        }

        public String getData1() {
            return data1;
        }
    }

    ArrayList<HomeData> hdatas;

    HomeList(){
        hdatas = null;
    }
    HomeList(String test){
        addList("test1");
        addList("test2");
        addList("test3");
        addList("test4");
    }
    void getdatas(){
        //내부 DB목록을 읽어서 서버로 요청한 다음 addList를 해줍니다.
    }
    void addList(String data){
        hdatas.add(new HomeData(data,data,data));
    }

    String[] readElem(int i){
        HomeData d = hdatas.get(i);
        String d1 = d.getData1();
        String d2 = d.getGrade();
        String d3 = d.getName();
        String[] x = {d1,d2,d3};
        return x;
    }


}
