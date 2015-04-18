package com.hamdikavak.data.retrieval.jasss;

import java.util.ArrayList;

public class IssuePage {

	private String volumeId;
	private String issueId;
	private ArrayList<String> refereedArticleURLs;
	private ArrayList<String> forumArticleURLs;
	private ArrayList<String> reviewArticleURLs;
	
	public String getVolumeId() {
		return volumeId;
	}
	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}
	public String getIssueId() {
		return issueId;
	}
	public void setIssueId(String issueid) {
		this.issueId = issueid;
	}
	public ArrayList<String> getRefereedArticleURLs() {
		return refereedArticleURLs;
	}
	public void setRefereedArticleURLs(ArrayList<String> refereedArticleURLs) {
		this.refereedArticleURLs = refereedArticleURLs;
	}
	public ArrayList<String> getForumArticleURLs() {
		return forumArticleURLs;
	}
	public void setForumArticleURLs(ArrayList<String> forumArticleURLs) {
		this.forumArticleURLs = forumArticleURLs;
	}
	public ArrayList<String> getReviewArticleURLs() {
		return reviewArticleURLs;
	}
	public void setReviewArticleURLs(ArrayList<String> reviewArticleURLs) {
		this.reviewArticleURLs = reviewArticleURLs;
	}
	
	
}
