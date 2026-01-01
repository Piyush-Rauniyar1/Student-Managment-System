package marks;

public class GradeService {

    // Calculate grade based on marks
    public String calculateGrade(double marks) {
        if (marks >= 80) return "A";
        else if (marks >= 60) return "B";
        else if (marks >= 40) return "C";
        else return "F";
    }

    // Assign grade to Marks object
    public boolean assignGrade(Marks marksObj) {
        // Use getter to get marks
        double marks = marksObj.getMarks();
        // Use setter to set grade
        marksObj.setGrade(calculateGrade(marks));
        return true;
    }
}
