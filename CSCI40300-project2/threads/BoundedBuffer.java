//class BoundedBuffer
//This class implements the synchronization methods to be used in 
//the bounded buffer problem 

public class BoundedBuffer
{
   //MP2 create any variables you need
   int length; // max length of buffer
   Lock mutex; // mutex
   Condition canProduce; // condition var to see if it can produce
   Condition canConsume; // condition var to see if it can consume
   char[] buffer; // array for buffer
   int current; // current # of items in buffer
   int indexP; // keep track of index for producer
   int indexC; // keep track of index for consumer

   //BoundedBuffer
   //constructor:  initialize any variables that are needed for a bounded 
   //buffer of size "size"
   public BoundedBuffer(int size)
   {
	   length = size;
	   mutex = new Lock("mutex");
	   canProduce = new Condition("produce");
	   canConsume = new Condition("consume");
	   buffer = new char[size];
	   current = 0;
	   indexP = 0;
	   indexC = 0;
   }

   //produce()
   //produces a character c.  If the buffer is full, wait for an empty
   //slot
   public void produce(char c)
   {
	   //MP2
	   mutex.acquire(); // acquire lock
	   while(current >= length){ // checks if buffer is full (current # of items in equal to or larger than the buffer size)
		   canProduce.wait(mutex); // wait if buffer is full
	   }
	   current++; // increments # of items in buffer
	   buffer[indexP] = c; // put item in buffer
	   indexP = (indexP + 1) % length; // new index for producer
	   canConsume.signal(mutex); // wake up consumer
	   mutex.release(); // release lock
   }

   //consume()
   //consumes a character.  If the buffer is empty, wait for a producer.
   //use method SynchTest.addToOutputString(c) upon consuming a character. 
   //This is used to test your implementation.
   public void consume()
   {
	   //MP2
	   char c;
	   mutex.acquire(); // acquire lock
	   while(current < 1){ // checks if buffer is empty (current # of items is less than 1)
		   canConsume.wait(mutex); // wait if buffer is empty
	   }
	   current--; // decrements # of items in buffer
	   c = buffer[indexC]; // set c to current item
	   indexC = (indexC + 1) % length; // new index for consumer
	   SynchTest.addToOutputString(c);
	   canProduce.signal(mutex); // wake up producer
	   mutex.release(); // release lock
   }
}
