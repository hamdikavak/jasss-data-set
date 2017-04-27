# JASSS Data Set
This tool downloads and organizes articles published in the open journal called "Journal of Artificial Societies and Social Simulation" ([JASSS](jasss.soc.surrey.ac.uk))

Usage:

```
java -cp jasss-dataset-tool.jar com.hamdikavak.data.retrieval.jasss.JASSSDatasetTool /path/to/the/output/folder
```

The tool generates 962 files (one per article) in XML format. The articles are in one of these categories: peer reviewed, forum, and book review. Based on a quick search, it is found that eight articles in the dataset did not have a content. That is due to inconsistent formatting seen in some JASSS issues. I manually added the content for those articles. I will not  provide the dataset here unless I get approval from JASSS editors. You can email me if you want to talk about it.

## Disclaimer
This software is developed for educational purposes and I am not affiliated with JASSS. I got permission from JASSS for crawling articles. It's user's own responsibility to get permission from the publisher.
