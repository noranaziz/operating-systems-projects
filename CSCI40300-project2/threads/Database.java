//class Database
//This class implements the synchronization methods to be used in 
//the readers writers problem
public class Database
{
   //MP2 create any variables that you need for implementation of the methods
   //of this class
   Semaphore readerMutex; // reader mutex for readerCount
   Semaphore writerMutex; // writer mutex for writerCount
   Semaphore database; // controls access to database
   Lock writerLock; // lock for writers
   Condition canWrite; // condition var to see if writers can write
   int readerCount; // # of readers reading or wanting to
   int writerCount; // # of writers writing or wanting to

   //Database
   //Initializes Database variables
   public Database()
   {
     //MP2
	 readerMutex = new Semaphore("reader", 1); // initial value is 1
	 writerMutex = new Semaphore("writer", 1); // initial value is 1
	 database = new Semaphore("db", 1); // initial value is 1
	 writerLock = new Lock("lock");
	 canWrite = new Condition("canWrite");
	 readerCount = 0;
	 writerCount = 0;
   }

   //napping()
   //this is called when a reader or writer wants to go to sleep and when 
   //a reader or writer is doing its work.
   //Do not change for MP2
   public static void napping()
   {
      Alarm ac = new Alarm(20);  
   }

   //startRead
   //this function should block any reader that wants to read if there 
   //is a writer that is currently writing.
   //it returns the number of readers currently reading including the
   //new reader.
   public int startRead()
   {
      //MP2
	  // the following if statement should block any reader that wants to read
	  // if there is a writer currently writing/waiting.
	  if(writerCount > 0){ // if there are writers writing/waiting...
		  writerLock.acquire(); // acquire lock
		  canWrite.wait(writerLock); // wait
		  writerLock.release(); // release lock
	  }
	  // rest of this method is the code from the textbook.
	  readerMutex.P(); // get access to readerCount
	  readerCount++; // increment readers
	  if(readerCount == 1){ // if this is the first reader...
		  database.P(); // access database
	  }
	  readerMutex.V(); // release access to readerCount
      return readerCount;
   }

   //endRead()
   //This function is called by a reader that has finished reading from the 
   //database.  It returns the current number of readers excluding the one who
   //just finished.
   public int endRead()
   {
      //MP2
	  // this method is the code from the textbook.
	  readerMutex.P(); // get access to readerCount
	  readerCount--; // decrement readers
	  if(readerCount == 0){ // if this is the last reader...
		  database.V();
	  }
	  readerMutex.V(); // release access to readerCount
      return readerCount;
   }

   //startWrite()
   //This function should allow only one writer at a time into the Database
   //and block the writer if anyone else is accessing the database for read 
   //or write.
   public void startWrite()
   {
      //MP2
	  // writers have priority, so increment writers.
	  writerMutex.P(); // get access to writerCount
	  writerCount++; // increment writers waiting
	  writerMutex.V(); // release access to writerCount

	  database.P(); // get access to database

	  // decrement writers once done with database.
	  writerMutex.P(); // get access to writerCount
	  writerCount--; // decrement writers
	  writerMutex.V(); // release access to writer count

	  // the following if statement should wake up all waiting if there are no more writers writing/waiting
	  if(writerCount == 0){ // if this is the last writer...
		  writerLock.acquire(); // acquire lock
		  canWrite.broadcast(writerLock); // wake up all waiting
		  writerLock.release(); // release lock
	  }
   }
   
   //endWrite()
   //signal that a writer is done writing and the database can now be accessed
   //by someone who is waiting to read or write.
   public void endWrite()
   {
      //MP2
	  // this method is the code from the textbook.
	  database.V(); // release access to database
   }
}
