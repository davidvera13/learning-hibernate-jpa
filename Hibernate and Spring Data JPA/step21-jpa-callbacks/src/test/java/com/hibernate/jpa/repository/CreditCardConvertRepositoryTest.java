package com.hibernate.jpa.repository;

import com.hibernate.jpa.domain.CreditCard;
import com.hibernate.jpa.domain.CreditCardConvert;
import com.hibernate.jpa.service.EncryptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditCardConvertRepositoryTest {
    final String CREDIT_CARD = "123467891114";
    @Autowired
    CreditCardConvertRepository creditCardConvertRepository;
    @Autowired
    EncryptionService encryptionService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void saveAndStoreCreditCard() {
        CreditCardConvert creditCard = new CreditCardConvert();
        creditCard.setCreditCardNumber(CREDIT_CARD);
        creditCard.setCvv("132");
        creditCard.setExpirationDate("12/2028");

        CreditCardConvert savedCreditCard = creditCardConvertRepository.save(creditCard);
        // CreditCard savedCreditCard = creditCardRepository.saveAndFlush(creditCard);

        System.out.println("Getting CC from database");
        CreditCardConvert storedCreditCard = creditCardConvertRepository.findById(savedCreditCard.getId()).orElseThrow();

        assertThat(savedCreditCard.getCreditCardNumber()).isEqualTo(storedCreditCard.getCreditCardNumber());
    }

    @Test
    void saveAndStoreCreditCardAddEncryption() {
        CreditCardConvert creditCard = new CreditCardConvert();
        creditCard.setCreditCardNumber(CREDIT_CARD);
        creditCard.setCvv("132");
        creditCard.setExpirationDate("12/2028");

        // will display: >> onSave() called
        // CreditCard savedCreditCard = creditCardRepository.save(creditCard);
        // will display:
        CreditCardConvert savedCreditCard = creditCardConvertRepository.saveAndFlush(creditCard);

        System.out.println("Getting CC from database: " + savedCreditCard.getCreditCardNumber());
        System.out.println("CC At Rest");
        System.out.println("CC Encrypted: " + encryptionService.encrypt(CREDIT_CARD));
        Map<String, Object> dbRow = jdbcTemplate.queryForMap("SELECT * FROM credit_card_convert WHERE id = " +  savedCreditCard.getId());
        String dbCardValue = (String) dbRow.get("credit_card_number");

        assertThat(savedCreditCard.getCreditCardNumber()).isNotEqualTo(dbCardValue);
        assertThat(dbCardValue).isEqualTo(encryptionService.encrypt(CREDIT_CARD));

        CreditCardConvert storedCreditCard = creditCardConvertRepository.findById(savedCreditCard.getId()).orElseThrow();

        assertThat(savedCreditCard.getCreditCardNumber()).isEqualTo(storedCreditCard.getCreditCardNumber());
    }

}