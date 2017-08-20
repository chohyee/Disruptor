package cn.wells.disruptor;

import java.nio.ByteBuffer;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
/**
 * 注意，最后的 ringBuffer.publish 方法必须包含在 finally 中以确保必须得到调用；
 * 如果某个请求的 sequence 未被提交，将会堵塞后续的发布操作或者其它的 producer。
 * Disruptor 还提供另外一种形式的调用来简化以上操作，并确保 publish 总是得到调用。
 * ===============================================================
 * With version 3.0 of the Disruptor a richer Lambda-style API was 
 * added to help developers by encapsulating this complexity within
 * the Ring Buffer, so post-3.0 the preferred approach for publishing
 * messages is via the Event Publisher/Event Translator portion of the API. E.g.
 * @author clover
 *
 */
public class LongEventProducerWithTranslator {
	private final RingBuffer<LongEvent> ringBuffer;
	public LongEventProducerWithTranslator(RingBuffer<LongEvent> ringBuffer) {
		this.ringBuffer = ringBuffer;
	} 
	private static final EventTranslatorOneArg<LongEvent,ByteBuffer> TRANSLATOR =
			new EventTranslatorOneArg<LongEvent,ByteBuffer>()
			{
				 public void translateTo(LongEvent event, long sequence, ByteBuffer bb)
		         {
		             event.set(bb.getLong(0));
		         }
			};
	public void onData(ByteBuffer bb)
    {
        ringBuffer.publishEvent(TRANSLATOR, bb);
    }
}
