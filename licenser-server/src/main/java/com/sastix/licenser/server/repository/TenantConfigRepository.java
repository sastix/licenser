package com.sastix.licenser.server.repository;

import com.sastix.licenser.server.model.TenantConfig;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface TenantConfigRepository extends CrudRepository<TenantConfig, Integer> {

}
