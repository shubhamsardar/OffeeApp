package in.co.tripin.offeeuser.Model;

/**
 * Created by Tripin1 on 7/6/2017.
 */

public class PostPaidPojo {
    private String name;
    private double amount;

    public PostPaidPojo(){
        //for firebase
    }

    public PostPaidPojo(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
