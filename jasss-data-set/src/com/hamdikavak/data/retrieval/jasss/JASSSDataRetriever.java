package com.hamdikavak.data.retrieval.jasss;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.thoughtworks.xstream.XStream;

public class JASSSDataRetriever {
	private String jasssIndex = "http://jasss.soc.surrey.ac.uk/index_by_issue.html";
	
	public static void main(String[] args) {
		String path = args[0];
		JASSSDataRetriever jasss = new JASSSDataRetriever();
		jasss.retrieveAndExport(path);
		
		//jasss.exportAnArticle(path, "http://jasss.soc.surrey.ac.uk/1/1/1.html");
	}
	/*
	public void exportAnArticle(String path, String url){
		JASSSArticle myArticle = this.extractArticle(url, 1, 1, ArticleType.Refereed);
		exportArticle (myArticle, path, 1);
	}*/
	public void retrieveAndExport(String path) {
		ArrayList<IssuePage> issuePages = new ArrayList<IssuePage>();
		ArrayList<String> issueURLs = this.extractIssueURLs();
		IssuePage tempIssue;
		
		int counter;
		
		for(String issueURL: issueURLs){
			System.out.println("Issue URL:" + issueURL);
			tempIssue = this.extractIssuePageContent(issueURL);
			issuePages.add(tempIssue);
		}
		
		
		
		for(IssuePage issue: issuePages){
			System.out.println("Volume:" + issue.getVolumeId() + " - Issue:" + issue.getIssueId());
			System.out.println("REFEREED");
			System.out.println("---------------------------------------");
			counter = 0;
			
			for(String articleURL: issue.getRefereedArticleURLs()){
				System.out.println( articleURL);
				counter++;
				JASSSArticle myArticle = this.extractArticle(articleURL, issue.getVolumeId(), issue.getIssueId(), ArticleType.Refereed);
				exportArticle (myArticle, path, counter);
			}
			System.out.println("FORUM");
			System.out.println("---------------------------------------");
			for(String articleURL: issue.getForumArticleURLs()){
				System.out.println(articleURL);
				counter++;
				JASSSArticle myArticle = this.extractArticle(articleURL, issue.getVolumeId(), issue.getIssueId(), ArticleType.Forum);
				exportArticle (myArticle, path, counter);
			}
			
			System.out.println("REVIEW");
			System.out.println("---------------------------------------");
			for(String articleURL: issue.getReviewArticleURLs()){
				System.out.println(articleURL);
				counter++;
				JASSSArticle myArticle = this.extractArticle(articleURL, issue.getVolumeId(), issue.getIssueId(), ArticleType.Review);
				exportArticle (myArticle, path, counter);
			}
		}
	}

