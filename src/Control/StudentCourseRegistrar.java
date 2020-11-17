package Control;

import DataAccessObject.ICourseDataAccessObject;
import DataAccessObject.IRegistrationDataAccessObject;
import DataAccessObject.IUserDataAccessObject;
import Helper.Factory;
import ValueObject.Course;
import ValueObject.RegistrationKey;

import java.time.LocalDateTime;

public class StudentCourseRegistrar {

    public void addRegistration(String matricNumber, String courseCode, int indexNumber) throws Exception {
        IUserDataAccessObject userDataAccess = Factory.getTextUserDataAccess();
        ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
        IRegistrationDataAccessObject registrationDataAccess = Factory.getTextRegistrationDataAccess();

        LocalDateTime startDate = registrationDataAccess.getRegistrationPeriod().getStartDate();
        LocalDateTime endDate = registrationDataAccess.getRegistrationPeriod().getEndDate();
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startDate) || now.isAfter(endDate)) {
            System.out.println("registration period have not started/ is over");
//            throw new Exception();
        }
        if (userDataAccess.getStudent(matricNumber) == null) {
            System.out.println("no such student");
//            throw new Exception();
        }
        Course course = courseDataAccessObject.getCourse(courseCode);
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
        registrationDataAccess.addRegistration(registrationKey);
    }
}
