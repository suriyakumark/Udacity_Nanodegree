package com.simplesolutions2003.jokesFactory;

import java.util.Random;

public class JokesSupplier {

    public String getJoke(){
        //for testing, let's try to return null
        //return null;
        return generateJoke();
    }

    private String generateJoke(){
        Random rand = new Random();
        int randomNum = 1 + rand.nextInt((5 - 1) + 1);
        String joke = new String("");
        switch(randomNum){
            case 1 :
                joke = "Apparently I snore so loudly that it scares everyone in the car I'm driving.";
                break;
            case 2 :
                joke = "If you think nobody cares whether you're alive, try missing a couple of payments.";
                break;
            case 3 :
                joke = "Life is all about perspective. The sinking of the Titanic was a miracle to the lobsters in the ship's kitchen.";
                break;
            case 4 :
                joke = "I changed my password to \"incorrect\". So whenever I forget what it is the computer will say \"Your password is incorrect\".";
                break;
            case 5 :
                joke = "Today a man knocked on my door and asked for a small donation towards the local swimming pool. I gave him a glass of water. ";
                break;
        }
        return joke + " (source:www.onelinefun.com)";
    }
}
