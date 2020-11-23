package ValueObject;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RegistrationPeriod implements Serializable {
    private final LocalDateTime startDate, endDate;
    private static final long serialVersionUID = 1L;

    public RegistrationPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean notWithinRegistrationPeriod() {
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(startDate) || now.isAfter(endDate);
    }
}
