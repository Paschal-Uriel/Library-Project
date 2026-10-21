package com.Paschal.LibraryProject.repository;

import com.Paschal.LibraryProject.entity.LibraryUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<LibraryUser, Long> {

}
