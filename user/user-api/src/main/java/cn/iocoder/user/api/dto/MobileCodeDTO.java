package cn.iocoder.user.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MobileCodeDTO {

    private String code;

}