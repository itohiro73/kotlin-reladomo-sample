package jp.itohiro.kotlinreladomo.domain.entity;
import com.gs.fw.finder.Operation;
import java.util.*;
public class DocumentList extends DocumentListAbstract
{
	public DocumentList()
	{
		super();
	}

	public DocumentList(int initialSize)
	{
		super(initialSize);
	}

	public DocumentList(Collection c)
	{
		super(c);
	}

	public DocumentList(Operation operation)
	{
		super(operation);
	}
}
