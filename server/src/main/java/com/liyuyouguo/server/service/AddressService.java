package com.liyuyouguo.server.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuyouguo.common.beans.dto.shop.AddressSaveDto;
import com.liyuyouguo.common.beans.vo.AddressVo;
import com.liyuyouguo.common.beans.vo.interfaces.IAddress;
import com.liyuyouguo.common.entity.shop.Address;
import com.liyuyouguo.common.entity.shop.Region;
import com.liyuyouguo.common.mapper.AddressMapper;
import com.liyuyouguo.common.mapper.RegionMapper;
import com.liyuyouguo.common.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService extends ServiceImpl<AddressMapper, Address> implements IService<Address> {

    private final AddressMapper addressMapper;

    private final RegionMapper regionMapper;

    /**
     * 获取收货地址
     *
     * @return List<AddressVo> 收货地址信息
     */
    public List<AddressVo> getAddresses() {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        List<Address> addressList = addressMapper.selectList(Wrappers.lambdaQuery(Address.class)
                .eq(Address::getUserId, userId)
                .eq(Address::getIsDelete, 0)
                .orderByDesc(Address::getId));
        List<AddressVo> addresses = (List<AddressVo>) ConvertUtils.convertCollection(addressList, AddressVo::new).orElseThrow();
        addresses.forEach(this::setAddressInfo);
        return addresses;
    }

    /**
     * 收货地址信息赋值名称
     *
     * @param a 收货地址信息
     */
    public void setAddressInfo(IAddress address) {
        String provinceName = this.getRegionName(address.getProvinceId());
        String cityName = this.getRegionName(address.getCityId());
        String districtName = this.getRegionName(address.getDistrictId());
        address.setProvinceName(provinceName);
        address.setCityName(cityName);
        address.setDistrictName(districtName);
        address.setFullRegion(provinceName + cityName + districtName);
    }

    /**
     * 获取区域名称（存在多个取第一条）
     *
     * @param regionId 区域id
     * @return String 区域名称
     */
    private String getRegionName(Integer regionId) {
        return regionMapper.selectList(Wrappers.lambdaQuery(Region.class).eq(Region::getId, regionId)).get(0).getName();
    }

    /**
     * 保存收货地址
     *
     * @param dto 收货地址信息传参
     * @return Address 收货地址信息
     */
    public Address saveAddress(AddressSaveDto dto) {
        // TODO 这里少一个从token获取登录人id的操作
        Integer userId = 1048;
        Integer addressId = dto.getAddressId();
        Address address;
        if (addressId != null && addressId > 0) {
            address = ConvertUtils.convert(dto, () -> addressMapper.selectById(addressId)).orElseThrow();
        } else {
            address = ConvertUtils.convert(dto, Address::new).orElseThrow();
        }
        this.saveOrUpdate(address);
        // 如果设置为默认，则取消其它的默认
        if (dto.getIsDefault()) {
            addressMapper.update(Wrappers.lambdaUpdate(Address.class)
                    .set(Address::getIsDefault, 0)
                    .eq(Address::getUserId, userId)
                    .ne(Address::getId, addressId));
        }
        return address;
    }

    /**
     * 删除收货地址
     *
     * @param addressId 收货地址id
     * @return Integer 影响行数
     */
    public Integer deleteAddress(Integer addressId) {
        return addressMapper.update(Wrappers.lambdaUpdate(Address.class)
                .set(Address::getIsDelete, 1)
                .eq(Address::getId, addressId));
    }

    /**
     * 获取收货地址详情
     *
     * @param addressId 收货地址id
     * @return AddressVo 收货地址详情信息
     */
    public AddressVo addressDetail(Integer addressId) {
        Address address = addressMapper.selectById(addressId);
        AddressVo addressVo = null;
        if (address != null) {
            addressVo = ConvertUtils.convert(address, AddressVo::new).orElseThrow();
            this.setAddressInfo(addressVo);
        }
        return addressVo;
    }
}
