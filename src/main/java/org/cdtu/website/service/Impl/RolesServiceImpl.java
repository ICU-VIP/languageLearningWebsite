package org.cdtu.website.service.Impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.cdtu.website.entity.Roles;
import org.cdtu.website.mapper.RolesMapper;
import org.cdtu.website.service.RolesService;
import org.springframework.stereotype.Service;

@Service
public class RolesServiceImpl extends ServiceImpl<RolesMapper, Roles> implements RolesService {
}
