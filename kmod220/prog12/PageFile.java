package prog12;

/** This class represents the information stored in a file to record a
  * web page. */
public class PageFile {
	//where it should go on the harddisk... identifier... unique
  public final Long index;
  public final String url;
  //number of times a page is referenced... vote... priority queue
  private int refCount = 0;

  public PageFile (Long index, String url) {
    this.index = index;
    this.url = url;
  }

  public int getRefCount () { return refCount; }
  public void setRefCount(int ref) { refCount = ref; }
  public int incRefCount () { return ++refCount; }

  public String toString () {
    return index + "(" + url + ")" + refCount;
  }
}


    
