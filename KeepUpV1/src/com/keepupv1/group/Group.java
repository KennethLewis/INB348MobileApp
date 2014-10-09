package com.keepupv1.group;

import java.util.ArrayList;
import java.util.List;


import com.keepupv1.post.Post;
import com.keepupv1.user.User;

public class Group {

	private String name;
	private String groupMembers;
	private String groupDescription;
	private String memberStudentId;
	private List<Post> groupPosts;
	
	public Group (String name, String groupMembers, String memberStudentId,
			String groupDescription){
		this.name = name;
		this.groupMembers = groupMembers;
		this.memberStudentId = memberStudentId;
		this.groupDescription = groupDescription;
		this.groupPosts = new ArrayList<Post>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(String groupMembers) {
		this.groupMembers = groupMembers;
	}

	public String getMemberStudentId() {
		return memberStudentId;
	}

	public void setMemberStudentId(String memberStudentId) {
		this.memberStudentId = memberStudentId;
	}

	public List<Post> getGroupPosts() {
		return groupPosts;
	}

	public void setGroupPosts(List<Post> groupPosts) {
		this.groupPosts = groupPosts;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}
	
	public List<Integer> gatherUsers(){
		
		List<Integer> studentNoAfterParse = new ArrayList<Integer>();
		String [] delimtedStudentNo = this.memberStudentId.split(",");
		
		for(String s: delimtedStudentNo){
			s = s.replace(",","");
			studentNoAfterParse.add(Integer.parseInt(s));
		}
		return studentNoAfterParse;
	}
	
	public String returnLastPost(){
		return groupPosts.get(groupPosts.size()-1).getContent();
	}
}
