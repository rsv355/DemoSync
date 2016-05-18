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

    @SerializedName("Data")
    private List<SubData> Data;

    public List<SubData> getData() {
        return Data;
    }

    public void setData(List<SubData> Data) {
        this.Data = Data;
    }

    public static class SubData {
        @SerializedName("Item1")
        private String Item1;
        @SerializedName("Item2")
        private String Item2;
        @SerializedName("Item3")
        private String Item3;

        public String getItem1() {
            return Item1;
        }

        public void setItem1(String Item1) {
            this.Item1 = Item1;
        }

        public String getItem2() {
            return Item2;
        }

        public void setItem2(String Item2) {
            this.Item2 = Item2;
        }

        public String getItem3() {
            return Item3;
        }

        public void setItem3(String Item3) {
            this.Item3 = Item3;
        }
    }
}
