package cmpt721A3;

import java.util.LinkedList;

public class BasicTree<T>
{
	private T element;
	//private BasicTree<T> parent;
	private LinkedList<BasicTree<T>> children;
	
	public BasicTree(T elem)
	{
		element = elem;
		children = new LinkedList<BasicTree<T>>();
	}
	
	/*private BasicTree(T elem, BasicTree<T> parent)
	{
		this(elem);
		this.parent = parent;
	}*/
	
	public BasicTree<T> addChild(T elem)
	{
		children.add(new BasicTree<T>(elem/*, this*/));
		return children.getLast();
	}
	
	public LinkedList<BasicTree<T>> getChildren()
	{
		return children;
	}
	
	public T getElement()
	{
		return element;
	}
	
	public void setElemenet(T elem)
	{
		this.element = elem;
	}
}
