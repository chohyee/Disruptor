package cn.wells.disruptor;

import com.lmax.disruptor.EventHandler;
/**
 * 消费者，处理事件（数据）。
 * @author clover
 *
 */
public class LongEventHandler implements EventHandler<LongEvent>{

	@Override
	public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
		System.out.println("Event: " + event.getValue()*event.getValue());
		System.out.println("Event: " + sequence);
		System.out.println("Event: " + endOfBatch);
	}

}
