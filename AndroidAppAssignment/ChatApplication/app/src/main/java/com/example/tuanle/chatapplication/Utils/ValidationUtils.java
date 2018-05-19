package com.example.tuanle.chatapplication.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
    public static boolean isValidUser(final String username){
        // Username bat dau bang letter, bao gom letters va numbers, co the co underscores giua cac letters
        Pattern pattern;
        Matcher matcher;

        final String USERNAME_PATTERN = "^[A-Za-z][A-Za-z0-9]*(?:_[A-Za-z0-9]+)*$";
        pattern = Pattern.compile(USERNAME_PATTERN);
        matcher = pattern.matcher(username);

        return matcher.matches();
    }

    public static boolean isValidPassword(final String pwd){
        // Password gom 4-8 characters, bao gom letters va numbers
        Pattern pattern;
        Matcher matcher;

        final String PWD_PATTERN = "^[a-zA-Z0-9]{4,8}$";
        pattern = Pattern.compile(PWD_PATTERN);
        matcher = pattern.matcher(pwd);

        return matcher.matches();
    }

    public static int isValidCategories(final String cat_name, final String cat_des){
        Pattern catname_pattern;
        Pattern catdes_pattern;
        Matcher catdes_matcher;
        Matcher catname_matcher;

        final String CAT_PATTERN = "[A-Za-z]{4,}";
        final String DES_PATTERN = "[A-Za-z0-9]{10,}";
        catname_pattern = Pattern.compile(CAT_PATTERN);
        catname_matcher = catname_pattern.matcher(cat_name);
        catdes_pattern = Pattern.compile(DES_PATTERN);
        catdes_matcher = catdes_pattern.matcher(cat_des);

        if (catname_matcher.matches() && catdes_matcher.matches())
            return 1;
        else
        {
            if (!catname_matcher.matches())
                return 2;
            else
                return 3;
        }
    }
}
