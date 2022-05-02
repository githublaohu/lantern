package com.lamp.lantern.service.action.login.utils;

import java.util.Random;

public class GenerateRandomVerifyCode {

    public static final String VERIFY_CODES = "1234567890";

    public static final String RANDOM_CODE = digitAndLetters();

    public static String digitAndLetters(){
        String allDigitAndLetters = "";
        for(int i = 0; i< 10; i++){
            allDigitAndLetters += String.valueOf(i);
        }

        char a_base = 'a';
        char A_base = 'A';

        for(int i = 0; i< 26; i++){
            allDigitAndLetters += String.valueOf(a_base + i);
            allDigitAndLetters += String.valueOf(A_base + i);
        }
        return allDigitAndLetters;
    }

    public static String generateVerifyCode(int verifySize){
        return generateVerifyCode(verifySize, VERIFY_CODES);
    }



    public static String generateVerifyCode(int verifySize, String sources){
        if(sources == null || sources.length() == 0){
            sources = VERIFY_CODES;
        }
        int codesLen = sources.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder verifyCode = new StringBuilder(verifySize);
        for(int i = 0; i < verifySize; i++){
            verifyCode.append(sources.charAt(rand.nextInt(codesLen-1)));
        }
        return verifyCode.toString();
    }

    public static String generateRandomPlaceholder(int randomLength){
        int codesLen = RANDOM_CODE.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder randomPlaceholder = new StringBuilder(randomLength);
        for(int i = 0; i < randomLength; i++){
            randomPlaceholder.append(RANDOM_CODE.charAt(rand.nextInt(codesLen-1)));
        }
        return randomPlaceholder.toString();


    }

    public static void main(String[] args) {
        System.out.println(generateVerifyCode(4));
    }

}

