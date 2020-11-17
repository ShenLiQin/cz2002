package DataAccessObject;

import Exception.*;
import ValueObject.RegistrationKey;
import ValueObject.RegistrationPeriod;

import java.io.IOException;

public interface IRegistrationDataAccessObject {
    RegistrationPeriod getRegistrationPeriod();

    void updateRegistrationPeriod(RegistrationPeriod newRegistrationPeriod) throws IdenticalRegistrationPeriodException;

    void addRegistration(RegistrationKey registrationKey) throws ExistingRegistrationException, IOException, ClassNotFoundException, ExistingCourseException, ExistingUserException, MaxClassSizeException;

    void deleteRegistration(RegistrationKey registrationKey) throws NonExistentRegistrationException, IOException, ClassNotFoundException, NonExistentUserException, NonExistentCourseException;
}
