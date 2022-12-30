package com.mindhub.homebanking.services;

import org.springframework.stereotype.Service;

@Service
public class UtilService {

    public int randomNumber(int max){
        return (int)(Math.random()*(max+1));
    }
    public int randomNumber(int max, int min){
        return (int)(Math.random()*(max+1-min))+min;
    }
}
