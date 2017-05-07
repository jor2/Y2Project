package models.users;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

/**
 * Created by Jordan on 20/04/2017.
 */
public class EditAddress extends Model {
    @Constraints.Required
    private String street1;

    private String street2;

    @Constraints.Required
    private String town;

    @Constraints.Required
    private String postCode;

    public EditAddress() {
    }

    public void updateAddress(Customer c){
        c.setStreet1(this.street1);
        c.setStreet2(this.street2);
        c.setTown(this.town);
        c.setPostCode(this.postCode);
        c.update();
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}
