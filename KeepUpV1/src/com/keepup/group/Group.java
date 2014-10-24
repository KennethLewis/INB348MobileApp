package com.keepup.group;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.keepup.post.Post;

public class Group {

	private int groupId;
	private int unitId;
	
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
	public Group (){}
	public Group (int groupId, int unitId, String groupName, String groupDescription){
		this.groupId = groupId;
		this.unitId = unitId;
		this.name = groupName;
		this.groupDescription = groupDescription;
	}
	//Constructing a User from an SQL String
		public void setupGroup(String builderString) {
			String[] segmentedStrings = new String[4];
			
			int offset = 0;
			Log.v("KEEPUP", builderString);
			Log.v("Length", String.valueOf(builderString.length()));
			segmentedStrings[0] = builderString.substring(offset, builderString.indexOf("^", offset));
			Log.v("SegmentedString 0", segmentedStrings[0]);
			offset += segmentedStrings[0].length() + 1;
			segmentedStrings[1] = builderString.substring(offset, builderString.indexOf("^", offset));
			Log.v("SegmentedString 1", segmentedStrings[1]);
			offset += segmentedStrings[1].length() + 1;
			segmentedStrings[2] = builderString.substring(offset, offset + 50);
			Log.v("SegmentedString 1", segmentedStrings[2]);
			offset += segmentedStrings[2].length();
			segmentedStrings[3] = builderString.substring(offset, offset + 100);
			Log.v("SegmentedString 1", segmentedStrings[3]);
			offset += segmentedStrings[3].length();
			
			this.groupId = Integer.parseInt(segmentedStrings[0].replace(" ", ""));
			this.unitId = Integer.parseInt(segmentedStrings[1].replace(" ", ""));
			this.name = segmentedStrings[2];
			this.groupDescription = "Group Doesnt Have a Description in db!";// segmentedStrings[3];

			
			
			//Log.v("KEEPUP", this.getName());
			//Log.v("KEEPUP", String.valueOf(this.getUserId()));
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
