import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

public class CourseDatabase implements Serializable{
    private final TreeMap<String, Course> courses = new TreeMap<>();
    private static CourseDatabase instance = null;

    private CourseDatabase() {
        super();
    }

    public static CourseDatabase getInstance() throws IOException, ClassNotFoundException{
        initialize();
        if(instance == null){
            instance = new CourseDatabase();
        }
        return instance;
    }

    private static void initialize() throws IOException, ClassNotFoundException {
//        InputStream file = new FileInputStream("Courses.txt");
//        InputStream buffer = new BufferedInputStream(file);
//        ObjectInput input = new ObjectInputStream(buffer);
//
//        instance = (CourseDatabase) input.readObject();
    }

    public static void persist(){
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try{
            fos = new FileOutputStream("Courses.txt");
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

    public void addCourse(Course newCourse) throws Exception {
        if (courses.containsKey(newCourse.getCourseCode())) {
            throw new Exception();
        } else {
            courses.put(newCourse.getCourseCode(), newCourse);
            persist();
        }
    }

    public void deleteCourse(Course course) throws Exception {
        if (!courses.containsKey(course.getCourseCode())) {
            throw new Exception();
        } else {
            courses.remove(course.getCourseCode(), course);
            persist();
        }
    }

    public void updateCourse(Course oldCourse, Course newCourse) throws Exception {
        if (!courses.containsKey(oldCourse.getCourseCode())) {
            throw new Exception();
        } else {
            courses.remove(oldCourse.getCourseCode());
            courses.put(newCourse.getCourseCode(), newCourse);
            persist();
        }
    }

    public Course checkForCourse(String courseCode) {
        return courses.get(courseCode);
    }
}
