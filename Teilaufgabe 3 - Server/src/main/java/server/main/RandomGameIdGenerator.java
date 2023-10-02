package server.main;

import messagesBase.UniqueGameIdentifier;

//TAKEN FROM START https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/
//This simple algorithm generates a string of a specific length and was inspired from the website

public class RandomGameIdGenerator {

    public static UniqueGameIdentifier generateRandomGameId(){
        return new UniqueGameIdentifier(getRandomString());
    }

    private static String getRandomString(){
        String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        int stringLength = 5;

        StringBuilder sb = new StringBuilder(stringLength);

        for (int i = 0; i < stringLength; i++) {
            int index = (int)(alphaNumericString.length() * Math.random());
            sb.append(alphaNumericString.charAt(index));
        }
        return sb.toString();
    }

}

//TAKEN FROM END
