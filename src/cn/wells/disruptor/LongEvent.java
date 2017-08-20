package cn.wells.disruptor;
//Firstly we will define the Event that will carry the data.
public class LongEvent {
	private long value;

    public void set(long value)
    {
        this.value = value;
    }
	public long getValue() {
		return value;
	}  
}
