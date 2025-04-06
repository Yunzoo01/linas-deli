package com.linasdeli.api.repository;

import com.linasdeli.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> { // uid가 PK이므로 Integer 유지
    @Query("SELECT u FROM User u WHERE u.id = :id") // ✅ id 필드로 검색하는 쿼리
    Optional<User> findByIdCustom(@Param("id") String id);
}