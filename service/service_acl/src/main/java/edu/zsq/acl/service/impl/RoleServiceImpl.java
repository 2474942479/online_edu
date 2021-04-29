package edu.zsq.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.zsq.acl.entity.Role;
import edu.zsq.acl.entity.RolePermission;
import edu.zsq.acl.entity.UserRole;
import edu.zsq.acl.entity.dto.RoleQueryDTO;
import edu.zsq.acl.entity.vo.RoleVO;
import edu.zsq.acl.mapper.RoleMapper;
import edu.zsq.acl.service.RolePermissionService;
import edu.zsq.acl.service.RoleService;
import edu.zsq.acl.service.UserRoleService;
import edu.zsq.utils.page.PageData;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zsq
 * @since 2020-08-29
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RolePermissionService rolePermissionService;

    @Resource
    private UserRoleService userRoleService;

    @Override
    public boolean setRolePermission(String rid, String[] permissionIds) {

        ArrayList<RolePermission> rolePermissions = new ArrayList<>();

        for (String permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(rid);
            rolePermission.setPermissionId(permissionId);
            rolePermissions.add(rolePermission);
        }

        return rolePermissionService.saveBatch(rolePermissions);
    }

    @Override
    public List<Role> selectRoleByUserId(String id) {
        //根据用户id拥有的角色id
        List<UserRole> userRoleList = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id", id).select("role_id"));
        List<String> roleIdList = userRoleList.stream().map(item -> item.getRoleId()).collect(Collectors.toList());
        List<Role> roleList = new ArrayList<>();
        if(roleIdList.size() > 0) {
            roleList = baseMapper.selectBatchIds(roleIdList);
        }
        return roleList;
    }

    @Override
    public Map<String, Object> findRoleByUserId(String userId) {
        //查询所有的角色
        List<Role> allRolesList = baseMapper.selectList(null);

        //根据用户id，查询用户拥有的角色id
        List<UserRole> existUserRoleList = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id", userId).select("role_id"));

        List<String> existRoleList = existUserRoleList.stream().map(c -> c.getRoleId()).collect(Collectors.toList());

        //对角色进行分类
        List<Role> assignRoles = new ArrayList<Role>();
        for (Role role : allRolesList) {
            //已分配
            if (existRoleList.contains(role.getId())) {
                assignRoles.add(role);
            }
        }

        Map<String, Object> roleMap = new HashMap<>(2);
        roleMap.put("assignRoles", assignRoles);
        roleMap.put("allRolesList", allRolesList);
        return roleMap;
    }

    @Override
    public void saveUserRoleRealtionShip(String userId, String[] roleIds) {
        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", userId));

        List<UserRole> userRoleList = new ArrayList<>();
        for (String roleId : roleIds) {
            if (StringUtils.isEmpty(roleId)) {
                continue;
            }
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);

            userRoleList.add(userRole);
        }
        userRoleService.saveBatch(userRoleList);
    }

    @Override
    public PageData<RoleVO> pageRole(RoleQueryDTO roleQueryDTO) {
        Page<Role> page = new Page<>(roleQueryDTO.getCurrent(), roleQueryDTO.getSize());
        lambdaQuery()
                .like(StringUtils.isNotBlank(roleQueryDTO.getRoleName()), Role::getRoleName, roleQueryDTO.getRoleName())
                .orderByDesc(Role::getGmtModified)
                .page(page);

        if (page.getRecords().isEmpty()) {
            return PageData.empty();
        }

        List<RoleVO> collect = page.getRecords().stream().map(this::convert2RoleVO).collect(Collectors.toList());
        return PageData.of(collect, page.getCurrent(), page.getSize(), page.getTotal());
    }

    private RoleVO convert2RoleVO(Role role) {
        return RoleVO.builder()
                .id(role.getId())
                .roleCode(role.getRoleCode())
                .roleName(role.getRoleName())
                .remark(role.getRemark())
                .build();
    }
}
