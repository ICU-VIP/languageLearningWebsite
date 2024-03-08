package org.cdtu.website.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Table(value = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    @Column(onInsertValue = "true")
    private Boolean enabled;
    @Column(onInsertValue = "true")
    private Boolean accountNonExpired;
    @Column(onInsertValue = "true")//账号是否过期
    private Boolean accountNonLocked;
    @Column(onInsertValue = "true")//账号是否被锁定
    private Boolean credentialsNonExpired; //凭证是否过期
    @RelationManyToMany(joinTable = "user_roles",
            selfField = "id",
            joinSelfColumn = "user_id",
            targetField = "id",
            joinTargetColumn = "role_id"
    )
    private List<Roles> roles;
    @Column(onInsertValue = "0")
    private Integer exp;
    private String avatar;
    @Column(onInsertValue = "30")
    private Integer reviewCnt;
}
