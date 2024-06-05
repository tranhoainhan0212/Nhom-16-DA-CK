package com.buoi1.webbanhang.UserRepository;

import com.buoi1.webbanhang.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IRoleRepository extends JpaRepository<Role, Long>{
    Role findRoleById(Long id);
}