package hello;

import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.publisher.BaseSubscriber;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application implements CommandLineRunner {

	private static final int NUMBER_OF_QUOTES = 10;

	@Bean
	ReplayProcessor createReplayProcessor() {
		
		ReplayProcessor<MyEvent> rp = ReplayProcessor.create();

		Flux<MyEvent> interest1 = rp.filter(ev -> filterInterest1(ev));

		Flux<MyEvent> interest2 = rp.filter(ev -> filterInterest2(ev));

		interest1.subscribe(new BaseSubscriber<MyEvent>() {
			@Override
			protected void hookOnSubscribe(Subscription subscription) {
				requestUnbounded();
			}
			@Override
			protected void hookOnNext(MyEvent value) {
				//todo: call service method
				System.out.println("event 1 handler -> event name:" + value.getEventName());
			}
						
		});
		

		interest2.subscribe(new BaseSubscriber<MyEvent>() {
			@Override
			protected void hookOnSubscribe(Subscription subscription) {
				requestUnbounded();
			}
			@Override
			protected void hookOnNext(MyEvent value) {
				//todo: call service method
				System.out.println("event2 handler -> event name:" + value.getEventName());
			}
		});

		return rp;
	}

	public boolean filterInterest1(MyEvent myEvent) {
		if (myEvent != null && myEvent.getEventName() != null
				&& myEvent.getEventName().equalsIgnoreCase("event1")) {
			return true;
		}
		return false;
	}

	public boolean filterInterest2(MyEvent myEvent) {
		if (myEvent != null && myEvent.getEventName() != null
				&& myEvent.getEventName().equalsIgnoreCase("event2")) {
			return true;
		}
		return false;
	}
	

	@Autowired
	private Publisher publisher;

	@Bean
	public CountDownLatch latch() {
		return new CountDownLatch(NUMBER_OF_QUOTES);
	}

	@Override
	public void run(String... args) throws Exception {
		publisher.publishQuotes(NUMBER_OF_QUOTES);
	}

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext app = SpringApplication.run(Application.class, args);

		app.getBean(CountDownLatch.class).await(10, TimeUnit.SECONDS);

		
	}

}
