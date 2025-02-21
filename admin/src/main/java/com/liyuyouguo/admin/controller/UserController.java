package com.liyuyouguo.admin.controller;

import com.lark.oapi.Client;
import com.lark.oapi.service.approval.v4.model.*;
import com.liyuyouguo.admin.service.AdminUserService;
import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.PageResult;
import com.liyuyouguo.common.beans.dto.shop.SaveAddressDto;
import com.liyuyouguo.common.beans.dto.shop.UpdateUserInfoDto;
import com.liyuyouguo.common.beans.dto.shop.UserStoreDto;
import com.liyuyouguo.common.beans.vo.OrderAdminVo;
import com.liyuyouguo.common.beans.vo.UserAddressVo;
import com.liyuyouguo.common.beans.vo.UserDataInfoVo;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.common.entity.shop.Cart;
import com.liyuyouguo.common.entity.shop.Footprint;
import com.liyuyouguo.common.entity.shop.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

/**
 * @author baijianmin
 */
@Slf4j
@FruitGreetController("/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public FruitGreetResponse<PageResult<User>> index(@RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "") String nickname) {
        return FruitGreetResponse.success(adminUserService.getUserList(page, size, nickname));
    }

    @GetMapping("/info")
    public FruitGreetResponse<User> info(@RequestParam Integer id) {
        return FruitGreetResponse.success(adminUserService.getUserInfo(id));
    }

    @GetMapping("/datainfo")
    public FruitGreetResponse<UserDataInfoVo> getUserDataInfo(@RequestParam Integer id) {
        return FruitGreetResponse.success(adminUserService.getUserDataInfo(id));
    }

    @GetMapping("/address")
    public FruitGreetResponse<PageResult<UserAddressVo>> getAddressList(@RequestParam("id") Integer id,
                                                                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return FruitGreetResponse.success(adminUserService.getAddressList(id, page, size));
    }

    @PostMapping("/saveaddress")
    public FruitGreetResponse<Void> saveAddress(@RequestBody SaveAddressDto dto) {
        adminUserService.saveUserAddress(dto);
        return FruitGreetResponse.success();
    }

    @GetMapping("/cartdata")
    public FruitGreetResponse<PageResult<Cart>> getCartData(@RequestParam("id") Integer id,
                                                            @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return FruitGreetResponse.success(adminUserService.getCartData(id, page, size));
    }

    @GetMapping("/foot")
    public FruitGreetResponse<PageResult<Footprint>> foot(@RequestParam Integer id,
                                                      @RequestParam(defaultValue = "1") Integer page,
                                                      @RequestParam(defaultValue = "10") Integer size) {
        return FruitGreetResponse.success(adminUserService.getFootprints(id, page, size));
    }

    @GetMapping("/order")
    public FruitGreetResponse<PageResult<OrderAdminVo>> orderAction(@RequestParam("id") Integer userId,
                                                                    @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return FruitGreetResponse.success(adminUserService.getOrderList(userId, page, size));
    }

    @PostMapping("/updateInfo")
    public FruitGreetResponse<Void> updateInfo(@RequestBody UpdateUserInfoDto dto) {
        adminUserService.updateUserInfo(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/updateName")
    public FruitGreetResponse<Void> updateName(@RequestBody UpdateUserInfoDto dto) {
        adminUserService.updateName(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/updateMobile")
    public FruitGreetResponse<Void> updateMobile(@RequestBody UpdateUserInfoDto dto) {
        adminUserService.updateMobile(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/store")
    public FruitGreetResponse<Void> store(@RequestBody UserStoreDto dto) {
        adminUserService.store(dto);
        return FruitGreetResponse.success();
    }

    @PostMapping("/destory")
    public FruitGreetResponse<Void> destory(@RequestParam Integer id) {
        adminUserService.destory(id);
        return FruitGreetResponse.success();
    }



















    private Client client;


    @PostMapping("/cleanHistory")
    public void cleanHistory(@RequestParam String userId) throws Exception {
        QueryTaskReq req = QueryTaskReq.newBuilder()
                .pageSize(200)
                .userId(userId)
                .topic("1")
                .userIdType("user_id")
                .build();
        QueryTaskResp resp = this.getFeishuClient().approval().v4().task().query(req);

        if (resp.success()) {
            Task[] tasks = resp.getData().getTasks();
            log.info("还剩: {}", tasks.length);
            for (int i = 0; i < 1; i++) {
                Task task = tasks[i];
                this.execHistory(task.getDefinitionCode(), task.getProcessExternalId(), userId);
            }
        }
    }

    private void execHistory(String approvalCode, String instanceId, String userId) {

        I18nResource resource = new I18nResource();
        resource.setIsDefault(true);
        resource.setLocale("zh-CN");
        I18nResourceText text = new I18nResourceText();
        text.setKey("@i18n@1");
        text.setValue("1");
        resource.setTexts(new I18nResourceText[]{text});

        CreateExternalInstanceReq req = CreateExternalInstanceReq.newBuilder()
                .externalInstance(ExternalInstance.newBuilder()
                        .approvalCode(approvalCode)
                        .status("APPROVED")
                        .instanceId(instanceId)
                        .links(ExternalInstanceLink.newBuilder()
                                .pcLink("https://open.feishu.cn")
                                .mobileLink("https://open.feishu.cn")
                                .build())
                        .title("@i18n@1")
                        .userId(userId)
                        .startTime("1722306790000")
                        .endTime("1722306790000")
                        .form(null)
                        .updateTime("1722306790000")
                        .taskList(null)
                        .ccList(null)
                        .i18nResources(new I18nResource[]{resource})
                        .build())
                .build();

        // 发起请求
        try {
            CreateExternalInstanceResp resp = client.approval().externalInstance().create(req);
            if (resp.success()) {
                log.info("更新 {}", resp.getMsg());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Client getFeishuClient() {
        if (Objects.isNull(client)) {
            client = Client.newBuilder("cli_a65558eb7e6bd00c", "yIcC8IvAHjOPctOIUzBgmf2TQfWXC6Em").build();
        }
        return client;
    }




}
