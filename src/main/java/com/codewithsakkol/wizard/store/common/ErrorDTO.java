package com.codewithsakkol.wizard.store.common;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {
    private HashMap<String, String> errors;
}
