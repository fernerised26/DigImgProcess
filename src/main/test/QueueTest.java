package main.test;

import java.util.LinkedList;
import java.util.Queue;

public class QueueTest {

	public static void main(String[] args) {
		Queue<String> queue = new LinkedList<String>();
		
		queue.add("First");
		queue.add("Second");
		queue.add("Third");
		queue.add("Fourth");
		
		
		System.out.println(queue);
		
//		System.out.println(queue.poll());
//		System.out.println(queue.poll());
//		
//		System.out.println(queue);
//		
//		System.out.println(queue.poll());
//		System.out.println(queue.poll());
//		
//		System.out.println(queue);
		
		System.out.println(queue.remove("Derp"));
		System.out.println(queue.remove("First"));
		System.out.println(queue);
	}
}
