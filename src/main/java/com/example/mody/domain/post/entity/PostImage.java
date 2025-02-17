package com.example.mody.domain.post.entity;

import com.example.mody.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostImage extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String url;

    /**
     * 필드 개수가 적고, 필드들이 필수값이므로 빌더 패턴을 사용하지 않고 생성자를 사용함
     * @param post
     * @param s3Url
     */
    public PostImage(Post post, String s3Url){
        this.post = post;
        this.url = s3Url;
    }
}
