package fun.vyse.cloud.validation;

import fun.vyse.cloud.test.dto.ModelReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * ValidatorTest
 *
 * @author junchen
 * @date 2019-12-17 22:39
 */
@Slf4j
public class ValidatorTest {

    private Validator validator;

    @BeforeTest
    public void init() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void test() {
        ModelReqDTO modelReqDto = new ModelReqDTO();
        modelReqDto.setTenantId("123456");
        log.debug("validate ...");
        Set<ConstraintViolation<ModelReqDTO>> validate = validator.validate(modelReqDto);
        for (ConstraintViolation violation : validate) {
            log.debug("message:{}", violation);
        }
    }
}
