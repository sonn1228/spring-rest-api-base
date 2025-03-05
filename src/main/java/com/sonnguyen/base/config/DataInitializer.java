package com.sonnguyen.base.config;

import com.sonnguyen.base.model.Permission;
import com.sonnguyen.base.model.Role;
import com.sonnguyen.base.model.User;
import com.sonnguyen.base.repository.PermissionRepository;
import com.sonnguyen.base.repository.RoleRepository;
import com.sonnguyen.base.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository,
                           PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Tạo quyền
        Permission viewPermission = new Permission();
        viewPermission.setName("VIEW_USER");
        viewPermission.setDescription("Quyền xem danh sách người dùng");
        permissionRepository.save(viewPermission);

        Permission editPermission = new Permission();
        editPermission.setName("EDIT_USER");
        editPermission.setDescription("Quyền chỉnh sửa người dùng");
        permissionRepository.save(editPermission);

        // Tạo vai trò
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        adminRole.setDescription("Quản trị viên");
        adminRole.setPermissions(Set.of(viewPermission, editPermission));
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        userRole.setDescription("Người dùng bình thường");
        userRole.setPermissions(Set.of(viewPermission));
        roleRepository.save(userRole);

        // Tạo người dùng
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setRoles(Set.of(adminRole));
            userRepository.save(adminUser);
        }

        if (userRepository.findByUsername("user").isEmpty()) {
            User normalUser = new User();
            normalUser.setUsername("user");
            normalUser.setPassword(passwordEncoder.encode("user"));
            normalUser.setRoles(Set.of(userRole));
            userRepository.save(normalUser);
        }

        System.out.println("Dữ liệu demo đã được khởi tạo.");
    }
}
