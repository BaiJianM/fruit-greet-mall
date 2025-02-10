package com.liyuyouguo.common.beans.vo;

import com.liyuyouguo.common.entity.shop.Settings;
import com.liyuyouguo.common.entity.shop.Shipper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author baijianmin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipperIndexVo {

    private List<Shipper> info;

    private Settings set;

}
