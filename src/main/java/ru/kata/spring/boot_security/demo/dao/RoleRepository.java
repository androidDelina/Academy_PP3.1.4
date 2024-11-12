package ru.kata.spring.boot_security.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kata.spring.boot_security.demo.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("SELECT role FROM Role role WHERE role.role = :roleName")
    Role findByName(@Param("roleName") String roleName);

}
