import java.util.ArrayList;

public interface DBInterface {
	ArrayList<Object[]> contents = new ArrayList<>();

	public ArrayList<Object[]> getContents();


	public boolean accessCheck();

}
