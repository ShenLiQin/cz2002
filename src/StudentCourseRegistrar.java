import java.time.LocalDateTime;

public class StudentCourseRegistrar {

    public void addRegistration(String matricNumber, String courseCode, int indexNumber) throws Exception {
        UserDatabase userDatabase = Factory.getUserDatabase();
        CourseDatabase courseDatabase = Factory.getCourseDatabase();
        RegistrationDatabase registrationDatabase = Factory.getRegistrationDatabase();

        LocalDateTime startDate = registrationDatabase.getRegistrationPeriod().getStartDate();
        LocalDateTime endDate = registrationDatabase.getRegistrationPeriod().getEndDate();
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startDate) || now.isAfter(endDate)) {
            System.out.println("registration period have not started/ is over");
//            throw new Exception();
        }
        if (userDatabase.getStudent(matricNumber) == null) {
            System.out.println("no such student");
//            throw new Exception();
        }
        Course course = courseDatabase.getCourse(courseCode);
        if (course == null) {
            System.out.println("no such course");
//            throw new Exception();
        } else if (course.getIndex(indexNumber) == null){
            System.out.println("no such index");
//            throw new Exception();
        }

//      ... check if student can still register (AU count)

        RegistrationKey registrationKey = Factory.createRegistrationKey(matricNumber, courseCode, indexNumber);
        //update registrationdatabase
        //update coursedatabase
        //update userdatabase
        registrationDatabase.addRegistration(registrationKey);
    }
}
