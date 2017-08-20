package cn.wells.disruptor;

import java.nio.ByteBuffer;
import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * The final step is to wire the whole thing together. 
 * It is possible to wire all of the components manually, 
 * however it can be a little bit complicated so a DSL is
 * provided to simplify construction. Some of the more complicated
 * options are not available via the DSL, however it is suitable for most circumstances.
 * @author clover
 *
 */
public class LongEventMain {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		// Executor that will be used to construct new threads for consumers
		//Executor executor = Executors.newCachedThreadPool();

		// The factory for the event
		LongEventFactory factory = new LongEventFactory();

		// Specify the size of the ring buffer, must be power of 2.
		int bufferSize = 1024;
		Disruptor<LongEvent> disruptor =new Disruptor<LongEvent>(
				factory,
				bufferSize,
	            new ThreadFactory(){

					@Override
					public Thread newThread(Runnable r) {
						return new Thread(r);
					}
					
				},
	            ProducerType.MULTI,
	            new YieldingWaitStrategy());
		// Construct the Disruptor
		//Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, executor);

		// Connect the handler
		//disruptor.handleEventsWith(new LongEventHandler());
		disruptor.handleEventsWith(new LongEventHandler());
		// Start the Disruptor, starts all threads running
		disruptor.start();

		// Get the ring buffer from the Disruptor to be used for publishing.
		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

		LongEventProducer producer = new LongEventProducer(ringBuffer);

		ByteBuffer bb = ByteBuffer.allocate(8);
		for (long l = 0; true; l++) {
			bb.putLong(0, l);
			producer.onData(bb);
			Thread.sleep(1000);
			System.out.println("add data " + l);
		}
	}
}
