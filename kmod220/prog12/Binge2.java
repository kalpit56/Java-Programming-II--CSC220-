package prog12;

import java.util.*;

public class Binge2 implements SearchEngine {

	HardDisk<PageFile> pageDisk = new HardDisk<PageFile>();
	PageTrie urlToIndex = new PageTrie();
	HardDisk<List<Long>> wordDisk = new HardDisk<List<Long>>();
	WordTable wordToIndex = new WordTable();
	
	@Override
	public void gather(Browser browser, List<String> startingURLs) {
		// TODO Auto-generated method stub
		
		urlToIndex.read(pageDisk);
	    wordToIndex.read(wordDisk);
	}

	@Override
	public String[] search(List<String> keyWords, int numResults) {
		// TODO Auto-generated method stub
		//return null;
		
		System.out.println("pageDisk\n" + pageDisk);
		System.out.println("urlToIndex\n" + urlToIndex);
		System.out.println("wordDisk\n" + wordDisk);
		System.out.println("wordToIndex\n" + wordToIndex);
		
		 Iterator<Long>[] wordFileIterators = (Iterator<Long>[]) new Iterator[keyWords.size()];
		 long[] currentPageIndices = new long[keyWords.size()];
		 PriorityQueue<Long> bestPageIndices = new PriorityQueue<Long>(numResults, new PageComparator());
		 
		 for(int i = 0; i < keyWords.size(); i++)
		 {
			 if(!wordToIndex.containsKey(keyWords.get(i)))
			 {
				 return new String[0];
			 }
			 wordFileIterators[i] = wordDisk.get(wordToIndex.get(keyWords.get(i))).iterator();
		 }
		 
		 while(getNextPageIndices(currentPageIndices, wordFileIterators))
		 {
			 if(allEquals(currentPageIndices))
			 {
				 if(numResults > bestPageIndices.size() || pageDisk.get(currentPageIndices[0]).getRefCount() > pageDisk.get(bestPageIndices.peek()).getRefCount())
				 {
					 if(numResults == bestPageIndices.size())
					 {
						bestPageIndices.poll();
					 }
					 bestPageIndices.offer(currentPageIndices[0]);
				 }
			 }
		 }
		 
		 String[] result = new String[bestPageIndices.size()];
		 for(int i = result.length-1; i >= 0; i--)
		 {
			 result[i] = pageDisk.get(bestPageIndices.poll()).url;
		 }
		 
		 return result;
	}
	
	public boolean allEquals(long[] array)
	 {
		 for(int i = 0; i < array.length; i++)
		 {
			 if(array[0] != array[i])
			 {
				 return false;
			 }
		 }
		 return true;
	 }
	
	private boolean getNextPageIndices (long[] currentPageIndices, Iterator<Long>[] wordFileIterators) 
	{
		long largeIndex = currentPageIndices[0];
		if(allEquals(currentPageIndices))
		{
			for(int i = 0; i < wordFileIterators.length; i++)
			{
				if(!wordFileIterators[i].hasNext())
				{
					return false;
				}
				else
				{
					currentPageIndices[i] = wordFileIterators[i].next();
				}
			}
		}
		else
		{
			for(Long longIndex : currentPageIndices)
			{
				if(longIndex > largeIndex)
				{
					largeIndex = longIndex;
				}
			}
			
			for(int i = 0; i < currentPageIndices.length; i++)
			{
				if(currentPageIndices[i] < largeIndex)
				{
					if(!wordFileIterators[i].hasNext())
					{
						return false;
					}
					else
					{
						currentPageIndices[i] = wordFileIterators[i].next();
					}
				}
			}
		}
		return true;
	}
	
	public class PageComparator implements Comparator<Long>
	{
		@Override
		public int compare(Long o1, Long o2) {
			// TODO Auto-generated method stub
			return pageDisk.get(o1).getRefCount() - pageDisk.get(o2).getRefCount();
		}
	}

}


