public class RegistrationKey implements Comparable<RegistrationKey>{
    private final int matricNumber;
    private final String courseCode;
    private final int indexNumber;

    public RegistrationKey(int matricNumber, String courseCode, int indexNumber) {
        this.matricNumber = matricNumber;
        this.courseCode = courseCode;
        this.indexNumber = indexNumber;
    }

    @Override
    public int compareTo(RegistrationKey other) {
        if (this.matricNumber == other.matricNumber) {
            if (this.courseCode.equals(other.courseCode)) {
                return this.indexNumber - other.indexNumber;
            } else {
                return this.courseCode.compareTo(other.courseCode);
            }
        } else {
            return this.matricNumber - other.matricNumber;
        }
    }
}
