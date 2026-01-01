package marks;

import java.util.ArrayList;
import java.util.List;

public class MarksDAO {

	public boolean addMarks(Marks marks) {
		return true;
	}

	public boolean updateMarks(Marks marks) {
    	return true;
	}

	public Marks getMarks(int studentId, int courseId) {
	    return null;
	}

	public List<Marks> getMarksByStudent(int studentId) {
	    return new ArrayList<>();
	}
}


