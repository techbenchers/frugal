package com.techbenchers.database;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.techbenchers.type.Blog;


public interface BlogRepository extends MongoRepository<Blog, String> {
}
