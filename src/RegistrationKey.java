public class RegistrationKey implements Comparable<RegistrationKey>{
    private final String matricNumber;
    private final String courseCode;
    private final int indexNumber;

    public String getMatricNumber() {
        return matricNumber;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public int getIndexNumber() {
        return indexNumber;
    }

    public RegistrationKey(String matricNumber, String courseCode, int indexNumber) {
        this.matricNumber = matricNumber;
        this.courseCode = courseCode;
        this.indexNumber = indexNumber;
    }

    @Override
    public int compareTo(RegistrationKey other) {
        if (this.matricNumber.equals(other.matricNumber)) {
            if (this.courseCode.equals(other.courseCode)) {
                return 0;
            } else {
                return this.courseCode.compareTo(other.courseCode);
            }
        } else {
            return this.matricNumber.compareTo(other.matricNumber);
        }
    }
}
