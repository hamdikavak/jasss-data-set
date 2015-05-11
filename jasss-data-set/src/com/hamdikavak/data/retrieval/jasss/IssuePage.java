package com.hamdikavak.data.retrieval.jasss;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class IssuePage {

	private int volumeId;
	private int issueId;
	@XStreamOmitField
	private ArrayList<String> refereedArticleURLs;
	@XStreamOmitField
	private ArrayList<String> forumArticleURLs;
	@XStreamOmitField
	private ArrayList<String> reviewArticleURLs;
	
	public int getVolumeId() {
		return volumeId;
	}
	public void setVolumeId(int volumeId) {
		this.volumeId = volumeId;
	}
	public int getIssueId() {
		return issueId;
	}
	public void setIssueId(int issueid) {
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
