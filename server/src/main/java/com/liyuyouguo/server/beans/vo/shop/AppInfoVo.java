package com.liyuyouguo.server.beans.vo.shop;

import com.liyuyouguo.server.entity.shop.Ad;
import com.liyuyouguo.server.entity.shop.Category;
import com.liyuyouguo.server.entity.shop.Notice;
import lombok.Data;

import java.util.List;

/**
 * 小程序信息
 *
 * @author baijianmin
 */
@Data
public class AppInfoVo {

    private List<Category> channel;

    private List<Ad> banner;

    private List<Notice> notice;

    private List<CategoryVo> categoryList;

    private Long cartCount;

}
