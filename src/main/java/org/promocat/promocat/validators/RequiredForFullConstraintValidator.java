package org.promocat.promocat.validators;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.constraints.RequiredForFull;
import org.promocat.promocat.dto.AbstractAccountDTO;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class RequiredForFullConstraintValidator {

    public static boolean check(final AbstractAccountDTO accountDTO) {
        Class<? extends AbstractAccountDTO> accountClass = accountDTO.getClass();
        for (Field field : accountClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(RequiredForFull.class)) {
                try {
                    String fieldName = field.getName();
                    String getterName = "get" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
                    Object value = accountClass.getMethod(getterName).invoke(accountDTO);
                    if (value == null) {
                        return false;
                    }
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    log.error("Error while getting value by field. (Reflection API)", e);
                }
            }
        }
        return true;
    }

}
