package com.hamdikavak.data.retrieval.jasss.test;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hamdikavak.data.retrieval.jasss.ArticleType;
import com.hamdikavak.data.retrieval.jasss.IssuePage;
import com.hamdikavak.data.retrieval.jasss.JASSSArticle;
import com.hamdikavak.data.retrieval.jasss.JASSSDataExtracter;

/**
 * 
 * @author Hamdi
 *
 */
public class ArticleContentTester {
	private static final Logger logger = LogManager
			.getLogger(ArticleContentTester.class);
	
	public static void main(String[] args) {
		(new ArticleContentTester()).runTest();
	}

	
	public void runTest(){
		logger.info("Test is starting...");
		JASSSDataExtracter jasssDataExtracter = new JASSSDataExtracter();
		ArrayList<String> issueURLs = jasssDataExtracter.extractIssueURLs();
		ArrayList<IssuePage> issuePages = new ArrayList<IssuePage>();
		IssuePage tempIssue;

		issueURLs = jasssDataExtracter.extractIssueURLs();

		for (String issueURL : issueURLs) {
			logger.info("issue url: " + issueURL);
			tempIssue = jasssDataExtracter.extractIssuePageContent(issueURL);
			issuePages.add(tempIssue);
		}

		for (IssuePage issue : issuePages) {
			logger.info("Volume:" + issue.getVolumeId() + " - Issue:" + issue.getIssueId());
			
			logger.info("REFEREED");
			logger.info("---------------------------------------");

			for (String articleURL : issue.getRefereedArticleURLs()) {
				JASSSArticle myArticle = jasssDataExtracter.extractArticle(
						articleURL, issue, ArticleType.PeerReviewed);
				testRefereedArticle(myArticle);
			}
			
			logger.info("FORUM");
			logger.info("---------------------------------------");
			for (String articleURL : issue.getForumArticleURLs()) {
				
				JASSSArticle myArticle = jasssDataExtracter.extractArticle(
						articleURL, issue, ArticleType.Forum);
				testForumArticle(myArticle);
			}
			

			logger.info("BOOK REVIEW");
			logger.info("---------------------------------------");
			for (String articleURL : issue.getReviewArticleURLs()) {
				JASSSArticle myArticle = jasssDataExtracter.extractArticle(
						articleURL, issue, ArticleType.Review);
				testReviewArticle(myArticle);
			}
		}
		
	}
	
	private void testRefereedArticle(JASSSArticle article){
		logger.info("Refereed Article URL: " + article.getURL());
		
		if(article.getAbstractText()== null || article.getAbstractText().length() < 5) {
			logger.info("Issue with abstract text: " + article.getAbstractText() );
		}
		if(article.getAuthors() == null || article.getAuthors().length == 0) {
			logger.info("Issue with authors: " + article.getAuthors() );
		}
		if(article.getContentText() == null || article.getContentText().length() < 300) {
			logger.info("Issue with text: " + article.getContentText() );
		}
		if(article.getIssuePage() == null || article.getIssuePage().getIssueId() == 0 ||  article.getIssuePage().getVolumeId() == 0 ) {
			logger.info("Issue with issue-volume id: " + article.getIssuePage() );
		}
		if(article.getKeywords() == null || article.getKeywords().length == 0) {
			logger.info("Issue with keywords: " + article.getKeywords() );
		}
		if(article.getPublicationYear() == 0 || article.getPublicationYear() < 1998) {
			logger.info("Issue with publication year: " + article.getPublicationYear() );
		}
		if(article.getTitle() == null || article.getTitle().length() < 5  ) {
			logger.info("Issue with title: " + article.getTitle() );
		}
	}
	private void testForumArticle(JASSSArticle article){
		logger.info("Forum Article URL: " + article.getURL());
		
		if(article.getAbstractText()== null || article.getAbstractText().length() < 5) {
			logger.info("Issue with abstract text: " + article.getAbstractText() );
		}
		if(article.getAuthors() == null || article.getAuthors().length == 0) {
			logger.info("Issue with authors: " + article.getAuthors() );
		}
		if(article.getContentText() == null || article.getContentText().length() < 300) {
			logger.info("Issue with text: " + article.getContentText() );
		}
		if(article.getIssuePage() == null || article.getIssuePage().getIssueId() == 0 ||  article.getIssuePage().getVolumeId() == 0 ) {
			logger.info("Issue with issue-volume id: " + article.getIssuePage() );
		}
		if(article.getKeywords() == null || article.getKeywords().length == 0) {
			logger.info("Issue with keywords: " + article.getKeywords() );
		}
		if(article.getPublicationYear() == 0 || article.getPublicationYear() < 1998) {
			logger.info("Issue with publication year: " + article.getPublicationYear() );
		}
		if(article.getTitle() == null || article.getTitle().length() < 5  ) {
			logger.info("Issue with title: " + article.getTitle() );
		}
	}
	private void testReviewArticle(JASSSArticle article){
		logger.info("Review Article URL: " + article.getURL());
	}
}
