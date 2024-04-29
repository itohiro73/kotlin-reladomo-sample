package jp.itohiro.sample.domain.entity;
import java.sql.Timestamp;
public class Product extends ProductAbstract
{
	public Product(Timestamp validTime
	, Timestamp transactionTime
	)
	{
		super(validTime
		,transactionTime
		);
		// You must not modify this constructor. Mithra calls this internally.
		// You can call this constructor. You can also add new constructors.
	}

	public Product(Timestamp validTime)
	{
		super(validTime);
	}
}
