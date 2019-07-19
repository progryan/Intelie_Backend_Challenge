/* RYAN ARTHUR MELO DE SÁ
 * ---------------------------------------------------------------------------------------------------------
 * SPECIFICATIONS ABOUT THE CODE
 * 
 * I chose stack data structure to store the events in memory
 * because I understand that events needs to follow a order of insertion, so I think
 * maybe this structure isn't more effective to search a specific event, but is OK to get the current event
 * with max efficiency because it will always be at the top of the stack.
 * The complexity of a query is O(n) in worst case, but to recovery the top of the stack, where the most recent event is, is O(1).  
 * When entering a synchronized method, the thread that called the method
 * locks the object whose method was called.
 * In the EventTest there some simple tests about all methods implemented in this code.
 * The thread-safe test uses Insertion, Removal and Query methods.
 * The top of the Stack is the more significant index of stack array
 * for example: 
 * insert event 1
 * insert event 2
 * insert event 3
 * 
 * the top of stack is event 3, because it was the last event added.
 * ---------------------------------------------------------------------------------------------------------
 */
package net.intelie.challenges;

import java.util.ArrayList;

public class MainClass implements EventStore {

	private Event[] stack = new Event[1];
	private int stackPointer = 0;

	@Override
	public synchronized void insert(Event event) {
		// TODO Auto-generated method stub
		synchronized (stack) {
			try {
				stack[stackPointer++] = event;

			} catch (ArrayIndexOutOfBoundsException e) {
				stack = duplicateArray(stack);
				stack[stackPointer - 1] = event;
			}
		}
	}

	@Override
	public synchronized void removeAll(String type) {
		// TODO Auto-generated method stub
		synchronized (stack) {
			for (int i = 0; i < stackPointer; i++) {
				if (stack[i].type().equals(type))
					stack[i] = null;
			}
			arrangeArray();
			// System.out.println(stackPointer);
		}
	}

	@Override
	public synchronized EventIterator query(String type, long startTime, long endTime) {
		// TODO Auto-generated method stub
		ArrayList<Integer> queryIndexVector = new ArrayList<Integer>();

		for (int i = stackPointer - 1; i >= 0; i--) {
			// System.out.println(stack[i].type().equals(type));
			if (stack[i].type().equals(type) && startTime <= stack[i].timestamp() && stack[i].timestamp() < endTime) {
				queryIndexVector.add(i);
			}
		}
		System.out.println("Query Result:");
		if (queryIndexVector.size() != 0)
			for (int iq : queryIndexVector) {
				System.out.println("\t{type:" + stack[iq].type() + "|timestamp:" + stack[iq].timestamp() + "}");
			}
		else
			System.out.println("\tNo results found.");
		// System.out.println(queryIndexVector.size());
		EventIterator ei = new EventIterator() {
			int currentPointer = queryIndexVector.size() != 0 ? queryIndexVector.get(0) : -1;

			@Override
			public void close() throws Exception {
				// TODO Auto-generated method stub
				System.exit(0);
			}

			@Override
			public synchronized void remove() {
				// TODO Auto-generated method stub
				synchronized (stack) {
					synchronized (queryIndexVector) {
						if (currentPointer != -1) {
							stack[currentPointer] = null;
							try {
								queryIndexVector.remove(0);
								currentPointer = queryIndexVector.get(0);
							} catch (IndexOutOfBoundsException e) {
								currentPointer = -1;
								System.out.println("About method remove() : No more events to remove!");
								return;
							}
						} else {
							return;
						}
					}
				}
			}

			@Override
			public synchronized boolean moveNext() {
				// TODO Auto-generated method stub
				synchronized (queryIndexVector) {
					try {
						queryIndexVector.remove(0);
						currentPointer = queryIndexVector.get(0);
						return true;
					} catch (IndexOutOfBoundsException e) {
						System.out.println("About method moveNext() : The latest event was reached!");
						return false;
					}
				}
			}

			@Override
			public synchronized Event current() {
				// TODO Auto-generated method stub
				synchronized (stack) {
					try {
						return stack[currentPointer];
					} catch (IndexOutOfBoundsException e) {
						System.out.println("About method current() : This method returned no event!");
						return null;
					}
				}
			}
		};
		return ei;
	}

	public synchronized Event[] duplicateArray(Event[] array) {
		Event[] newArray = new Event[array.length * 2];
		for (int i = 0; i < array.length; i++) {
			newArray[i] = array[i];
		}
		return newArray;
	}

	public synchronized void arrangeArray() {
		synchronized (stack) {
			int i = 0;
			while (i < stackPointer - 1) {
				if (stack[i] == null) {
					for (int j = i; j < stackPointer - 1; j++) {
						stack[j] = stack[j + 1];
					}
					stackPointer--;
				} else {
					i++;
				}

			}
		}
	}

	public static void printQuery(EventIterator ei) {
		do {
			System.out.println("Event: " + ei.current().type() + " | t: " + ei.current().timestamp());
		} while (ei.moveNext());
	}

	public Event[] stack() {
		return stack;
	}

	public int stackPointer() {
		return stackPointer;
	}

	public static void main(String[] args) {
		MainClass mainClass = new MainClass();
		// do something using mainClass instance
	}
}
