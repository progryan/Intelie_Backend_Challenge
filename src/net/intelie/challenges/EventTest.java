package net.intelie.challenges;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Test;

public class EventTest {
	@Test
	public void thisIsAWarning() throws Exception {
		Event event = new Event("some_type", 123L);

		// THIS IS A WARNING:
		// Some of us (not everyone) are coverage freaks.
		assertEquals(123L, event.timestamp());
		assertEquals("some_type", event.type());
	}

	@Test
	public void INSERTION_TEST() {
		System.out.println("------------------------------------------------------------------------");
		System.out.println("'INSERTION TEST'");
		System.out.println("------------------------------------------------------------------------");
		MainClass mc = new MainClass();
		Event event = new Event("some_type", 123L);

		mc.insert(event);
		assertEquals("some_type", mc.stack()[0].type());
		assertEquals(123L, mc.stack()[0].timestamp());

	}

	@Test
	public void TYPE_REMOVAL_TEST() {
		System.out.println("------------------------------------------------------------------------");
		System.out.println("'TYPE REMOVAL TEST'");
		System.out.println("------------------------------------------------------------------------");
		MainClass mc = new MainClass();
		Event event0 = new Event("_type_1", 0001L);
		Event event1 = new Event("_type_2", 0002L);
		Event event2 = new Event("_type_1", 0003L);
		Event event3 = new Event("_type_2", 0004L);

		mc.insert(event0);
		mc.insert(event1);
		mc.insert(event2);
		mc.insert(event3);

		assertEquals("_type_1", mc.stack()[0].type());
		assertEquals(0001L, mc.stack()[0].timestamp());
		assertEquals("_type_2", mc.stack()[1].type());
		assertEquals(0002L, mc.stack()[1].timestamp());
		assertEquals("_type_1", mc.stack()[2].type());
		assertEquals(0003L, mc.stack()[2].timestamp());
		assertEquals("_type_2", mc.stack()[3].type());
		assertEquals(0004L, mc.stack()[3].timestamp());

		mc.removeAll("_type_1");

		assertEquals("_type_2", mc.stack()[0].type());
		assertEquals(0002L, mc.stack()[0].timestamp());
		assertEquals("_type_2", mc.stack()[1].type());
		assertEquals(0004L, mc.stack()[1].timestamp());

		assertNotEquals("_type_1", mc.stack()[0].type());
		assertNotEquals(0001L, mc.stack()[0].timestamp());
		assertNotEquals("_type_1", mc.stack()[2].type());
		assertNotEquals(0003L, mc.stack()[2].timestamp());

	}

	@Test
	public void TOP_ELEMENT_QUERY_REMOVAL_TEST() {
		System.out.println("------------------------------------------------------------------------");
		System.out.println("'TOP ELEMENT QUERY REMOVAL TEST'");
		System.out.println("------------------------------------------------------------------------");
		MainClass mc = new MainClass();
		Event event0 = new Event("_type_1", 0001L);
		Event event1 = new Event("_type_2", 0002L);
		Event event2 = new Event("_type_1", 0003L);
		Event event3 = new Event("_type_2", 0004L);

		mc.insert(event0);
		mc.insert(event1);
		mc.insert(event2);
		mc.insert(event3);

		EventIterator e = mc.query("_type_1", 0001L, 0004L); // two results
		assertEquals("_type_1", e.current().type());
		assertEquals(0003L, e.current().timestamp());

		e.remove(); // now left one result

		assertEquals("_type_1", e.current().type());
		assertNotEquals(0003L, e.current().timestamp());

		mc = new MainClass();

		mc.insert(event0);
		mc.insert(event1);
		mc.insert(event2);
		mc.insert(event3);

		e = mc.query("_type_2", 0002L, 0004L);

		assertEquals("_type_2", e.current().type());
		assertEquals(0002L, e.current().timestamp()); // only one result

		e.remove(); // now left no result

		assertEquals(null, e.current());

	}

	@Test
	public void NEXT_QUERY_ELEMENT_MOVEMENT_TEST() {
		System.out.println("------------------------------------------------------------------------");
		System.out.println("'NEXT QUERY ELEMENT MOVEMENT TEST'");
		System.out.println("------------------------------------------------------------------------");
		MainClass mc = new MainClass();
		Event event0 = new Event("_type_1", 0001L);
		Event event1 = new Event("_type_2", 0002L);
		Event event2 = new Event("_type_1", 0003L);
		Event event3 = new Event("_type_2", 0004L);

		mc.insert(event0);
		mc.insert(event1);
		mc.insert(event2);
		mc.insert(event3);

		EventIterator e = mc.query("_type_1", 0001L, 0004L); // two results
		assertEquals("_type_1", e.current().type());
		assertEquals(0003L, e.current().timestamp());

		assertEquals(true, e.moveNext()); // next result

		assertEquals("_type_1", e.current().type());
		assertEquals(0001L, e.current().timestamp());

		assertEquals(false, e.moveNext()); // there no next result

	}

