package com.hamdikavak.data.retrieval.jasss;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class JASSSDataRetriever {
	private String jasssIndex = "http://jasss.soc.surrey.ac.uk/index_by_issue.html";
	
	public static void main(String[] args) {
		JASSSDataRetriever jasss = new JASSSDataRetriever();
		ArrayList<IssuePage> issuePages = new ArrayList<IssuePage>();
		ArrayList<String> issueURLs = jasss.extractIssueURLs();
		IssuePage tempIssue;
		
		for(String issueURL: issueURLs){
			System.out.println("Issue URL:" + issueURL);
			tempIssue = jasss.extractIssuePageContent(issueURL);
			issuePages.add(tempIssue);
		}
		
		for(IssuePage issue: issuePages){
			System.out.println("Volume:" + issue.getVolumeId() + " - Issue:" + issue.getIssueId());
			System.out.println("REFEREED");
			System.out.println("---------------------------------------");
			for(String articleURL: issue.getRefereedArticleURLs()){
				System.out.println( articleURL);
			}
			System.out.println("FORUM");
			System.out.println("---------------------------------------");
			for(String articleURL: issue.getForumArticleURLs()){
				System.out.println(articleURL);
			}
			
			System.out.println("REVIEW");
			System.out.println("---------------------------------------");
			for(String articleURL: issue.getReviewArticleURLs()){
				System.out.println(articleURL);
			}
		}
		
	}
	
	
	/**
	 * This method extracts issue URLs from index page
	 * @return List of
	 */
	public ArrayList<String> extractIssueURLs(){
		ArrayList<String> issueURLs = new ArrayList<String>();
		
		try {
			Document doc = Jsoup.connect(jasssIndex).get();
			for(Element el: doc.getElementsByAttributeValueEnding("href", "contents.html") ){	
				issueURLs.add(el.attr("href"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return issueURLs;
	}
	
	public IssuePage extractIssuePageContent(String url){
		IssuePage issuePage = new IssuePage();
		ArrayList<String> refereedURLs = new ArrayList<String>();
		ArrayList<String> forumURLs = new ArrayList<String>();
		ArrayList<String> reviewURLs = new ArrayList<String>();
		
		try {
			Document doc = Jsoup.connect(url).get();
			
			int mode = 0; // 0: nothing, 1: refereed article, 2: forum, 3: book review 
			for(Element el: doc.getAllElements()){
				
				if(el.tagName().toLowerCase().equals("h1") && el.text().toLowerCase().contains("volume")){ 
					// h1 tag shows volume and issue number 
					String volumeIssueString = el.text().trim();
					
					String[] volumeIssue = volumeIssueString.split(", ");
					issuePage.setVolumeId(volumeIssue[0].toLowerCase().replace("volume", "").trim());
					issuePage.setIssueId(volumeIssue[1].toLowerCase().replace("issue", "").trim());
				}
				
				else if(el.tagName().toLowerCase().equals("h2") || el.tagName().toLowerCase().equals("h3")){
					if(el.text().toLowerCase().contains("articles") == true){
						mode = 1;
					}
					else if(el.text().toLowerCase().equals("forum") == true){
						mode = 2;
					}
					else if(el.text().toLowerCase().contains("review") == true){
						mode = 3;
					}
				}
				else if (el.tagName().toLowerCase().equals("b") && el.text().toLowerCase().startsWith ("important note about") ){
					mode = 1;
				}
				else if( el.tagName() == "a" && el.attr("href").trim().startsWith("../") == false ){
					if(mode == 1){
						refereedURLs.add(el.attr("abs:href"));
					}
					else if(mode == 2){
						forumURLs.add(el.attr("abs:href"));
					}
					else if(mode == 3){
						reviewURLs.add(el.attr("abs:href"));
					}
				}
			}
			issuePage.setRefereedArticleURLs(refereedURLs);
			issuePage.setReviewArticleURLs(reviewURLs);
			issuePage.setForumArticleURLs(forumURLs);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return issuePage;
	}
}
