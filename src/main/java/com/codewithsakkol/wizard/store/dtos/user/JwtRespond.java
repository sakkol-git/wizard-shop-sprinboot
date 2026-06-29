package com.codewithsakkol.wizard.store.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class JwtRespond {
    private String token;
}
