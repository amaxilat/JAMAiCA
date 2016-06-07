package eu.organicity.annotation.jamaica.www.utils;

import java.util.UUID;

/**
 * Created by katdel on 01-Jun-16.
 */
public class RandomStringGenerator {

    String uuid;

    public String getUuid(){
        this.uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }
}
