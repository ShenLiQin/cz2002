package DataAccessObject;

import Exception.ExistingCourseException;
import Exception.NonExistentCourseException;
import ValueObject.Course;

public interface ICourseDataAccessObject {
    void addCourse(Course newCourse) throws ExistingCourseException;

    void deleteCourse(Course course) throws NonExistentCourseException;

    void updateCourse(Course newCourse);

    Course getCourse(String courseCode);
}
