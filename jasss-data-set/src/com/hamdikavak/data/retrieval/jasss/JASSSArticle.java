package com.hamdikavak.data.retrieval.jasss;

public class JASSSArticle {
	
	private String title;
	private ArticleType type;
	private Integer publicationYear;
	private IssuePage issuePage;
	private String URL;
	
	private String[] authors;
	private String[] keywords;
	
	private String abstractText;
	private String contentText;
	
	
	public IssuePage getIssuePage() {
		return issuePage;
	}
	public void setIssuePage(IssuePage issuePage) {
		this.issuePage = issuePage;
	}
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
	public Integer getPublicationYear() {
		return publicationYear;
	}
	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
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
