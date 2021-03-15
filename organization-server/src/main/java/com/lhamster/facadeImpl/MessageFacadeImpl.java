package com.lhamster.facadeImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.lhamster.entity.OrgMessage;
import com.lhamster.facade.MessageFacade;
import com.lhamster.response.OrgMessageInfoResponse;
import com.lhamster.response.exception.ServerException;
import com.lhamster.response.result.Response;
import com.lhamster.service.OrgMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Damon_Edward
 * @version 1.0
 * @date 2021/3/15
 */
@Slf4j
@Service
@Component
@RequiredArgsConstructor
public class MessageFacadeImpl implements MessageFacade {
    private final OrgMessageService orgMessageService;

    @Override
    @Transactional(rollbackFor = ServerException.class)
    public Response<List<OrgMessageInfoResponse>> msgList(Long userId) {
        List<OrgMessage> messageList = orgMessageService.list(new QueryWrapper<OrgMessage>()
                .eq("mes_target_id", userId)
                .ne("mes_status", "DELETED")
                .orderByDesc("create_at"));
        List<OrgMessageInfoResponse> messageInfoResponseList = new ArrayList<>();
        messageList.forEach(msg -> {
            messageInfoResponseList.add(OrgMessageInfoResponse.builder()
                    .createAt(msg.getCreateAt())
                    .mesContent(msg.getMesContent())
                    .mesId(msg.getMesId())
                    .mesStatus(msg.getMesStatus())
                    .build());
        });
        return new Response<>(Boolean.TRUE, "查询成功", messageInfoResponseList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response read(String status, Long msgId, Long userId) {
        // 单个通知修改状态
        if (Objects.nonNull(msgId)) {
            if (!("READ".equals(status) || "DELETED".equals(status))) {
                throw new ServerException(Boolean.FALSE, "状态入参异常");
            }
            OrgMessage message = orgMessageService.getById(msgId);
            message.setMesStatus(status);
            orgMessageService.updateById(message);
        } else {
            // 多个通知修改状态
            List<String> statusList = null;
            if ("READ".equals(status)) {
                statusList = Lists.newArrayList("UNREAD");
            } else if ("DELETED".equals(status)) {
                statusList = Lists.newArrayList("READ", "UNREAD");
            } else {
                throw new ServerException(Boolean.FALSE, "状态入参异常");
            }
            List<OrgMessage> messageList = orgMessageService.list(new QueryWrapper<OrgMessage>()
                    .eq("mes_target_id", userId)
                    .in("mes_status", statusList));
            messageList.forEach(msg -> {
                msg.setMesStatus(status);
                orgMessageService.updateById(msg);
            });
        }
        return new Response(Boolean.TRUE, "状态修改成功");
    }
}
