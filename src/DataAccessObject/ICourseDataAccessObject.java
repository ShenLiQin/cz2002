package DataAccessObject;

import Exception.ExistingCourseException;
import Exception.NonExistentCourseException;
import ValueObject.Course;

import java.util.List;
import java.util.Set;

public interface ICourseDataAccessObject {
    void addCourse(Course newCourse) throws ExistingCourseException;

    void deleteCourse(Course course) throws NonExistentCourseException;

    void updateCourse(Course newCourse);

    Course getCourse(String courseCode);

    List<String> getCourses();
}
