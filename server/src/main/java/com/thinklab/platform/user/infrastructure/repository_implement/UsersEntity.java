package com.thinklab.platform.user.infrastructure.repository_implement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@AllArgsConstructor
@Document(collection = "user")
public class UsersEntity {
    @Indexed
    private final UUID id;
    @Indexed(unique = true)
    private String email;
    private String username;
    private String hashedPassword;
    private LocalDateTime createdAt;
    
}
