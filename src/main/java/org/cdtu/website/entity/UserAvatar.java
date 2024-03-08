package org.cdtu.website.entity;

import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(value = "user_avatar")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAvatar {
    private Long userId;
    private String avatarUrl;
}
