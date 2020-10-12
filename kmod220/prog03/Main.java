package prog03;
import prog02.UserInterface;
import prog02.GUI;

/**
 *
 * @author vjm
 */
public class Main {
  /** Use this variable to store the result of each call to fib. */
  public static double fibn;

  /** Determine the average time in microseconds it takes to calculate
      the n'th Fibonacci number.
      @param fib an object that implements the Fib interface
      @param n the index of the Fibonacci number to calculate
      @param ncalls the number of calls to average over
      @return the average time per call
  */
  public static double averageTime (Fib fib, int n, int ncalls) {
	  // Get the current time in nanoseconds.
	    long start = System.nanoTime();
	    
	    for(int i = 0; i <= ncalls; i++)
	    {
	    // Call fib(n) ncalls times (needs a loop!).
	      fibn = fib.fib(n);
	    }
	    // Get the current time in nanoseconds.
	    long end = System.nanoTime();

	    // Return the average time converted to microseconds averaged over ncalls.
	    return (end - start) / 1000.0 / ncalls;
  }

  /** Determine the time in microseconds it takes to to calculate the
      n'th Fibonacci number.  Average over enough calls for a total
      time of at least one second.
      @param fib an object that implements the Fib interface
      @param n the index of the Fibonacci number to calculate
      @return the time it it takes to compute the n'th Fibonacci number
  */
  public static double accurateTime (Fib fib, int n) {
	// Get the time in microseconds using the time method above.
	double t = averageTime(fib, n, 1);

	// If the time is (equivalent to) more than a second, return it.
	if(t >= 1000000)
	{
	    return t;
	}

	// Estimate the number of calls that would add up to one second.
	// Use   (int)(YOUR EXPESSION)   so you can save it into an int variable.
	int numcalls = (int)(1000000/t);


	// Get the average time using averageTime above and that many
	// calls and return it.
	return averageTime(fib, n, numcalls);
  }

  //private static UserInterface ui = new GUI("Fibonacci experiments");
  private static UserInterface ui = new TestUI("Fibonacci experiments");

  public static void doExperiments (Fib fib) {
	  //System.out.println("doExperiments " + fib);
    // EXERCISES 8 and 9
	  double constant = 0;
	  double curRunTime = 0;
	  while(true)
	  {
		 String input = ui.getInfo("Please enter an integer: ");
		 if(input == null)
		 {
			 break;
		 }
		 if(input.length() == 0 || input.contains(".") || input.contains("-") || !(input.matches("[0-9]+")))
		 {
			 ui.sendMessage("Enter a positive integer.");
			 continue;
		 }
		 int n = Integer.parseInt(input);
		 double accTime = 0;
		 if(constant != 0)
		  {
			  curRunTime = constant * fib.O(n);
			  constant = accTime/fib.O(n);
			  double perError;
			  //ui.sendMessage("Current Running Time is " + curRunTime);
			  ui.sendMessage("Estimated time on input " + n + " is " + curRunTime + " microseconds.");
			  if(curRunTime >= (3.6*Math.pow(10, 9)))
			  {
				  ui.sendMessage("Estimated time is longer than an hour. Do you want to continue?");
				  String[] cont = {"yes", "no"};
				  int contin = ui.getCommand(cont);
				  if(contin == 0)
				  {
					 accTime = accurateTime(fib, n);
					 perError = ((curRunTime - accTime)/accTime)*100;
					//ui.sendMessage("n: " + n + " fib(n) " + fibn + " time " + accTime + " " + perError + " % error.");
					  ui.sendMessage("f(" + n + ") = " + fibn + " in " + accTime + " microseconds. " + perError + "% error.");
				  }
				  else if(contin == 1)
				  {
					  continue;
				  }
			  }
			  accTime = accurateTime(fib, n);
			  perError = ((curRunTime - accTime)/accTime)*100;
			  //ui.sendMessage("n: " + n + " fib(n) " + fibn + " time " + accTime + " " + perError + " % error.");
			  ui.sendMessage("f(" + n + ") = " + fibn + " in " + accTime + " microseconds. " + perError + "% error.");
		  }
		 else
		 {
			 accTime = accurateTime(fib, n);
			 constant = accTime/fib.O(n);
			 //ui.sendMessage("n: " + n + " fib(n) " + fibn + " time " + accTime + ". ");
			 ui.sendMessage("f(" + n + ") = " + fibn + " in " + accTime + " microseconds.");
		 }
	  }
  }

  public static void doExperiments () {
    // EXERCISE 10
	  String[] commands = {
				"ExponentialFib",
				"LinearFib",
				"LogFib",
				"ConstantFib",
				"MysteryFib",
		"EXIT"};
	  
	  while(true)
	  {
		  int c = ui.getCommand(commands);
		  switch(c)
		  {
		  	case -1:
				ui.sendMessage("You shut down the program, restarting. Use EXIT to exit.");
				break;
		  	case 0:
		  		doExperiments(new ExponentialFib());
		  		break;
		  	case 1:
		  		doExperiments(new LinearFib());
		  		break;
		  	case 2:
		  		doExperiments(new LogFib());
		  		break;
		  	case 3:
		  		doExperiments(new ConstantFib());
		  		break;
		  	case 4: 
		  		doExperiments(new MysteryFib());
		  		break;
		  	case 5:
		  		return;
		  }
	  }
	  
  }

  static void labExperiments () {
    // Create (Exponential time) Fib object and test it.
    //Fib lfib = new ConstantFib();
	  Fib lfib = new ExponentialFib();
	  System.out.println(lfib);
    for (int i = 0; i < 11; i++)
      System.out.println(i + " " + lfib.fib(i));
    
    // Determine running time for n1 = 20 and print it out.
    int n1 = 20;
    double time1 = accurateTime(lfib, n1);
    System.out.println("n1 " + n1 + " time1 " + time1);
    
    
    //calculating ncalls
    int ncalls = (int)(1000000/time1);
    //call average time
    double avgTime = averageTime(lfib, n1, ncalls);
    //print the results
    System.out.println("n1 " + n1 + " time1 " + avgTime);
    //call accurate time
    double accTime = accurateTime(lfib, n1);
    //Print results
    System.out.println("n1 " + n1 + " time1 " + accTime);
    
    
    // Calculate constant:  time = constant times O(n).
    double c = time1 / lfib.O(n1);
    System.out.println("c " + c);
    
    // Estimate running time for n2=30.
    int n2 = 30;
    double time2est = c * lfib.O(n2);
    System.out.println("n2 " + n2 + " estimated time " + time2est);
    
    // Calculate actual running time for n2=30.
    double time2 = accurateTime(lfib, n2);
    System.out.println("n2 " + n2 + " actual time " + time2);
    
    
  //calculating ncalls
    int ncalls2 = (int)(1000000/time2);
    //call average time
    double avgTime2 = averageTime(lfib, n2, ncalls2);
    //print the results
    System.out.println("n2 " + n2 + " time2 " + avgTime2);
    //call accurate time
    double accTime2 = accurateTime(lfib, n2);
    //Print results
    System.out.println("n2 " + n2 + " time2 " + accTime2);
    
 // Estimate running time for n3=100.
    int n3 = 100;
    double time3est = c * lfib.O(n3);
    System.out.println("n3 " + n3 + " estimated time " + time3est);
//It will finish in 2*10^19 microseconds.
    
  }

  /**
   * @param args the command line arguments
   */
  public static void main (String[] args) {
    //labExperiments();
    // doExperiments(new ExponentialFib());
    doExperiments();
  }
}
