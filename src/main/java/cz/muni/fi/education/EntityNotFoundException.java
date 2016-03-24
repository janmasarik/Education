package cz.muni.fi.education;

/**
 * Created by John on 17-Mar-16.
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String string) {
        super(string);
    }

    public EntityNotFoundException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public EntityNotFoundException(Throwable thrwbl) {
        super(thrwbl);
    }

    
}
