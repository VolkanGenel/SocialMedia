package com.volkan.repository.entity;

import com.volkan.repository.enums.EStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@SuperBuilder // Builder, bir sınıftan nesne türetmek için özel oluşturulmuş bir method
@Data // Data,get, set methodlarını tanımlar
@NoArgsConstructor // Parametresiz constructor tanımlar
@AllArgsConstructor // 1....n kadar olan tüm parametreli constructorları tanımlar
@Document
public class UserProfile extends BaseEntity{
    @Id
    private String id;
    private Long authId;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private String adres;
    private String about;
    @Builder.Default()
    private EStatus status = EStatus.PENDING;
    @Builder.Default()
    private List<String> follows = new ArrayList<>();
    @Builder.Default()
    private List<String> follower = new ArrayList<>();

}
