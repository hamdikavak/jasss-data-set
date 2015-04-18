package com.hamdikavak.data.retrieval.jasss;

public class JASSSArticle {
	
	private String title;
	private ArticleType type;
	private int publicationYear;
	private int volumeNumber;
	private int issueNumber;

	private String[] authors;
	private String[] keywords;
	
	private String abstractText;
	private String contentText;
	private String URL;
	
	public String getURL() {
		return URL;
	}
	public void setURL(String url) {
		this.URL = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ArticleType getType() {
		return type;
	}
	public void setType(ArticleType type) {
		this.type = type;
	}
	public int getPublicationYear() {
		return publicationYear;
	}
	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}
	public int getVolumeNumber() {
		return volumeNumber;
	}
	public void setVolumeNumber(int volumeNumber) {
		this.volumeNumber = volumeNumber;
	}
	public int getIssueNumber() {
		return issueNumber;
	}
	public void setIssueNumber(int issueNumber) {
		this.issueNumber = issueNumber;
	}
	public String[] getAuthors() {
		return authors;
	}
	public void setAuthors(String[] authors) {
		this.authors = authors;
	}
	public String[] getKeywords() {
		return keywords;
	}
	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}
	public String getAbstractText() {
		return abstractText;
	}
	public void setAbstractText(String abstractText) {
		this.abstractText = abstractText;
	}
	public String getContentText() {
		return contentText;
	}
	public void setContentText(String contentText) {
		this.contentText = contentText;
	}
	
	
}