	private static void exportArticle(JASSSArticle myArticle, String path, int counter) {
		XStream xstream = new XStream();
		xstream.alias("article", JASSSArticle.class);
		
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(path+ myArticle.getVolumeNumber() + "-" + myArticle.getIssueNumber() 
					+ "-" + myArticle.getType().toString().toLowerCase() + "-" + counter + ".xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OutputStreamWriter writer = new OutputStreamWriter(outputStream, Charset.forName("UTF-8"));
		xstream.toXML(myArticle, writer);
	}

	public JASSSArticle extractArticle(String url, int volumeNumber, int issueNumber, ArticleType type){
		JASSSArticle jasssArticle = new JASSSArticle();
		
		jasssArticle.setVolumeNumber(volumeNumber);
		jasssArticle.setIssueNumber(issueNumber);
		jasssArticle.setType(type);
		
		try {
			Document doc = Jsoup.connect(url).get();
			jasssArticle.setURL(url);
			jasssArticle.setTitle(doc.select("[name=DC.Title]").attr("content"));
			String desc = doc.select("[name=DC.Description]").attr("content");
			
			if(desc.equals("301")){
				// This is a special case in the first issue. Here description does not contain the abstract.
				
				Elements els = doc.getAllElements();
				int i = 0;
				
				for(i=0; i< els.size(); i++){
					if(els.get(i).text().toLowerCase().equals("abstract")){
						
						break;
					}
				}
				i=i+2;
				
				jasssArticle.setAbstractText(els.get(i).text());
			}
			else{
				jasssArticle.setAbstractText(desc.trim());
			}
			
			if(type != ArticleType.Review){ // reviews do not have keywords 
				String keywordsString = doc.select("[name=DC.Subject]").attr("content").trim();
				String[] keywords = separateKeywords(keywordsString);
				jasssArticle.setKeywords(keywords);
			}
			
			String authorsString = doc.select("[name=DC.Creator]").attr("content");
			String[] authors = separateAuthors(authorsString);
			jasssArticle.setAuthors(authors);
			
			String dateString = doc.select("[name=DC.Date]").attr("content");
			int year = extractYear(dateString);
			jasssArticle.setPublicationYear(year);
			
			String articleText = extractArticleText(doc,  type);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jasssArticle;
	}
	
	
	private String extractArticleText(Document doc, ArticleType type) {
		
		
		
		return null;
	}

	private int extractYear(String dateString) {
		// There are two date formats in articles
		// 1) d-mmm-yy(earlier format)
		// 2) yyyy-MM-dd(current format)
		DateTimeFormatter oldFormatter = new DateTimeFormatterBuilder()
		        .appendDayOfMonth(2)
		        .appendLiteral('-')
		        .appendMonthOfYearShortText()
		        .appendLiteral('-')
		        .appendTwoDigitYear(2000)
		        .toFormatter();
		DateTimeFormatter currentFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		LocalDate date;
		
		try{
			date = LocalDate.parse(dateString, currentFormatter);
		}
		catch(Exception ex){
			// not the old format
			try{
				date = LocalDate.parse(dateString, oldFormatter);
			}
			catch(Exception ex2){
				return 0;
			}
		}
		return date.getYear();
	}

	private String[] separateKeywords(String keywordsString) {
		String[] allkeywords = null;
		// we may have different keyword splitters such as ; or ,
		if(keywordsString.contains(";")){
			allkeywords = keywordsString.split("; ");
		}
		else if(keywordsString.contains(",")){
			allkeywords = keywordsString.split(", ");
		}
		else{
			// if we are here, it means the article has only 1 keyword
			allkeywords = new String[1];
			allkeywords[0] = keywordsString;
		}
		
		for(int i=0; i<allkeywords.length; i++){
			allkeywords[i] = allkeywords[i].trim();
		}
		
		return allkeywords;
	}

	private String[] separateAuthors(String authorsString) {
		String[] authorsSplitted = authorsString.split(" and ");
		
		// check if there is only one author
		
		if(authorsSplitted.length == 1){
			authorsSplitted[0] = authorsSplitted[0].trim();
			return authorsSplitted;
		}
		
		String[] firstAuthors = authorsSplitted[0].split(", ");
		String[] allAuthors = new String[firstAuthors.length+1];
		int i;
		for(i=0; i<firstAuthors.length; i++){
			allAuthors[i] = firstAuthors[i].trim();
		}
		allAuthors[i] = authorsSplitted[1].trim();
		
		return allAuthors;
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
					String vol = volumeIssue[0].toLowerCase().replace("volume", "").trim();
					String iss = volumeIssue[1].toLowerCase().replace("issue", "").trim();
					
					issuePage.setVolumeId(Integer.parseInt(vol));
					
					if(iss.startsWith("s")){ 
						// this is an exceptional case in volume 2, where issues 3 and 4 are merged in a single content page  
						// the above issue extraction method finds "s 3 and 4" as the issue Id. We use issue id as 3 for both issues
						issuePage.setIssueId(3);
					}
					else{
						issuePage.setIssueId(Integer.parseInt(iss));
					}
					
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
				else if( el.tagName() == "a" && el.attr("href").trim().startsWith("../") == false && el.attr("href").trim().startsWith("/") == false){
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
