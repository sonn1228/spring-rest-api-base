    package com.sonnguyen.base.model;

    import jakarta.persistence.*;
    import lombok.Data;
    import lombok.Getter;
    import lombok.Setter;

    import java.util.Set;

    @Getter
    @Setter
    @Entity
    @Table(name = "roles")
    public class Role {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private String id;
        private String name;
        private String description;

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "role_permission",
                joinColumns = @JoinColumn(name = "role_id"),
                inverseJoinColumns = @JoinColumn(name = "permission_id")
        )
        private Set<Permission> permissions;
    }
