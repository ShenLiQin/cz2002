package DataAccessObject;

import Exception.*;
import ValueObject.RegistrationKey;
import ValueObject.RegistrationPeriod;

import java.io.IOException;

public interface IRegistrationDataAccessObject {
    RegistrationPeriod getRegistrationPeriod();

    void setRegistrationPeriod(RegistrationPeriod registrationPeriod);

    void updateRegistrationPeriod(RegistrationPeriod newRegistrationPeriod) throws IdenticalRegistrationPeriodException;

    void addRegistration(RegistrationKey registrationKey) throws ExistingRegistrationException, IOException, ClassNotFoundException, ExistingCourseException, ExistingUserException, NonExistentUserException, MaxEnrolledStudentsException;

    void deleteRegistration(RegistrationKey registrationKey) throws NonExistentRegistrationException, IOException, ClassNotFoundException, NonExistentUserException, NonExistentCourseException, ExistingCourseException, MaxEnrolledStudentsException, ExistingUserException;
}
