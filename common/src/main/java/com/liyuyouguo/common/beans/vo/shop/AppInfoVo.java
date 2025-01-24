package com.liyuyouguo.common.beans.vo.shop;

import com.liyuyouguo.common.entity.shop.Ad;
import com.liyuyouguo.common.entity.shop.Category;
import com.liyuyouguo.common.entity.shop.Notice;
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
