package com.sonnguyen.base.repository;

import com.sonnguyen.base.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}
