package com.mycompany.authguard;

public class PhoneValidation {
    public static boolean validate(String phone) {
        return phone.matches("^0[678]\\d{8}$");
    }
}