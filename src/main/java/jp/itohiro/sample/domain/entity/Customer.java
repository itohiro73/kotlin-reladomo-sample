package jp.itohiro.sample.domain.entity;
import java.sql.Timestamp;
public class Customer extends CustomerAbstract
{
	public Customer(Timestamp applicableRange
	)
	{
		super(applicableRange
		);
		// You must not modify this constructor. Mithra calls this internally.
		// You can call this constructor. You can also add new constructors.
	}
}
