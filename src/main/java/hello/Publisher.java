package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.ReplayProcessor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Publisher {

	@Autowired
	ReplayProcessor rp;

	@Autowired
	CountDownLatch latch;

	public void publishQuotes(int numberOfQuotes) throws InterruptedException {
		long start = System.currentTimeMillis();

		rp.onNext(new MyEvent("event1"));
		rp.onNext(new MyEvent("event2"));
		rp.onNext(new MyEvent("event3"));
		
		long elapsed = System.currentTimeMillis() - start;

		System.out.println("Elapsed time: " + elapsed + "ms");
		System.out.println("Average time per quote: " + elapsed / numberOfQuotes + "ms");
	}

}