	@Test
	public void QUERY_TEST() {
		System.out.println("------------------------------------------------------------------------");
		System.out.println("'QUERY TEST'");
		System.out.println("------------------------------------------------------------------------");
		MainClass mc = new MainClass();
		Event event0 = new Event("_type_1", 0001L);
		Event event1 = new Event("_type_2", 0002L);
		Event event2 = new Event("_type_1", 0003L);
		Event event3 = new Event("_type_2", 0004L);
		Event event4 = new Event("_type_3", 0005L);
		Event event5 = new Event("_type_2", 0006L);
		Event event6 = new Event("_type_1", 0007L);
		Event event7 = new Event("_type_3", 0007L);

		mc.insert(event0);
		mc.insert(event1);
		mc.insert(event2);
		mc.insert(event3);
		mc.insert(event4);
		mc.insert(event5);
		mc.insert(event6);
		mc.insert(event7);

		EventIterator query0 = mc.query("_type_1", 0001L, 0004L); // two results
		EventIterator query1 = mc.query("_type_2", 0001L, 0007L); // three results
		EventIterator query2 = mc.query("_type_3", 0004L, 0006L); // one result

		assertEquals("_type_1", query0.current().type());
		assertEquals(0003L, query0.current().timestamp());
		assertEquals(true, query0.moveNext());
		assertEquals("_type_1", query0.current().type());
		assertEquals(0001L, query0.current().timestamp());
		assertEquals(false, query0.moveNext());

		assertEquals("_type_2", query1.current().type());
		assertEquals(0006L, query1.current().timestamp());
		assertEquals(true, query1.moveNext());
		assertEquals("_type_2", query1.current().type());
		assertEquals(0004L, query1.current().timestamp());
		assertEquals(true, query1.moveNext());
		assertEquals("_type_2", query1.current().type());
		assertEquals(0002L, query1.current().timestamp());
		assertEquals(false, query1.moveNext());

		assertEquals("_type_3", query2.current().type());
		assertEquals(0005L, query2.current().timestamp());
		assertEquals(false, query2.moveNext());

	}

	@Test
	public void CURRENT_ELEMENT_QUERY_RECOVERY_TEST() {
		System.out.println("------------------------------------------------------------------------");
		System.out.println("'CURRENT ELEMENT QUERY RECOVERY TEST'");
		System.out.println("------------------------------------------------------------------------");
		MainClass mc = new MainClass();
		Event event0 = new Event("_type_1", 0001L);

		mc.insert(event0);

		EventIterator query0 = mc.query("_type_1", 0001L, 0004L);

		assertEquals("_type_1", query0.current().type());
		assertEquals(0001L, query0.current().timestamp());

	}

