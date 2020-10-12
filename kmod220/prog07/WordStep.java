package prog07;
import java.io.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

import prog02.GUI;
import prog02.UserInterface;
import prog06.ArrayQueue;

public class WordStep {
	
	static UserInterface ui = new GUI("Word Step");
	//static TestUI ui = new TestUI("Word Step");
	//static TestUI2 ui = new TestUI2("Word Step");
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
		 IndexComparator compare = new IndexComparator(parent, target);

		 Queue<Integer> solve = new PriorityQueue<Integer>(list.size(), compare);
		 //Queue<Integer> solve = new Heap<Integer>(compare);
		 
		 int x = find(start);
		 solve.offer(x);
		 String current;
		 int count = 0;
		 while(!solve.isEmpty())
		 {
			 int currentIndex = solve.poll();
			 current = list.get(currentIndex);
			 count++;
			 for(int i = 0; i < list.size(); i++)
			 {
				 if(!(start.contentEquals(list.get(i))) && (parent[i] == -1 || 
						 numSteps(parent, currentIndex) + 1 < numSteps(parent, i)) && 
						 offBy1(list.get(i), current)) 
				 {
					 if(parent[i] != -1)
					 {
						 solve.remove(i);
					 }
					 parent[i] = find(current);
					 solve.offer(i);
					 				 
					 
					 if(list.get(i).contentEquals(target))
					 {
						 ui.sendMessage("Got to " + target + " from " + current);
						 ui.sendMessage("The target is " + numSteps(parent, find(target)) + " away from start.");
						 String solution = target;
						 int j = find(target);
						
						 while(parent[j] != -1)
						{
						 int temp = parent[j];
						 solution =  list.get(temp) + "\n" + solution;
						 j = temp;
						}
						 ui.sendMessage(solution);
						 ui.sendMessage("This solve polls " + count + " times.");
					 }
				 }
			 }
		 }
	 }
	 
	 
	 static int numSteps(int[] parents, int index)
	 {
		 int j = index;
		 int count = 0;
		 while(parents[j] != -1)
		{
		 int temp = parents[j];
		 count++;
		 j = temp;
		}
		 
		 return count;
	 }
	 
	 static int numDifferent(String word1, String word2)
	 {
		 int differ = 0;
		 if(word1.length() == word2.length())
			{
				for(int i = 0; i < word1.length(); i++)
				{
					if(word1.charAt(i) != (word2.charAt(i)))
					{
						differ++;
					}
				}
			}
				return differ;
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
	 
	 
	 
	 
	public class IndexComparator implements Comparator<Integer> 
	{
		int[] parents;
		String target;
		
		public IndexComparator(int[] parents, String target)
		{
			this.parents = parents;
			this.target = target;
		}
		
		int sumNums(int index)
		{
			int sum = numDifferent(list.get(index), target) + numSteps(parents, index);
			return sum;
		}
		
		public int compare(Integer index1, Integer index2)
		{
			return sumNums(index1) - sumNums(index2);
		}

		
	}
	 
	
}
