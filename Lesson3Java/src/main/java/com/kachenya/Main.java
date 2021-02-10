package com.kachenya;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.*;

public class Main {
    private static final String HMAC_SHA512 = "HmacSHA512";

    public static String HMAC(String key,String message) throws NoSuchAlgorithmException, InvalidKeyException {
            Mac sha512Hmac;
            String result;
            final byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
            sha512Hmac = Mac.getInstance(HMAC_SHA512);
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
            sha512Hmac.init(keySpec);
            byte[] macData = sha512Hmac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            result = Base64.getEncoder().encodeToString(macData);
            return result.toString();
    }


    private static boolean incorateSteps(List<String> steps) {
        boolean isValid=true;
        if(steps.size()>=3){
            for (int i=1;i<steps.size()-1;i++) {
                for(int j=i+1;j<steps.size();j++){
                    if(steps.get(i).equals(steps.get(j))){
                        isValid=false;
                        break;
                    }
                }
                if(!isValid){
                    break;
                }
            }
        }else{
            isValid=false;
        }

        return isValid;
    }

    private static String game(List<String> steps, String userStep, String computerStep) {
        System.out.println("computer step: "+computerStep);
        System.out.println("user step: "+userStep);
        String result="";
        int computerIndex=steps.indexOf(computerStep);
        int userIndex=steps.indexOf(userStep);
        int winKey=steps.size()/2;

        if(userIndex!=computerIndex) {
            if (computerIndex - winKey >= 0) {
                if (userIndex >= computerIndex - winKey && userIndex < computerIndex) {
                    result = "computer win";
                }else{
                    result = "user win";
                }
            } else if (computerIndex - winKey < 0) {
                if(userIndex >= computerIndex && userIndex <= computerIndex+winKey){
                    result = "user win";
                }else {
                    result = "computer win";
                }
            }
        }else{
            result="win both";
        }
        return result;
    }

    private static String randomStep(List<String> steps) {
        Random random=new Random();
        int step=random.nextInt(steps.size()) ;
        return steps.get(step);
    }
    
    private static String  randomKey(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString() ;

    }

    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException {
        List<String> steps=new ArrayList<String>();
        for (String str : args) {
            steps.add(str);
        }

       if(incorateSteps(steps)){

           String key=randomKey();
           String computerStep=randomStep(steps);
           String codeCompucterStep=HMAC(key,computerStep);
           System.out.println("HMAC:");
           System.out.println(codeCompucterStep);
           String userStep=userChoise(steps);
           System.out.println(game(steps, userStep, computerStep));
           System.out.println("HMAC key: "+key);
       }

    }

    private static String userChoise(List<String> steps) {
        String userStep="";
        Scanner in = new Scanner(System.in);

        for (int i=0;i<steps.size();i++){
            System.out.println((i+1)+" - "+steps.get(i));
        }
        System.out.println("0 - exit");

        System.out.print("Enter your move: ");
        int choise = in.nextInt();
        if(choise==0){
            System.exit(0);
        }
        userStep=steps.get(choise-1);
        return userStep;
    }


}
