package prog12;

import java.util.List;
import java.util.*;

public class Binge implements SearchEngine {

	HardDisk<PageFile> pageDisk = new HardDisk<PageFile>();
	PageTrie urlToIndex = new PageTrie();
	HardDisk<List<Long>> wordDisk = new HardDisk<List<Long>>();
	WordTable wordToIndex = new WordTable();
	
	@Override
	public void gather(Browser browser, List<String> startingURLs) {
		// TODO Auto-generated method stub

		System.out.println("gather " + startingURLs);
		Queue<Long> pageIndices = new ArrayDeque<Long>();
		for(String URLs : startingURLs)
		{
			if(!urlToIndex.containsKey(URLs))
			{
				pageIndices.offer(indexPage(URLs));
			}
		}
		while(!pageIndices.isEmpty())
		{
			System.out.println("queue " + pageIndices);
			Long pageIndex = pageIndices.poll();
			PageFile pageFile = pageDisk.get(pageIndex);
			System.out.println("dequeued " + pageFile);
			if(browser.loadPage(pageFile.url))
			{
				Set<Long> indices = new HashSet<Long>();
				List<String> urls = browser.getURLs();
				System.out.println("urls " + urls);
				for(String URL : urls)
				{
					if(!urlToIndex.containsKey(URL))
					{
						pageIndices.offer(indexPage(URL));
					}
					indices.add(urlToIndex.get(URL));
				}
				for(Long index : indices)
				{
					pageDisk.get(index).incRefCount();
					System.out.println("inc ref " + pageDisk.get(index));
				}
				System.out.println("words " + browser.getWords());
				
				List<String> words = browser.getWords();
				Long index;
				for(String word : words)
				{
					if(!wordToIndex.containsKey(word))
					{
						index = indexWord(word);
					}
					else
					{
						index = wordToIndex.get(word);
					}
					if(wordDisk.get(index).size() == 0 || wordDisk.get(index).get(wordDisk.get(index).size()-1) != pageIndex)
					{
						wordDisk.get(index).add(pageIndex);
						System.out.println("add page " + wordToIndex.get(word) + "(" + word + ")" + wordDisk.get(wordToIndex.get(word)));
					}
				}
			}
		}
		System.out.println("pageDisk\n" + pageDisk);
		System.out.println("urlToIndex\n" + urlToIndex);
		System.out.println("wordDisk\n" + wordDisk);
		System.out.println("wordToIndex\n" + wordToIndex);
		urlToIndex.write(pageDisk);
		wordToIndex.write(wordDisk);
	}

	@Override
	public String[] search(List<String> keyWords, int numResults) {
		// TODO Auto-generated method stub
		return new String[0];
	}
	
	public Long indexPage(String url)
	{
		Long index = pageDisk.newFile();
		PageFile page = new PageFile(index, url);
		pageDisk.put(index, page);
		urlToIndex.put(url, index);
		System.out.println("indexing page " + page);
		return index;
	}
	
	public Long indexWord (String word)
	{
		Long index = wordDisk.newFile();
		List<Long> page = new ArrayList<Long>();
		wordDisk.put(index, page);
		wordToIndex.put(word, index);
		System.out.println("indexing word " + index + "(" + word + ")" + page);
		return index;
	}

}
