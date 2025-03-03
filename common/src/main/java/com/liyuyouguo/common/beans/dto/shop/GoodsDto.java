package com.liyuyouguo.common.beans.dto.shop;

import com.liyuyouguo.common.beans.vo.GalleryDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author baijianmin
 */
@Data
public class GoodsDto {

    private Integer id;

    private Integer categoryId;

    private Boolean isOnSale;

    private String name;

    private Integer goodsNumber;

    private Integer sellVolume;

    private String keywords;

    private String retailPrice;

    private BigDecimal minRetailPrice;

    private String costPrice;

    private BigDecimal minCostPrice;

    private String goodsBrief;

    private String goodsDesc;

    private Integer sortOrder;

    private Boolean isIndex;

    private Boolean isNew;

    private String goodsUnit;

    private String httpsPicUrl;

    private String listPicUrl;

    private Integer freightTemplateId;

    private Integer freightType;

    private Boolean isDelete;

    private Boolean hasGallery;

    private Boolean hasDone;

    private String categoryName;

    private List<GalleryDto> gallery;

}
