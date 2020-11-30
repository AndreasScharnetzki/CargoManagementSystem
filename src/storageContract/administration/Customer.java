package storageContract.administration;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;

public interface Customer extends Serializable {
    String getName();
    BigDecimal getMaxValue();
    Duration getMaxDurationOfStorage();
}
