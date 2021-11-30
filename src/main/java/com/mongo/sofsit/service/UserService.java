package com.mongo.sofsit.service;
import com.mongo.sofsit.model.User;
import org.bson.types.ObjectId;

public interface UserService {


    public User findById(ObjectId id);

    public User deleteById(ObjectId id);

    public User saveUser(User user);

    public User updatePassword(User user);
}
