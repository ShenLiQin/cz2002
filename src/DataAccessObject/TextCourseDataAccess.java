package DataAccessObject;

import Exception.ExistingCourseException;
import Exception.NonExistentCourseException;
import ValueObject.Course;
import ValueObject.Index;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class TextCourseDataAccess implements Serializable, ICourseDataAccessObject {
    private final TreeMap<String, Course> courses = new TreeMap<>();
    private static TextCourseDataAccess instance = null;

    private TextCourseDataAccess() {
        super();
    }

    public static TextCourseDataAccess getInstance() throws IOException, ClassNotFoundException{
        initialize();
        if(instance == null){
            instance = new TextCourseDataAccess();
        }
        return instance;
    }

    private static void initialize() throws IOException, ClassNotFoundException {
//        InputStream file = new FileInputStream("./data/Courses.ser");
//        InputStream buffer = new BufferedInputStream(file);
//        ObjectInput input = new ObjectInputStream(buffer);
//
//        instance = (CourseDatabase) input.readObject();
    }

    public static void persist(){
        FileOutputStream fos;
        ObjectOutputStream out = null;
        try{
            fos = new FileOutputStream("./data/Courses.ser");
            out = new ObjectOutputStream(fos);
            out.writeObject(instance);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addCourse(Course newCourse) throws ExistingCourseException {
        if (courses.containsKey(newCourse.getCourseCode())) {
            throw new ExistingCourseException();
        } else {
            courses.put(newCourse.getCourseCode(), newCourse);
            persist();
        }
    }

    @Override
    public void deleteCourse(Course course) throws NonExistentCourseException {
        if (!courses.containsKey(course.getCourseCode())) {
            throw new NonExistentCourseException();
        } else {
            courses.remove(course.getCourseCode(), course);
            persist();
        }
    }

    @Override
    public void updateCourse(Course newCourse) {
        courses.replace(newCourse.getCourseCode(), newCourse);
        persist();
    }

    @Override
    public Course getCourse(String courseCode) {
        return courses.get(courseCode.toLowerCase());
    }

    @Override
    public List<String> getCourses() {
        return new ArrayList<>(courses.keySet());
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("_______All Available Courses_______").append('\n');
        for (Course course : courses.values()) {
            str.append(course.toString()).append('\n');;
        }
        return str.toString();
    }
}
