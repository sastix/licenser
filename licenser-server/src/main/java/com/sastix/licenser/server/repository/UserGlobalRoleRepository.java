package com.sastix.licenser.server.repository;

import com.sastix.licenser.server.model.UserGlobalRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

@Transactional
public interface UserGlobalRoleRepository extends CrudRepository<UserGlobalRole, Integer> {

    UserGlobalRole getByUserId(@Param("user_id") long userId);
}
