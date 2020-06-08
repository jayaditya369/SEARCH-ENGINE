package project;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLJsoup {

    private HashSet<String> links;
    ArrayList<String> arrayL= new ArrayList<String>();
  

    public HTMLJsoup() {
        links = new HashSet<String>();
    }

    public void getPageLinks(String URL,int length) {
        //4. Check if you have already crawled the URLs 
        
        if (!links.contains(URL)) {
            try {
                //4. (i) If not add it to the index
                if (links.add(URL)) {
                    System.out.println(URL);
                    arrayL.add(URL);
                    int i = arrayL.indexOf(URL);
                    String s = Integer.toString(i);
                    String str = URL;
                    saveUrl(s,str); 
                }
                //2. Fetch the HTML code
                Document document = Jsoup.connect(URL).get();
                //3. Parse the HTML to extract links to other URLs
                Elements linksOnPage = document.select("a[href]");

                //5. For each extracted URL... go back to Step 4.
                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"),length);
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }
    
    public void saveUrl(final String filename, final String urlString)
            throws MalformedURLException, IOException {
    	{ 
            try { 
      
                // Create URL object 
                URL url = new URL(urlString); 
                BufferedReader readr =  
                  new BufferedReader(new InputStreamReader(url.openStream())); 
      
                // Enter filename in which you want to download 
                String str = filename +".html";
                BufferedWriter BufferWriter =  
                  new BufferedWriter(new FileWriter("C:\\Users\\jayad\\eclipse-workspace\\Search Engine\\src\\Webpages\\"+str)); 
                  
                // read each line from stream till end 
                String line; 
                while ((line = readr.readLine()) != null) { 
                	BufferWriter.write(line); 
                } 
      
                readr.close(); 
                BufferWriter.close(); 
                System.out.println("Successfully Downloaded."); 
            } 
      
            // Exceptions 
            catch (MalformedURLException mue) { 
                System.out.println("Malformed URL Exception raised"); 
            } 
            catch (IOException ie) { 
                System.out.println("IOException raised"); 
            } 
        } 
    }
 
    	public static void main(String[] args) {
        //1. Pick a URL from the frontier
        new HTMLJsoup().getPageLinks("https://www.google.ca/",10);
    }

}