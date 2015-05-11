package com.hamdikavak.data.retrieval.jasss.test;

public class IssuePageTest {
	private int volumeId;
	private int issueId;
	private int numOfRefereed;
	private int numOfForum;
	private int numOfReview;
	
	public IssuePageTest(){
		
	}
	public IssuePageTest(int volId, int issueId, int refereed, int forum, int review){
		setVolumeId(volId);
		setIssueId(issueId);
		setNumOfRefereed(refereed);
		setNumOfForum(forum);
		setNumOfReview(review);
	}
	
	public int getVolumeId() {
		return volumeId;
	}
	public void setVolumeId(int volumeId) {
		this.volumeId = volumeId;
	}
	public int getIssueId() {
		return issueId;
	}
	public void setIssueId(int issueId) {
		this.issueId = issueId;
	}
	public int getNumOfRefereed() {
		return numOfRefereed;
	}
	public void setNumOfRefereed(int numOfRefereed) {
		this.numOfRefereed = numOfRefereed;
	}
	public int getNumOfForum() {
		return numOfForum;
	}
	public void setNumOfForum(int numOfForum) {
		this.numOfForum = numOfForum;
	}
	public int getNumOfReview() {
		return numOfReview;
	}
	public void setNumOfReview(int numOfReview) {
		this.numOfReview = numOfReview;
	}
}
