package com.liyuyouguo.server.beans;

import com.liyuyouguo.server.beans.vo.UserInfoVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 用户信息
 *
 * @author baijianmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "用户id")
public class UserInfo extends UserInfoVo implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = -923240651105918554L;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "微信昵称")
    private String wxNick;

    @Schema(description = "token令牌")
    private String token;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (StringUtils.isNotBlank(super.roleCode)) {
            authorities.add(() -> super.roleCode);
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return super.isEnable || !super.isDelete;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return super.isEnable != null && super.isEnable;
    }

    public void cleanPassword() {
        this.password = null;
    }
}
