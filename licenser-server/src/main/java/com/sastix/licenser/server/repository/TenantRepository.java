package com.sastix.licenser.server.repository;

import com.sastix.licenser.server.model.Tenant;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface TenantRepository extends CrudRepository<Tenant, Integer> {

}
