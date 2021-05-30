package com.congnghejava.webbanhang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.congnghejava.webbanhang.models.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

}
