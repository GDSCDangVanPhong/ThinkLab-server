package com.thinklab.platform.user.infrastructure.repository_implement;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserRepositoryImplement {

    @Autowired
    private final MongoTemplate mongo;





}
