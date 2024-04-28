package jp.itohiro.sample.domain.entity;
import com.gs.fw.common.mithra.MithraSequence;

import java.sql.Timestamp;
public class ObjectSequence extends ObjectSequenceAbstract implements MithraSequence {
	public ObjectSequence()
	{
		super();
		// You must not modify this constructor. Mithra calls this internally.
		// You can call this constructor. You can also add new constructors.
	}

	@Override
	public void setSequenceName(String s) {
		super.setSimulatedSequenceName(s);
	}

	@Override
	public long getNextId() {
		return super.getNextValue();
	}

	@Override
	public void setNextId(long l) {
		super.setNextValue(l);
	}
}
