package org.cdtu.website.entity.dto;

import lombok.Data;

@Data
public class PasswordDto {
    String old_pwd;
    String new_pwd;
    String re_pwd;
}
