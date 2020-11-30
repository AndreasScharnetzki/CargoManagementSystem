package storageContract.administration;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerImplTest {

    @Test
    void getName() {
        final String expected = "A";
        Customer customer = new CustomerImpl(expected);

        assertEquals(expected, customer.getName());
    }

    @Test
    void testToString() {
        final String expected = "A";
        Customer customer = new CustomerImpl(expected);

        assertEquals(expected, customer.toString());
    }

    @Test
    void getMaxValue() {
        Customer customer = new CustomerImpl("Ronnie");
        assertEquals(BigDecimal.ZERO,customer.getMaxValue());
    }

    @Test
    void getMaxDurationOfStorage() {
        Customer customer = new CustomerImpl("Ronnie");
        assertEquals(Duration.ZERO,customer.getMaxDurationOfStorage());
    }



    @Test
    void badTest_customerConstructor_emptyArgumentOnCreate() {
        assertThrows(IllegalArgumentException.class, () -> {
            Customer designedToFail = new CustomerImpl("");
        });
    }

    @Test
    void badTest_customerConstructor_nullArgumentOnCreate() {
        assertThrows(IllegalArgumentException.class, () -> {
            Customer designedToFail = new CustomerImpl(null);
        });
    }

    @Test
    void badTest_customerConstructor_separated_name() {
        String twoLetterWord = "a a";
        assertThrows(IllegalArgumentException.class, () -> {
            Customer designedToFail = new CustomerImpl(twoLetterWord);
        });
    }

    @Test
    void badTest_customerConstructor_invalidCharset_numbers() {
        String numbersForNames = null;
        for (int i = -1000; i < 1000; i++) {
            String.valueOf(i);
            assertThrows(IllegalArgumentException.class, () -> {
                Customer designedToFail = new CustomerImpl(numbersForNames);
            });
        }
    }

    @Test
    void badTest_customerConstructor_invalidCharset_Chars_with_numbers() {
        String charAndInt = "a1";
        assertThrows(IllegalArgumentException.class, () -> {
            Customer designedToFail = new CustomerImpl(charAndInt);
        });
    }

    @Test
    void badTest_customerConstructor_CustomerName_equals_exit() {
        String invalidName = "exit";
        assertThrows(IllegalArgumentException.class, () -> {
            Customer designedToFail = new CustomerImpl(invalidName);
        });
    }

    @Test
    void badTest_customerConstructor_inputLengthTooLong() {
        String toolong = "badTestcustomerConstructorinvalidCharsetCharswithnumbersbadTestcustomerConstructorinvalidCharset";
        assertThrows(IllegalArgumentException.class, () -> {
            Customer designedToFail = new CustomerImpl(toolong);
        });
    }

}