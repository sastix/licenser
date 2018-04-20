package com.sastix.licenser.server.repository;

import com.sastix.licenser.server.model.GlobalRole;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface GlobalRoleRepository extends CrudRepository<GlobalRole, Integer> {

}
