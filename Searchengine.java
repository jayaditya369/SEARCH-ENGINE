package project;

import java.io.BufferedReader;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Searchengine 
{


	static ArrayList<String> aList = new ArrayList<String>();
	static Hashtable<String, Integer> hashTable = new Hashtable<String, Integer>();
	

	// get occurrence and position of word
	public int searchWord(File filePath, String s1) throws IOException, NullPointerException
	{
		int counter = 0;
		String data = "";
		BufferedReader bufferReader = new BufferedReader(new FileReader(filePath));
		String line = null;
		while ((line = bufferReader.readLine()) != null)
		{
			data = data + line;
		}
		bufferReader.close();
		String txt = data;
		BoyerMoore bm = new BoyerMoore(s1);
		int offset = 0;
		for (int loc = 0; loc <= txt.length(); loc += offset + s1.length())
		{
			  offset = bm.search(s1, txt.substring(loc));
			  if ((offset + loc) < txt.length())
			  { 
				  counter++; 
				  System.out.printf("The word '"+s1 +"' is present at the position " + (offset + loc)+"\n"); //printing position of word 
			  } 
		}
		if(counter!=0)
		{ 
			  System.out.println("   In the FILE: "+filePath.getName()+"\n");
		}
		return counter; 
	}                                                          // End of function searchWord()

	// Ranking of Web Pages using merge sort
	// Collections.sort by default uses merge sort
	public void rank(Hashtable<?, Integer> fname, int occur)
	{
		// Transfer as List and sort it
		ArrayList<Map.Entry<?, Integer>> list = new ArrayList<Entry<?, Integer>>(fname.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<?, Integer>>()
		{
			public int compare(Map.Entry<?, Integer> obj1, Map.Entry<?, Integer> obj2)
			{
				return obj1.getValue().compareTo(obj2.getValue());
			}
		});
		Collections.reverse(list);
		
		if (occur != 0)
		{
			System.out.println("\nTop 5 Search Results\n");
			int num = 5;
			int j = 1;
			while (list.size() > j && num > 0)
			{
				System.out.println("(" + j + ") " + list.get(j) + " times ");
				j++;
				num--;
			}
		}
	}                                                        //End of function rankFiles()

	/* using regex to find similar string to pattern */
	@SuppressWarnings("rawtypes")
	public void suggestword(String pattern) throws Exception
	{
			// String to be scanned to find the pattern.
			String line = " ";
			String reg = "[\\w]+[@$%^&*()!?{}\b\n\t]*";

			// Create a Pattern object
			Pattern p = Pattern.compile(reg);
			
			// Now create matcher object.
			Matcher m = p.matcher(line);
			
			int fileNum = 0;
			
				File directory = new File("C:\\Users\\jayad\\eclipse-workspace\\Search Engine\\src\\Webpages\\Text\\");
				File[] fileArray = directory.listFiles();
				for (int i = 0; i < fileArray.length; i++)
				{
					find(fileArray[i], fileNum, m, pattern);                  // Calls the function find()
					fileNum++;
				}

				
				@SuppressWarnings({ "unused" })
				Set keys = new HashSet();
				Integer value1 = 1;
				Integer value2 = 0;
				int counter = 0;

				System.out.println("\nDid you mean?:");
				for (Map.Entry entry : hashTable.entrySet())
				{
					if (value2 == entry.getValue())
					{
						break;
					}
					else
					{
						if (value1 == entry.getValue())
						{
							if (counter == 0)
							{
								System.out.print(entry.getKey());
								counter++;
							}
							else
							{
								System.out.print(" , " + entry.getKey());
								counter++;
							}

						} // End of if

					}   // End of else
				}       // End of for loop
	}                                                // End of function suggestions()

	
	// finds strings with similar pattern and calls edit distance() on those strings
	public void find(File sourceFile, int fileNumber, Matcher match, String str) 
			throws FileNotFoundException, ArrayIndexOutOfBoundsException, IOException
	{
		
			BufferedReader br = new BufferedReader(new FileReader(sourceFile));
			String line = null;
			while ((line = br.readLine())!= null)
			{
				match.reset(line);
				while (match.find())
				{
					aList.add(match.group());
				}
			}
			br.close();
			for (int p = 0; p < aList.size(); p++)
			{
				hashTable.put(aList.get(p), editDistance(str.toLowerCase(), aList.get(p).toLowerCase()));
			}

	}                                    // End of function findWord()

	
	// Uses Edit distance to compare nearest distance between keyword and similar
	// patterns obtained from regex
	public static int editDistance(String str1, String str2)
	{
		int length1 = str1.length();
		int length2 = str2.length();

		int[][] arr = new int[length1 + 1][length2 + 1];

		for (int i = 0; i <= length1; i++)
		{
			arr[i][0] = i;
		}
		for (int j = 0; j <= length2; j++)
		{
			arr[0][j] = j;
		}

		// iterate though, and check last char
		for (int i = 0; i < length1; i++)
		{
			char c1 = str1.charAt(i);
			for (int j = 0; j < length2; j++)
			{
				char c2 = str2.charAt(j);

				if (c1 == c2)
				{
					arr[i + 1][j + 1] = arr[i][j];
				}
				else
				{
					int replace = arr[i][j] + 1;
					int insert = arr[i][j + 1] + 1;
					int delete = arr[i + 1][j] + 1;

					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					arr[i + 1][j + 1] = min;
				}
			}
		}

		return arr[length1][length2];
	}                                                  // End of function editDistance()

	public void search(String word)
	{
		Hashtable<String, Integer> hashtable = new Hashtable<String, Integer>();
		File dir = new File("C:\\Users\\jayad\\eclipse-workspace\\Search Engine\\src\\Webpages\\Text\\");
		File[] fileArray = dir.listFiles();
		int repeats = 0;    // No. of times the searched word repeated in a same file
		int numofFiles = 0; // No. of files that contains the Searched word
		try
		{
			double startTime = System.nanoTime();
			double startTimesearch = System.nanoTime();
			for (int i = 1; i < fileArray.length; i++)
			{
				repeats = searchWord(fileArray[i], word);
				hashtable.put(fileArray[i].getName(), repeats);
				if (repeats != 0)
				{
					numofFiles++;
					if (fileArray[i].getName().toString().substring(0, 3) == "null")
					{
						fileArray[i].getName().toString();
					}
				}
			}
			System.out.println("\nNumber of files containing the word '" + word + "' is = " + numofFiles);
			long endTimesearch = System.nanoTime();
			double srchtime = endTimesearch - startTimesearch;
			System.out.println("\nSeacrh Result execution time: " + srchtime/1000000 + " Milli Seconds");
			if (numofFiles == 0)
			{
				System.out.println("\nSearching closely related words");
				suggestword(word);
			}
//			System.out.println("\nFor Synonyms of the word "+ p+" enter yes or no");
//			String o =s1.nextLine();
//			if(o.equals("yes")) {
//				
//		        System.out.println("\nSearching synonyms");
//		        
//				websearch.suggestions(word);	
//				
//			}
				double rankstart = System.nanoTime();
					rank(hashtable, numofFiles);
				double rankend = System.nanoTime();
				double rankingTime = rankend - rankstart;
			System.out.println("\nRanking Algorithm time: " + rankingTime + " nano Seconds");
			
			double endTime = System.nanoTime();
			System.out.println("\nTotal Execution Time: " + (endTime - startTime)/1000000 + " Milli Seconds");
		}
		catch (Exception e)
		{
			System.out.println("Exception:" + e);
		}
	}                                                 // End of function search()

	public static void main(String args[])
	{
		int a;
		String word;
		Scanner input = new Scanner(System.in);
		Searchengine websearch = new Searchengine();
		while(true)
		{
			System.out.printf("1. Start-Search 2.Exit: ");
			a = input.nextInt();
			input.nextLine();
			switch(a)
			{
				case 1:
				{
					System.out.printf("Enter the Word to Search : ");
					word = input.nextLine();
					websearch.search(word);
					break;
				}
				case 2:
				{
					input.close();
					System.exit(0);
				}
			}         // End of Switch case
		}             // End of while loop
	}                                                 // End of main method
}

