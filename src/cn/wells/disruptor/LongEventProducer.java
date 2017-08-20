package cn.wells.disruptor;

import java.nio.ByteBuffer;

import com.lmax.disruptor.RingBuffer;

/**
 * 生产者，We will need a source for these events, 
 * for the sake of an example I am going to assume
 * that the data is coming from some sort of I/O device,
 * e.g. network or file in the form of a ByteBuffer.
 * @author clover
 *
 */
public class LongEventProducer {
	private final RingBuffer<LongEvent> ringBuffer;
	public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}
	public void onData(ByteBuffer bb){
		long sequence = ringBuffer.next();//Grab the next sequence
		try{
			LongEvent event = ringBuffer.get(sequence);// Get the entry in the Disruptor
													   // for the sequence（空的数据槽）
			event.set(bb.getLong(0));//模拟从IO、文件等的输入流,在event中填充数据
		}finally{
			ringBuffer.publish(sequence);//不管成功与否，均将拿到的序列号归还
		}
	}
}
