package models.users;

import java.util.*;
import javax.persistence.*;
import play.data.format.*;
import play.data.validation.*;
import com.avaje.ebean.*;

@Entity
// This is a User of type admin
@DiscriminatorValue("admin")

// Administrator inherits from the User class
public class Administrator extends User{

	@OneToMany(mappedBy = "administrator", cascade = CascadeType.ALL)
	private List<ContactUs> messages;

	public Administrator() {

	}

	public Administrator(String email, String role, String name, String password)
	{
		super(email, role, name, password);
	}

	public List<ContactUs> getMessages() {
		return messages;
	}

	public void setMessages(List<ContactUs> messages) {
		this.messages = messages;
	}
}