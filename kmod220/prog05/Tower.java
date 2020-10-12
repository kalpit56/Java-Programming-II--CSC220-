package prog05;

import java.util.Stack;
import prog02.UserInterface;
import prog02.GUI;

public class Tower {
  static UserInterface ui = new GUI("Towers of Hanoi");
//static UserInterface ui = new TestUI("Towers of Hanoi");
  //static UserInterface ui = new TestUI2("Towers of Hanoi");

  static public void main (String[] args) {
    int n = getInt("How many disks?");
    if (n <= 0)
      return;
    Tower tower = new Tower(n);

    String[] commands = { "Human plays.", "Computer plays." };
    int c = ui.getCommand(commands);
    if (c == -1)
      return;
    if (c == 0)
      tower.play();
    else
      tower.solve();
  }

  /** Get an integer from the user using prompt as the request.
   *  Return 0 if user cancels.  */
  static int getInt (String prompt) {
    while (true) {
      String number = ui.getInfo(prompt);
      if (number == null)
        return 0;
      try {
        return Integer.parseInt(number);
      } catch (Exception e) {
        ui.sendMessage(number + " is not a number.  Try again.");
      }
    }
  }

  int nDisks;
  StackInt<Integer>[] pegs = (StackInt<Integer>[]) new ArrayStack[3];

  Tower (int nDisks) {
    this.nDisks = nDisks;
    for (int i = 0; i < pegs.length; i++)
      pegs[i] = new ArrayStack<Integer>();

    // EXERCISE: Initialize game with pile of nDisks disks on peg 'a' (pegs[0]).
    for(int j = nDisks; j > 0; j--)
    {
    	pegs[0].push(j);
    }

  }

  void play () {
    String[] moves = { "ab", "ac", "ba", "bc", "ca", "cb" };

    while (!(pegs[0].empty()) || !(pegs[1].empty())) {
      displayPegs();
      int imove = ui.getCommand(moves);
      if (imove == -1)
        return;
      String move = moves[imove];
      int from = move.charAt(0) - 'a';
      int to = move.charAt(1) - 'a';
      move(from, to);
    }

    ui.sendMessage("You win!");
  }

  String stackToString (StackInt<Integer> peg) {
    StackInt<Integer> helper = new ArrayStack<Integer>();
    // String to append items to.
    String s = "";

    while(!peg.empty())
    {
    	helper.push(peg.pop());
    }
    
    while(!helper.empty())
    {
    	s += " " + helper.peek();
    	peg.push(helper.pop());    	
    }
    // EXERCISE:  append the items in peg to s from bottom to top.


    return s;
  }

  void displayPegs () {
    String s = "";
    for (int i = 0; i < pegs.length; i++) {
      char abc = (char) ('a' + i);
      s = s + abc + stackToString(pegs[i]);
      if (i < pegs.length-1)
        s = s + "\n";
    }
    ui.sendMessage(s);
  }

  void move (int from, int to) {
    // EXERCISE:  move one disk form pegs[from] to pegs[to].
    // Don't allow illegal moves:  send a warning message instead.
    // For example "Cannot place disk 2 on top of disk 1."
    // Use ui.sendMessage() to send messages.

	  if(pegs[from].empty())
	  {
		  ui.sendMessage("Cannot move disk from empty stack.");
	  }
	  else if(pegs[to].empty() || pegs[to].peek() > pegs[from].peek())
	  {
		  pegs[to].push(pegs[from].pop());
	  }
	  else
	  {
		  ui.sendMessage("Cannot place disk " + pegs[from].peek() 
				  + " on top of disk " + pegs[to].peek() +".");
	  }


  }

  // EXERCISE:  create Goal class.
  public class Goal {
    // Data.
	  int num;
	  // 0 for a, 1 for b, 2 for c
	  int fromPeg; 
	  int toPeg;

    // Constructor.
	 Goal(int num, int fromPeg, int toPeg)
	  {
		  this.num = num;
		  this.fromPeg = fromPeg;
		  this.toPeg = toPeg;
	  }



	 public int from()
	 {
		 return fromPeg;
	 }
	 public int to()
	 {
		 return toPeg;
	 }
	 public int num()
	 {
		 return num;
	 }
	 
    public String toString () {
      String[] pegNames = { "a", "b", "c" };
      String s = "";
      
      if(num != 1)
      {
      s += "Move " + num + " disks" + " from peg " + pegNames[fromPeg] + 
    		  " to peg " + pegNames[toPeg];
      }
      else
      {
    	  s += "Move " + num + " disk" + " from peg " + pegNames[fromPeg] + 
        		  " to peg " + pegNames[toPeg];
      }
      
      return s;
    }
  }
  


  //EXERCISE:  display contents of a stack of goals
  void displayGoals(StackInt<Goal> goal)
  {
	  StackInt<Goal> helper = new ArrayStack<Goal>();
	  String s = "";
	  int x = 0;
	  while(!goal.empty())
	  {
		  if(x != 0)
			  s += "\n";
		 s += helper.push(goal.pop());
		 s += ".";
		 x++;
	  }
	  while(!helper.empty())
	  {
		  goal.push(helper.pop());
	  }
	  ui.sendMessage(s);
  }


  
  void solve () {
    // EXERCISE
	  StackInt<Goal> goals = new ArrayStack<Goal>();
	  Goal x = new Goal(nDisks, 0, 2);
	  goals.push(x);
	  displayPegs();
	  displayGoals(goals);
	  while(!goals.empty())
	  {
		  Goal top = goals.pop();
		  if(top.num() == 1)
		  {
			  move(top.from(), top.to());
			  displayPegs();
			  displayGoals(goals);
		  }
		  else
		  {
			  int pegx = 3 - top.from() - top.to();
			  goals.push(new Goal(top.num()-1, pegx, top.to()));
			  goals.push(new Goal(1, top.from(), top.to()));
			  goals.push(new Goal(top.num()-1, top.from(), pegx));
			  displayGoals(goals);
		  }
	  }
  }
}
