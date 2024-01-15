package com.daesoo.sso.member.entity;

import com.daesoo.sso.member.entity.enums.ProviderType;
import com.daesoo.sso.member.entity.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 회원 이름 */
    @Column(nullable = false)
    private String name;

    /* email(login id) */
    @Column(nullable = false)
    private String email;

    /* 비밀번호 */
//    @Column(nullable = false)
    private String passwd;

    /* 권한 */
    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
    private Role role;

    /* 공급자 */
    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
    private ProviderType providerType;

    public String getRoleKey(){
        return this.role.getKey();
    }

    public String getProviderTypeKey(){
        return this.providerType.getKey();
    }

    @Builder
    public User(String name, String email, String passwd, Role role, ProviderType providerType){
        this.name = name;
        this.email = email;
        this.passwd = passwd;
        this.role  = role;
        this.providerType = providerType;
    }

    public User update(String name) {
        this.name = name;
        return this;
    }
}
