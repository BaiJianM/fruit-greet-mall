package com.liyuyouguo.admin.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liyuyouguo.common.beans.dto.shop.AdminAddDto;
import com.liyuyouguo.common.beans.dto.shop.AdminSaveDto;
import com.liyuyouguo.common.beans.dto.shop.SettingsDto;
import com.liyuyouguo.common.beans.dto.shop.ShowSettingsDto;
import com.liyuyouguo.common.beans.vo.AdminVo;
import com.liyuyouguo.common.commons.FruitGreetError;
import com.liyuyouguo.common.commons.FruitGreetException;
import com.liyuyouguo.common.entity.shop.Admin;
import com.liyuyouguo.common.entity.shop.Settings;
import com.liyuyouguo.common.entity.shop.ShowSettings;
import com.liyuyouguo.common.mapper.AdminMapper;
import com.liyuyouguo.common.mapper.SettingsMapper;
import com.liyuyouguo.common.mapper.ShowSettingsMapper;
import com.liyuyouguo.common.utils.ConvertUtils;
import com.liyuyouguo.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminMapper adminMapper;

    private final SettingsMapper settingsMapper;

    private final ShowSettingsMapper showSettingsMapper;

    private final PasswordEncoder passwordEncoder;

    public List<AdminVo> index() {
        List<Admin> data = adminMapper.selectList(Wrappers.lambdaQuery(Admin.class)
                .eq(Admin::getIsDelete, 0));
        return (List<AdminVo>) ConvertUtils.convertCollection(data, AdminVo::new, (s, t) -> {
            if (s.getLastLoginTime() != null) {
                t.setLastLoginTime(DateUtils.parseTime(s.getLastLoginTime()));
            } else {
                t.setLastLoginTime("还没登录过");
            }
        }).orElseThrow();
    }

    public Admin adminDetail(Integer id) {
        return adminMapper.selectOne(Wrappers.lambdaQuery(Admin.class)
                .eq(Admin::getId, id));
    }

    public void adminAdd(AdminAddDto adminAddDto) {
        String password = adminAddDto.getPassword();

        // 构造要插入的数据
        Admin upData = new Admin();
        upData.setUsername(adminAddDto.getUsername());

        // 处理密码
        if (password != null && !password.trim().isEmpty()) {
            upData.setPassword(passwordEncoder.encode(password));
        }

        // 插入数据库
        adminMapper.insert(upData);
    }

    public void adminSave(AdminSaveDto adminSaveDto, boolean change) {
        // 查询是否存在重名用户（排除当前ID）
        Admin existingAdmin = adminMapper.selectOne(Wrappers.lambdaQuery(Admin.class)
                .eq(Admin::getUsername, adminSaveDto.getUsername())
                .ne(Admin::getId, adminSaveDto.getId()));

        if (existingAdmin != null) {
            throw new FruitGreetException(FruitGreetError.NAME_EXIST);
        }

        // 构造更新数据
        Admin upData = new Admin();
        upData.setUsername(adminSaveDto.getUsername());

        if (change) {
            String newPassword = adminSaveDto.getNewPassword();
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                upData.setPassword(passwordEncoder.encode(newPassword));
            }
        }
        // 更新数据库
        adminMapper.update(upData, new LambdaUpdateWrapper<Admin>().eq(Admin::getId, adminSaveDto.getId()));
    }

    public Admin info(Long id) {
        return adminMapper.selectById(id);
    }

    public void deleteAdmin(Long id) {
        adminMapper.deleteById(id);
    }

    public ShowSettings showSet() {
        return showSettingsMapper.selectOne(Wrappers.lambdaQuery(ShowSettings.class).last("LIMIT 1"));
    }

    public ShowSettings showSetStore(ShowSettingsDto showSettingsDto) {
        ShowSettings showSettings = ConvertUtils.convert(showSettingsDto, ShowSettings::new).orElseThrow();
        showSettingsMapper.update(showSettings, Wrappers.lambdaUpdate(ShowSettings.class)
                .eq(ShowSettings::getId, 1));
        return showSettings;
    }

    public void changeAutoStatus(Boolean status) {
        Settings updateData = new Settings();
        updateData.setAutoDelivery(status ? 1 : 0);

        settingsMapper.update(updateData, Wrappers.lambdaUpdate(Settings.class)
                .eq(Settings::getId, 1));
    }

    public void storeShipperSettings(SettingsDto settingsDto) {
        Settings settings = ConvertUtils.convert(settingsDto, Settings::new).orElseThrow();
        settingsMapper.updateById(settings);
    }

    public Settings senderInfo() {
        return settingsMapper.selectOne(Wrappers.lambdaQuery(Settings.class)
                .eq(Settings::getId, 1)
                .last("LIMIT 1"));
    }
}
