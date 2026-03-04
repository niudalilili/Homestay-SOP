package com.tanyde.dto.FavoriteDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoritePageQueryDTO implements Serializable {
    private Integer page;

    private Integer pageSize;

    private Long userId;

    private Long activityId;

}
