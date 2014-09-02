package post;

import java.sql.Date;

import com.example.keepupv1.user.User;

public class Post {

	private User user;
	private String student; //for testing incase no user present
	private Date date;
	private String testDate;
	private String post;
	
	public Post(){
		
	}
	
	public Post (String student, String testDate, String post){
		this.student = student;
		this.testDate = testDate;
		this.post = post;
	}
	
	public String getStudent() {
		return student;
	}

	public void setStudent(String student) {
		this.student = student;
	}

	public String getTestDate() {
		return testDate;
	}

	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}

	public Post (User user, Date date, String post){
		this.user = user;
		this.date = date;
		this.post = post;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}
	
	
}
