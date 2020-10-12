package prog04;

public class SortedDLLPD extends DLLBasedPD {
	
	/** Add a new entry at a location.
    @param location The location to add the new entry, null to add
    it at the end of the list
    @param name The name in the new entry
    @param number The number in the new entry
    @return the new entry
*/
protected DLLEntry add (DLLEntry location, String name, String number) {
  DLLEntry entry = new DLLEntry(name, number);
  // EXERCISE
  
  if(tail == null)
  {
  	head = entry;
  	tail = entry;
  }
  else if(location == null)
  {
	  tail.setNext(entry);
	  entry.setPrevious(tail);
	  tail = entry;
  }
  else
  {
	  DLLEntry prev = location.getPrevious();
	  
	  if(prev == null)
	  {
		  entry.setNext(head);
		  head.setPrevious(entry);
		  head = entry;
	  }
	  else
	  {
		  prev.setNext(entry);
		  entry.setPrevious(prev);
		  location.setPrevious(entry);
		  entry.setNext(location);
	  }
	  
  }
  
  return entry;
}

/** Find an entry in the directory.
    @param name The name to be found
    @return The entry with the same name or the location next to where the entry should go.
*/
protected DLLEntry find (String name) {
  // EXERCISE
  // For each entry in the directory.
  // What is the first?  What is the next?  How do you know you got them all?
	DLLEntry entry = head;  
	while(entry != null && entry.getName().compareTo(name) < 0)
	{
		entry = entry.getNext();
	}
	return entry; //where name is or where it should be
}

/** Check if a name is found at a location.
    @param location The location to check
    @param name The name to look for at that location
    @return false, if location is null or it does not have that
    name; true, otherwise.
*/
protected boolean found (DLLEntry location, String name) {
  if (location == null || !(location.getName().contentEquals(name)))
    return false;
  return true;
}
}
