package com.service.kookchild.domain.mission.exception;

public class MissionNotFoundException extends RuntimeException{
    public MissionNotFoundException(String message){
        super(message);
    }
}
