package prog06;
import java.io.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import prog02.GUI;
import prog02.UserInterface;

public class WordStep {
	
	//static UserInterface ui = new GUI("Word Step");
	//static TestUI ui = new TestUI("Word Step");
	static TestUI2 ui = new TestUI2("Word Step");
	static List<String> list = new ArrayList<String>();
	
	public static void main(String[] args) {
		
		WordStep game = new WordStep();
		File fileName = new File(ui.getInfo("Enter word file: "));
		game.loadWords(fileName);
		String start = ui.getInfo("Enter start word");
		if(start == null)
			return;
		while(find(start) == -1)
		{
			ui.sendMessage(start + " is not a word.");
			start = ui.getInfo("Try again:");
			if(start == null)
				return;
		}
		String target = ui.getInfo("Enter target word");
		if(target == null)
			return;
		while(find(target) == -1)
		{
			ui.sendMessage(target + " is not a word.");
			target = ui.getInfo("Try again:");
			if(target == null)
				return;
		}
		String[] commands = { "Human plays.", "Computer plays." };
		int c = ui.getCommand(commands);
		if (c == -1)
		      return;
		if (c == 0)
		     game.play(start, target);
		else
		     game.solve(start, target);
		
	}
	
	
	void play (String start, String target) 
	{
		String enter = start;
		while(true)
		{
			ui.sendMessage("Current Word: " + start + "\n" + "Target Word: " + target);
			enter = ui.getInfo("What is your next word?");
			if(enter == null)
			{
				return;
			}
			while(find(enter) == -1)
			{
				ui.sendMessage(enter + " is not a word.");
				enter = ui.getInfo("Try again:");
				if(enter == null)
					return;
			}
			if(!offBy1(enter, start))
			{
				ui.sendMessage("Sorry, but " + enter + " differs by more than one letter from " + start);
				continue;
			}
			else
			{
				start = enter;
			}
			if(start.contentEquals(target))
			{
				ui.sendMessage("You Win!");
				return;
			}
		}
	}
	
	boolean offBy1(String start, String target)
	{
		if(start.length() == target.length())
		{
			int differ = 0;
			for(int i = 0; i < start.length(); i++)
			{
				if(start.charAt(i) != (target.charAt(i)))
				{
					differ++;
				}
			}
			if(differ == 1)
			{
				return true;
			}
		}
		return false;
	}

	
	 void solve (String start, String target) 
	 {
		 int[] parent = new int[list.size()];
		 Arrays.fill(parent, -1);

		 ArrayQueue<Integer> solve = new ArrayQueue<Integer>();
		 int x = find(start);
		 solve.offer(x);
		 String current;
		 while(!solve.isEmpty())
		 {
			 current = list.get(solve.poll());
			 for(int i = 0; i < list.size(); i++)
			 {
				 if(!(start.contentEquals(list.get(i))) && parent[i] == -1 && offBy1(list.get(i), current)) 
				 {
					 solve.offer(i);
					 parent[i] = find(current);
					 
					 if(list.get(i).contentEquals(target))
					 {
						 ui.sendMessage("Got to " + target + " from " + current);
					 
						 String solution = target;
						 int j = find(target);
						
						 while(parent[j] != -1)
						{
						 int temp = parent[j];
						 solution =  list.get(temp) + "\n" + solution;
						 j = temp;
						}
						 ui.sendMessage(solution);
					 }
				 }
			 }
		 }
	 }
	 
	 void loadWords(File fileName)
	 {
		 try {
			 	Scanner file = new Scanner(fileName);				

				// Write each directory entry to the file.
				while(file.hasNextLine())
				{
					// Write the name.
					String next = file.nextLine();
					
					list.add(next);
				}

			} catch (Exception ex) {
				System.err.println("Save of directory failed");
				ex.printStackTrace();
				System.exit(1);
			}
	 }
	 
	 static int find(String word)
	 {
		 for(int i = 0; i < list.size(); i++)
		 {
			 if(word.contentEquals(list.get(i)))
			 {
				 return i;
			 }
		 }
		 return -1;
	 }
}
