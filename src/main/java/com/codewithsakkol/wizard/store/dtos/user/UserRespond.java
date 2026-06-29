package com.codewithsakkol.wizard.store.dtos.user;

import com.codewithsakkol.wizard.store.entities.enums.Rols;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserRespond {
    private Long id;
    private String name;
    private String email;
    private Rols role;
}
