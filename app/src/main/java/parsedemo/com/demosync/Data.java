package parsedemo.com.demosync;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by krishnakumar on 18-05-2016.
 */
public class Data {

    /**
     * Item1 : 101
     * Item2 : Krishna
     * Item3 : 18/05/2016
     */

    @SerializedName("data")
    private List<SubData> data;

    public List<SubData> getData() {
        return data;
    }

    public void setData(List<SubData> Data) {
        this.data = Data;
    }

    public static class SubData {
        @SerializedName("item1")
        private String item1;
        @SerializedName("item2")
        private String item2;
        @SerializedName("item3")
        private String item3;

        public String getItem1() {
            return item1;
        }

        public void setItem1(String Item1) {
            this.item1 = Item1;
        }

        public String getItem2() {
            return item2;
        }

        public void setItem2(String Item2) {
            this.item2 = Item2;
        }

        public String getItem3() {
            return item3;
        }

        public void setItem3(String Item3) {
            this.item3 = Item3;
        }
    }
}
