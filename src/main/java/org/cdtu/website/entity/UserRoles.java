package org.cdtu.website.entity;

import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("user_roles")
public class UserRoles {
    private Long userId;
    private Long roleId;
}
