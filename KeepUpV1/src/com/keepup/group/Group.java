package com.keepup.group;

import android.util.Log;

public class Group {

	private int groupId;
	private int unitId;
	private String name;
	private String groupDescription;
	
	public Group (){}
	public Group (int groupId, int unitId, String groupName, String groupDescription){
		this.groupId = groupId;
		this.unitId = unitId;
		this.name = groupName;
		this.groupDescription = groupDescription;
	}
	
		//Constructing a User from an SQL String
		public int setupGroup(String builderString) {
			String[] segmentedStrings = new String[4];
			
			int offset = 0;
			//Log.v("Length", String.valueOf(builderString.length()));
			segmentedStrings[0] = builderString.substring(offset, builderString.indexOf("^", offset));
			//Log.v("SegmentedString 0", segmentedStrings[0]);
			offset += segmentedStrings[0].length() + 1;
			segmentedStrings[1] = builderString.substring(offset, builderString.indexOf("^", offset));
			//Log.v("SegmentedString 1", segmentedStrings[1]);
			offset += segmentedStrings[1].length() + 1;
			int numberCounter = offset;
			segmentedStrings[2] = builderString.substring(offset, offset + 50);
			//Log.v("SegmentedString 2", segmentedStrings[2]);
			offset += segmentedStrings[2].length();
			segmentedStrings[3] = builderString.substring(offset, offset + 100);
			//Log.v("SegmentedString 3", segmentedStrings[3]);
			offset += segmentedStrings[3].length();
			
			this.groupId = Integer.parseInt(segmentedStrings[0].replace(" ", ""));
			this.unitId = Integer.parseInt(segmentedStrings[1].replace(" ", ""));
			this.name = segmentedStrings[2];
			this.groupDescription = segmentedStrings[3];

			return numberCounter;
			
			//Log.v("KEEPUP", this.getName());
			//Log.v("KEEPUP", String.valueOf(this.getUserId()));
		}
		
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getGroupDescription() {
		return groupDescription;
	}
	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

}