	@Test
	public void SIMULTANEOUS_INSERTION_TEST() {
		System.out.println("------------------------------------------------------------------------");
		System.out.println("'SIMULTANEOUS INSERTION TEST'");
		System.out.println("------------------------------------------------------------------------");
		MainClass mc = new MainClass();

		Event event0 = new Event("_type_1", 0001L);
		Event event1 = new Event("_type_2", 0002L);
		Event event2 = new Event("_type_1", 0003L);
		Event event3 = new Event("_type_2", 0004L);
		Event event4 = new Event("_type_3", 0005L);
		Event event5 = new Event("_type_2", 0006L);
		Event event6 = new Event("_type_1", 0007L);
		Event event7 = new Event("_type_3", 0007L);

		Runnable t1 = new Runnable() {
			public void run() {
				try {
					mc.insert(event0);
					mc.insert(event1);
					mc.insert(event2);
					mc.insert(event3);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};

		Runnable t2 = new Runnable() {
			public void run() {
				try {
					mc.insert(event4);
					mc.insert(event5);
					mc.insert(event6);
					mc.insert(event7);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};
		long time = System.currentTimeMillis();
		long targetTime = time + 2000;
		Thread th1 = new Thread(t1);
		Thread th2 = new Thread(t2);

		Runnable t3 = new Runnable() {
			public void run() {
				while (System.currentTimeMillis() != targetTime) {
				}
				th1.start();
			}
		};
		Thread th3 = new Thread(t3);
		th3.start();
		while (System.currentTimeMillis() != targetTime) {
		}
		th2.start();

		try {
			th3.join();
			th2.join();
			th1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Printing the stack after insertion to show that all insertions was done with
		// success.

		System.out.println("\nPrinting the stack after insertion to show that all insertions was done with success\n");
		for (int i = 0; i < mc.stackPointer(); i++)
			System.out.println("{" + mc.stack()[i].type() + " | " + mc.stack()[i].timestamp() + "}");

	}

	@Test
	public void SIMULTANEOUS_REMOVAL_TEST() {
		System.out.println("------------------------------------------------------------------------");
		System.out.println("'SIMULTANEOUS REMOVAL TEST'");
		System.out.println("------------------------------------------------------------------------");
		MainClass mc = new MainClass();

		Event event0 = new Event("_type_1", 0001L);
		Event event1 = new Event("_type_2", 0002L);
		Event event2 = new Event("_type_1", 0003L);
		Event event3 = new Event("_type_2", 0004L);
		Event event4 = new Event("_type_3", 0005L);
		Event event5 = new Event("_type_2", 0006L);
		Event event6 = new Event("_type_1", 0007L);
		Event event7 = new Event("_type_3", 0007L);

		mc.insert(event0);
		mc.insert(event1);
		mc.insert(event2);
		mc.insert(event3);
		mc.insert(event4);
		mc.insert(event5);
		mc.insert(event6);
		mc.insert(event7);

		Runnable t1 = new Runnable() {
			public void run() {
				try {
					mc.removeAll("_type_1");

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};

		Runnable t2 = new Runnable() {
			public void run() {
				try {
					mc.removeAll("_type_1");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};
		long time = System.currentTimeMillis();
		long targetTime = time + 2000;
		Thread th1 = new Thread(t1);
		Thread th2 = new Thread(t2);

		Runnable t3 = new Runnable() {
			public void run() {
				while (System.currentTimeMillis() != targetTime) {
				}
				th1.start();
			}
		};
		Thread th3 = new Thread(t3);
		th3.start();
		while (System.currentTimeMillis() != targetTime) {
		}
		th2.start();

		try {
			th3.join();
			th2.join();
			th1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("_type_2", mc.stack()[0].type());
		assertEquals(0002L, mc.stack()[0].timestamp());
		assertEquals("_type_2", mc.stack()[1].type());
		assertEquals(0004L, mc.stack()[1].timestamp());
		assertEquals("_type_3", mc.stack()[2].type());
		assertEquals(0005L, mc.stack()[2].timestamp());
		assertEquals("_type_2", mc.stack()[3].type());
		assertEquals(0006L, mc.stack()[3].timestamp());
		assertEquals("_type_3", mc.stack()[4].type());
		assertEquals(0007L, mc.stack()[4].timestamp());

		// Printing the stack after insertion to show that all insertions was done with
		// success.

		System.out
				.println("\nPrinting the stack after REMOVAL to show that REMOVAL was done without inconsistencies\n");
		for (int i = 0; i < mc.stackPointer(); i++)
			System.out.println("{" + mc.stack()[i].type() + " | " + mc.stack()[i].timestamp() + "}");
	}

	@Test
	public void SIMULTANEOUS_QUERY_TEST() {
		System.out.println("------------------------------------------------------------------------");
		System.out.println("'SIMULTANEOUS QUERY TEST'");
		System.out.println("------------------------------------------------------------------------");
		MainClass mc = new MainClass();

		Event event0 = new Event("_type_1", 0001L);
		Event event1 = new Event("_type_2", 0002L);
		Event event2 = new Event("_type_1", 0003L);
		Event event3 = new Event("_type_2", 0004L);
		Event event4 = new Event("_type_3", 0005L);
		Event event5 = new Event("_type_2", 0006L);
		Event event6 = new Event("_type_1", 0007L);
		Event event7 = new Event("_type_3", 0007L);

		mc.insert(event0);
		mc.insert(event1);
		mc.insert(event2);
		mc.insert(event3);
		mc.insert(event4);
		mc.insert(event5);
		mc.insert(event6);
		mc.insert(event7);

		Runnable t1 = new Runnable() {
			public void run() {
				try {
					EventIterator e0 = mc.query("_type_1", 0001L, 0007L); // 2 results
					assertEquals("_type_1", e0.current().type());
					assertEquals(0003L, e0.current().timestamp());
					assertEquals(true, e0.moveNext());
					assertEquals("_type_1", e0.current().type());
					assertEquals(0001L, e0.current().timestamp());
					assertEquals(false, e0.moveNext());
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};

		Runnable t2 = new Runnable() {
			public void run() {
				try {
					EventIterator e1 = mc.query("_type_2", 0001L, 0007L); // 3 results
					assertEquals("_type_2", e1.current().type());
					assertEquals(0006L, e1.current().timestamp());
					assertEquals(true, e1.moveNext());
					assertEquals("_type_2", e1.current().type());
					assertEquals(0004L, e1.current().timestamp());
					assertEquals(true, e1.moveNext());
					assertEquals("_type_2", e1.current().type());
					assertEquals(0002L, e1.current().timestamp());
					assertEquals(false, e1.moveNext());

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};
		long time = System.currentTimeMillis();
		long targetTime = time + 2000;
		Thread th1 = new Thread(t1);
		Thread th2 = new Thread(t2);

		Runnable t3 = new Runnable() {
			public void run() {
				while (System.currentTimeMillis() != targetTime) {
				}
				th1.start();
			}
		};
		Thread th3 = new Thread(t3);
		th3.start();
		while (System.currentTimeMillis() != targetTime) {
		}
		th2.start();

		try {
			th3.join();
			th2.join();
			th1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}