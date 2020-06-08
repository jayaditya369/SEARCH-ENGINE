package project;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLtoText {
	// Question 3 using Jsoup
		public static void main(String[] args) throws IOException {
			File htmlFilesStore = new File ("C:\\Users\\jayad\\eclipse-workspace\\Search Engine\\src\\Webpages\\");
			File[] htmlFiles = new File[100];
			htmlFiles = htmlFilesStore.listFiles();
			BufferedWriter bfdw;
			int a=htmlFiles.length;
			
			for(int j = 0 ; j<a; j++) {
				//method that is to be called 100 times			
				if(!htmlFiles[j].isDirectory()) {
					Document txt = Jsoup.parse(htmlFiles[j],"UTF-8");
					bfdw = new BufferedWriter(new FileWriter(htmlFilesStore.getPath()+"\\Text\\"+htmlFiles[j].getName()+".txt"));
					bfdw.write(txt.text());
					bfdw.close();
					System.out.println(htmlFiles[j].getName()+" Converted" );
				}
			}
			
		}
	}
