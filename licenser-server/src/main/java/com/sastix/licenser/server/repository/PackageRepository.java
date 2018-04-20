package com.sastix.licenser.server.repository;

import com.sastix.licenser.server.model.Package;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface PackageRepository extends CrudRepository<Package, Integer> {
}
