package rodriguezgonzalez.control.exceptions;

import javax.jms.JMSException;

public class StoreException extends JMSException {
    public StoreException(String reason) {
        super(reason);
    }
}
