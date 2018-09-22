package cn.iocoder.user.service.dao;

import cn.iocoder.user.service.po.OAuth2AccessTokenPO;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2AccessTokenMapper {

    void insert(OAuth2AccessTokenPO entity);

    OAuth2AccessTokenPO selectByTokenId(String tokenId);

}