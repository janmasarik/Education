package cz.muni.fi.education;

/**
 * Created by John on 17-Mar-16.
 */
public class AttendanceFailureException extends RuntimeException {

    public AttendanceFailureException(String s) {
        super(s);
    }

    public AttendanceFailureException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AttendanceFailureException(Throwable throwable) {
        super(throwable);
    }
}
