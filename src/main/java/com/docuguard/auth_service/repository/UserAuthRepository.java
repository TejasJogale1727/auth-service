package com.docuguard.auth_service.repository;

import com.docuguard.auth_service.entities.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuth, String> {
}
