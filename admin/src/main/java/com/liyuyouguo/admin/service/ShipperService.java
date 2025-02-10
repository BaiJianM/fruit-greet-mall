package com.liyuyouguo.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.dto.shop.*;
import com.liyuyouguo.common.beans.vo.FreightDetailVo;
import com.liyuyouguo.common.beans.vo.FreightSaveTableDto;
import com.liyuyouguo.common.beans.vo.FreightTemplateGroupVo;
import com.liyuyouguo.common.beans.vo.ShipperIndexVo;
import com.liyuyouguo.common.entity.shop.*;
import com.liyuyouguo.common.mapper.*;
import com.liyuyouguo.common.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShipperService {

    private final ShipperMapper shipperMapper;

    private final SettingsMapper settingsMapper;

    private final FreightTemplateMapper freightTemplateMapper;

    private final FreightTemplateGroupMapper freightTemplateGroupMapper;

    private final FreightTemplateDetailMapper freightTemplateDetailMapper;

    private final RegionMapper regionMapper;

    private final ExceptAreaMapper exceptAreaMapper;

    private final ExceptAreaDetailMapper exceptAreaDetailMapper;

    public ShipperIndexVo getShipperIndexData() {
        // 查询启用的 Shipper
        List<Shipper> shippers = shipperMapper.selectList(Wrappers.lambdaQuery(Shipper.class)
                .eq(Shipper::getEnabled, 1));

        // 查询 Settings 配置
        Settings settings = settingsMapper.selectById(1);

        return new ShipperIndexVo(shippers, settings);
    }

    public List<Shipper> getEnabledShippers() {
        return shipperMapper.selectList(Wrappers.lambdaQuery(Shipper.class)
                .eq(Shipper::getEnabled, 1));
    }

    public PageResult<Shipper> getShipperList(ShipperListDto dto) {
        LambdaQueryWrapper<Shipper> query = Wrappers.lambdaQuery(Shipper.class)
                .like(Shipper::getName, "%" + dto.getName() + "%")
                .orderByAsc(Shipper::getSortOrder);
        Page<Shipper> shipperPage = shipperMapper.selectPage(new Page<>(dto.getPage(), dto.getSize()), query);
        return ConvertUtils.convert(shipperPage, PageResult<Shipper>::new).orElseThrow();
    }

    public void updateStatus(ShipperUpdateDto dto) {
        shipperMapper.update(
                Wrappers.lambdaUpdate(Shipper.class)
                        .set(Shipper::getEnabled, dto.getStatus() ? 1 : 0)
                        .eq(Shipper::getId, dto.getId())
        );
    }

    public void updateSort(ShipperUpdateDto dto) {
        shipperMapper.update(
                Wrappers.lambdaUpdate(Shipper.class)
                        .set(Shipper::getSortOrder, dto.getSort())
                        .eq(Shipper::getId, dto.getId())
        );
    }

    public Shipper getShipperInfo(Integer id) {
        return shipperMapper.selectById(id);
    }

    public void store(ShipperStoreDto dto) {
        if (dto.getId() != null && dto.getId() > 0) {
            shipperMapper.updateById(ConvertUtils.convert(dto, Shipper::new).orElseThrow());
        } else {
            shipperMapper.insert(ConvertUtils.convert(dto, Shipper::new, (s, t) -> t.setId(null)).orElseThrow());
        }
    }

    public void destory(ShipperDestoryDto dto) {
        shipperMapper.deleteById(dto.getId());
    }

    public List<FreightTemplate> freight() {
        return freightTemplateMapper.selectList(Wrappers.lambdaQuery(FreightTemplate.class).eq(FreightTemplate::getIsDelete, 0));
    }

    public List<Region> getareadata() {
        return regionMapper.selectList(Wrappers.lambdaQuery(Region.class).eq(Region::getType, 1));
    }

    public FreightDetailVo getFreightDetail(FreightDetailDto dto) {
        Integer templateId = dto.getId();

        // 获取非默认的运费规则
        List<FreightTemplateGroup> groups = freightTemplateGroupMapper.selectList(
                Wrappers.lambdaQuery(FreightTemplateGroup.class)
                        .eq(FreightTemplateGroup::getTemplateId, templateId)
                        .eq(FreightTemplateGroup::getIsDelete, 0)
                        .ne(FreightTemplateGroup::getArea, 0)
        );

        List<FreightTemplateGroupVo> freightTemplateGroups =
                (List<FreightTemplateGroupVo>) ConvertUtils.convertCollection(groups, FreightTemplateGroupVo::new, (s, t) -> {
                    if (s.getFreeByMoney().compareTo(BigDecimal.ZERO) > 0) {
                        t.setFreeByMoney(false);
                    }
                    if (s.getFreeByNumber() > 0) {
                        t.setFreeByNumber(false);
                    }
                    List<String> areaList = Arrays.asList(s.getArea().split(","));
                    String areaNames = regionMapper.selectList(
                            Wrappers.lambdaQuery(Region.class)
                                    .in(Region::getId, areaList)
                    ).stream().map(Region::getName).collect(Collectors.joining(","));
                    t.setAreaName(areaNames);
                }).orElseThrow();

        // 获取默认的运费规则
        List<FreightTemplateGroup> defaultGroups = freightTemplateGroupMapper.selectList(
                Wrappers.lambdaQuery(FreightTemplateGroup.class)
                        .eq(FreightTemplateGroup::getTemplateId, templateId)
                        .eq(FreightTemplateGroup::getIsDelete, 0)
                        .eq(FreightTemplateGroup::getArea, 0));

        // 获取运费模板信息
        FreightTemplate freightTemplate = freightTemplateMapper.selectById(templateId);

        return new FreightDetailVo(freightTemplate, freightTemplateGroups, defaultGroups);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveTable(FreightSaveTableDto dto) {
        List<FreightTemplateGroup> data = dto.getTable();
        List<FreightTemplateGroup> defaultData = dto.getDefaultData();
        FreightTemplate info = dto.getInfo();

        List<Integer> existingIds = data.stream()
                .map(FreightTemplateGroup::getId)
                .filter(id -> id > 0)
                .toList();

        if (!existingIds.isEmpty()) {
            // 逻辑1：删除 `freight_template_group` 中不在 `existingIds` 里的数据
            List<Integer> deleteGroupIds = freightTemplateGroupMapper.selectList(
                            Wrappers.lambdaQuery(FreightTemplateGroup.class)
                                    .notIn(FreightTemplateGroup::getId, existingIds)
                                    .eq(FreightTemplateGroup::getTemplateId, info.getId())
                                    .eq(FreightTemplateGroup::getIsDefault, 0)
                                    .eq(FreightTemplateGroup::getIsDelete, 0))
                    .stream().map(FreightTemplateGroup::getId).toList();

            // 逻辑2：逻辑删除 `freight_template_detail`

            if (!deleteGroupIds.isEmpty()) {
                freightTemplateDetailMapper.update(Wrappers.lambdaUpdate(FreightTemplateDetail.class)
                        .in(FreightTemplateDetail::getGroupId, deleteGroupIds)
                        .eq(FreightTemplateDetail::getTemplateId, info.getId())
                        .eq(FreightTemplateDetail::getIsDelete, 0)
                        .set(FreightTemplateDetail::getIsDelete, 1)
                );

                // 逻辑3：逻辑删除 `freight_template_group`
                freightTemplateGroupMapper.update(Wrappers.lambdaUpdate(FreightTemplateGroup.class)
                        .notIn(FreightTemplateGroup::getId, deleteGroupIds)
                        .eq(FreightTemplateGroup::getTemplateId, info.getId())
                        .eq(FreightTemplateGroup::getIsDefault, 0)
                        .eq(FreightTemplateGroup::getIsDelete, 0)
                        .set(FreightTemplateGroup::getIsDelete, 1)
                );
            }

            // 逻辑4：更新和新增 `freight_template_group`
            for (FreightTemplateGroup item : data) {
                Integer groupId = item.getId();
                if (groupId > 0) {
                    freightTemplateGroupMapper.update(Wrappers.lambdaUpdate(FreightTemplateGroup.class)
                            .eq(FreightTemplateGroup::getId, groupId)
                            .eq(FreightTemplateGroup::getTemplateId, info.getId())
                            .eq(FreightTemplateGroup::getIsDelete, 0)
                            .set(FreightTemplateGroup::getArea, item.getArea())
                            .set(FreightTemplateGroup::getStart, item.getStart())
                            .set(FreightTemplateGroup::getStartFee, item.getStartFee())
                            .set(FreightTemplateGroup::getAdd, item.getAdd())
                            .set(FreightTemplateGroup::getAddFee, item.getAddFee())
                            .set(FreightTemplateGroup::getFreeByMoney, item.getFreeByMoney())
                            .set(FreightTemplateGroup::getFreeByNumber, item.getFreeByNumber())
                    );

                    List<String> areaList = Arrays.asList(item.getArea().split(","));

                    // 逻辑5：逻辑删除 `freight_template_detail` 中不在 `areaList` 里的数据
                    freightTemplateDetailMapper.update(Wrappers.lambdaUpdate(FreightTemplateDetail.class)
                            .notIn(FreightTemplateDetail::getArea, areaList)
                            .eq(FreightTemplateDetail::getTemplateId, info.getId())
                            .eq(FreightTemplateDetail::getGroupId, groupId)
                            .set(FreightTemplateDetail::getIsDelete, 1)
                    );

                    // 逻辑6：新增 `freight_template_detail`
                    for (String area : areaList) {
                        long count = freightTemplateDetailMapper.selectCount(
                                Wrappers.lambdaQuery(FreightTemplateDetail.class)
                                        .eq(FreightTemplateDetail::getTemplateId, info.getId())
                                        .eq(FreightTemplateDetail::getGroupId, groupId)
                                        .eq(FreightTemplateDetail::getArea, area)
                        );
                        if (count == 0) {
                            FreightTemplateDetail detail = new FreightTemplateDetail();
                            detail.setTemplateId(info.getId());
                            detail.setGroupId(groupId);
                            detail.setArea(area);
                            freightTemplateDetailMapper.insert(detail);
                        }
                    }
                } else {
                    // 逻辑7：新增 `freight_template_group`
                    item.setTemplateId(info.getId());
                    item.setId(null);
                    item.setArea(item.getArea().substring(2));
                    freightTemplateGroupMapper.insert(item);
                    Integer newGroupId = item.getId();

                    // 逻辑8：新增 `freight_template_detail`
                    List<String> areaList = Arrays.asList(item.getArea().split(","));
                    List<FreightTemplateDetail> details = areaList.stream()
                            .map(area -> {
                                        FreightTemplateDetail detail = new FreightTemplateDetail();
                                        detail.setTemplateId(info.getId());
                                        detail.setGroupId(newGroupId);
                                        detail.setArea(area);
                                        return detail;
                                    }
                            ).toList();
                    details.forEach(freightTemplateDetailMapper::insert);
                }
            }
        } else {
            // 逻辑9：如果前端删除了所有 `table` 数据，则逻辑删除 `freight_template_group`
            freightTemplateGroupMapper.update(Wrappers.lambdaUpdate(FreightTemplateGroup.class)
                            .eq(FreightTemplateGroup::getTemplateId, info.getId())
                            .eq(FreightTemplateGroup::getIsDefault, 0)
                            .eq(FreightTemplateGroup::getIsDelete, 0)
                            .set(FreightTemplateGroup::getIsDelete, 1)
            );
            // TODO 有一个不明所以的操作未解决，在hioshop-server的admin/controller/shipper.js的281到307行
        }

        // 逻辑10：更新 `freight_template_group` 默认项
        FreightTemplateGroup def = defaultData.get(0);
        freightTemplateGroupMapper.update(Wrappers.lambdaUpdate(FreightTemplateGroup.class)
                        .eq(FreightTemplateGroup::getId, def.getId())
                        .eq(FreightTemplateGroup::getTemplateId, info.getId())
                        .eq(FreightTemplateGroup::getIsDefault, 1)
                        .set(FreightTemplateGroup::getStart, def.getStart())
                        .set(FreightTemplateGroup::getStartFee, def.getStartFee())
                        .set(FreightTemplateGroup::getAdd, def.getAdd())
                        .set(FreightTemplateGroup::getAddFee, def.getAddFee())
                        .set(FreightTemplateGroup::getFreeByMoney, def.getFreeByMoney())
                        .set(FreightTemplateGroup::getFreeByNumber, def.getFreeByNumber())
        );

        // 逻辑11：更新 `freight_template`
        freightTemplateMapper.updateById(info);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addTable(FreightAddTableDto dto) {
        FreightTemplate template = dto.getInfo();
        template.setId(null);
        freightTemplateMapper.insert(template);
        Integer templateId = template.getId();

        if (templateId > 0) {
            FreightTemplateGroup defaultGroup = dto.getDefaultData().get(0);
            defaultGroup.setTemplateId(templateId);
            defaultGroup.setIsDefault(1);
            defaultGroup.setId(null);
            freightTemplateGroupMapper.insert(defaultGroup);
            Integer defaultGroupId = defaultGroup.getId();

            if (defaultGroupId > 0) {
                FreightTemplateDetail detail = new FreightTemplateDetail();
                detail.setTemplateId(templateId);
                detail.setGroupId(defaultGroupId);
                detail.setArea("0");
                freightTemplateDetailMapper.insert(detail);
            }

            for (FreightTemplateGroup item : dto.getTable()) {
                String newArea = item.getArea().substring(2);
                item.setTemplateId(templateId);
                item.setArea(newArea);
                item.setId(null);
                freightTemplateGroupMapper.insert(item);
                Integer groupId = item.getId();

                List<String> areaList = Arrays.asList(item.getArea().split(","));
                List<FreightTemplateDetail> details = areaList.stream()
                        .map(area -> {
                                    FreightTemplateDetail detail = new FreightTemplateDetail();
                                    detail.setTemplateId(templateId);
                                    detail.setGroupId(groupId);
                                    detail.setArea(area);
                                    return detail;
                                }
                        ).toList();
                details.forEach(freightTemplateDetailMapper::insert);
            }
        }
    }

    public List<ExceptArea> exceptarea() {
        List<ExceptArea> list = exceptAreaMapper.selectList(Wrappers.lambdaQuery(ExceptArea.class)
                .eq(ExceptArea::getIsDelete, 0));

        list.forEach(item -> {
            if (item.getArea() != null) {
                List<String> regionIds = Arrays.asList(item.getArea().split(","));
                String areaNames = regionMapper.selectList(Wrappers.lambdaQuery(Region.class)
                                .in(Region::getId, regionIds))
                        .stream()
                        .map(Region::getName)
                        .collect(Collectors.joining(","));
                item.setArea(areaNames);
            }
        });

        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public void exceptAreaDelete(Integer id) {
        exceptAreaMapper.update(Wrappers.lambdaUpdate(ExceptArea.class)
                .eq(ExceptArea::getId, id)
                .set(ExceptArea::getIsDelete, 1));

        exceptAreaDetailMapper.update(Wrappers.lambdaUpdate(ExceptAreaDetail.class)
                .eq(ExceptAreaDetail::getExceptAreaId, id)
                .set(ExceptAreaDetail::getIsDelete, 1));
    }

    public ExceptArea getExceptAreaDetail(Integer id) {
        ExceptArea exceptArea = exceptAreaMapper.selectOne(
                Wrappers.lambdaQuery(ExceptArea.class)
                        .eq(ExceptArea::getId, id)
                        .eq(ExceptArea::getIsDelete, 0)
        );

        if (exceptArea == null) {
            return null;
        }

        String area = exceptArea.getArea();
        if (StringUtils.isNotBlank(area)) {
            List<String> areaIds = Arrays.asList(area.split(","));
            String areaNames = regionMapper.selectList(
                    Wrappers.lambdaQuery(Region.class)
                            .in(Region::getId, areaIds)
            ).stream().map(Region::getName).collect(Collectors.joining(","));

            exceptArea.setArea(areaNames);
        }

        return exceptArea;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveExceptArea(SaveExceptAreaDto dto) {
        // 更新 except_area 表
        exceptAreaMapper.update(Wrappers.lambdaUpdate(ExceptArea.class)
                .eq(ExceptArea::getId, dto.getInfo().getId())
                .set(ExceptArea::getArea, dto.getTable().get(0).getArea())
                .set(ExceptArea::getContent, dto.getInfo().getContent()));

        // 解析区域 ID
        List<String> areaIds = Arrays.asList(dto.getTable().get(0).getArea().split(","));

        // 逻辑删除 except_area_detail 中不在新列表中的数据
        exceptAreaDetailMapper.update(Wrappers.lambdaUpdate(ExceptAreaDetail.class)
                        .notIn(ExceptAreaDetail::getArea, areaIds)
                        .eq(ExceptAreaDetail::getExceptAreaId, dto.getInfo().getId())
                        .eq(ExceptAreaDetail::getIsDelete, 0)
                        .set(ExceptAreaDetail::getIsDelete, 1)
        );

        // 遍历新区域，添加不存在的记录
        for (String areaId : areaIds) {
            Long count = exceptAreaDetailMapper.selectCount(
                    Wrappers.lambdaQuery(ExceptAreaDetail.class)
                            .eq(ExceptAreaDetail::getExceptAreaId, dto.getInfo().getId())
                            .eq(ExceptAreaDetail::getArea, areaId)
                            .eq(ExceptAreaDetail::getIsDelete, 0)
            );

            if (count == 0) {
                ExceptAreaDetail detail = new ExceptAreaDetail();
                detail.setExceptAreaId(dto.getInfo().getId());
                detail.setArea(areaId);
                exceptAreaDetailMapper.insert(detail);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void addExceptArea(AddExceptAreaDto dto) {
        // 处理区域数据
        String area = dto.getTable().get(0).getArea().substring(2);
        List<String> areaList = Arrays.asList(area.split(","));

        // 新增 except_area 记录
        ExceptArea exceptArea = new ExceptArea();
        exceptArea.setArea(area);
        exceptArea.setContent(dto.getInfo().getContent());
        exceptAreaMapper.insert(exceptArea);
        Integer exceptAreaId = exceptArea.getId();

        // 批量插入 except_area_detail 记录
        List<ExceptAreaDetail> details = areaList.stream()
                .map(areaItem -> {
                    ExceptAreaDetail detail = new ExceptAreaDetail();
                    detail.setExceptAreaId(exceptAreaId);
                    detail.setArea(areaItem);
                    return detail;
                })
                .toList();

        details.forEach(exceptAreaDetailMapper::insert);
    }

}
