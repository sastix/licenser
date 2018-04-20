package com.sastix.licenser.server.repository;

import com.sastix.licenser.server.model.MaxUsersByPackageGlobalRole;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface MaxUsersByPackageGlobalRoleRepository extends CrudRepository<MaxUsersByPackageGlobalRole, Integer> {

}
